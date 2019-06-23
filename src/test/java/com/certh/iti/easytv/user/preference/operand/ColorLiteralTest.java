package com.certh.iti.easytv.user.preference.operand;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ColorLiteralTest {
	
	@Test
	public void test_constructor() {
		new ColorLiteral("#000000");
	}
	
	@Test(expectedExceptions=NumberFormatException.class)
	public void test_wrong_constructor() {
		new ColorLiteral("Not color fomrated");
	}
	
	@Test
	public void test_equals() {
		ColorLiteral colorLiteral1 = new ColorLiteral("#000000");
		ColorLiteral colorLiteral2 = new ColorLiteral("#000000");

		Assert.assertEquals(colorLiteral1, colorLiteral2);
	}
	
	@Test
	public void test_not_equals() {
		ColorLiteral colorLiteral1 = new ColorLiteral("#000000");
		ColorLiteral colorLiteral2 = new ColorLiteral("#008000");

		Assert.assertNotEquals(colorLiteral1, colorLiteral2);
	}
}
