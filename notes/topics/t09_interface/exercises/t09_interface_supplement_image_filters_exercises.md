---
title: "Image Filters — Exercises"
subtitle: "COMP C8Z03 — Year 2 OOP"
topic_code: t09_interface
description: "Ten step-by-step exercises for the t09 image filters supplement: invert, threshold, horizontal flip and sepia filters behind IImageFilter, a filter-order experiment, a JUnit test for a filter, and pixelate, rotate, brightness and posterize filters."
related_notes: t09_interface_supplement_image_filters
created: 2026-11-27
last_updated: 2026-11-27
version: 1.2
status: published
authors: ["OOP Teaching Team"]
tags: [java, interfaces, polymorphism, image-processing, 2d-arrays, junit, exercises, year2, comp-c8z03]
---
# Image Filters — Exercises

These exercises practise writing new classes that implement `IImageFilter`, from the [image filters supplement](../t09_interface_supplement_image_filters.md). Each new filter goes in a `List<IImageFilter>` next to the filters you already have, and none of the existing code changes. They follow on from the [t09 exercises](t09_interface_exercises.md), so they are numbered 10 to 19.

Every exercise follows the same layout: a goal, the steps, a worked example, the rules, hints you can open if you are stuck, and a checklist. Each one ends with a link to a worked solution and to its tests.

| Exercise | You practise | Difficulty |
| :-- | :-- | :-- |
| 10 Invert | implementing `IImageFilter`, reading and packing channels | Foundation |
| 11 Threshold | a filter with a constructor setting, a three-filter pipeline | Foundation |
| 12 Flip | working out which input pixel each output pixel copies | Intermediate |
| 13 Sepia | weighted sums of channels, clamping | Intermediate |
| 14 Filter order | the same filters in a different order | Intermediate |
| 15 Testing a filter | a JUnit test on a tiny image | Stretch |
| 16 Pixelate | loops that step by a block, partial blocks at the edges | Intermediate |
| 17 Rotate | an index formula, a result with width and height swapped | Stretch |
| 18 Brightness | a signed constructor setting, clamping at both ends | Foundation |
| 19 Posterize | rounding with `double`, counting colours with a `HashSet` | Intermediate |

## How to run

Each exercise goes in its own package, `t09_interface.exercises.exNN`. Create a class `Exercise` in that package with a static `run()` method, and call `Exercise.run()` from `Main`:

```java
package t09_interface.exercises.ex10;

public class Exercise {

    public static void run() {
        // your code here
    }
}
```

Your filters use the interface and helper from the supplement, so import them:

```java
import t09_interface.demos.de01.IImageFilter;
import t09_interface.demos.de01.ImageHelper;
import t09_interface.demos.de01.ImageProcessor;
```

Every exercise reads `code/data/t09_image_filters/input.png` and writes to `code/data/t09_image_filters/output/`. Build each path from `ImageHelper.dataFolder()`, for example `ImageHelper.dataFolder() + "input.png"`. It finds the folder whether the program runs from the project root, as IntelliJ does, or from the `code` folder. The examples below were run from the `code` folder; from the project root, each printed path starts with `code/`. `ImageHelper.readPng` and `ImageHelper.writePng` declare `throws IOException`, so call them inside a `try` block with `catch (IOException e)`, as `Demo` does. [t10](../../t10_exception_handling/t10_exception_handling_notes.md) explains why.

---

## Exercise 10 — Invert

**Goal:** Write your first filter class: a photo negative. Each colour channel value `v` becomes `255 - v`.

