package t05_collections_1.demos.de04;

import java.util.ArrayList;

public class Demo {
    public static void run()
    {
        new Demo().start();
    }

    public void start()
    {
        ArrayList<Integer> a = new ArrayList<>();
        long t0 = System.nanoTime();
        for (int i = 0; i < 50_000; i++) a.add(i);             // append
        long t1 = System.nanoTime();
        for (int i = 0; i < 10_000; i++) a.add(0, i);          // insert at front
        long t2 = System.nanoTime();
        System.out.printf("append: %d us, front-insert: %d us\n", (t1-t0)/1000, (t2-t1)/1000);
    }
}
