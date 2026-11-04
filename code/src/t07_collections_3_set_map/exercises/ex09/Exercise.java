package t07_collections_3_set_map.exercises.ex09;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Exercise {

    // Counts loot drops per rarity; every rarity is a key, even with a count of 0
    public static Map<Rarity, Integer> countByRarity(List<Rarity> drops) {
        Map<Rarity, Integer> counts = new EnumMap<>(Rarity.class);
        for (Rarity r : Rarity.values()) {
            counts.put(r, 0);
        }

        if (drops == null)
            return counts;

        for (Rarity r : drops) {
            if (r != null)
                counts.merge(r, 1, Integer::sum);
        }
        return counts;
    }

    public static void run() {
        List<Rarity> drops = List.of(
                Rarity.COMMON, Rarity.RARE, Rarity.COMMON, Rarity.LEGENDARY, Rarity.COMMON);
        System.out.println(countByRarity(drops));
    }
}