**You will need:** [Step 1](../t09_interface_supplement_image_filters.md#step-1--an-image-is-a-2d-array-of-pixels), [Step 2](../t09_interface_supplement_image_filters.md#step-2--the-interface), [Step 4](../t09_interface_supplement_image_filters.md#step-4--greyscalefilter) and [ImageProcessor](../t09_interface_supplement_image_filters.md#running-a-whole-list-imageprocessor) in the supplement.

**Steps**

1. Create a class `InvertFilter` that implements `IImageFilter`.
2. In `filter`, read the height and width, and create a new `result` array of the same size.
3. For each pixel, read the four channels with `ImageHelper`. Pack `alpha`, `255 - red`, `255 - green` and `255 - blue` into the result with `ImageHelper.toArgb`.
4. In `run()`, load the input image, apply an `InvertFilter`, and save the result as `ex10_InvertFilter.png`.
5. Print the top-left pixel before and after with `ImageHelper.describe`.
6. Put an `InvertFilter` and a `GreyscaleFilter` in a `List<IImageFilter>`, run the list with `ImageProcessor.applyAll`, and save the result as `ex10_Invert_then_Greyscale.png`.

```java
public int[][] filter(int[][] pixels)
```

**Example**

```text
Top-left pixel before: a=255 r=70 g=130 b=200
Top-left pixel after:  a=255 r=185 g=125 b=55
Saved data/t09_image_filters/output/ex10_InvertFilter.png
Saved data/t09_image_filters/output/ex10_Invert_then_Greyscale.png
```

**Rules**

- Keep alpha as it is. Do not invert it, or opaque pixels become transparent.
- Do not change the array you are given. Write only to `result`.

<details>
<summary>Hints</summary>

1. Copy the shape of `GreyscaleFilter.filter`: same size, same two loops, one new line inside.
2. `ImageHelper.toArgb(ImageHelper.alpha(pixel), 255 - ImageHelper.red(pixel), ...)`. Finish the green and blue arguments the same way.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] `ex10_InvertFilter.png` shows a negative: the yellow sun is dark blue.
- [ ] `InvertFilter` does not override `name()`, but `filter.name()` still returns `"InvertFilter"`.

**Package:** `t09_interface.exercises.ex10`

**Solution:** [t09 exercise 10 solution code](../../../../code/src/t09_interface/exercises/ex10/). Its [README](../../../../code/src/t09_interface/exercises/ex10/README.md) has a flowchart of the algorithm.
**Tests:** [t09 exercise 15 tests](../../../../code/test/t09_interface/exercises/ex15/InvertFilterTest.java), which you write yourself in exercise 15.

---

## Exercise 11 — Threshold

**Goal:** Write a filter with a setting. `ThresholdFilter(level)` turns every pixel pure black or pure white. Put it after `SobelFilter` so that the edges become clean white lines on black.

**You will need:** [Step 5](../t09_interface_supplement_image_filters.md#step-5--boxblurfilter) (a filter with a constructor setting) and [Step 6](../t09_interface_supplement_image_filters.md#step-6--sobelfilter) of the supplement.

**Steps**

1. Create `ThresholdFilter` with a `private final int level` field, set by the constructor.
2. In the constructor, throw `IllegalArgumentException` if `level` is below 0 or above 255.
3. In `filter`, work out each pixel's brightness with `GreyscaleFilter.luminance(pixel)`. Below `level`, the result pixel is black (0, 0, 0). Otherwise it is white (255, 255, 255). Keep the alpha.
4. Override `name()` to return `"ThresholdFilter_"` followed by the level, for example `"ThresholdFilter_80"`.
5. Write a static method `countWhite` in `Exercise` that counts the pixels whose red channel is 255.
6. In `run()`, build a list of `BoxBlurFilter(1)`, `SobelFilter` and `ThresholdFilter(80)`. Run it with `ImageProcessor.applyAll`, save it as `"ex11_" + name + ".png"` using the last filter's name, and print the white pixel count.

```java
public ThresholdFilter(int level)
public int[][] filter(int[][] pixels)
public String name()
public static int countWhite(int[][] pixels)
```

**Example**

```text
White pixels: 4381 of 76800
Saved data/t09_image_filters/output/ex11_ThresholdFilter_80.png
```

**Rules**

- A pixel whose luminance equals `level` becomes white.
- Compare luminance, not the red channel. Pure blue has red 0 but luminance 29.

<details>
<summary>Hints</summary>

1. `int value = GreyscaleFilter.luminance(pixel) < level ? 0 : 255;` then pack `value` into red, green and blue.
2. `filters.getLast().name()` gives the name of the last filter in the list.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] The saved image has only pure black and pure white pixels.
- [ ] `new ThresholdFilter(256)` throws `IllegalArgumentException`.

