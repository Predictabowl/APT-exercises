package factorial.example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FactorialTest {

	private Factorial factorial;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		factorial = new Factorial();
	}

	@Test
	public void testBaseCase() {
		assertEquals(1, factorial.compute(0));
	}

	@Test
	public void testFactorialOf1() {
		assertEquals(1, factorial.compute(1));
	}

	@Test
	public void testFactorialOf2() {
		assertEquals(2, factorial.compute(2));
	}

	@Test
	public void testFactorialOf3() {
		assertEquals(6, factorial.compute(3));
	}

	@Test
	public void testFactorialOf4() {
		assertEquals(24, factorial.compute(4));
	}

	@Test
	public void testNegativeInput() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Negative input: -1");
		factorial.compute(-1);
	}

	// Test for documentation purpose, should not use the method under test to
	// verify both end of equals.
	@Test
	public void testInductiveCase() {
		assertEquals(24,factorial.compute(4));
		assertEquals(5*factorial.compute(4), factorial.compute(5));
	}
}
