# Exercise 10 — Invert: algorithm

This flowchart shows the algorithm in [InvertFilter.java](InvertFilter.java). Try the exercise first: see [Exercise 10 — Invert](../../../../../notes/topics/t09_interface/exercises/t09_interface_supplement_image_filters_exercises.md#exercise-10--invert) on the image filters exercise sheet.

Each pixel is handled on its own. Each colour channel is replaced by its distance from 255, and alpha is kept.

```mermaid
flowchart TD
    accTitle: InvertFilter algorithm
    accDescr: Create a result array of the same size. For each pixel, read its four channels, replace red, green and blue with 255 minus their value, and write the new pixel with the original alpha. When every pixel is done, return the result.
    start["Create a result array<br/>with the same height<br/>and width as the input"] --> more{"Any pixels<br/>left to visit?"}
    more -- "Yes" --> read["Read alpha, red,<br/>green and blue<br/>of the next pixel"]
    read --> inv["newRed = 255 - red<br/>newGreen = 255 - green<br/>newBlue = 255 - blue"]
    inv --> write["Write alpha, newRed,<br/>newGreen, newBlue<br/>to the same place<br/>in result"]
    write --> more
    more -- "No" --> done["Return result"]
```
