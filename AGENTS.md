# Fork collaboration: egovframe-msa-common-components

These instructions apply to `leejongyoung/egovframe-msa-common-components`. This file and the fork
automation are fork-only; `scripts/open_upstream_pr.py` excludes them from
contributions to `eGovFramework/egovframe-msa-common-components`.

## Branch roles and review

- `main` must remain byte-identical to upstream `main`. Never commit to it.
  `.github/workflows/sync-upstream-main.yml` fast-forwards it daily and on
  manual dispatch. Divergence fails without force-pushing.
- `work` is this fork's default integration branch. Develop upstream-bound
  changes on a topic from `main`; develop fork infrastructure on a topic from
  `work`. Open a fork PR against `work` and review its CI before submission.
- Upstream also has 4.3.x and 5.0.x. This setup does not change or sync
  those branches; inspect their roles before creating any extra tracks.

Upstream's existing workflows target `main`. The separate fork CI targets
`work` PRs and pushes and runs, for the 11 modules in upstream CI (JDK 17): Maven package
with tests skipped, then `mvn test` against a MySQL 8.0.33 container prepared like
`docker-compose/mysql` (DB/account `com`, `docker-compose/mysql/init/*.sql`). The test step is
fork-only; upstream `build.yml` stays `-DskipTests`. Use that result as the baseline check. For changes outside its tested scope,
add a targeted verification and record the exact command/result in the PR.

## Syncing upstream `main` into `work`

`sync-upstream-main.yml` only fast-forwards the fork's `main`; it never touches `work`.
When upstream `main` advances, open a fork PR from a topic branch that merges `origin/main`
into `work` (`git merge --no-ff origin/main`) and wait for fork CI before merging.
Upstream v5.0.4 deleted the `EgovMobileId` module (vendor-provided source excluded from
contributions). `work` no longer contains it; do not restore it, and do not add it to the CI
matrices.

## Upstream submissions

Batch reviewed work and use a clean checkout to run
`python3 scripts/open_upstream_pr.py work main --title "..." --body-file /path/to/body.md`.
It creates a disposable `upstream-submit/*` head and removes fork-only files.
Inspect the proposed diff and file list against current upstream `main` before
sending. Add any new fork-only files to the script's `DENYLIST`. Never use
`work` or a continuing development branch as an upstream PR head: open PR
diffs change after later pushes to their head branch.

The daily `.github/workflows/check-upstream-pr-hygiene.yml` checks all open
upstream PR heads authored by this fork owner from default branch `work`.
A failure remains an alert until an unsafe PR is closed or resubmitted.
Record cross-branch and tool-version dependencies in PR bodies; creation,
draft/ready state, CI, and merge are separate events. Upstream maintainers
control the merge.

## Issues and roadmap

Use verified evidence in issue bodies, `type:*` and `area:*` labels, a fitting
milestone, and [the fork Project](https://github.com/users/leejongyoung/projects/7).
Keep upstream-provided labels if their issue templates require them. Check a
box only when a specific change implements it. Link full upstream PR URLs,
mark closed/superseded submissions, and close upstream-bound issues once all
work is complete and linked upstream PRs are merged. Fork-only setup issues
may close after their fork PR and verification complete.

This repository's `.gitattributes` checks out all text with CRLF, including
shell scripts. Invoke the Python submission tool with `python3` so its
execution does not depend on a Unix shebang or LF line endings.
