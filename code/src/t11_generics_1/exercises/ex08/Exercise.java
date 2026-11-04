package t11_generics_1.exercises.ex08;

public class Exercise {

    public static void run() {
        ObjectPool<StringBuilder> pool = new ObjectPool<>(StringBuilder::new);

        StringBuilder first = pool.acquire();
        first.append("hello");
        pool.release(first);

        StringBuilder second = pool.acquire();
        System.out.println("Same object reused: " + (first == second));
        System.out.println("Still holds: " + second);

        second.setLength(0);
        pool.release(second);
        pool.acquire();
        pool.acquire();
        System.out.println("Objects created: " + pool.created());
    }
}
