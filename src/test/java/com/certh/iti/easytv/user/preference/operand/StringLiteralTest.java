package com.certh.iti.easytv.user.preference.operand;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StringLiteralTest {
	
	@Test
	public void test_constructor() {
		new StringLiteral("19:00");
	}
	
	@Test(expectedExceptions=ClassCastException.class)
	public void test_wrong_constructor() {
		new StringLiteral(15);
	}
	
	@Test
	public void test_equals() {
		StringLiteral stringLiteral1 = new StringLiteral("test1");
		StringLiteral stringLiteral2 = new StringLiteral("test1");

		Assert.assertEquals(stringLiteral1, stringLiteral2);
	}
	
	@Test
	public void test_not_equals() {
		StringLiteral stringLiteral1 = new StringLiteral("test1");
		StringLiteral stringLiteral2 = new StringLiteral("test2");

		Assert.assertNotEquals(stringLiteral1, stringLiteral2);
	}

}
