package t24_pathfinding.exercises.ex04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import t24_pathfinding.common.BreadthFirstPathfinder;
import t24_pathfinding.common.DijkstraPathfinder;
import t24_pathfinding.common.Maps;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t24 ex04 - Dijkstra against breadth-first")
class ExerciseTest {

    @Test
    void compare_marsh_fewerStepsAreNotCheaper() {
        String table = Exercise.compare(Maps.marsh(),
                List.of(new BreadthFirstPathfinder(), new DijkstraPathfinder()));

        assertTrue(table.contains("steps=10 cost=46"));
        assertTrue(table.contains("steps=14 cost=14"));
    }
}
