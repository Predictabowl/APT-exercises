package string.example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MyStringUtilsTest {

	private MyStringUtils myStringUtils;

	@Before
	public void setup() {
		myStringUtils = new MyStringUtils();
	}

	@Test
	public void testLeftTrimWithNullString() {
		assertNull(myStringUtils.leftTrim(null));
	}
	
	@Test
	public void testLeftTrimWithEmptyString() {
		assertEquals("", myStringUtils.leftTrim(""));
	}
	
	@Test
	public void testLeftTrimWithOneLeadingSpace() {
		assertEquals("abc", myStringUtils.leftTrim(" abc"));
	}

	@Test
	public void testLeftTrimWithNoLeadingSpace() {
		assertEquals("abc", myStringUtils.leftTrim("abc"));
	}
	
	@Test
	public void testLeftTrimWithOneLeadingTab() {
		assertEquals("abc", myStringUtils.leftTrim("\tabc"));
	}
	
	@Test
	public void testLeftTrimWithSeveralSpaces() {
		assertEquals("abc", myStringUtils.leftTrim("  abc"));
	}
	
	@Test
	public void testLeftTrimWithAllSpaces() {
		assertEquals("", myStringUtils.leftTrim("  "));
	}
}
