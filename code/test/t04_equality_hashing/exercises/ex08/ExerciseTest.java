package t04_equality_hashing.exercises.ex08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tag is deliberately MUTABLE and uses its mutable field in hashCode. These tests
 * demonstrate the resulting bug: mutate a key after inserting it into a hash-based
 * collection and the object can no longer be found.
 */
@DisplayName("t04 ex08 - why hash keys must be immutable")
class ExerciseTest {

    @Test
    void equals_sameName_areEqual() {
        assertTrue(new Exercise.Tag("enemy").equals(new Exercise.Tag("enemy")));
    }

    @Test
    void hashCode_changesWhenTheNameChanges() {
        Exercise.Tag tag = new Exercise.Tag("enemy");
        int before = tag.hashCode();
        tag.setName("friend");
        assertNotEquals(before, tag.hashCode(),
                        "hashCode depends on a mutable field - this is the hazard");
    }

    @Test
    void hashSet_mutatingAStoredTag_makesItUnfindable() {
        Set<Exercise.Tag> tags = new HashSet<>();
        Exercise.Tag tag = new Exercise.Tag("enemy");
        tags.add(tag);

        tag.setName("friend");   // mutate AFTER insertion

        assertFalse(tags.contains(tag),
                    "the tag is still in the set but sits in the wrong bucket");
    }

    @Test
    void hashSet_mutatedTagCannotEvenBeRemoved() {
        Set<Exercise.Tag> tags = new HashSet<>();
        Exercise.Tag tag = new Exercise.Tag("enemy");
        tags.add(tag);
        tag.setName("friend");

        assertFalse(tags.remove(tag), "remove() looks in the new bucket and finds nothing");
        assertFalse(tags.isEmpty(), "so the element is stranded in the set");
    }
}
