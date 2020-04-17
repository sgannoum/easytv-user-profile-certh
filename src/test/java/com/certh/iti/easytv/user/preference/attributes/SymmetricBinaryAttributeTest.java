package com.certh.iti.easytv.user.preference.attributes;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class SymmetricBinaryAttributeTest {
	
	SymmetricBinaryAttribute attr1; 
	
	@BeforeClass
	public void beforTest() {
		attr1 = new SymmetricBinaryAttribute();
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
		Assert.assertFalse(attr1.isInBinRange(false, 1));
		Assert.assertFalse(attr1.isInBinRange(true, 0));

		Assert.assertTrue(attr1.isInBinRange(false, 0));
		Assert.assertTrue(attr1.isInBinRange(true, 1));
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
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_handle_outOfRange() {
		attr1.handle(0);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_isInBinRange() {
		attr1.isInBinRange(false, -1);
		attr1.isInBinRange(true, 26);
	}
		
}
