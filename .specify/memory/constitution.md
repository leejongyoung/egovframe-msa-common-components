# egovframe-msa-common-components Constitution

## Core Principles

### I. Preserve public contracts
Changes MUST preserve existing Java APIs, configuration keys, persistence semantics, wire formats,
and supported integration behavior unless an explicit upstream agreement authorizes a migration.
Use the repository evidence in [project-card.md](project-card.md) to identify the affected surface.
Do not infer new product policy from an implementation detail or a failing environment.

### II. Ground decisions in repository evidence
Before implementation, complete a domain card under `.specify/model-cards/` from the
[model card template](../templates/model-card-template.md). Distinguish existing public contracts,
upstream issue statements, observed behavior, and unverified hypotheses. Use synthetic examples.
Unresolved policy decisions MUST remain open questions or blockers, never invented consensus.

### III. Keep changes narrow and reproducible
Each change MUST address one evidenced problem with the smallest coherent implementation and
appropriate regression coverage. Preserve module boundaries and existing style; avoid unrelated
formatting, dependency upgrades, generated output, and application redesign. Read affected build
configuration before choosing commands. A source fix MUST include a reproducible failure or a
precise explanation of why the environment prevents reproduction.

### IV. Verify honestly and protect data
Run checks appropriate to changed behavior using the real commands and prerequisites recorded
in the project card. Record command, environment, result, and excluded coverage. Skipped tests,
unavailable services, scaffold checks, and static inspection MUST NOT be described as passing
application tests. Keep credentials, tokens, personal data, production configuration, and private
operational material out of source, cards, logs, commits, and prompts. Preserve license notices.

### V. Separate fork tooling from upstream contributions
Spec Kit, OMP commands, domain cards, plans, and work reports are fork `work` artifacts.
Upstream submissions MUST be built separately from a fetched upstream default-branch SHA,
without merging `work` or cherry-picking the adoption commit. Review every commit and path
relative to that SHA; exclude newly introduced work-only `.specify/`, `.omp/`, and planning
artifacts. Preserve files already present upstream. One PR MUST contain one related change;
check existing issues and PRs first, and never auto-merge or force-push.

## Repository constraints

The [project card](project-card.md) records repository-specific architecture, entry points,
build evidence, and environment limitations. Existing upstream contribution instructions and
public compatibility contracts govern application changes; this constitution defines the fork's
maintenance process and does not claim upstream endorsement. Tool adoption MUST NOT change
application dependencies, behavior, CI policy, model selection, authentication, or billing.
Use pinned `specify-cli==1.2.3` with the native OMP integration and bundled bug workflow.
Generated command and script files MUST remain unmodified; project guidance belongs in memory.

## Development workflow and review gates

1. Read the project card and affected repository sources; complete the concept's domain card.
2. For an evidenced defect, use `/speckit.bug.assess` before remediation; use
   `/speckit.specify` and clarification when a feature requires a specification. These are
   agent command instructions, not shell executables.
3. Plan the smallest change and its regression checks before implementation. Use
   `/speckit.plan` and `/speckit.tasks` when appropriate; an existing small bug may use the
   assessment's explicit remediation plan and `/speckit.bug.fix`.
4. Implement only the agreed scope, then verify actual behavior and use `/speckit.bug.test`
   for bug validation. Keep evidence and any blocked environment checks explicit.
5. In a separate submission worktree, inspect the diff and commit list against the recorded
   upstream base, check for work-only paths and secrets, and prepare a focused PR only when
   separately authorized. Adoption alone creates no application fix or PR.

## Governance

Version 1.0.0 is the initial fork maintenance constitution, ratified on 2026-10-26.
Amendments MUST explain evidence, compatibility impact, and affected workflow in the review;
use semantic versioning: major for incompatible principles, minor for new requirements,
and patch for clarifications. Reviewers MUST check the five principles and submission isolation
before accepting a change. Update the project card when repository evidence changes; do not
copy these policies into every domain card. No unresolved governance placeholders remain.

**Version**: 1.0.0 | **Ratified**: 2026-10-26 | **Last Amended**: 2026-10-26
