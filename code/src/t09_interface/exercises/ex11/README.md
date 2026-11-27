# Exercise 11 — Threshold: algorithm

This flowchart shows the algorithm in [ThresholdFilter.java](ThresholdFilter.java). Try the exercise first: see [Exercise 11 — Threshold](../../../../../notes/topics/t09_interface/exercises/t09_interface_supplement_image_filters_exercises.md#exercise-11--threshold) on the image filters exercise sheet.

The constructor checks and stores the level. Then each pixel becomes pure black or pure white, depending on its brightness.

```mermaid
flowchart TD
    accTitle: ThresholdFilter algorithm
    accDescr: The constructor rejects a level outside 0 to 255 and otherwise stores it. In filter, create a result array of the same size. For each pixel, work out its luminance. If the luminance is below the level, the value is 0, otherwise 255. Write a grey pixel with that value and the original alpha. When every pixel is done, return the result.
    ctor{"Constructor:<br/>is level<br/>0 to 255?"} -- "No" --> bad["Throw<br/>IllegalArgumentException"]
    ctor -- "Yes" --> store["Store level"]
    store -.-> start
    start["Create a result array<br/>with the same height<br/>and width as the input"] --> more{"Any pixels<br/>left to visit?"}
    more -- "Yes" --> lum["luminance = 0.299 × red<br/>+ 0.587 × green<br/>+ 0.114 × blue"]
    lum --> below{"Is luminance<br/>below level?"}
    below -- "Yes" --> black["value = 0<br/>black"]
    below -- "No" --> white["value = 255<br/>white"]
    black --> write["Write alpha, value,<br/>value, value to the<br/>same place in result"]
    white --> write
    write --> more
    more -- "No" --> done["Return result"]
```
