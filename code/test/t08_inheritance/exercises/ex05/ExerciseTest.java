package t08_inheritance.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("t08 ex05 - polymorphic update/render loop")
class ExerciseTest {

    /**
     * update() and render() return void and report through System.out, so the
     * only observable behaviour is what they print. Capturing the stream is the
     * honest way to assert that dispatch reached the right subclass.
     */
    private static String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void update_onPlayerEntity_runsThePlayerImplementation() {
        String out = capture(() -> new PlayerEntity("Hero").update(0.5));
        assertTrue(out.contains("Updating player Hero"), out);
    }

    @Test
    void render_onEnemyEntity_runsTheEnemyImplementation() {
        String out = capture(() -> new EnemyEntity("Slime").render());
        assertTrue(out.contains("Rendering enemy Slime"), out);
    }

    @Test
    void loopOverGameEntities_reachesEachSubclassImplementation() {
        List<GameEntity> entities = new ArrayList<>(
                List.of(new PlayerEntity("Hero"), new EnemyEntity("Slime")));

        String out = capture(() -> {
            for (GameEntity ge : entities) {
                ge.update(0.016);
                ge.render();
            }
        });

        // The loop knows only GameEntity, yet four distinct subclass methods ran.
        assertTrue(out.contains("Updating player Hero"), out);
        assertTrue(out.contains("Rendering player Hero"), out);
        assertTrue(out.contains("Updating enemy Slime"), out);
        assertTrue(out.contains("Rendering enemy Slime"), out);
    }

    @Test
    void gameEntity_declaresUpdateAndRenderAbstract() {
        assertTrue(Modifier.isAbstract(GameEntity.class.getModifiers()));
        assertEquals(2, java.util.Arrays.stream(GameEntity.class.getDeclaredMethods())
                .filter(m -> Modifier.isAbstract(m.getModifiers()))
                .count(), "update(double) and render() must both stay abstract");
    }
}
