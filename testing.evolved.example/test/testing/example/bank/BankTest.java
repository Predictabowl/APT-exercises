package testing.example.bank;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import testing.example.bank.Bank;
import testing.example.bank.BankAccount;

public class BankTest {

	private static final int AMOUNT = 5;

	private static final int INITIAL_BALANCE = 10;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Bank bank;

	private List<BankAccount> bankAccounts;

	@Before
	public void setup() {
		bankAccounts = new ArrayList<BankAccount>();
		bank = new Bank(bankAccounts);
	}

	@Test
	public void testOpenNewAccountShouldReturnPositiveIdAndStoreTheAccount() {
		int newAccountId = bank.openNewBankAccount(INITIAL_BALANCE);
		assertThat(newAccountId).isGreaterThan(0);
		// the following line make 2 assertions on the same subject, the size and
		// account Id.
		assertThat(bankAccounts).hasSize(1).extracting(BankAccount::getId).contains(newAccountId);
		assertEquals(INITIAL_BALANCE, bankAccounts.get(0).getBalance(), 0);
	}

	@Test
	public void testDepositWhenAccountIsNotFoundShouldThrow() {
		thrown.expect(NoSuchElementException.class);
		thrown.expectMessage("No account found with id: 1");
		bank.deposit(1, AMOUNT);
	}

	@Test
	public void testDepositWhenAccountIsFoundShouldIncrementBalance() {
		// setup
		BankAccount anotherAccount = createTestAccount(INITIAL_BALANCE);
		bankAccounts.add(anotherAccount);
		BankAccount testAccount = createTestAccount(INITIAL_BALANCE);
		bankAccounts.add(testAccount);
		// exercise
		bank.deposit(testAccount.getId(), AMOUNT);
		// verify
		assertEquals(INITIAL_BALANCE + AMOUNT, testAccount.getBalance(), 0);
	}

	@Test
	public void testWithdrawnWhenAccountIsNotFoundShouldThrow() {
		thrown.expect(NoSuchElementException.class);
		thrown.expectMessage("No account found with id: 1");
		bank.withdraw(1, INITIAL_BALANCE);
	}

	@Test
	public void testWithdrawnWhenAccountIsFoundShouldDecrementBalance() {
		// setup
		BankAccount anotherAccount = createTestAccount(INITIAL_BALANCE);
		bankAccounts.add(anotherAccount);
		BankAccount testAccount = createTestAccount(INITIAL_BALANCE);
		bankAccounts.add(testAccount);
		// exercise
		bank.withdraw(testAccount.getId(), AMOUNT);
		// verify
		assertEquals(INITIAL_BALANCE - AMOUNT, testAccount.getBalance(), 0);
	}

	/**
	 * Utility method for testing
	 * 
	 * @param i
	 * @return
	 */
	private BankAccount createTestAccount(int initialBalance) {
		BankAccount bankAccount = new BankAccount();
		bankAccount.setBalance(initialBalance);
		return bankAccount;
	}

}
