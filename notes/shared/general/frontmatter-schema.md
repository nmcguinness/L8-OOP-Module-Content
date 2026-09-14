---
title: "Frontmatter Schema"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: shared_general
description: "The YAML frontmatter contract for topic notes and exercise files: required fields, their meaning, and how to verify a file conforms."
created: 2026-09-10
last_updated: 2026-09-10
version: 1.0
status: published
authors: ["OOP Teaching Team"]
tags: [metadata, frontmatter, yaml, standards, comp-c8z03]
---

# Frontmatter Schema

Every topic file carries YAML frontmatter. Tooling reads it, so the field set is fixed:
a file either conforms or it does not. This page is the contract.

Two schemas exist. **Notes** carry the full 14 fields, because a topic is described once.
**Exercises** carry a 10-field subset — the same keys, in the same order, minus the four
that describe the *topic* rather than the *file*.

## Rules

- The frontmatter block is the **first thing in the file**. No blank line, no BOM, nothing
  before the opening `---`.
- `title` must match the file's H1 exactly. This is also required by R14 of the
  [accessibility style guide](accessibility-style-guide.md).
- Key **order** is part of the schema, so a diff shows real changes rather than reordering.
- Frontmatter is never line-wrapped, so a long `description` stays on one line.
- Values containing a colon must be **quoted**, or the parser reads a nested mapping.

## Identity

| Field | Type | Meaning |
|:--|:--|:--|
| `title` | string | Matches the H1. Exercise titles end in `<Topic> — Exercises`. |
| `subtitle` | string | Always `"COMP C8Z03 — Year 2 OOP"`. |
| `topic_code` | string | The topic folder name, e.g. `t15_dao`. |
| `description` | string | One sentence: what the file covers. Shown in listings. |

## Provenance

| Field | Type | Meaning |
|:--|:--|:--|
| `created` | date | When the file was first authored. Never changes. |
| `last_updated` | date | Date of the most recent substantive edit. |
| `version` | number | Bumped on substantive revision, not on typo fixes. |
| `status` | string | `published`, or `draft` while a file is incomplete. |
| `authors` | list | Usually `["OOP Teaching Team"]`. |

## Classification

Notes carry all five. **Exercises carry only `tags`.**

| Field | Type | Meaning |
|:--|:--|:--|
| `tags` | list | Lowercase, hyphenated. Ends `year2, comp-c8z03`. Exercises insert `exercises` before `year2`. |
| `difficulty_tier` | string | `Foundation`, `Intermediate` or `Advanced`. Notes only. |
| `mlos` | list | Module learning outcomes, e.g. `[MLO2, MLO3]`. Notes only. |
| `previous_topic` | string | The preceding `topic_code`, or `null` for t00. Notes only. |
| `prerequisites` | list | What a student needs first, in prose. Notes only. |

## Why exercises omit four fields

`difficulty_tier`, `mlos`, `previous_topic` and `prerequisites` describe **the topic**, not
the exercise file. Recording them twice means they drift: the notes get updated, the
exercises quietly keep last year's values, and neither is obviously wrong. They are
declared once, in the notes, and the exercise file is joined to them by `topic_code`.

## The `Lesson Context` footer

Every topic notes file ends with a `## Lesson Context` section holding a YAML block:

```yaml
previous_lesson:
  topic_code: t04_equality_hashing
this_lesson:
  topic_code: t05_collections_1
  difficulty_tier: Foundation
mlos: [MLO3]
```

Four of those values — `topic_code`, `difficulty_tier`, `mlos` and the previous topic —
**also live in the frontmatter**, so the same fact is recorded twice in one file.

**The frontmatter wins.** When the two disagree, the frontmatter is correct and the footer
is stale. Change the frontmatter first, then bring the footer into line.

This is not a hypothetical risk. An audit of all 25 topics found two files where the pair
had already diverged: t00 omitted `mlos` from its footer, and t01 claimed it had no
previous lesson when it follows t00. Both are fixed, but the duplication that allowed them
remains — the footer is kept because it is part of the documented section structure.
Re-run the comparison after any edit to either block.

## Verifying a file

A `grep` for `^title:` proves nothing — it cannot tell you the block parses, that the keys
are the right ones, or that they are in order. Parse it:

```python
import yaml

text = open(path, encoding="utf-8").read()
assert text.startswith("---\n"), "no frontmatter, or a BOM precedes it"
data = yaml.safe_load(text[4:text.find("\n---\n", 3)])
assert list(data) == EXPECTED_KEYS, list(data)
```

A UTF-8 BOM is the failure worth knowing about: the file *looks* correct in an editor, but
because the first bytes are not `---`, every YAML front-matter parser reports no
frontmatter at all rather than an error.
