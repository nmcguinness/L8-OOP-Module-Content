# Self-Assessment Question Bank

200 multiple-choice questions — **8 per topic, across all 25 topics**. Every option
carries its own feedback line explaining why it is right or wrong, so the bank works
as a study tool and not only as a test.

## For students

Use these to check yourself **after each week's topic**, not only before an exam.
Answer without looking at the notes first: the feedback on the wrong options is
where most of the learning is, so read it even when you guessed correctly.

Three kinds of question appear:

| Marker | What it asks of you |
| :-- | :-- |
| *(unmarked)* | Recall and understanding — definitions, contracts, trade-offs |
| `[Predict the output]` | Read a short snippet and say what it does |
| `[Spot the bug]` | Given a symptom, identify the fault and the fix |

The last two matter most. They are the ones that expose a misunderstanding you did
not know you had, because a plausible-looking wrong answer is often the belief you
were actually holding.

## Layout

| Path | What it is |
|:-|:-|
| `README.md` | This file |
| `oop_self_assessment_mcq.gift` | Generated: all 25 topics in one file |
| `build_bank.sh` | Validates, then rebuilds the combined file |
| `validate_gift.sh` | Structural checks on GIFT files |
| `topics/` | One file per topic, 8 questions each, from `t00_oop_fundamentals.gift` through `t25_documenting_a_project.gift` |

Import **one topic file** for a weekly self-test, or the **combined file** to load
the whole bank at once. Both are Moodle GIFT format:

> Course then Question bank then Import then choose *GIFT format* then upload

Questions are filed automatically under `$CATEGORY` tags matching the topic codes
in [notes/topics/](../../topics/).

## For staff: editing

`oop_self_assessment_mcq.gift` is **generated — do not edit it directly.** Edit the
relevant file in `topics/`, then:

```bash
bash build_bank.sh      # validates every topic file, then regenerates the combined file
```

`validate_gift.sh` runs as part of that build and checks each file for the errors
that make a Moodle import fail silently or produce a malformed question:

- question count matching `{` and `}` block delimiters
- exactly one `=` (correct) answer per question, with at least four options
- a `#` feedback line for every option
- unescaped `{` or `}` in question or answer text

Two conventions worth keeping when adding questions:

- **No HTML.** GIFT needs an explicit `[html]` marker for tags to render, and the
  bank does not use one. Show code inline in backticks. Where a `<` appears, follow
  it with a space or `=` so no viewer mistakes it for a tag.
- **Escape GIFT control characters** in question and answer text: `=` `~` `#` `{`
  `}` `:` must be written `\=` `\~` `\#` `\{` `\}` `\:`.

## Coverage

All 25 topics have 8 questions. The bank is deliberately weighted towards the
misconceptions that recur in submitted work — aliasing, `equals`/`hashCode`,
iterator invalidation, generic invariance, stream laziness, and silent failure in
exception handling and JSON parsing.
