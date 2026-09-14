package t05_collections_1.demos.de02;
import java.util.*;

public class Demo {
    public static void run()
    {
        new Demo().start();
    }
    public void start()
    {
        ArrayList<String> sList1
                = new ArrayList<>(List.of("a","b","c","d","e"));

        ArrayList<String> sList2
                = new ArrayList<>(List.of("e","d","c","b","a"));

      //  print(sList1, true);
      //  print(sList1, false);
        print(sList1.listIterator());
        LinkedList<String> sLinkedList = new LinkedList<>(List.of("X","Y","Z"));
        print(sLinkedList.iterator());
        print(sLinkedList.descendingIterator());
        HashSet<String> shoppingList = new HashSet<>(List.of("eggs", "milk", "bread"));
        print(shoppingList.iterator());

        ArrayList<String> sListA = new ArrayList<>(List.of("a","f","g","h"));
        ArrayList<String> sListB = new ArrayList<>(List.of("a","f","g", "Z"));
        if(sListA.size() == sListB.size())
            System.out.println(findDiffIndex(
                    sListA.iterator(),
                    sListB.iterator(),
                    new StringValueComparator()));

        LinkedList<String> sLinkedListA = new LinkedList<>(List.of("X","Y","Z"));
        LinkedList<String> sLinkedListB = new LinkedList<>(List.of("AAA","Y","Z"));
        System.out.println(findDiffIndex(
                sLinkedListA.descendingIterator(),
                sLinkedListB.descendingIterator(),
                new StringLengthComparator()));
    }
    public SearchResult findDiffIndex(Iterator<String> iterA,
                                      Iterator<String> iterB,
                                      Comparator<String> comp){
        int index = 0;
        while(iterA.hasNext())
        {
            String sA = iterA.next(); String sB = iterB.next();
            if(comp.compare(sA, sB) != 0)
                return new SearchResult(index, sA, sB);
            index++;
        }
        return null;
    }
    public void print(Iterator<String> iter)
    {
        while(iter.hasNext())
            System.out.println(iter.next());
    }

    public void print(ArrayList<String> list, boolean isForward)
    {
        if(isForward) {
            for (int i = 0; i < list.size(); i++)
                System.out.println(list.get(i));
        } else {
            for (int i = list.size() - 1; i >= 0; i--)
                System.out.println(list.get(i));
        }
    }


}
