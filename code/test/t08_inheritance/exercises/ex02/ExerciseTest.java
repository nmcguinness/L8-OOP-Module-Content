package t08_inheritance.exercises.ex02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("t08 ex02 - Entity, Player and Enemy hierarchy")
class ExerciseTest {

    @Test
    void describe_onEntity_usesTheBaseImplementation() {
        assertEquals("Entity: thing", new Entity("thing").describe());
    }

    @Test
    void describe_onPlayer_includesTheScore() {
        assertEquals("Player Hero (score=10)", new Player("Hero", 10).describe());
    }

    @Test
    void describe_onEnemy_usesTheEnemyPrefix() {
        assertEquals("Enemy: Slime", new Enemy("Slime").describe());
    }

    @Test
    void describe_overAListOfEntities_dispatchesPerRuntimeType() {
        List<Entity> entities = List.of(
                new Entity("thing"), new Player("Hero", 10), new Enemy("Slime"));

        assertEquals(
                List.of("Entity: thing", "Player Hero (score=10)", "Enemy: Slime"),
                entities.stream().map(Entity::describe).toList());
    }
}
