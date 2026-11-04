package t24_pathfinding.exercises.ex07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import t24_pathfinding.common.Maps;
import t24_pathfinding.exercises.ex07.Exercise.Run;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t24 ex07 - an inadmissible heuristic")
class ExerciseTest {

    @Test
    void scaledRuns_marsh_costStaysOptimalUpToFactor3ThenJumps() {
        List<Run> runs = Exercise.scaledRuns(Maps.marsh(), 1, 2, 3, 5, 10);

        assertEquals(14, runs.get(0).cost());
        assertEquals(14, runs.get(2).cost());
        assertEquals(46, runs.get(3).cost());
        assertEquals(46, runs.get(4).cost());
    }

    // The test that was missing from the "optimised pathfinding" change
    @Test
    void aStar_admissibleHeuristic_findsTheOptimalCost() {
        Run manhattan = Exercise.scaledRuns(Maps.marsh(), 1).get(0);

        assertEquals(14, manhattan.cost());
    }

    @Test
    void overEstimating_expandsFewerCellsButCostsMore() {
        List<Run> runs = Exercise.scaledRuns(Maps.marsh(), 1, 10);

        assertTrue(runs.get(1).expanded() < runs.get(0).expanded());
        assertTrue(runs.get(1).cost() > runs.get(0).cost());
    }
}
