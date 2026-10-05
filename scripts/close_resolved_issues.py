#!/usr/bin/env python3
"""Close fork issues after completed checklists and merged upstream PRs."""

from __future__ import annotations

import json
import os
import re
import subprocess

FORK = os.environ["GITHUB_REPOSITORY"]
UPSTREAM = os.environ["UPSTREAM_REPOSITORY"]
PR_URL_RE = re.compile(rf"github\.com/{re.escape(UPSTREAM)}/pull/(\d+)")
CHECKBOX_RE = re.compile(r"^- \[([ x])\]", re.MULTILINE)


def gh_json(*args: str):
    result = subprocess.run(["gh", *args], capture_output=True, text=True, check=True)
    return json.loads(result.stdout)


def main() -> None:
    issues = gh_json(
        "issue", "list", "--repo", FORK, "--state", "open", "--limit", "1000",
        "--json", "number,body",
    )
    for issue in issues:
        number = issue["number"]
        body = issue["body"] or ""
        if " " in CHECKBOX_RE.findall(body):
            continue

        comments = gh_json(
            "issue", "view", str(number), "--repo", FORK, "--json", "comments",
            "--jq", ".comments",
        )
        text = body + "\n" + "\n".join(comment["body"] for comment in comments)
        pr_numbers = sorted({int(match) for match in PR_URL_RE.findall(text)})
        if not pr_numbers:
            continue

        states = {
            pr: gh_json("pr", "view", str(pr), "--repo", UPSTREAM, "--json", "state")["state"]
            for pr in pr_numbers
        }
        live = {pr: state for pr, state in states.items() if state != "CLOSED"}
        if live and all(state == "MERGED" for state in live.values()):
            links = ", ".join(f"#{pr}" for pr in live)
            subprocess.run(
                ["gh", "issue", "close", str(number), "--repo", FORK,
                 "--comment", f"연결된 업스트림 PR ({links})이 모두 병합되고 체크리스트가 완료되어 닫습니다."],
                check=True,
            )
            print(f"Closed fork issue #{number}: {links}")


if __name__ == "__main__":
    main()
