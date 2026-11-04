package t12_generics_2.exercises.ex03;

import java.util.List;

public final class Numbers {

    private Numbers() {
    }

    // The list produces Numbers: it might be a List<Integer>, a List<Double>, and so on
    public static double sumNumbers(List<? extends Number> nums) {
        if (nums == null) {
            return 0.0;
        }
        double sum = 0.0;
        for (Number n : nums) {
            sum += n.doubleValue();
        }
        return sum;
    }
}
