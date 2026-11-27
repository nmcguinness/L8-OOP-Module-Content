# Exercise 12 — Flip: algorithm

This flowchart shows the algorithm in [FlipHorizontalFilter.java](FlipHorizontalFilter.java). Try the exercise first: see [Exercise 12 — Flip](../../../../../notes/topics/t09_interface/exercises/t09_interface_supplement_image_filters_exercises.md#exercise-12--flip) on the image filters exercise sheet.

Each output pixel copies the pixel in the same row at the mirrored column.

```mermaid
flowchart TD
    accTitle: FlipHorizontalFilter algorithm
    accDescr: Create a result array of the same size. For each position, work out the mirrored column as width minus 1 minus the column, and copy the input pixel at the same row and the mirrored column into the result. When every position is done, return the result.
    start["Create a result array<br/>with the same height<br/>and width as the input"] --> more{"Any pixels<br/>left to visit?"}
    more -- "Yes" --> mirror["sourceCol =<br/>width - 1 - col"]
    mirror --> copy["Copy the input pixel<br/>at row, sourceCol<br/>to result at row, col"]
    copy --> more
    more -- "No" --> done["Return result"]
```
