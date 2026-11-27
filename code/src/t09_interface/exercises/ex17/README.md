# Exercise 17 — Rotate: algorithm

This flowchart shows the algorithm in [Rotate90Filter.java](Rotate90Filter.java). Try the exercise first: see [Exercise 17 — Rotate](../../../../../notes/topics/t09_interface/exercises/t09_interface_supplement_image_filters_exercises.md#exercise-17--rotate) on the image filters exercise sheet.

The result has the width and height swapped. The loop visits each output pixel and works back to the input pixel it copies.

```mermaid
flowchart TD
    accTitle: Rotate90Filter algorithm
    accDescr: Create a result array with as many rows as the input has columns, and as many columns as the input has rows. For each output position, the source row is the input height minus 1 minus the output column, and the source column is the output row. Copy that input pixel into the result. When every position is done, return the result.
    start["Create a result array<br/>with width rows and<br/>height columns,<br/>so the size is swapped"] --> more{"Any output<br/>pixels left<br/>to visit?"}
    more -- "Yes" --> src["sourceRow =<br/>height - 1 - col<br/>sourceCol = row"]
    src --> copy["Copy the input pixel<br/>at sourceRow, sourceCol<br/>to result at row, col"]
    copy --> more
    more -- "No" --> done["Return result"]
```
