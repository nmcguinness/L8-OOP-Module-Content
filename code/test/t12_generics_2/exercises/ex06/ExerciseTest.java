package t12_generics_2.exercises.ex06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t12 ex06 - domain-specific extends to super")
class ExerciseTest {

    @Test
    void addAllEntities_fromTwoDifferentSubtypes_collectsIntoOneList() {
        ArrayList<Entity> all = new ArrayList<>();
        Entities.addAllEntities(List.of(new Enemy(), new Enemy()), all);
        Entities.addAllEntities(List.of(new Pickup()), all);
        assertEquals(3, all.size());
    }

    @Test
    void addAllEntities_preservesInsertionOrderAndRuntimeTypes() {
        ArrayList<Entity> all = new ArrayList<>();
        Entities.addAllEntities(List.of(new Enemy()), all);
        Entities.addAllEntities(List.of(new Pickup()), all);
        assertInstanceOf(Enemy.class, all.get(0));
        assertInstanceOf(Pickup.class, all.get(1));
    }

    @Test
    void addAllEntities_intoAnObjectList_isAllowedByTheSuperBound() {
        ArrayList<Object> all = new ArrayList<>();
        Entities.addAllEntities(List.of(new Enemy()), all);
        assertEquals(1, all.size());
    }

    @Test
    void addAllEntities_emptySource_addsNothing() {
        ArrayList<Entity> all = new ArrayList<>();
        Entities.addAllEntities(List.of(), all);
        assertEquals(0, all.size());
    }

    @Test
    void addAllEntities_nullArguments_throwNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> Entities.addAllEntities(null, new ArrayList<Entity>()));
        assertThrows(NullPointerException.class,
                () -> Entities.addAllEntities(List.of(new Enemy()), null));
    }
}
