package com.certh.iti.easytv.user.preference.operand;

import org.testng.annotations.Test;

import java.time.format.DateTimeParseException;

import org.testng.Assert;

public class TimeLiteralTest {
	
	@Test
	public void test_constructor() {
		new TimeLiteral("19:00");
	}
	
	@Test(expectedExceptions=DateTimeParseException.class)
	public void test_wrong_constructor() {
		TimeLiteral timeLiteral1 = new TimeLiteral("Not time format");
	}
	
	@Test
	public void test_distanceTo1() {
		TimeLiteral timeLiteral1 = new TimeLiteral("19:00");
		TimeLiteral timeLiteral2 = new TimeLiteral("20:25");

		Assert.assertEquals(timeLiteral1.distanceTo(timeLiteral2), 1.1736111111111112);
	}

	@Test
	public void test_distanceTo2() {
		TimeLiteral timeLiteral1 = new TimeLiteral("19:00");
		TimeLiteral timeLiteral2 = new TimeLiteral("22:30");

		Assert.assertEquals(timeLiteral1.distanceTo(timeLiteral2), 9.25);
	}
	
	@Test
	public void test_equals() {
		TimeLiteral timeLiteral1 = new TimeLiteral("19:00");
		TimeLiteral timeLiteral2 = new TimeLiteral("19:00");

		Assert.assertEquals(timeLiteral1, timeLiteral2);
	}
	
	@Test
	public void test_not_equals() {
		TimeLiteral timeLiteral1 = new TimeLiteral("19:00");
		TimeLiteral timeLiteral2 = new TimeLiteral("19:01");

		Assert.assertNotEquals(timeLiteral1, timeLiteral2);
	}

}
