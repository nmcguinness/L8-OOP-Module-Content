package t06_collections_2.demos.de02;

import java.util.*;

public class Demo {
    public static void run()
    {
        new Demo().start();
    }

    public void start()
    {
        var linkedList = new LinkedList<String>(List.of("a","b","c"));

        ListIterator<String> iter = linkedList.listIterator();

        iter.next();  //a
        iter.next();  //b
        System.out.println(iter.next());  //c

        iter.previous();
        System.out.println(iter.next()); //c

        iter.previous();
        iter.set("C");                   //set() sets value in place and doesnt advance iterator
        System.out.println(iter.next());

    }
}

