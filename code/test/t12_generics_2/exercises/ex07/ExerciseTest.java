package t12_generics_2.exercises.ex07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t12 ex07 - a consumer signature that accepts three list types")
class ExerciseTest {

    @Test
    void addEnemies_exactTypeList_addsTwo() {
        ArrayList<Enemy> out = new ArrayList<>();
        Spawner.addEnemies(out);
        assertEquals(2, out.size());
    }

    @Test
    void addEnemies_supertypeList_addsTwo() {
        ArrayList<Entity> out = new ArrayList<>();
        Spawner.addEnemies(out);
        assertEquals(2, out.size());
    }

    @Test
    void addEnemies_objectList_addsTwo() {
        // List<? super Enemy> accepts Enemy, Entity and Object lists alike.
        ArrayList<Object> out = new ArrayList<>();
        Spawner.addEnemies(out);
        assertEquals(2, out.size());
    }

    @Test
    void addEnemies_addsActualEnemyInstances() {
        ArrayList<Entity> out = new ArrayList<>();
        Spawner.addEnemies(out);
        assertInstanceOf(Enemy.class, out.get(0));
        assertInstanceOf(Enemy.class, out.get(1));
    }

    @Test
    void addEnemies_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Spawner.addEnemies(null));
    }
}
