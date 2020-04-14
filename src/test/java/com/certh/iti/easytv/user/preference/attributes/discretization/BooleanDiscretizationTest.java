package com.certh.iti.easytv.user.preference.attributes.discretization;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class BooleanDiscretizationTest {
	
	BooleanDiscretization attr1; 
	
	@BeforeClass
	public void beforTest() {
		attr1 = new BooleanDiscretization();
	}
	
	@Test
	public void test_BinNumber() {
		Assert.assertEquals(2, attr1.getBinNumber());
	}

	@Test
	public void test_code_attribute1() {
		Assert.assertEquals(0, attr1.code(false));
		Assert.assertEquals(1, attr1.code(true));
	}
	
	@Test
	public void test_isInBinRange_attribute1() {
		Assert.assertFalse(attr1.inRange(false, 1));
		Assert.assertFalse(attr1.inRange(true, 0));

		Assert.assertTrue(attr1.inRange(false, 0));
		Assert.assertTrue(attr1.inRange(true, 1));
	}
	
	@Test
	public void test_decode_attribute1() {
		Assert.assertEquals(false, attr1.decode(0));
		Assert.assertEquals(true, attr1.decode(1));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_code_wrongDataType() {
		attr1.code(0);
	}

	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID1() {
		attr1.decode(attr1.getBinNumber());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID2() {
		attr1.decode(-1);
	}
		
}
