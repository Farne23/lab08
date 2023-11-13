package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestStrictBankAccount {

    private final static int INITIAL_AMOUNT = 100;

    // 1. Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    /**
     * Configuration step: this is performed BEFORE each test.
     * A new accountholder and a StrictBankaccount is created
     */
    @BeforeEach
    public void setUp() {
        mRossi = new AccountHolder("Mario", "Rossi",1);
        bankAccount = new StrictBankAccount(mRossi, INITIAL_AMOUNT);
    }

    // 2. Test the initial state of the StrictBankAccount
    @Test
    public void testInitialization() {
        Assertions.assertEquals(mRossi,bankAccount.getAccountHolder());
        Assertions.assertEquals(INITIAL_AMOUNT,bankAccount.getBalance());
        Assertions.assertEquals(0,bankAccount.getTransactionsCount());
    }


    // 3. Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
    @Test
    public void testManagementFees() {
        bankAccount.deposit(mRossi.getUserID(),INITIAL_AMOUNT);
        final double expectedBalance = INITIAL_AMOUNT + INITIAL_AMOUNT - StrictBankAccount.MANAGEMENT_FEE - ( bankAccount.getTransactionsCount() * StrictBankAccount.TRANSACTION_FEE ) ;
        bankAccount.chargeManagementFees(mRossi.getUserID());
        Assertions.assertEquals(expectedBalance, bankAccount.getBalance());
    }

    // 4. Test the withdraw of a negative value
    @Test
    public void testNegativeWithdraw() {
        try {
            bankAccount.withdraw(mRossi.getUserID(),-INITIAL_AMOUNT);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Cannot withdraw a negative amount", e.getMessage());
        }
    }

    // 5. Test withdrawing more money than it is in the account
    @Test
    public void testWithdrawingTooMuch() {
        try {
            var illegalAmount = bankAccount.getBalance()+INITIAL_AMOUNT;
            bankAccount.withdraw(mRossi.getUserID(),illegalAmount);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Insufficient balance", e.getMessage());
        }
    }
}
