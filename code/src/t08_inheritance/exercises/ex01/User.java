package t08_inheritance.exercises.ex01;

public class User {
    private final String username;

    public User(String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }

    public String describe() {
        return "User: " + username;
    }
}
