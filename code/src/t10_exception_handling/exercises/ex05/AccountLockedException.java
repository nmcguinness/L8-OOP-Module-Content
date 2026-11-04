package t10_exception_handling.exercises.ex05;

public class AccountLockedException extends BankException {

    private final String accountId;

    public AccountLockedException(String accountId) {
        super("Account " + accountId + " is locked");
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