**Think about it:** try levels 40 and 160. How does the white pixel count change, and which edges disappear first?

**Package:** `t09_interface.exercises.ex11`

**Solution:** [t09 exercise 11 solution code](../../../../code/src/t09_interface/exercises/ex11/). Its [README](../../../../code/src/t09_interface/exercises/ex11/README.md) has a flowchart of the algorithm.
**Tests:** [t09 exercise 11 tests](../../../../code/test/t09_interface/exercises/ex11/).

---

## Exercise 12 — Flip

**Goal:** Write a filter that mirrors the image left to right. The hard part is working out which input pixel each output pixel copies.

**You will need:** [Step 1](../t09_interface_supplement_image_filters.md#step-1--an-image-is-a-2d-array-of-pixels) and [Step 7](../t09_interface_supplement_image_filters.md#step-7--scalefilter) of the supplement.

**Steps**

1. Create `FlipHorizontalFilter`, which implements `IImageFilter`.
2. On paper, take a row of width 5. Write down which input column goes to output columns 0, 1, 2, 3 and 4. Find a formula for the input column in terms of `col` and `width`.
3. In `filter`, copy each pixel from the input column your formula gives. The row does not change.
4. In `run()`, flip the input image and save it as `ex12_FlipHorizontalFilter.png`.
5. Print the pixel at row 200, column 50 of the original, and the pixel at the mirrored column of the result. They must be the same.

**Example**

```text
Before: (200, 50) a=255 r=200 g=60 b=50
After:  (200, 269) a=255 r=200 g=60 b=50
Saved data/t09_image_filters/output/ex12_FlipHorizontalFilter.png
```

**Rules**

- The image is 320 pixels wide, so the last column is 319, not 320.
- The result has the same size as the input.

<details>
<summary>Hints</summary>

1. For width 5: output column 0 copies input column 4, and output column 4 copies input column 0.
2. The input column is `width - 1 - col`. If you get `ArrayIndexOutOfBoundsException: Index 320`, you left out the `- 1`.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] In the saved image, the house is on the right and the sun is on the left.
- [ ] Flipping twice gives back the original image.

**Package:** `t09_interface.exercises.ex12`

**Solution:** [t09 exercise 12 solution code](../../../../code/src/t09_interface/exercises/ex12/). Its [README](../../../../code/src/t09_interface/exercises/ex12/README.md) has a flowchart of the algorithm.
**Tests:** [t09 exercise 12 tests](../../../../code/test/t09_interface/exercises/ex12/).

---

## Exercise 13 — Sepia

**Goal:** Write a filter where each new channel mixes all three old channels, and see why it needs clamping.

**You will need:** [Step 1](../t09_interface_supplement_image_filters.md#step-1--an-image-is-a-2d-array-of-pixels) , [Step 4](../t09_interface_supplement_image_filters.md#step-4--greyscalefilter) and [ImageProcessor](../t09_interface_supplement_image_filters.md#running-a-whole-list-imageprocessor) in the supplement.

**Steps**

1. Create `SepiaFilter`, which implements `IImageFilter`.
2. For each pixel, read `r`, `g` and `b`, and work out the three new channels with the formulas below. Round each one with `Math.round`.
3. Pack the original alpha and the three new values with `ImageHelper.toArgb`.
4. In `run()`, apply the filter, save it as `ex13_SepiaFilter.png`, and print two pixels before and after: the sky at (0, 0) and the sun at row 50, column 250.

```text
newRed   = 0.393 r + 0.769 g + 0.189 b
newGreen = 0.349 r + 0.686 g + 0.168 b
newBlue  = 0.272 r + 0.534 g + 0.131 b
```

**Example**

```text
Sky before:  a=255 r=70 g=130 b=200
Sky after:   a=255 r=165 g=147 b=115
Sun before:  a=255 r=255 g=200 b=40
Sun after:   a=255 r=255 g=233 b=181
Saved data/t09_image_filters/output/ex13_SepiaFilter.png
```

**Rules**

- Pack with `ImageHelper.toArgb`, which clamps each channel to 0 to 255.
- Use the old `r`, `g` and `b` in all three formulas, not a new value you have just worked out.

<details>
<summary>Hints</summary>

1. `int newRed = (int) Math.round(0.393 * r + 0.769 * g + 0.189 * b);`
2. For the sun, `newRed` works out at about 262. `toArgb` stores 255. Without clamping, the extra bits spill into the alpha channel and the colour is wrong.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] The saved image has a warm brown tone.
- [ ] You can say why `GreyscaleFilter` never needs clamping but `SepiaFilter` does. (Add up each row of weights.)

