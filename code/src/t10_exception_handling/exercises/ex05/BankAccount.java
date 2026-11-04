package t10_exception_handling.exercises.ex05;

/**
 * A teaching example. Real banking code stores money as whole cents (long) or
 * BigDecimal, because double cannot represent most decimal amounts exactly.
 */
public class BankAccount {

    private final String id;
    private double balance;
    private boolean locked;

    public BankAccount(String id, double openingBalance) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
        if (openingBalance < 0) {
            throw new IllegalArgumentException("opening balance must not be negative");
        }
        this.id = id;
        balance = openingBalance;
    }

    public void deposit(double amount) {
        requirePositive(amount);
        requireUnlocked();
        balance += amount;
    }

    public void withdraw(double amount) {
        requirePositive(amount);
        requireUnlocked();
        if (amount > balance) {
            throw new InsufficientFundsException(amount, balance);
        }
        balance -= amount;
    }

    public void lock() {
        locked = true;
    }

    public double balance() {
        return balance;
    }

    public String id() {
        return id;
    }

    // A bad argument is the caller's bug: use the standard exception, not a bank one
    private static void requirePositive(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive: " + amount);
        }
    }

    private void requireUnlocked() {
        if (locked) {
            throw new AccountLockedException(id);
        }
    }
}
