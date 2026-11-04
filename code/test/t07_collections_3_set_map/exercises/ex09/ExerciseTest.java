package t07_collections_3_set_map.exercises.ex09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t07 ex09 - counting by enum with EnumMap")
class ExerciseTest {

    @Test
    void countByRarity_drops_countsEachRarity() {
        Map<Rarity, Integer> counts =
                Exercise.countByRarity(List.of(Rarity.COMMON, Rarity.RARE, Rarity.COMMON));

        assertEquals(2, counts.get(Rarity.COMMON));
        assertEquals(1, counts.get(Rarity.RARE));
    }

    @Test
    void countByRarity_everyRarityPresent_evenWhenZero() {
        Map<Rarity, Integer> counts = Exercise.countByRarity(List.of());

        assertEquals(Rarity.values().length, counts.size());
        assertEquals(0, counts.get(Rarity.LEGENDARY));
    }

    @Test
    void countByRarity_keys_iterateInDeclarationOrder() {
        Map<Rarity, Integer> counts = Exercise.countByRarity(List.of(Rarity.EPIC));

        assertEquals(List.of(Rarity.values()), new ArrayList<>(counts.keySet()));
    }

    @Test
    void countByRarity_nullEntries_areSkipped() {
        List<Rarity> drops = new ArrayList<>();
        drops.add(null);
        drops.add(Rarity.EPIC);

        assertEquals(1, Exercise.countByRarity(drops).get(Rarity.EPIC));
    }
}
