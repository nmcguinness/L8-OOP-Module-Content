package t11_generics_1.challenges.ce03;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PrintHelper
{
    /// <summary>
    /// Prints map contents sorted by key using the key's toString() ordering.
    /// Keeps things simple for Year 2: no Comparable constraint needed.
    /// </summary>
    public static <K, V> void printSortedByKey(Map<K, V> map)
    {
        if (map == null)
            throw new IllegalArgumentException("map is null.");

        List<Map.Entry<K, V>> entries = new ArrayList<>(map.entrySet());
        entries.sort(Comparator.comparing(e -> e.getKey().toString()));

        for (Map.Entry<K, V> entry : entries)
        {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

