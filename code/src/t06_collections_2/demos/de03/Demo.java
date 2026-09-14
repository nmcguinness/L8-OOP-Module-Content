package t06_collections_2.demos.de03;

import java.util.LinkedList;

public class Demo {

    public static void run(){
        new Demo().start();
    }

    public void start()
    {
        var letters = new LinkedList<Character>();
        for (char c : "aaabbcc".toCharArray())
            letters.add(c);

        var it = letters.listIterator();
        char prev = 0;
        while (it.hasNext()) {
            char cur = it.next();
            if (prev != 0 && cur != prev) {
                it.previous();    // move back to insertion point
                it.add('|');     // mark boundary by inserting bar character
                it.next();
            }
            prev = cur;
        }

    }
}
