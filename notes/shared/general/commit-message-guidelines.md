---
title: Commit Message Guidelines
topic_code: shared-general
related_module: COMP C8Z03
intended_audience: Year 2 Software Development Students
pedagogical_focus: Communication, Collaboration, Version Control Best Practice
integration_context: Used in Stage 1 and Stage 2 OOP CA GitHub workflow
author: Lecturer in OOP / DkIT
version: 1.0
academic_year: 2025-26
license: CC BY-NC-SA 4.0
file_name: commit-message-guidelines.md
---

# Commit Message Guidelines

## Overview

When you work on software projects — especially in pairs or teams — everyone needs to
understand **what changed and why**.

A clear, consistent commit message helps you **communicate intent**, **track progress**, and
**debug issues later**.  It is not just about Git syntax; it’s about developing a professional
mindset for collaboration, accountability, and traceability.

You’ll directly apply these practices in your **OOP CA project**, where your GitHub commit
history forms part of your assessment.

## Why Commit Messages Matter

- Improve collaboration and traceability.
- Simplify reviews and debugging.
- Let others (and future you) understand reasoning.
- Help generate automatic changelogs.

Good commit messages are evidence of **clear thinking** and **responsible development**.

## Standard Structure

```text
<type>: <short summary>

[optional longer explanation]
```

**Examples**

```text
feat: add comparator for PlayerScore by descending order
fix: handle empty CSV rows during load
refactor: move validation logic to helper class
docs: update README with Stage 2 schema
```

### Common Commit Types

| Type | Used for... |
|:--|:--|
| `feat` | New feature or functionality |
| `fix` | Bug fix |
| `refactor` | Code restructure without changing behaviour |
| `test` | Adding or improving tests |
| `docs` | Documentation changes |
| `style` | Formatting or naming conventions |
| `chore` | Build tools or small maintenance updates |

## Writing Tips

- Use **imperative mood** (“Add feature” not “Added feature”).
- Keep the first line under **72 characters**.
- Write one logical change per commit.
- Explain *why*, not just *what*, if not obvious from code.
- Capitalise the first word and skip punctuation at the end.

## Try It / Fix It

Rewrite these poor commit messages to follow best practice:

| Poor Example | Improved Example |
|:--|:--|
| “stuff fixed” | `fix: correct null pointer issue in Player constructor` |
| “new file” | `feat: add CsvLoader class for player data import` |
| “update” | `refactor: extract validation logic into Validator utility` |

**Challenge:**
Write your own commit message describing one of your Stage 1 CA milestones.

## Reflection

How do clear commit messages improve teamwork and make debugging easier later?
Write two sentences explaining how you’ll apply this habit during your OOP project.

## Further reading

- [FreeCodeCamp - How to Write Better Git Commit Messages – A Step-By-Step
  Guide](https://www.freecodecamp.org/news/how-to-write-better-git-commit-messages/)
- [How to Write a Git Commit Message – Chris Beams](https://chris.beams.io/posts/git-commit/)
- [Gitmoji – An emoji guide for your commit messages](https://gitmoji.dev/)
