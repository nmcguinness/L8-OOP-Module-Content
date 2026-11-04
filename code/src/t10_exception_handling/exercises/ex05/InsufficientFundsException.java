package t10_exception_handling.exercises.ex05;

public class InsufficientFundsException extends BankException {

    private final double requested;
    private final double balance;

    public InsufficientFundsException(double requested, double balance) {
        super(String.format("Cannot withdraw %.2f: balance is %.2f", requested, balance));
        this.requested = requested;
        this.balance = balance;
    }

    public double getRequested() {
        return requested;
    }

    public double getBalance() {
        return balance;
    }
}
