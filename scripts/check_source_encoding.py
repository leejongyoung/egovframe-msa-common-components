"""Fail when a tracked text file contains lines that are not valid UTF-8.

Fork-only guard (see AGENTS.md). Binary files (containing NUL bytes) are skipped.
Usage: python3 scripts/check_source_encoding.py
"""
import subprocess
import sys


def main() -> int:
    files = subprocess.check_output(["git", "ls-files", "-z"]).split(b"\0")
    bad = []
    for raw in files:
        if not raw:
            continue
        path = raw.decode("utf-8")
        try:
            with open(path, "rb") as fh:
                data = fh.read()
        except OSError:
            continue
        if b"\0" in data:
            continue
        for number, line in enumerate(data.split(b"\n"), 1):
            try:
                line.decode("utf-8")
            except UnicodeDecodeError:
                guess = line.decode("cp949", errors="replace").strip()
                bad.append((path, number, guess))
    for path, number, guess in bad:
        print(f"{path}:{number}: not UTF-8 (as CP949: {guess})")
    if bad:
        print(f"{len(bad)} non-UTF-8 line(s) found")
        return 1
    print("All tracked text files are valid UTF-8")
    return 0


if __name__ == "__main__":
    sys.exit(main())
