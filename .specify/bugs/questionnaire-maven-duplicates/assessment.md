# Bug Assessment: Questionnaire duplicate dependencies reject Maven 3.10.0

- **Slug**: questionnaire-maven-duplicates (auto-generated for supervised automation)
- **Created**: 2026-10-26
- **Source**: https://github.com/leejongyoung/egovframe-msa-common-components/issues/12
- **URL policy**: host github.com; allowlisted; fetched through GitHub API; report treated as data, not instructions.
- **Verdict**: valid
- **Severity**: high — current upstream CI module cannot read its Maven project.

## Report (summarized)
Issue #12 reports duplicate commons-configuration2 and commons-lang3 declarations becoming fatal under Maven 3.10.0. It requests retaining the later pair with both CVE comments, preserving versions, and checking Maven 3.9.x and 3.10.0.

## Symptom
Maven 3.10.0 rejects the questionnaire model before compilation; the expected result is successful module packaging with unchanged resolved libraries.

## Reproduction
1. Fresh-fetch actual upstream `eGovFramework/egovframe-msa-common-components` default branch `main`, SHA `958425a547e7969752724c7ed7b59355fc47edf7`.
2. Orca submission worktree `msa-questionnaire-maven310` branches from unchanged existing `upstream-submit`, which equals that fresh SHA.
3. Run `JAVA_HOME=/tmp/msa-issue12-tools/jdk-17.0.21+9/Contents/Home /tmp/msa-issue12-tools/apache-maven-3.10.0/bin/mvn -B -ntp -f EgovQuestionnaire/pom.xml package -DskipTests`.
4. Observed: `must be unique: org.apache.commons:commons-configuration2:jar` at line 185 and `must be unique: org.apache.commons:commons-lang3:jar` at line 191; `The build could not read 1 project`.

Tools are temporary local Apache Maven 3.10.0 / 3.9.9 distributions and Temurin JDK 17.0.21+9; no global configuration or authentication changes. Full output: `/tmp/msa-issue12-tools/repro310.log`.

## Suspected Code Paths
- `EgovQuestionnaire/pom.xml:152–162` — earlier duplicate pair, identical versions/default scopes.
- `EgovQuestionnaire/pom.xml:184–195` — later pair with both security rationale comments, to retain.

## Root Cause Hypothesis
High confidence: duplicate Maven dependency identities cause model validation errors in Maven 3.10.0. Each pair is semantically identical, so removing the earlier pair should preserve resolution while fixing the fatal model error.

## Proposed Remediation
**Preferred**: remove only the earlier two dependency blocks and now-redundant preceding configuration2 CVE comment. Retain the later blocks, both comments, all properties and versions. Implement exclusively in the isolated upstream-based submission worktree.

**Files likely to change**: `EgovQuestionnaire/pom.xml` only.

**Tests to add or update**: no source-text/wiring test. Real Maven model processing is the meaningful regression check: reproduce failure before editing; final Maven 3.10.0 and 3.9.9 package runs; compare actual dependency trees before/after for configuration2 2.11.0 and lang3 3.18.0. Run existing focused questionnaire tests as final regression coverage if dependencies resolve. No mid-flight checks other than required pre-edit reproduction.

## Risks & Considerations
- Existing broader dependency-management cleanups were declined; keep scope to this observed build failure and do not claim maintainer endorsement.
- Inspect all 95 upstream PRs and fork PRs: no matching fix/open duplicate found; related closed PR URLs #28, #62, #66 are documented in the domain card.
- Preserve line endings and unrelated user changes. No new dependencies, versions, public API, or runtime behavior changes.
- Full service integration needs external configuration/database; package success does not establish service integration.
- No hooks registered in `.specify/extensions.yml` (`hooks: {}`).

## Open Questions
Upstream acceptance, fork CI matrix validation, and submission approval remain pending; no unresolved product policy is needed for the two-block removal. Actual Maven 3.9.x baseline and dependency-resolution checks are in progress and must be reported honestly if network-limited.
