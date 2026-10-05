#!/usr/bin/env python3
"""Submit reviewed fork work from a disposable, fork-tooling-free branch."""

from __future__ import annotations

import datetime
import os
import subprocess
import sys

UPSTREAM = "eGovFramework/egovframe-msa-common-components"
DENYLIST = [
    "AGENTS.md",
    ".github/workflows/fork-ci.yml",
    ".github/workflows/sync-upstream-main.yml",
    ".github/workflows/check-upstream-pr-hygiene.yml",
    ".github/workflows/close-resolved-issues.yml",
    "scripts/open_upstream_pr.py",
    "scripts/close_resolved_issues.py",
]


def git(*args: str, check: bool = True) -> subprocess.CompletedProcess[str]:
    return subprocess.run(["git", *args], text=True, check=check)


def main() -> None:
    if len(sys.argv) < 3 or sys.argv[1:3] != ["work", "main"]:
        raise SystemExit("usage: python3 scripts/open_upstream_pr.py work main [gh-pr-create-options...]")
    root = subprocess.check_output(["git", "rev-parse", "--show-toplevel"], text=True).strip()
    os.chdir(root)
    if subprocess.check_output(["git", "status", "--porcelain"], text=True).strip():
        raise SystemExit("Commit or remove local changes before creating a submission.")

    branch = "upstream-submit/" + datetime.datetime.now(datetime.timezone.utc).strftime("%Y%m%d%H%M%S")
    git("fetch", "origin", "work")
    git("switch", "-c", branch, "origin/work")
    git("rm", "-rq", "--ignore-unmatch", "--", *DENYLIST)
    if subprocess.run(["git", "diff", "--cached", "--quiet"]).returncode == 1:
        git("-c", "user.name=Fork submission", "-c", "user.email=fork-submission@users.noreply.github.com",
            "commit", "-m", "chore: strip fork-only files from upstream submission")
    if subprocess.run(["git", "remote", "get-url", "upstream"], capture_output=True).returncode:
        git("remote", "add", "upstream", f"https://github.com/{UPSTREAM}.git")
    git("fetch", "upstream", "main")
    diff = subprocess.run(["git", "diff", "--quiet", "upstream/main...HEAD"])
    if diff.returncode == 0:
        raise SystemExit("No upstream-bound changes remain after stripping fork-only files.")
    if diff.returncode != 1:
        raise SystemExit("Unable to compare the upstream diff.")
    print("Proposed upstream file list:", flush=True)
    git("diff", "--name-status", "upstream/main...HEAD")
    print("Inspect the full diff before asking for merge: git diff upstream/main...HEAD", flush=True)
    git("push", "-u", "origin", branch)
    subprocess.run(
        ["gh", "pr", "create", "--repo", UPSTREAM, "--base", "main",
         "--head", f"leejongyoung:{branch}", *sys.argv[3:]],
        check=True,
    )
    print(f"Source work is unchanged. Submission head: {branch}")


if __name__ == "__main__":
    main()
