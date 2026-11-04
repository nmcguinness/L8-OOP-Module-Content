package t17_streams_api.exercises.ex01;

import java.util.List;

public class Exercise {

    // Names longer than minLength, in alphabetical order
    public static List<String> longNames(List<String> products, int minLength) {
        if (products == null) {
            return List.of();
        }
        return products.stream()
                .filter(p -> p.length() > minLength)
                .sorted()
                .toList();
    }

    public static void run() {
        List<String> products = List.of("Lamp", "Bookshelf", "Desk", "Armchair");
        System.out.println(longNames(products, 4));
    }
}
