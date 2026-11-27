# Exercise 19 — Posterize: algorithm

This flowchart shows the algorithm in [PosterizeFilter.java](PosterizeFilter.java). Try the exercise first: see [Exercise 19 — Posterize](../../../../../notes/topics/t09_interface/exercises/t09_interface_supplement_image_filters_exercises.md#exercise-19--posterize) on the image filters exercise sheet.

The constructor works out the gap between two allowed values. Then each colour channel is rounded to the nearest allowed value.

```mermaid
flowchart TD
    accTitle: PosterizeFilter algorithm
    accDescr: The constructor rejects a number of levels outside 2 to 256, stores it, and works out step as 255.0 divided by levels minus 1. In filter, create a result array of the same size. For each pixel and each of red, green and blue, divide the value by step and round to get an index, then multiply the index by step and round to get the new value. Write the three new values with the original alpha. When every pixel is done, return the result.
    ctor{"Constructor:<br/>is levels<br/>2 to 256?"} -- "No" --> bad["Throw<br/>IllegalArgumentException"]
    ctor -- "Yes" --> store["Store levels<br/>step = 255.0 ÷<br/>(levels - 1)"]
    store -.-> start
    start["Create a result array<br/>with the same height<br/>and width as the input"] --> more{"Any pixels<br/>left to visit?"}
    more -- "Yes" --> chan{"Any of red, green<br/>and blue left<br/>for this pixel?"}
    chan -- "Yes" --> index["index = round<br/>(value ÷ step)"]
    index --> level["newValue = round<br/>(index × step)"]
    level --> chan
    chan -- "No" --> write["Write alpha and<br/>the three new values<br/>to result"]
    write --> more
    more -- "No" --> done["Return result"]
```
