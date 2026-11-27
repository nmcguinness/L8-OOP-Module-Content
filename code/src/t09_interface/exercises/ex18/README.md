# Exercise 18 — Brightness: algorithm

This flowchart shows the algorithm in [BrightnessFilter.java](BrightnessFilter.java). Try the exercise first: see [Exercise 18 — Brightness](../../../../../notes/topics/t09_interface/exercises/t09_interface_supplement_image_filters_exercises.md#exercise-18--brightness) on the image filters exercise sheet.

The constructor checks and stores the amount. Then the same amount is added to each colour channel, and toArgb clamps the result at both ends.

```mermaid
flowchart TD
    accTitle: BrightnessFilter algorithm
    accDescr: The constructor rejects an amount outside -255 to 255 and otherwise stores it. In filter, create a result array of the same size. For each pixel, add the amount to red, green and blue. Pack the values with toArgb, which clamps them to 0 to 255, and keep the original alpha. When every pixel is done, return the result.
    ctor{"Constructor:<br/>is amount<br/>-255 to 255?"} -- "No" --> bad["Throw<br/>IllegalArgumentException"]
    ctor -- "Yes" --> store["Store amount"]
    store -.-> start
    start["Create a result array<br/>with the same height<br/>and width as the input"] --> more{"Any pixels<br/>left to visit?"}
    more -- "Yes" --> add["newRed =<br/>red + amount<br/>newGreen =<br/>green + amount<br/>newBlue =<br/>blue + amount"]
    add --> write["Write alpha and<br/>the three new values<br/>toArgb clamps to 0..255"]
    write --> more
    more -- "No" --> done["Return result"]
```
