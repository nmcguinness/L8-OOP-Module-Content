package t10_exception_handling.exercises.ex05;

public class Exercise {

    public static void run() {
        BankAccount account = new BankAccount("IE-001", 100);
        account.deposit(50);
        System.out.println("Balance after deposit: " + account.balance());

        try {
            account.withdraw(500);
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }

        try {
            account.deposit(-10);
        } catch (IllegalArgumentException e) {
            System.out.println("Bad input: " + e.getMessage());
        }

        account.lock();
        try {
            account.withdraw(10);
        } catch (BankException e) {
            // Catching the base type handles every bank error the same way
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
