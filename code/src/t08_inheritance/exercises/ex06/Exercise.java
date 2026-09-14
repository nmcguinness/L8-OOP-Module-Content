package t08_inheritance.exercises.ex06;

public class Exercise {

    public static void run() {
        BaseFormatter a = new BaseFormatter();
        BaseFormatter b = new FancyFormatter();

        System.out.println(a.format()); // Base format
        System.out.println(b.format()); // Fancy format
    }
}
