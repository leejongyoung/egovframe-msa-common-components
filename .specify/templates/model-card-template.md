# Domain model card: concept name

This is a reusable feature/concept template, not an ML performance evaluation.
Replace the prompts below with repository evidence before starting implementation.
Follow [the constitution](../memory/constitution.md) and
[project context](../memory/project-card.md) for process and verification policy.

## One-sentence definition
Define the concept, its boundary, and the actors or systems that use it.

## Why this concept is needed
Describe the evidenced user or maintenance problem and the intended outcome.

## Existing models and public contracts
Link relevant interfaces, entities, configuration, schemas, documentation, and upstream issues.
Explain which model is reused and which semantics must remain compatible.

## Synthetic example data
Provide minimal invented inputs, state, identifiers, and outputs. Include relevant boundary values.
Do not copy production records, personal data, secrets, or private project examples.

## Successful behavior examples
Write concrete input/state → action → expected result examples, including observable side effects.

## Rejected behavior examples
Write invalid input/state → action → expected rejection examples and explain error semantics.

## Outside this concept's responsibility
List explicit exclusions and the components or actors responsible for them.

## Rule sources and assumptions
For each rule, cite a source path or public URL and label it as public contract, upstream proposal,
observed implementation behavior, or unverified inference. Record reproduction evidence separately
from assumptions; do not turn a hypothesis into user agreement.

## Open questions and next evidence
Record unresolved questions, their impact, and the evidence or decision needed to resolve them.
Identify blockers that require product policy; record “None” only after checking.