> [!WARNING]
> Work out all three new values before you pack them. If you overwrite `r` with `newRed` first, the green and blue formulas use the wrong red.

**Package:** `t09_interface.exercises.ex13`

**Solution:** [t09 exercise 13 solution code](../../../../code/src/t09_interface/exercises/ex13/). Its [README](../../../../code/src/t09_interface/exercises/ex13/README.md) has a flowchart of the algorithm.
**Tests:** [t09 exercise 13 tests](../../../../code/test/t09_interface/exercises/ex13/).

---

## Exercise 14 — Filter order

**Goal:** Run the same three filter objects in two orders and explain why the results differ. No new filter class is needed.

**You will need:** [Step 8](../t09_interface_supplement_image_filters.md#step-8--the-demo-one-filter-per-image-then-two-in-series) and [ImageProcessor](../t09_interface_supplement_image_filters.md#running-a-whole-list-imageprocessor) of the supplement.

**Steps**

1. In `run()`, build a first list: `GreyscaleFilter`, `BoxBlurFilter(1)`, `SobelFilter`.
2. Build a second list with the same objects in reverse order. `new ArrayList<>(list.reversed())` does this.
3. Run both lists on the input image with `ImageProcessor.applyAll` and save both results.
4. Write `countDifferent(a, b)`, which counts the positions where two same-sized images hold different pixels.
5. Print the filter names in each order, the number of pixels that differ, and the number of bright pixels (red above 128) in each result.
6. Open both images side by side and write two or three sentences on why they differ.

```java
public static int countDifferent(int[][] a, int[][] b)
```

**Example**

```text
First order:    GreyscaleFilter -> BoxBlurFilter_r1 -> SobelFilter
Reversed order: SobelFilter -> BoxBlurFilter_r1 -> GreyscaleFilter
Pixels that differ: 28166 of 76800
Bright pixels (red > 128), first order:    2758
Bright pixels (red > 128), reversed order: 2491
```

**Rules**

- Use the same three filter objects in both lists. Do not create new ones.
- `countDifferent` compares whole pixels with `!=`.

<details>
<summary>Hints</summary>

1. Ask what each filter needs as input. Sobel works on brightness, so does it matter whether greyscale runs before it?
2. In the first order, the blur smooths the picture before Sobel looks for edges. In the reversed order, the blur softens the edge lines that Sobel has already drawn.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] Your explanation says which filter's position matters and which does not.

**Think about it:** the reversed order ends with `GreyscaleFilter`. Does that step change anything? Check by removing it.

**Package:** `t09_interface.exercises.ex14`

**Solution:** [t09 exercise 14 solution code](../../../../code/src/t09_interface/exercises/ex14/). The explanation is in the class comment.
**Tests:** [t09 exercise 14 tests](../../../../code/test/t09_interface/exercises/ex14/).

---

## Exercise 15 — Testing a filter

**Goal:** Write JUnit tests for your `InvertFilter` from exercise 10. Use images so small that you can work out every expected pixel by hand.

**You will need:** the supplement's [ImageFiltersTest.java](../../../../code/test/t09_interface/demos/de01/ImageFiltersTest.java) as a model. [t23 Unit Testing](../../t23_unit_testing/t23_unit_testing_notes.md) covers JUnit in full later; this exercise needs only `@Test` and `assertEquals`.

**Steps**

1. Create `InvertFilterTest` in `code/test/t09_interface/exercises/ex15/`, in the package `t09_interface.exercises.ex15`. Import `InvertFilter` from `ex10`.
2. Write a test that inverts a 1 x 1 image holding `toArgb(255, 70, 130, 200)` and checks the result is `toArgb(255, 185, 125, 55)`.
3. Write a test that checks alpha is kept.
4. Write a test that inverts twice and checks you get the original pixels back.
5. Write a test that checks the input array is not changed.
6. Write a test that checks the result has the same height and width as the input.

