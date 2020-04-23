package com.certh.iti.easytv.user.preference.attributes;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;

import junit.framework.Assert;

public class SymmetricBinaryAttributeTest {
	
	SymmetricBinaryAttribute attr1; 
	Discretization dist_attr1;
	
	@BeforeClass
	public void beforTest() {
		attr1 = new SymmetricBinaryAttribute();
		
		//load values
		attr1.handle(false);
		attr1.handle(true);
		
		//get discretization
		dist_attr1 = attr1.getDiscretization();
	}
	
	@Test
	public void test_BinNumber() {
		Assert.assertEquals(2, dist_attr1.getBinNumber());
	}

	@Test
	public void test_code_attribute1() {
		Assert.assertEquals(0, dist_attr1.code(false));
		Assert.assertEquals(1, dist_attr1.code(true));
	}
	
	@Test
	public void test_isInBinRange_attribute1() {
		Assert.assertFalse(dist_attr1.inRange(false, 1));
		Assert.assertFalse(dist_attr1.inRange(true, 0));

		Assert.assertTrue(dist_attr1.inRange(false, 0));
		Assert.assertTrue(dist_attr1.inRange(true, 1));
	}
	
	@Test
	public void test_decode_attribute1() {
		Assert.assertEquals(false, dist_attr1.decode(0));
		Assert.assertEquals(true, dist_attr1.decode(1));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_code_wrongDataType() {
		dist_attr1.code(0);
	}

	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID1() {
		dist_attr1.decode(dist_attr1.getBinNumber());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID2() {
		dist_attr1.decode(-1);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_handle_outOfRange() {
		dist_attr1.handle(0);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_isInBinRange() {
		dist_attr1.inRange(false, -1);
		dist_attr1.inRange(true, 26);
	}
		
}
