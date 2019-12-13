package com.certh.iti.easytv.user.preference.attributes;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class NominalAttributeTest {
	
	NominalAttribute attr1, attr2; 
	
	@BeforeClass
	public void beforTest() {
		attr1 = new NominalAttribute(new String[] {"15", "20", "23"});
		attr2 = new NominalAttribute(new String[] {"none", "gaze_control", "gesture_control"});
		
		System.out.println("\n\nBefore Class");
		System.out.println(attr1.toString());
		System.out.println(attr2.toString());
	}
	
	@AfterClass
	public void afterClass() {
		
		System.out.println("\n\nAfter Class");
		System.out.println(attr1.toString());
		System.out.println(attr2.toString());
	}
	
	@Test
	public void test_code_attribute1() {
		Assert.assertEquals(0, attr1.code("15"));
		Assert.assertEquals(1, attr1.code("20"));
		Assert.assertEquals(2, attr1.code("23"));
	}
	
	@Test
	public void test_isInBinRange_attribute1() {
		Assert.assertFalse(attr1.isInBinRange("15", 2));
		Assert.assertTrue(attr1.isInBinRange("15", 0));
		Assert.assertTrue(attr1.isInBinRange("20", 1));
		Assert.assertTrue(attr1.isInBinRange("23", 2));
	}
	
	@Test
	public void test_decode_attribute1() {
		Assert.assertEquals("15", attr1.decode(0));
		Assert.assertEquals("20", attr1.decode(1));
		Assert.assertEquals("23", attr1.decode(2));
	}
	
	@Test
	public void test_code_attribute2() {		
		Assert.assertEquals(0, attr2.code("none"));
		Assert.assertEquals(1, attr2.code("gaze_control"));
		Assert.assertEquals(2, attr2.code("gesture_control"));
	}
	
	@Test
	public void test_decode_attribute2() {
		Assert.assertEquals("none", attr2.decode(0));
		Assert.assertEquals("gaze_control", attr2.decode(1));
		Assert.assertEquals("gesture_control", attr2.decode(2));
	}

	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID1() {
		attr1.decode(attr1.getBinNumber());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID2() {
		attr1.decode(-1);
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void test_code_outOfrange() {
		attr1.code("Null");
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void test_handle_outOfRange() {
		attr1.handle("Null");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_isInBinRange() {
		attr1.isInBinRange("15", -1);
		attr1.isInBinRange("15", 26);
	}
	
}
