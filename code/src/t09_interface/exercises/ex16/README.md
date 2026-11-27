# Exercise 16 — Pixelate: algorithm

This flowchart shows the algorithm in [PixelateFilter.java](PixelateFilter.java). Try the exercise first: see [Exercise 16 — Pixelate](../../../../../notes/topics/t09_interface/exercises/t09_interface_supplement_image_filters_exercises.md#exercise-16--pixelate) on the image filters exercise sheet.

The outer loops jump from the top-left corner of one block to the next. The inner loops fill one block, and stop at the edge of the image.

```mermaid
flowchart TD
    accTitle: PixelateFilter algorithm
    accDescr: The constructor rejects a block size below 1 and otherwise stores it. In filter, create a result array of the same size. For each block, starting at top and left values that step by the block size, read the colour of the top-left pixel of the block. Work out where the block ends: the smaller of top plus block size and the height, and the same for the width. Fill every pixel of the block in the result with that colour. When every block is done, return the result.
    ctor{"Constructor:<br/>is blockSize<br/>1 or more?"} -- "No" --> bad["Throw<br/>IllegalArgumentException"]
    ctor -- "Yes" --> store["Store blockSize"]
    store -.-> start["Create a result array<br/>with the same height<br/>and width as the input"]
    start --> blocks{"Any blocks<br/>left to visit?<br/>top and left step<br/>by blockSize"}
    blocks -- "Yes" --> colour["colour = the input<br/>pixel at top, left"]
    colour --> ends["bottom = smaller of<br/>top + blockSize<br/>and height<br/>right = smaller of<br/>left + blockSize<br/>and width"]
    ends --> fill{"Any pixels left<br/>in this block?"}
    fill -- "Yes" --> write["Write colour to<br/>that pixel in result"]
    write --> fill
    fill -- "No" --> blocks
    blocks -- "No" --> done["Return result"]
```
