package t10_exception_handling.exercises.ex05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("t10 ex05 - a custom exception hierarchy")
class BankAccountTest {

    @Test
    void depositThenWithdraw_updatesBalance() {
        BankAccount account = new BankAccount("A1", 100);

        account.deposit(50);
        account.withdraw(30);

        assertEquals(120, account.balance(), 1e-9);
    }

    @Test
    void withdraw_moreThanBalance_throwsWithAmounts_andBalanceUnchanged() {
        BankAccount account = new BankAccount("A1", 100);

        InsufficientFundsException e = assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(500));

        assertEquals(500, e.getRequested(), 1e-9);
        assertEquals(100, e.getBalance(), 1e-9);
        assertEquals(100, account.balance(), 1e-9);
    }

    @Test
    void lockedAccount_rejectsDepositAndWithdraw() {
        BankAccount account = new BankAccount("A1", 100);
        account.lock();

        AccountLockedException e = assertThrows(AccountLockedException.class,
                () -> account.withdraw(10));
        assertEquals("A1", e.getAccountId());
        assertThrows(AccountLockedException.class, () -> account.deposit(10));
    }

    @Test
    void nonPositiveAmount_isACallerBug_soUsesIllegalArgument() {
        BankAccount account = new BankAccount("A1", 100);

        assertThrows(IllegalArgumentException.class, () -> account.deposit(-5));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
    }

    @Test
    void hierarchy_bothSubtypesAreBankExceptions_andUnchecked() {
        assertInstanceOf(BankException.class, new InsufficientFundsException(1, 0));
        assertInstanceOf(BankException.class, new AccountLockedException("A1"));
        assertInstanceOf(RuntimeException.class, new BankException("x"));
    }
}
