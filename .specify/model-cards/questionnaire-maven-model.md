# Domain model card: Questionnaire Maven dependency model

## One-sentence definition
The EgovQuestionnaire Maven model describes the dependencies and build inputs consumed by Maven, developers, and the module CI job to produce the questionnaire service artifact.

## Why this concept is needed
Public fork issue #12 reports that Maven 3.10.0 rejects two repeated dependency identities, preventing the module build before compilation. Restore buildability without changing resolved dependency versions, service APIs, or security override comments.

## Existing models and public contracts
- `EgovQuestionnaire/pom.xml`: standalone child of `org.egovframe.boot:egovframe-boot-starter-parent:5.0.1`; upstream artifact version 5.0.4, Java 17.
- Dependency identities include group, artifact, type (default jar), and classifier (default empty). Both repeated pairs specify identical versions, default scope, and no differing exclusions.
- Preserve explicit properties `commons-configuration2.version=2.11.0` and `commons-lang3.version=3.18.0`, plus the later direct declarations and their CVE comments.
- `.github/workflows/build.yml`: Java 17 matrix runs module package with tests skipped.
- Public report: https://github.com/leejongyoung/egovframe-msa-common-components/issues/12

## Synthetic example data
An invented child artifact `example:survey-demo:1.0` has two `org.apache.commons:commons-lang3:jar` entries with `${commons-lang3.version}=3.18.0`. Keeping one yields the same direct dependency version; keeping both causes Maven 3.10.0 model rejection. A distinct classifier would be a different identity and must not be removed by this reasoning.

## Successful behavior examples
- Fresh upstream module plus one declaration for each pair → Maven 3.10.0 package → a packaged questionnaire artifact.
- Maven 3.9.x resolves the original POM and corrected POM → configuration2 2.11.0 and lang3 3.18.0 in both dependency trees.
- Existing module tests execute against the corrected model without changing production service behavior.

## Rejected behavior examples
- Two identical default-jar identities → Maven 3.10.0 rejects the model with `must be unique`; this is the reproduced defect, not intended user input validation.
- Removing both declarations or their version properties is unacceptable because it may change direct dependency resolution.
- A dependency upgrade, global Maven downgrade, or CI runner pin is outside the narrow remedy.

## Outside this concept's responsibility
Application questionnaire validation, database schema, authentication, external service availability, and broader parent/BOM policy remain with their existing owners. No runtime vulnerability remediation is claimed from removal of repeated declarations.

## Rule sources and assumptions
- Observed upstream source: fresh `main` SHA `958425a547e7969752724c7ed7b59355fc47edf7`; source contains both repeated pairs at lines 152–162 and 184–195.
- Observed behavior: unmodified upstream POM fails a real Maven 3.10.0 package invocation on Temurin 17.0.21 with exactly the two reported uniqueness errors; see assessment reproduction.
- Public report: issue #12 describes earlier Maven warning versus new Maven error; independent local 3.9.x compatibility/build checks are planned, not assumed.
- Public maintainer decisions: https://github.com/eGovFramework/egovframe-msa-common-components/pull/28#issuecomment-5113060740 and https://github.com/eGovFramework/egovframe-msa-common-components/pull/62#issuecomment-5579764670 and https://github.com/eGovFramework/egovframe-msa-common-components/pull/66#issuecomment-5630751446 reject broader ConfigServer version-management cleanup; they do not establish approval of this fix.
- Observed deduplication: all upstream PRs (95 returned, below the 100 limit) and all fork PRs inspected; no matching duplicate-declaration fix or open upstream PR found.

## Open questions and next evidence
Upstream acceptance remains a maintainer decision. Verify both Maven package commands and actual resolved versions before review; external artifact repositories may limit full build validation. Fork CI matrix and upstream submission remain separate review gates; neither is claimed complete here.