**Example**

A 1 x 1 image is a 2D array with one row of one pixel:

```java
int[][] input = {{ImageHelper.toArgb(255, 70, 130, 200)}};
int[][] result = new InvertFilter().filter(input);
assertEquals(ImageHelper.toArgb(255, 185, 125, 55), result[0][0]);
```

**Rules**

- Do not load `input.png` in a test. A tiny array is faster, and you know every expected value.
- Give each test method a name that says what it checks, for example `invert_keepsAlpha`.

<details>
<summary>Hints</summary>

1. `assertNotSame(input, result)` checks that `filter` returned a new array.
2. An `int[3][5]` array is a valid 3 x 5 image of transparent black pixels. You do not need to fill it to test the size.

</details>

**Done when**

- [ ] All five tests pass.
- [ ] If you break `InvertFilter` on purpose (for example, invert alpha too), at least one test fails.

**Package:** `t09_interface.exercises.ex15` (in `code/test`)

**Solution:** [t09 exercise 15 solution tests](../../../../code/test/t09_interface/exercises/ex15/InvertFilterTest.java).

---

## Exercise 16 — Pixelate

**Goal:** Write a filter that gives the image a blocky mosaic look. The new skill is a loop that jumps from one block to the next, not from one pixel to the next.

**You will need:** [Step 5](../t09_interface_supplement_image_filters.md#step-5--boxblurfilter) of the supplement (a filter with a constructor setting, and a bounds check).

**Steps**

1. Create `PixelateFilter` with a `private final int blockSize` field, set by the constructor. Reject a `blockSize` below 1 with `IllegalArgumentException`.
2. Override `name()` to return `"PixelateFilter_"` followed by the block size, for example `"PixelateFilter_30"`.
3. In `filter`, write two outer loops that visit the top-left corner of each block: `top` goes 0, `blockSize`, `2 * blockSize` and so on down the rows, and `left` does the same across the columns.
4. For each block, read the colour of its top-left pixel, `pixels[top][left]`.
5. Write two inner loops that copy that colour into every pixel of the block in `result`. Stop each inner loop at the edge of the image.
6. In `run()`, pixelate `input.png` with a block size of 30, save it, and print the facts shown below. Then pixelate `images/jelly_beans.png` and save that too.

```java
public PixelateFilter(int blockSize)
public int[][] filter(int[][] pixels)
public String name()
```

**Example**

```text
Image: 320 x 240 pixels
Blocks: 11 across, 8 down
(200, 50) now copies (180, 30): true
Last block is 20 pixels wide
Saved data/t09_image_filters/output/ex16_PixelateFilter_30.png
Saved data/t09_image_filters/output/ex16_jelly_beans_PixelateFilter_30.png
```

**Rules**

- 320 is not a multiple of 30, so the last block in each row is only 20 pixels wide. It must still be filled.
- A block size of 1 leaves the image unchanged.
- A block size bigger than the image fills the whole image with the colour of pixel (0, 0).

<details>
<summary>Hints</summary>

1. `for (int top = 0; top < height; top += blockSize)` visits the first row of each block.
2. Work out where each block ends before the inner loops: `int bottom = Math.min(top + blockSize, height);` and the same for `right`. Then the inner loops never go past the edge.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] The saved image shows the test picture as large flat squares.
- [ ] The right-hand column of blocks is narrower than the rest, and has no black gap.

**Think about it:** this filter copies the top-left pixel of each block. How would you change it to use the **average** colour of the block instead? Which part of `BoxBlurFilter` could you reuse?

**Package:** `t09_interface.exercises.ex16`

**Solution:** [t09 exercise 16 solution code](../../../../code/src/t09_interface/exercises/ex16/). Its [README](../../../../code/src/t09_interface/exercises/ex16/README.md) has a flowchart of the algorithm.
**Tests:** [t09 exercise 16 tests](../../../../code/test/t09_interface/exercises/ex16/).

---

## Exercise 17 — Rotate

**Goal:** Write a filter that turns the image a quarter turn clockwise. Like exercise 12, the hard part is the index formula. Unlike exercise 12, the result has a different shape from the input.

