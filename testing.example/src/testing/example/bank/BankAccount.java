package testing.example.bank;

public class BankAccount {
	private int id;
	private double balance = 0;
	private static int lastId = 0;

	public BankAccount() {
		this.id = ++lastId;
	}

	public double getBalance() {
		return balance;
	}

	public int getId() {
		return id;
	}

	public void deposit(double amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Negative amount: " + amount);
		}
		balance += amount;
	}

	public void withdraw(double amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Negative amount: " + amount);
		}
		if (balance - amount < 0) {
			throw new IllegalArgumentException("Cannot withdrawn " + amount + " from " + balance);
		}
		balance -= amount;
	}

	/**
	 * Package-private, for internal use only, for example, for testing.
	 * 
	 * @param balance
	 */
	void setBalance(double balance) {
		this.balance = balance;
	}

}
