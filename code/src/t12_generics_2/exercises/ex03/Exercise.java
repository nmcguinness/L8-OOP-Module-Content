package t12_generics_2.exercises.ex03;

class Numbers {
    public static double sumNumbers(java.util.List<? extends Number> nums) {
        if (nums == null || nums.isEmpty())
            return 0.0;

        double sum = 0.0;

        for (Number n : nums)
            sum += n.doubleValue();

        return sum;
    }
}

public class Exercise {
    public static void run() {
        System.out.println(Numbers.sumNumbers(java.util.List.of(1, 2, 3)));        // 6.0
        System.out.println(Numbers.sumNumbers(java.util.List.of(0.5, 1.5, 2.0))); // 4.0
        System.out.println(Numbers.sumNumbers(null));                               // 0.0

        // Cannot add to List<? extends Number>:
        // java.util.List<? extends Number> b = new java.util.ArrayList<>();
        // b.add(1); // compile error
    }
}
