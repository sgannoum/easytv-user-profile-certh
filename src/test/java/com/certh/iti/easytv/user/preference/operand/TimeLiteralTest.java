package com.certh.iti.easytv.user.preference.operand;

import org.testng.annotations.Test;

import java.time.format.DateTimeParseException;

import org.testng.Assert;

public class TimeLiteralTest {
	
	@Test
	public void test_constructor() {
		new TimeLiteral("2019-05-30T09:47:47.619Z");
	}
	
	@Test(expectedExceptions=DateTimeParseException.class)
	public void test_wrong_constructor() {
		new TimeLiteral("Not time format");
	}
	
	@Test
	public void test_equals() {
		TimeLiteral timeLiteral1 = new TimeLiteral("2019-05-30T09:47:47.619Z");
		TimeLiteral timeLiteral2 = new TimeLiteral("2019-05-30T09:47:47.619Z");

		Assert.assertEquals(timeLiteral1, timeLiteral2);
	}
	
	@Test
	public void test_not_equals() {
		TimeLiteral timeLiteral1 = new TimeLiteral("2019-05-30T09:47:47.619Z");
		TimeLiteral timeLiteral2 = new TimeLiteral("2019-05-30T09:47:47.620Z");

		Assert.assertNotEquals(timeLiteral1, timeLiteral2);
	}
	
	@Test
	public void test_not_equals_1() {
		OperandLiteral timeLiteral1 = new TimeLiteral("2019-05-30T09:47:47.619Z");
		OperandLiteral timeLiteral2 = new TimeLiteral("2019-05-30T09:47:47.620Z");

		Assert.assertNotEquals(timeLiteral1, timeLiteral2);
	}

}
