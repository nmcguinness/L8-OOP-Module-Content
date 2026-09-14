package t08_inheritance.exercises.ex01;

public class Exercise {

    public static void run() {
        System.out.println("Player and Entity: is-a");
        System.out.println("Customer and Order: has-a");
        System.out.println("Sword and Weapon: is-a");
        System.out.println("Team and Player: has-a");
        System.out.println("AdminUser and User: is-a");

        User regular = new User("alice");
        AdminUser admin = new AdminUser("root");

        System.out.println(regular.describe());
        System.out.println(admin.describe());

        /*
         * AdminUser extends User because every admin account is still a user:
         * it has the same identity and core behaviours (like logging in),
         * but with additional permissions and responsibilities. Inheritance
         * lets us reuse all User logic and only add admin-specific behaviour.
         */
    }
}
