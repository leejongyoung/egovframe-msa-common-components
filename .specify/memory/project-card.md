# egovframe-msa-common-components: project context

## Role, scope, and public entry points

`README.md` describes Spring Boot / Spring Data JPA / Spring Cloud microservices providing
common components. Root `pom.xml` lists twelve modules: configuration, discovery, gateway,
main UI, login, login policy, authorization, board, common code, questionnaire, search,
and mobile ID. Public surfaces include gateway routes, service REST contracts, JWT cookies,
authorization decisions, persistence mappings, messaging, and rendered pages.
Preserve service ownership and cross-service authentication and data contracts.

## Build and verification evidence

- README lists Java 17, Spring Boot 3.5.6, Spring Cloud 2025.0.0 and external Docker services.
  Root `pom.xml` is a Maven aggregator; module POMs define their application builds.
- `.github/workflows/build.yml` (`main`) and `fork-ci.yml` (`work`) use JDK 17 and run
  `mvn -B -ntp -f <module>/pom.xml package -DskipTests` across eleven modules.
  `EgovMobileId` is in the root reactor but absent from these matrices. Tests are skipped;
  a package result establishes no service integration, mobile ID, or authentication correctness.
- README starts MySQL with `docker-compose up -d` in `docker-compose/mysql`, then discovery,
  configuration, gateway, main, login, and required component services. Redis is needed for login
  token storage; search adds OpenSearch/RabbitMQ. Read module README files before running a service.
  Configuration and DB/token values require local setup; do not reproduce real values in cards.
- `.gitattributes` forces CRLF globally. A scoped LF rule for `.specify/scripts/**/*.sh` keeps
  generated shell tools runnable after checkout without changing application line-ending policy.

## Contribution and submission baseline

`AGENTS.md` treats `main` as an upstream mirror and `work` as fork integration. The actual parent
is `eGovFramework/egovframe-msa-common-components`; fetch the current default ref and record its SHA.
Preserve existing submission branches and PRs. The Python submission helper is required by local
instructions because other scripts check out with CRLF; its denylist now excludes `.specify`/`.omp`.
It still starts from `work`, so do not use it for the new upstream-based contribution workflow:
file stripping alone does not exclude tooling history. Use a separate submission worktree rooted
at the fetched upstream SHA and explicitly stage application changes. Adoption creates no PR;
subsequent application work still follows the existing fork review/CI requirements.

## Exploration limits

No `.codegraph/` index was present. Evidence is from targeted README, AGENTS, POM, attributes,
CI, and helper reads; no service stack or broad build was started.


## Maintenance process

Follow [constitution.md](constitution.md) for the domain-card, assessment, planning, verification,
and submission gates. Commands are installed under `.omp/commands/`; read them as agent
instructions. No application build or test was run for tool adoption. Scaffold checks establish
tool installation only, not application correctness.
