package t11_generics_1.challenges.ce03;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exercise {

    public static void run() {
        new Exercise().start();
    }

    public void start() {

        //helper to tell you what directory to put your data file in
        System.out.println("Put your data file in: " + System.getProperty("user.dir"));
//
//        System.out.println("=== Part A: Country code frequency (CSV) ===");
//        runStringTest();
//
//        System.out.println("=== Part B: Weapon inventory frequency (XML) ===");
//        runXMLTest();
    }

//    public void runStringTest()
//    {
//        String filePath = "code/data/ce03/country_codes.csv";
//        Path csvPath = Path.of(filePath);
//
//        List<String> codes;
//
//        try {
//            codes = t11_generics_1.challenges.ce03.FileHelper.readStringsFromCsv(csvPath, false);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        long start = System.nanoTime();
//        Map<String, Integer> counts = countByKey(codes);
//        long end = System.nanoTime();
//
//
//
//        System.out.println("Rows read: " + codes.size());
//        System.out.println("Unique codes: " + counts.size());
//        long elapsedNs = end - start;
//        double elapsedMs = elapsedNs / 1_000_000.0;
//        System.out.println("Elapsed: " + elapsedMs + " ms");
//        System.out.println();
//
//        System.out.println("Sorted report:");
//        t11_generics_1.challenges.ce03.PrintHelper.printSortedByKey(counts);
//    }
//
//    public void runXMLTest(){
//        String filePath = "code/data/ce03/weapon_inventory.xml";
//        Path xmlPath = Path.of(filePath);
//
//        List<t11_generics_1.challenges.ce03.Weapon> weapons;
//        try {
//            weapons = t11_generics_1.challenges.ce03.FileHelper.readWeaponsFromXml(xmlPath);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        long start = System.nanoTime();
//        Map<t11_generics_1.challenges.ce03.Weapon, Integer> counts = countByKey(weapons);
//        long end = System.nanoTime();
//
//        System.out.println("Weapons read: " + weapons.size());
//        System.out.println("Unique weapon names: " + counts.size());
//        long elapsedNs = end - start;
//        double elapsedMs = elapsedNs / 1_000_000.0;
//        System.out.println("Elapsed: " + elapsedMs + " ms");
//        System.out.println();
//
//        System.out.println("Sorted report:");
//        t11_generics_1.challenges.ce03.PrintHelper.printSortedByKey(counts);
//    }
//
//    public <T> Map<T, Integer> countByKey(List<T> items)
//    {
//        Map<T, Integer> counts = new HashMap<>();
//        for (T item : items)
//        {
//            Integer current = counts.get(item);
//            if (current == null)
//                counts.put(item, 1);
//            else
//                counts.put(item, current + 1);
//        }
//        return counts;
//    }
}
