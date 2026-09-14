package t08_inheritance.exercises.ex03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t08 ex03 - constructor chaining and super.describe()")
class ExerciseTest {

    @Test
    void describe_onBossEnemy_extendsRatherThanReplacesTheParentText() {
        assertEquals("Enemy: Dragon [Boss level 3]", new BossEnemy("Dragon", 3).describe());
    }

    @Test
    void describe_onBossEnemy_startsWithWhatEnemyWouldHaveReturned() {
        // super.describe() means the parent's text must remain a prefix.
        String parent = new Enemy("Dragon").describe();
        assertTrue(new BossEnemy("Dragon", 3).describe().startsWith(parent),
                "BossEnemy.describe() should build on super.describe()");
    }

    @Test
    void name_travelsUpTwoConstructorsToEntity() {
        // BossEnemy -> Enemy -> Entity, where the field actually lives.
        assertEquals("Dragon", new BossEnemy("Dragon", 3).name());
    }

    @Test
    void describe_onPlayer_isUnaffectedByTheEnemyBranch() {
        assertEquals("Player Hero (score=42)", new Player("Hero", 42).describe());
    }
}
