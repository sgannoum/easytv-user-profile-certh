package com.certh.iti.easytv.user.preference.operand;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BooleanLiteralTest {
	
	@Test
	public void test_constructor() {
		 new BooleanLiteral(true);
	}
	
	@Test(expectedExceptions=ClassCastException.class)
	public void test_wrong_constructor() {
		BooleanLiteral booleanLiteral1 = new BooleanLiteral("Not boolean");
	}
	
	@Test
	public void test_distanceTo1() {
		BooleanLiteral booleanLiteral1 = new BooleanLiteral(true);
		BooleanLiteral booleanLiteral2 = new BooleanLiteral(true);

		Assert.assertEquals(booleanLiteral1.distanceTo(booleanLiteral2), 0.0);
	}
	
	@Test
	public void test_distanceTo2() {
		BooleanLiteral booleanLiteral1 = new BooleanLiteral(true);
		BooleanLiteral booleanLiteral2 = new BooleanLiteral(false);

		Assert.assertEquals(booleanLiteral1.distanceTo(booleanLiteral2), 1.0);
	}
	
	@Test
	public void test_equals() {
		BooleanLiteral booleanLiteral1 = new BooleanLiteral(true);
		BooleanLiteral booleanLiteral2 = new BooleanLiteral(true);

		Assert.assertEquals(booleanLiteral1, booleanLiteral2);
	}
	
	
	@Test
	public void test_not_equals() {
		BooleanLiteral booleanLiteral1 = new BooleanLiteral(true);
		BooleanLiteral booleanLiteral2 = new BooleanLiteral(false);

		Assert.assertNotEquals(booleanLiteral1, booleanLiteral2);
	}

}
