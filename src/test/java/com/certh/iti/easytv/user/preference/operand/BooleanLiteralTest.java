package com.certh.iti.easytv.user.preference.operand;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BooleanLiteralTest {
	
	@Test
	public void test_constructor() {
		 new SymmetricBooleanLiteral(true);
	}
	
	@Test(expectedExceptions=ClassCastException.class)
	public void test_wrong_constructor() {
		new SymmetricBooleanLiteral("Not boolean");
	}
	
	@Test
	public void test_equals() {
		BooleanLiteral booleanLiteral1 = new SymmetricBooleanLiteral(true);
		BooleanLiteral booleanLiteral2 = new SymmetricBooleanLiteral(true);

		Assert.assertEquals(booleanLiteral1, booleanLiteral2);
	}
	
	
	@Test
	public void test_not_equals() {
		BooleanLiteral booleanLiteral1 = new SymmetricBooleanLiteral(true);
		BooleanLiteral booleanLiteral2 = new SymmetricBooleanLiteral(false);

		Assert.assertNotEquals(booleanLiteral1, booleanLiteral2);
	}

}
