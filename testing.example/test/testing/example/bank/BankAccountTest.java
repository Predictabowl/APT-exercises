package testing.example.bank;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BankAccountTest {

	// All of the following must be >= 0
	private static final int WITHDRAW_VALUE = 3;
	private static final int DEPOSIT_VALUE = 10;
	private static final int INITIAL_BALANCE = 5;

	@Test
	public void testIdIsAutomaticallyAssignedAsPositiveNumber() {
		// setup
		BankAccount bankAccount = new BankAccount();
		// verify
		assertTrue("Id should be positive", bankAccount.getId() > 0);
	}

	@Test
	public void testIdsAreIncremental() {
		assertTrue("Ids should be incremental", new BankAccount().getId() < new BankAccount().getId());
	}

	@Test
	public void testDepositWhenAmountIsCorrectShouldIncreaseBalance() {
		// setup
		BankAccount bankAccount = new BankAccount();
		bankAccount.setBalance(INITIAL_BALANCE);
		// exercise
		bankAccount.deposit(DEPOSIT_VALUE);
		// verify
		assertEquals(INITIAL_BALANCE+DEPOSIT_VALUE, bankAccount.getBalance(), 0);
	}

	@Test
	public void testDepositWhenAmountIsNegativeShouldThrow() {
		// setup
		BankAccount bankAccount = new BankAccount();
		try {
			// exercise
			bankAccount.deposit(-DEPOSIT_VALUE);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			// verify
			assertEquals("Negative amount: "+(double)(-DEPOSIT_VALUE), e.getMessage());
			assertEquals(0, bankAccount.getBalance(), 0);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositWhenAmountIsNegativeShouldThrowWithExcpected() {
		BankAccount bankAccount = new BankAccount();
		bankAccount.deposit(-DEPOSIT_VALUE);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testDepositWhenAmountIsNegativeShouldThrowAlternative() {
		BankAccount bankAccount = new BankAccount();
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Negative amount: "+(double)(-DEPOSIT_VALUE));
		bankAccount.deposit(-DEPOSIT_VALUE);
		// but we can't perform further assertions...
	}

	@Test
	public void testWithdrawWhenAmountIsNegativeShouldThrow() {
		BankAccount bankAccount = new BankAccount();
		try {
			bankAccount.withdraw(-WITHDRAW_VALUE);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Negative amount: "+(double)(-WITHDRAW_VALUE), e.getMessage());
			assertEquals(0, bankAccount.getBalance(), 0);
		}
	}

	@Test
	public void testWithdrawWhenBalanceIsInsufficientShouldThrow() {
		BankAccount bankAccount = new BankAccount();
		try {
			bankAccount.withdraw(WITHDRAW_VALUE);
			fail("Expected an IllegalArgumentException to be thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Cannot withdrawn "+(double)WITHDRAW_VALUE+" from 0.0", e.getMessage());
			assertEquals(0, bankAccount.getBalance(), 0);
		}
	}

	@Test
	public void testWithdrawnWhenBalanceIsSufficientShouldDecreaseBalance() {
		// setup
		BankAccount bankAccount = new BankAccount();
		bankAccount.setBalance(INITIAL_BALANCE + WITHDRAW_VALUE);
		// exercise
		bankAccount.withdraw(WITHDRAW_VALUE);// method to test
		// verify
		assertEquals(INITIAL_BALANCE, bankAccount.getBalance(), 0);
	}
}
