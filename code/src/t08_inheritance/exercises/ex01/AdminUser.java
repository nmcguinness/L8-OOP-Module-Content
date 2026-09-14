package t08_inheritance.exercises.ex01;

public class AdminUser extends User {

    public AdminUser(String username) {
        super(username);
    }

    @Override
    public String describe() {
        return "AdminUser (elevated privileges): " + username();
    }
}