**You will need:** [Step 7](../t09_interface_supplement_image_filters.md#step-7--scalefilter) of the supplement (a filter that returns a different-sized array) and your solution to [exercise 12](#exercise-12--flip).

**Steps**

1. Create `Rotate90Filter`, which implements `IImageFilter`.
2. In `filter`, create the result with the width and height swapped: `new int[width][height]`. A 320 x 240 image becomes 240 x 320.
3. On paper, draw a 2 x 3 grid holding the numbers 1 to 6, and draw it again turned clockwise. For each output position, write down which input position it came from. Find a formula for the input row and column in terms of the output `row` and `col`.
4. Fill `result` with your formula.
5. In `run()`, rotate `input.png` and save it. Print the size before and after, and check that the new top-left pixel is the old bottom-left pixel.
6. Write a static method `sameImage(a, b)` that returns `true` when two images have the same size and the same pixels. Rotate the image four times and check you get the original back.

```java
public int[][] filter(int[][] pixels)
public static boolean sameImage(int[][] a, int[][] b)
```

**Example**

```text
Before: 320 x 240 pixels
After:  240 x 320 pixels
New top-left is the old bottom-left: true
Four turns give the original: true
Saved data/t09_image_filters/output/ex17_Rotate90Filter.png
```

**Rules**

- The result has as many rows as the input has columns, and as many columns as the input has rows.
- Do not assume the image is square. A formula that only works for square images fails on `input.png` with `ArrayIndexOutOfBoundsException`.

<details>
<summary>Hints</summary>

1. Turning clockwise moves the left column of the input to the top row of the result, read from bottom to top. So `result[0][0]` is the input's bottom-left pixel, `pixels[height - 1][0]`.
2. The formula is `result[row][col] = pixels[height - 1 - col][row]`. Check it on your 2 x 3 grid from step 3.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] In the saved image, the grass is down the left side and the sky is on the right.
- [ ] Rotating four times gives back the original image.

**Think about it:** you could also rotate anticlockwise by applying `Rotate90Filter` three times. Put three `Rotate90Filter` objects in a list and run it with `ImageProcessor.applyAll`. Is that a good way to write an anticlockwise filter? What does it cost?

**Package:** `t09_interface.exercises.ex17`

**Solution:** [t09 exercise 17 solution code](../../../../code/src/t09_interface/exercises/ex17/). Its [README](../../../../code/src/t09_interface/exercises/ex17/README.md) has a flowchart of the algorithm.
**Tests:** [t09 exercise 17 tests](../../../../code/test/t09_interface/exercises/ex17/).

---

## Exercise 18 — Brightness

**Goal:** Write a filter that makes the image brighter or darker, and see clamping at work at both ends of the 0 to 255 range.

**You will need:** [Step 1](../t09_interface_supplement_image_filters.md#step-1--an-image-is-a-2d-array-of-pixels) of the supplement and your solution to [exercise 10](#exercise-10--invert).

**Steps**

1. Create `BrightnessFilter` with a `private final int amount` field, set by the constructor. Reject an amount below -255 or above 255 with `IllegalArgumentException`.
2. In `filter`, add `amount` to the red, green and blue channels of each pixel. Keep the alpha.
3. Pack the new values with `ImageHelper.toArgb`, which clamps each channel to 0 to 255.
4. Override `name()` to return `"BrightnessFilter_"` followed by the amount, for example `"BrightnessFilter_-60"`.
5. In `run()`, make one filter with `60` and one with `-60`. Apply both to `input.png`, save both, and print the sky pixel at (0, 0) and the sun pixel at row 50, column 250, as shown.

```java
public BrightnessFilter(int amount)
public int[][] filter(int[][] pixels)
public String name()
```

**Example**

```text
Sky:     a=255 r=70 g=130 b=200
  +60:   a=255 r=130 g=190 b=255
  -60:   a=255 r=10 g=70 b=140
Sun:     a=255 r=255 g=200 b=40
  +60:   a=255 r=255 g=255 b=100
Saved data/t09_image_filters/output/ex18_BrightnessFilter_60.png
Saved data/t09_image_filters/output/ex18_BrightnessFilter_-60.png
```

**Rules**

- A negative amount darkens the image. Do not write a separate `DarknessFilter`.
- Do not change alpha, or the image turns transparent.

<details>
<summary>Hints</summary>

1. Copy the shape of your `InvertFilter`. Only the three channel expressions change: `ImageHelper.red(pixel) + amount`, and so on.
2. You do not need `Math.min` or `Math.max` yourself. `toArgb` already clamps.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] The sky's blue channel stops at 255 with +60, and the sun's red channel stays at 255.
- [ ] One class makes both the brighter and the darker image.

