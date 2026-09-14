# Challenge Exercise: Array of Suspects - Crime License Plate Hunt

## Scenario

A tollbooth operator saw a suspicious car passing through the toll booth moments after a crime
in the area. Strangely, they remember:

1. The **county code** contained an **“L”**.
2. The **year digits (YYY)** summed to **10**.
3. The **serial contained at least two 6’s** in any position.

The Irish license plate was in the format (simplified, post-2013) shown below:

```text
[YYY] [CC] [SERIAL]
Example: 181 LH 626
```

- `YYY` to 3 digits (`18` = year, `1` = Jan–Jun, `2` = Jul–Dec).
- `CC` to county code (`LH`, `LK`, `LM`, etc.; Dublin is just `D`).
- `SERIAL` to 1–6 digit number.

---

## Starter Tasks

**Model a `LicensePlate` as a class**
- Create a class to represent a license plate with fields for year, period, county, and serial.
- Add methods to:
  - Return the sum of the digits of the year.
  - Check if the county contains an "L".
  - Check if the serial has at least two '6' digits.
- Override `toString()` for easy display.

---

### Hints

- Use a **class** to structure the plate data.
- Think about **arrays vs ArrayLists** — where would you use each?
- Consider using **regular expressions** to validate the format of input plates.
- When checking for two sixes in the serial, loop through characters or use a counting approach.
- Start small: test your class with a few known plates before generating thousands.
- Remember: arrays in Java have fixed size, but ArrayLists grow dynamically.

---

### Extensions (optional)

- Allow the user to type in a license plate and check if it matches the witness rules.
- Support multiple witnesses with different conditions (combine with AND/OR logic).
- Add sorting of results.

---

## Utility: Reading Plates from a File

Sometimes you may want to load license plate data from a text file rather than generating it
randomly. The file will contain comma-separated plate entries.

Here is a helper method that reads the file and returns an `ArrayList<String>`:

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileUtils {

    /**
     * Reads a UTF-8 text file and splits by the given single-character delimiter.
     * Newlines are ignored. Returns {@code null} on I/O error.
     */
    public static ArrayList<String> readDelimitedFile(String filePath, char delimiter) {

        //check the file path is valid
        if (filePath == null) throw new IllegalArgumentException("filePath is null");

        ArrayList<String> result = new ArrayList<>();
        StringBuilder token = new StringBuilder();

        //read each character and stop when you hit the delimiter (i.e. a comma)
        try (BufferedReader br = Files.newBufferedReader(Path.of(filePath), StandardCharsets.UTF_8)) {
            int ch;
            while ((ch = br.read()) != -1) {
                char c = (char) ch;
                if (c == '\n' || c == '\r') continue;
                if (c == delimiter) {
                    result.add(token.toString().trim());
                    token.setLength(0);
                } else {
                    token.append(c);
                }
            }
            result.add(token.toString().trim());
            return result;
        } catch (IOException e) {
            return null;
        }
    }
    // Example usage
    public static void main(String[] args) {
        ArrayList<String> plates
                = readDelimitedFile("plates_10k.txt", ',');

        if(plates == null)
            return;

        if(plates.isEmpty())
            return;

        System.out.println("Read " + plates.size() + " entries.");
        System.out.println("First 5: " + plates.subList(0, Math.min(5, plates.size())));

    }
}
```

*Hint:* Consider where in your project you might put this helper method so it can be reused.
