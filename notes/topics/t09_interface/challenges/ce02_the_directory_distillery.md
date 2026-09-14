# Challenge Exercise: Unified Contact Directory — Multi-Source Merge & HTML Formatting

## Scenario

Your organisation has a long-standing data quality problem.
Over the years, different departments—**HR**, **IT**, **Sales**, and **Management**—have
maintained their own contact spreadsheets, each with slightly different formatting styles,
typos, spacing, missing fields, and even conflicting versions of the same person’s details.

Now the company wants to retire these old spreadsheets and replace them with a **single unified
directory**.

You have been asked to produce a prototype **Contact Consolidation Tool** that can:

1. Load **3–4 CSV files** of contact data
2. **De-duplicate** people who appear in multiple files
3. Produce **high-level statistics**
4. Pass all records through an **IFormat** pipeline
5. Output a **single HTML directory**, using expressive tags such as:
   - `<strong>`
   - `<a href="tel:...">`
   - `<span class="avatar">`
   - `<span class="wa-link">`

---

## Starter Tasks

### **1. Model a Contact**

Create a `Contact` class with the usual identity, formatting, and validation responsibilities.
Think carefully about:

- How to normalise emails
- Which field(s) form the identity (for equality & hashing)
- How to represent phone numbers that appear in different styles

---

### **2. Load All CSV Files**

Read all files into memory and convert every record into a `Contact`.
Expect inconsistent formatting (spacing, casing, punctuation).
Your loader should be defensive and resilient.

---

### **3. De-dupe Using Correct Identity Logic**

Merge all contacts into a single list, then de-duplicate into a `HashSet`.
Print summary counts for:

- Raw contacts
- Unique contacts
- Duplicates removed

Be prepared to justify your equality/hashing strategy.

---

### **4. Generate High-Level Statistics**

Produce metrics such as:

- number of contacts per department
- number of contacts per email domain
- longest/shortest name
- average phone number length

You can embed these metrics in the HTML.

---

### **5. Create the IFormat Pipeline**

Define your formatting interface:

An interface named `IFormat` with a single method:

```java
Contact format(Contact c)
```

Then create formatters that:

- Add avatar initials using `<span class="avatar">`
- Highlight surnames using `<strong>`
- Convert phone numbers to `tel:` links
- Add WhatsApp links with a `wa-link` span
- Wrap emails in `mailto:` links
- Normalise names, phone numbers, and emails

Ordering matters — think like a designer of a rendering pipeline.

---

### **6. Sort the Contacts**

Sort by department, by surname, or by any sensible comparator.
Your sorted list goes into the HTML file.

---

### **7. Generate the Final HTML Output**

Create a clean HTML page that:

- Shows statistics at the top
- Displays a directory table
- Uses your pipeline’s formatted output
- Presents avatar chips, strong tags, tel links, and WhatsApp links
- Contains simple inline CSS

Your HTML document is the final deliverable.

---

## Optional Extensions

- Add an `IFilter<Contact>` stage before formatting
- Allow users to switch formatter pipelines
- Add JSON exporting
- Add clickable headers that trigger alternative sorted outputs

---

## Appendix A — Utility: Write String Content to a Text File

```java
public static boolean writeToFile(String path, String content) {
    if (path == null || content == null)
        throw new IllegalArgumentException("path/content null");

    try (BufferedWriter bw =
            Files.newBufferedWriter(Path.of(path), StandardCharsets.UTF_8)) {

        bw.write(content);
        return true;
    }
    catch (IOException e) {
        return false;
    }
}
```

---

## Appendix B — Utility: Map Department to Avatar Label/Emoji

```java
public static String avatarForDepartment(String dept) {
    if (dept == null) return "❓";

    return switch (dept.trim().toLowerCase()) {
        case "computing", "it", "information technology" -> "💻";
        case "hr", "human resources" -> "👥";
        case "sales" -> "💼";
        case "management", "mgmt" -> "⭐";
        case "finance" -> "💰";
        default -> "📁";
    };
}
```

---

## Appendix C — Useful HTML Tags for This Exercise

| Tag / Pattern | Purpose | Example |
|--------------|----------|---------|
| `<strong>` | Highlight key text | `Niall <strong>McGuinness</strong>` |
| `<a href="tel:...">` | Clickable telephone link | `<a href="tel:+353861234567">Call</a>` |
| `<a href="mailto:...">` | Clickable email link | `<a href="mailto:alex@example.com">alex@example.com</a>` |
| `<span class="avatar">` | Avatar chip for initials or emoji | `<span class="avatar">NM</span>` |
| `<span class="wa-link">` | WhatsApp contact | `<span class="wa-link"><a href="https://wa.me/353861234567">WhatsApp</a></span>` |
| `<table>` / `<tr>` / `<td>` | Tabular presentation | Directory table |
| `<style>` | Inline styling | CSS for avatar chips & spacing |
| `<!DOCTYPE html>` | Valid HTML document root | Start of your file |

---

## Appendix D — Regular Expressions for Contact Fields

Regular expressions can help you **validate** and **recognise patterns** in your input data
before turning it into `Contact` objects.
Below are some example patterns you can adapt.

> These are simplified and are **not** production-grade validators.
> They are good enough for this exercise.

---

## D1. Email Address Pattern

A simple pattern to match basic `local@domain` email shapes:

```java
// Very simple email regex (letters, digits, dots, underscores, plus, hyphen)
String EMAIL_RX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$";
```

Example usage:

```java
boolean isEmail(String value) {
    if (value == null) return false;
    return value.trim().matches(EMAIL_RX);
}
```

---

## D2. Department Name Pattern

Suppose your known department names are things like:

- `Computing`
- `HR`
- `IT`
- `Sales`
- `Management`

You can use a regex to check if a string **looks like** one of these (case-insensitive):

```java
String DEPT_RX = "^(?i)(computing|it|information technology|hr|human resources|sales|management|mgmt|finance)$";
```

Example usage:

```java
boolean isKnownDepartment(String dept) {
    if (dept == null) return false;
    return dept.trim().matches(DEPT_RX);
}
```

This lets you:

- Flag unknown departments
- Apply default avatars or categories

---

## D3. Telephone Number Pattern

For this exercise, you might want to accept:

- Leading `+`
- Digits
- Spaces and dashes as separators

A simple pattern could be:

```java
String PHONE_RX = "^\+?[0-9][0-9 \-]{5,}$";
```

Explanation:

- `^\+?` to optional leading `+`
- `[0-9]` to require at least one digit after that
- `[0-9 \-]{5,}` to then at least 5 more characters that are digits, spaces, or dashes

Example usage:

```java
boolean isPhoneLike(String phone) {
    if (phone == null) return false;
    return phone.trim().matches(PHONE_RX);
}
```

You can use this to:

- Validate the raw input before creating a `Contact`
- Decide whether to treat a string as a phone number
- Highlight invalid entries in your HTML output

---

> **Tip:** You can keep these regex strings together in a small `RegexUtils` class
> and reuse them across loaders, validators, and formatters.
