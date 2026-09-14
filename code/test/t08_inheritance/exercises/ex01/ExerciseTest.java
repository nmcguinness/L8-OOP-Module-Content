package t08_inheritance.exercises.ex01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("t08 ex01 - overriding describe() in a subclass")
class ExerciseTest {

    @Test
    void describe_onUser_usesTheBaseImplementation() {
        assertEquals("User: alice", new User("alice").describe());
    }

    @Test
    void describe_onAdminUser_usesTheOverride() {
        assertEquals("AdminUser (elevated privileges): root", new AdminUser("root").describe());
    }

    @Test
    void describe_adminHeldInAUserVariable_stillCallsTheOverride() {
        // The compile-time type is User, but dispatch follows the runtime type.
        User u = new AdminUser("root");
        assertEquals("AdminUser (elevated privileges): root", u.describe());
    }

    @Test
    void username_isInheritedRatherThanRedeclared() {
        AdminUser admin = new AdminUser("root");
        assertEquals("root", admin.username());
        assertInstanceOf(User.class, admin, "AdminUser must be a User");
    }
}
