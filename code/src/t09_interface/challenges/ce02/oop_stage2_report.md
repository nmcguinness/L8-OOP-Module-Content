# OOP - GCA - Stage 2 Report

## Adil Orijeh, Tom Brown

## Report Components

### Equality & Hashing Rationale

### Testing Summary

### Defensive Coding

Here is an example us null checking in our solution

```java

public Contact format(Contact input) {
        String email = input.getEmail();
        if (email == null || email.isBlank()) {
            return input;
        }

        String trimmed = email.trim();
        String html = "<a href=\"mailto:" + trimmed + "\">" + trimmed + "</a>";
        return input.withEmail(html);
    }
```

### Reflections

### Commit Contributions

You just write your text and if I want *italics* and if I want **bold**.

### Contribution Matrix

| Function | Adil | Tom |
|:-|-:|:-:|
| Write JUnit tests | [x] | []|

### Final References

- [DkIT — Dundalk Institute of Technology](https://www.dkit.ie) [Accessed: Nov 2025]
