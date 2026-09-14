package t05_collections_1.demos.de02;

import java.util.Comparator;

public class StringValueComparator implements Comparator<String>
{
    @Override
    public int compare(String a, String b) { //Alan, Zach => -ve
        a = a.toLowerCase();
        b = b.toLowerCase();
        return a.compareTo(b);
    }
}
