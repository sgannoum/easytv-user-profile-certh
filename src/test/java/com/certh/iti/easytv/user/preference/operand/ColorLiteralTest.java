package com.certh.iti.easytv.user.preference.operand;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ColorLiteralTest {
	
	@Test
	public void test_constructor() {
		ColorLiteral colorLiteral1 = new ColorLiteral("#000000");
	}
	
	@Test(expectedExceptions=NumberFormatException.class)
	public void test_wrong_constructor() {
		ColorLiteral colorLiteral1 = new ColorLiteral("Not color fomrated");
	}
		
	@Test
	public void test_distanceTo1() {
		ColorLiteral colorLiteral1 = new ColorLiteral("#000000");
		ColorLiteral colorLiteral2 = new ColorLiteral("#000000");

		Assert.assertEquals(colorLiteral1.distanceTo(colorLiteral2), 0.0);
	}
	
	@Test
	public void test_distanceTo2() {
		ColorLiteral colorLiteral1 = new ColorLiteral("#000000");
		ColorLiteral colorLiteral2 = new ColorLiteral("#000001");

		Assert.assertEquals(colorLiteral1.distanceTo(colorLiteral2), 1.0);
	}
	
	@Test
	public void test_distanceTo3() {
		ColorLiteral colorLiteral1 = new ColorLiteral("#000000");
		ColorLiteral colorLiteral2 = new ColorLiteral("#010101");

		Assert.assertEquals(colorLiteral1.distanceTo(colorLiteral2), 3.0);
	}
	
	@Test
	public void test_distanceTo4() {
		ColorLiteral colorLiteral1 = new ColorLiteral("#010000");
		ColorLiteral colorLiteral2 = new ColorLiteral("#020101");

		Assert.assertEquals(colorLiteral1.distanceTo(colorLiteral2), 3.0);
	}
	
	@Test
	public void test_distanceTo5() {
		ColorLiteral colorLiteral1 = new ColorLiteral("#000000");
		ColorLiteral colorLiteral2 = new ColorLiteral("#008000");

		Assert.assertEquals(colorLiteral1.distanceTo(colorLiteral2), 128.0);
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
