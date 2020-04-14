package com.certh.iti.easytv.user.preference.attributes.discretization;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class StringDiscretizationTest {
	
	StringDiscretization attr1, attr2, attr3; 
	
	@BeforeClass
	public void beforTest() {
		attr1 = new StringDiscretization(new String[] {"15", "20", "23"});
		attr2 = new StringDiscretization(new String[] {"none", "gaze_control", "gesture_control"});
		attr3 = new StringDiscretization(new String[][] {new String[] {"0", "1"},
														 new String[] {"2", "3", "4"},
														 new String[] {"5"}});
	}
	
	@Test
	public void test_BinNumber() {
		Assert.assertEquals(3, attr1.getBinNumber());
		Assert.assertEquals(3, attr2.getBinNumber());
	}
	
	@Test
	public void test_range() {
		Assert.assertEquals(0.0, attr1.getRange()[0]); 	Assert.assertEquals( 2.0, attr1.getRange()[1]);
		Assert.assertEquals(0.0, attr2.getRange()[0]); 	Assert.assertEquals( 2.0, attr2.getRange()[1]);
		Assert.assertEquals(0.0, attr3.getRange()[0]); 	Assert.assertEquals( 5.0, attr3.getRange()[1]);
	}
	
	@Test
	public void test_code_attribute1() {
		Assert.assertEquals(0, attr1.code("15"));
		Assert.assertEquals(1, attr1.code("20"));
		Assert.assertEquals(2, attr1.code("23"));
	}
	
	@Test
	public void test_inRange_attribute1() {
		Assert.assertFalse(attr1.inRange("15", 2));
		Assert.assertTrue(attr1.inRange("15", 0));
		Assert.assertTrue(attr1.inRange("20", 1));
		Assert.assertTrue(attr1.inRange("23", 2));
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
	
	@Test
	public void test_code_attribute3() {
		Assert.assertEquals(0, attr3.code("0")); Assert.assertEquals(0, attr3.code("1")); 
		Assert.assertEquals(1, attr3.code("2")); Assert.assertEquals(1, attr3.code("3")); Assert.assertEquals(1, attr3.code("4"));
		Assert.assertEquals(2, attr3.code("5"));
	}
	
	@Test
	public void test_inRange_attribute3() {
		Assert.assertFalse(attr3.inRange("0", 2));
		Assert.assertTrue(attr3.inRange("0", 0));
		Assert.assertTrue(attr3.inRange("3", 1));
		Assert.assertTrue(attr3.inRange("5", 2));
	}
	
	@Test
	public void test_decode_attribute3() {
		Assert.assertEquals("1", attr3.decode(0));
		Assert.assertEquals("3", attr3.decode(1));
		Assert.assertEquals("5", attr3.decode(2));
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
	public void test_code_outOfrange() {
		attr1.code("Null");
	}
	
}
