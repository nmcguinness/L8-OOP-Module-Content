# Exercise 13 — Sepia: algorithm

This flowchart shows the algorithm in [SepiaFilter.java](SepiaFilter.java). Try the exercise first: see [Exercise 13 — Sepia](../../../../../notes/topics/t09_interface/exercises/t09_interface_supplement_image_filters_exercises.md#exercise-13--sepia) on the image filters exercise sheet.

Each new channel is a weighted sum of all three old channels. The weights in each row add up to more than 1, so the result is clamped.

```mermaid
flowchart TD
    accTitle: SepiaFilter algorithm
    accDescr: Create a result array of the same size. For each pixel, read red, green and blue. Work out new red, green and blue as weighted sums of the old three values, rounding each one. Pack them with toArgb, which clamps any value above 255, and keep the original alpha. When every pixel is done, return the result.
    start["Create a result array<br/>with the same height<br/>and width as the input"] --> more{"Any pixels<br/>left to visit?"}
    more -- "Yes" --> read["Read r, g and b<br/>of the next pixel<br/>and keep them for<br/>all three formulas"]
    read --> red["newRed =<br/>0.393 r + 0.769 g<br/>+ 0.189 b"]
    red --> green["newGreen =<br/>0.349 r + 0.686 g<br/>+ 0.168 b"]
    green --> blue["newBlue =<br/>0.272 r + 0.534 g<br/>+ 0.131 b"]
    blue --> round["Round all three<br/>new values"]
    round --> write["Write alpha and<br/>the three new values<br/>toArgb clamps to 255"]
    write --> more
    more -- "No" --> done["Return result"]
```
