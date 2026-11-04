# GitHub Alerts and Callouts: Test Page

A scratch page for trying out alerts and callouts on GitHub. Open it on github.com to see the real rendering, because many editors and previews draw these differently.

## Contents

- [1. The five GitHub alerts](#1-the-five-github-alerts)
- [2. Enhanced alerts with rich content](#2-enhanced-alerts-with-rich-content)
- [3. Edge cases for alert syntax](#3-edge-cases-for-alert-syntax)
- [4. Callout alternatives that work on GitHub](#4-callout-alternatives-that-work-on-github)
- [5. HTML that GitHub does and does not keep](#5-html-that-github-does-and-does-not-keep)
- [6. Syntax from other platforms](#6-syntax-from-other-platforms)

---

## 1. The five GitHub alerts

GitHub has exactly five alert types, each with its own colour and icon.

> [!NOTE]
> Useful information that users should know, even when skimming content.

> [!TIP]
> Helpful advice for doing things better or more easily.

> [!IMPORTANT]
> Key information users need to know to achieve their goal.

> [!WARNING]
> Urgent info that needs immediate user attention to avoid problems.

> [!CAUTION]
> Advises about risks or negative outcomes of certain actions.

---

## 2. Enhanced alerts with rich content

An alert can hold most Markdown. Every line, including blank ones, needs the `>` prefix.

### Bold title, numbered list, code block and link

> [!WARNING]
> **Database Migration Alert**
>
> This migration is destructive and cannot be reversed. Please:
>
> 1. **Backup your database** before proceeding
> 2. Test in a staging environment first
> 3. Schedule downtime during low-traffic periods
>
> ```bash
> # Create backup before migration
> pg_dump production_db > backup_$(date +%Y%m%d).sql
> ```
>
> For more detail, see the [GitHub alerts documentation](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax#alerts).

### Code block with inline code

> [!TIP]
> **Performance Optimization**
>
> For better query performance, consider adding these indexes:
>
> ```sql
> CREATE INDEX idx_user_email ON users(email);
> CREATE INDEX idx_order_status ON orders(status, created_date);
> ```
>
> Monitor query performance using `EXPLAIN ANALYZE` before and after implementation.

### Java code block

> [!NOTE]
> A class that exists only to hold static methods has a private constructor:
>
> ```java
> public final class MathUtils {
>
>     private MathUtils() {
>     }
>
>     public static int square(int n) {
>         return n * n;
>     }
> }
> ```

### Bulleted list and task list

> [!IMPORTANT]
> **Before you submit**
>
> - Run all the tests
> - Check your commit messages
>
> Then tick these off:
>
> - [x] Code compiles
> - [ ] Tests pass
> - [ ] README updated

### Table

> [!NOTE]
> **Which collection?**
>
> | Need | Use |
> | :-- | :-- |
> | Lookup by key | `HashMap` |
> | Unique elements | `HashSet` |
> | Everything else | `ArrayList` |

### Heading inside an alert

> [!TIP]
> ### A heading in an alert
>
> The heading sits below the alert's own title.

### Quote inside an alert

> [!CAUTION]
> The reviewer wrote:
>
> > Do not merge this until the migration has run.

### Text formatting and an emoji

> [!NOTE]
> **Bold**, *italic*, ~~strikethrough~~, `inline code`, <kbd>Ctrl</kbd>+<kbd>C</kbd>, H<sub>2</sub>O, x<sup>2</sup> and :rocket:.

### Collapsible section inside an alert

> [!TIP]
> Try this first, then open the hint if you are stuck.
>
> <details>
> <summary>Show the hint</summary>
>
> Check the base case before the recursive call.
>
> </details>

### Mermaid diagram inside an alert

> [!NOTE]
> A diagram can sit inside an alert:
>
> ```mermaid
> flowchart LR
>     A[Start] --> B[End]
> ```

### Image inside an alert

> [!NOTE]
> An image from this repository:
>
> ![The topic map](../mind%20maps/topic_map.svg)

### Consecutive alerts with no blank line between them

This tests whether the second alert survives. It appears as a single block if not.

> [!NOTE]
> The first alert.
> [!WARNING]
> The second alert, with no blank line before it.

---

## 3. Edge cases for alert syntax

Each test shows what is being tried, then the result. Some of these render as alerts and some fall back to a plain quote, which is the point of the page.

### Lower case keyword

> [!note]
> Written as `[!note]`.

### Mixed case keyword

> [!Warning]
> Written as `[!Warning]`.

### Custom title on the same line

> [!NOTE] My custom title
> Written as `[!NOTE] My custom title`.

### Text on the same line as the keyword

> [!NOTE] All of the message is on the first line.

### Keyword with nothing after it

> [!NOTE]

### Keyword that does not exist

> [!DANGER]
> Written as `[!DANGER]`, which is a type used by other platforms.

> [!INFO]
> Written as `[!INFO]`, which is a type used by other platforms.

> [!SUCCESS]
> Written as `[!SUCCESS]`, which is a type used by other platforms.

### Space inside the brackets

> [! NOTE]
> Written as `[! NOTE]`.

### Blank line before the content

> [!NOTE]
>
> A blank quoted line follows the keyword.

### Blank line that is not quoted, in the middle of an alert

> [!NOTE]
> The first paragraph.

> The second paragraph, after an unquoted blank line.

### Alert inside a list

- A list item:

  > [!NOTE]
  > An alert indented inside a list item.

### Alert inside a quote

> A plain quote:
>
> > [!NOTE]
> > An alert nested inside a quote.

### Alert inside a collapsible section

<details>
<summary>Open to see the alert</summary>

> [!TIP]
> An alert inside `<details>`.

</details>

### Alert inside an alert

> [!WARNING]
> The outer alert.
>
> > [!NOTE]
> > The inner alert.

---

## 4. Callout alternatives that work on GitHub

These rely on ordinary Markdown, so they also work in places without alert support.

### Plain quote with an emoji and a bold label

> :bulb: **Tip:** Plain quotes with an emoji work almost anywhere.

> :warning: **Warning:** The colour comes only from the emoji.

> :information_source: **Note:** Shortcodes such as `:warning:` become emoji on GitHub.

> :x: **Do not:** Commit passwords.

> :white_check_mark: **Do:** Keep them in a file that Git ignores.

### Quote with a literal emoji

> ⚠️ **Warning:** Typed as the emoji character, not as a shortcode.

### One-cell table as a box

| :memo: Note |
| :-- |
| A table with one column draws a bordered box. |

### Coloured code block using `diff`

```diff
+ This line is green: the safe way to do it.
- This line is red: the risky way to do it.
! This line may be orange in some themes.
```

### Collapsible callout

<details>
<summary>:bulb: <b>Tip:</b> click to expand</summary>

Everything here is hidden until the reader opens it. It can hold lists:

- Markdown works here
- as long as there are blank lines around it

</details>

<details open>
<summary>:warning: <b>Warning:</b> open by default</summary>

The `open` attribute makes this section start expanded.

</details>

---

## 5. HTML that GitHub does and does not keep

GitHub removes `style` and `class` attributes and all scripts for security, so the colours below should not appear. The text should remain.

<div style="background: #fff3cd; border-left: 4px solid #ffc107; padding: 12px;">
A <code>div</code> with a yellow background and a border, set with a <code>style</code> attribute.
</div>

<div class="admonition warning">
<p class="admonition-title">Warning</p>
<p>A <code>div</code> using the class names that some documentation sites style.</p>
</div>

<blockquote>
<b>Note:</b> a raw HTML blockquote.
</blockquote>

<kbd>Ctrl</kbd>+<kbd>Shift</kbd>+<kbd>P</kbd> keeps its key-cap look.

<mark>Marked text</mark> may or may not be highlighted.

---

## 6. Syntax from other platforms

None of this is GitHub syntax, so each block should show as plain text or a plain quote. It's here to show what happens when notes written for another tool are viewed on GitHub.

### MkDocs Material

!!! note "Configuration Requirements"
    This uses `!!!` and four-space indentation.

??? tip "Collapsible"
    This uses `???` to start collapsed.

### Docusaurus and MyST

:::note
This uses triple colons.
:::

:::tip Performance Optimization
This has a title after the type.
:::

### Hugo shortcode

{{< alert "warning" >}}
This is a theme-specific shortcode.
{{< /alert >}}

### Sphinx and reStructuredText

.. note::
   This is a reStructuredText directive.

### Obsidian

> [!quote] Research Citation
> Obsidian accepts lower case types and custom titles.

> [!tip]- Collapsible Tip
> A minus sign makes this collapsible in Obsidian.