**Think about it:** apply `BrightnessFilter(60)` and then `BrightnessFilter(-60)`. Do you get the original image back? Look at the sun's red channel.

**Package:** `t09_interface.exercises.ex18`

**Solution:** [t09 exercise 18 solution code](../../../../code/src/t09_interface/exercises/ex18/). Its [README](../../../../code/src/t09_interface/exercises/ex18/README.md) has a flowchart of the algorithm.
**Tests:** [t09 exercise 18 tests](../../../../code/test/t09_interface/exercises/ex18/).

---

## Exercise 19 — Posterize

**Goal:** Write a filter that allows only a few values per channel, which gives a photo a flat, poster-like look. Then count how many colours are left.

**You will need:** [Step 1](../t09_interface_supplement_image_filters.md#step-1--an-image-is-a-2d-array-of-pixels) of the supplement, and `HashSet` from [t07](../../t07_collections_3_set_map/t07_collections_3_set_map_notes.md).

**Steps**

1. Create `PosterizeFilter` with a `private final int levels` field. Reject `levels` below 2 or above 256 with `IllegalArgumentException`.
2. In the constructor, also work out the gap between two allowed values: `step = 255.0 / (levels - 1)`. Store it in a `private final double` field. For 4 levels the step is 85.0, so the allowed values are 0, 85, 170 and 255.
3. Write a method `nearestLevel(int value)`. Divide `value` by `step` and round, to find which allowed value is nearest. Multiply back by `step` and round again.
4. In `filter`, apply `nearestLevel` to the red, green and blue channels of each pixel. Keep the alpha.
5. Override `name()` to return `"PosterizeFilter_"` followed by the number of levels.
6. In `Exercise`, write `countColours`, which uses a `HashSet<Integer>` to count the different pixel values in an image.
7. In `run()`, posterize `images/jelly_beans.png` with 4 levels, save it, and print the top-left pixel and the number of colours before and after.

```java
public PosterizeFilter(int levels)
int nearestLevel(int value)
public int[][] filter(int[][] pixels)
public String name()
public static int countColours(int[][] pixels)
```

**Example**

```text
Top-left before: a=255 r=143 g=144 b=107
Top-left after:  a=255 r=170 g=170 b=85
Different colours before: 18646
Different colours after:  13
Saved data/t09_image_filters/output/ex19_jelly_beans_PosterizeFilter_4.png
```

**Rules**

- Use `double` for `step`. With 3 levels the step is 127.5, and integer division would give 127, so 255 could never be reached.
- `nearestLevel(255)` must return 255, and `nearestLevel(0)` must return 0.

<details>
<summary>Hints</summary>

1. For 4 levels, 143 / 85.0 is about 1.68, which rounds to 2. Then 2 * 85.0 is 170.
2. `Math.round` on a `double` returns a `long`, so cast the final result: `(int) Math.round(index * step)`.

</details>

**Done when**

- [ ] `run()` prints the output shown.
- [ ] The saved image shows the jelly beans in a few flat colours.
- [ ] With 256 levels, the image does not change.

**Think about it:** 4 levels per channel allows 4 x 4 x 4 = 64 colours. Why does the jelly beans image use only 13 of them?

**Package:** `t09_interface.exercises.ex19`

**Solution:** [t09 exercise 19 solution code](../../../../code/src/t09_interface/exercises/ex19/). Its [README](../../../../code/src/t09_interface/exercises/ex19/README.md) has a flowchart of the algorithm.
**Tests:** [t09 exercise 19 tests](../../../../code/test/t09_interface/exercises/ex19/).
