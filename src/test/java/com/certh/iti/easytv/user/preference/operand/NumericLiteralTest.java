package com.certh.iti.easytv.user.preference.operand;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NumericLiteralTest {
	
	@Test
	public void test_constructor() {
		new NumericLiteral(1);
	}
	
	@Test(expectedExceptions=ClassCastException.class)
	public void test_wrong_constructor() {
		new NumericLiteral("Not numberic");
	}
	
	@Test
	public void test_equals() {
		NumericLiteral numericLiteral1 = new NumericLiteral(1.0);
		NumericLiteral numericLiteral2 = new NumericLiteral(1);

		Assert.assertEquals(numericLiteral1, numericLiteral2);
	}
	
	@Test
	public void test_not_equals() {
		NumericLiteral numericLiteral1 = new NumericLiteral(1.0);
		NumericLiteral numericLiteral2 = new NumericLiteral(2);

		Assert.assertNotEquals(numericLiteral1, numericLiteral2);
	}

}
