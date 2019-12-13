package com.certh.iti.easytv.user.preference.attributes;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class DoubleAttributeTest {
	
	DoubleAttribute attr1, attr2, attr3, attr4, attr5, attr6; 
	
	@BeforeClass
	public void beforTest() {
		
		attr1 = new DoubleAttribute(new double[] {0.0, 100.0}, 1.0, 25, 0);
		attr2 = new DoubleAttribute(new double[] {1.0, 8.0}, 0);
		attr3 = new DoubleAttribute(new double[] {1.0, 2.0}, 0.5, 0);
		attr4 = new DoubleAttribute(new double[] {1.5, 3.5}, 0.5, 0);

		System.out.println("\n\nBefore Class");
		System.out.println(attr1.toString());
		System.out.println(attr2.toString());
		System.out.println(attr3.toString());
		System.out.println(attr4.toString());
	}
	
	@AfterClass
	public void afterClass() {
		
		System.out.println("\n\nAfter Class");
		System.out.println(attr1.toString());
		System.out.println(attr2.toString());
		System.out.println(attr3.toString());
		System.out.println(attr4.toString());
	}
	
	
	@Test
	public void test_code_attribute1() {
		//bin 0 bin size + 1 elements = 5
		Assert.assertEquals(0, attr1.code(0.0) );
		Assert.assertEquals(0, attr1.code(1.0) );
		Assert.assertEquals(0, attr1.code(2.0) );
		Assert.assertEquals(0, attr1.code(3.0) );
		Assert.assertEquals(0, attr1.code(4.0) );
		
		//bin 1 has bin size elements = 4
		Assert.assertEquals(1, attr1.code(5.0) );
		Assert.assertEquals(1, attr1.code(6.0) );
		Assert.assertEquals(1, attr1.code(7.0) );
		Assert.assertEquals(1, attr1.code(8.0) );
		
		//bin 2 has bin size elements = 4
		Assert.assertEquals(2, attr1.code(9.0) );
		Assert.assertEquals(2, attr1.code(10.0) );
		Assert.assertEquals(2, attr1.code(11.0) );
		Assert.assertEquals(2, attr1.code(12.0) );
	}
	
	@Test
	public void test_isInBinRange_attribute1() {
		//bin 0 bin size + 1 elements = 5
		Assert.assertFalse(attr1.isInBinRange(-10., 0));
		Assert.assertTrue(attr1.isInBinRange(0.0, 0));
		Assert.assertTrue(attr1.isInBinRange(1.0, 0));
		Assert.assertTrue(attr1.isInBinRange(2.0, 0));
		Assert.assertTrue(attr1.isInBinRange(3.0, 0));
		Assert.assertTrue(attr1.isInBinRange(4.0, 0));
		Assert.assertFalse(attr1.isInBinRange(5.0, 0));

		
		//bin 1 has bin size elements = 4
		Assert.assertFalse(attr1.isInBinRange(4.0, 1));
		Assert.assertTrue(attr1.isInBinRange(5.0, 1));
		Assert.assertTrue(attr1.isInBinRange(6.0, 1));
		Assert.assertTrue(attr1.isInBinRange(7.0, 1));
		Assert.assertTrue(attr1.isInBinRange(8.0, 1));
		Assert.assertFalse(attr1.isInBinRange(9.0, 1));
		
		//bin 2 has bin size elements = 4
		Assert.assertTrue(attr1.isInBinRange(9.0, 2));
		Assert.assertTrue(attr1.isInBinRange(10.0, 2));
		Assert.assertTrue(attr1.isInBinRange(11.0, 2));
		Assert.assertTrue(attr1.isInBinRange(12.0, 2));
	}
	
	@Test
	public void test_decode_attribute1() {
		Assert.assertEquals(2.0, attr1.decode(0));
		Assert.assertEquals(7.0, attr1.decode(1));
		Assert.assertEquals(11.0, attr1.decode(2));
	}
	
	@Test
	public void test_code_attribute2() {		
		Assert.assertEquals(0, attr2.code(1.0));
		Assert.assertEquals(1, attr2.code(2.0));
		Assert.assertEquals(2, attr2.code(3.0));
		Assert.assertEquals(3, attr2.code(4.0));
		Assert.assertEquals(4, attr2.code(5.0));
		Assert.assertEquals(5, attr2.code(6.0));
		Assert.assertEquals(6, attr2.code(7.0));
		Assert.assertEquals(7, attr2.code(8.0));
	}
	
	@Test
	public void test_isInBinRange_attribute2() {
		//bin 0 bin size + 1 elements = 5
		Assert.assertTrue(attr2.isInBinRange(1.0, 0));
		Assert.assertTrue(attr2.isInBinRange(2.0, 1));
		Assert.assertTrue(attr2.isInBinRange(3.0, 2));
		Assert.assertTrue(attr2.isInBinRange(4.0, 3));
		Assert.assertTrue(attr2.isInBinRange(5.0, 4));
		Assert.assertTrue(attr2.isInBinRange(6.0, 5));
		Assert.assertTrue(attr2.isInBinRange(7.0, 6));
		Assert.assertTrue(attr2.isInBinRange(8.0, 7));
		
		Assert.assertFalse(attr2.isInBinRange(1.0, 1));
		Assert.assertFalse(attr2.isInBinRange(2.0, 2));
		Assert.assertFalse(attr2.isInBinRange(3.0, 3));
		Assert.assertFalse(attr2.isInBinRange(4.0, 4));
		Assert.assertFalse(attr2.isInBinRange(5.0, 5));
		Assert.assertFalse(attr2.isInBinRange(6.0, 6));
		Assert.assertFalse(attr2.isInBinRange(7.0, 7));
	}
	
	@Test
	public void test_decode_attribute2() {
		Assert.assertEquals(1.0, attr2.decode(0));
		Assert.assertEquals(2.0, attr2.decode(1));
		Assert.assertEquals(3.0, attr2.decode(2));
	}

	@Test
	public void test_code_attribute3() {		
		Assert.assertEquals(0, attr3.code(1.0));
		Assert.assertEquals(1, attr3.code(1.5));
		Assert.assertEquals(2, attr3.code(2.0));
	}
	
	@Test
	public void test_decode_attribute3() {
		Assert.assertEquals(1.0, attr3.decode(0));
		Assert.assertEquals(1.5, attr3.decode(1));
		Assert.assertEquals(2.0, attr3.decode(2));
	}
	
	@Test
	public void test_code_attribute4() {		
		Assert.assertEquals(0, attr4.code(1.5));
		Assert.assertEquals(1, attr4.code(2.0));
		Assert.assertEquals(2, attr4.code(2.5));
		Assert.assertEquals(3, attr4.code(3.0));
		Assert.assertEquals(4, attr4.code(3.5));
	}
	
	@Test
	public void test_decode_attribute4() {
		Assert.assertEquals(1.5, attr4.decode(0));
		Assert.assertEquals(2.0, attr4.decode(1));
		Assert.assertEquals(2.5, attr4.decode(2));
		Assert.assertEquals(3.0, attr4.decode(3));
		Assert.assertEquals(3.5, attr4.decode(4));

	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_code_outOfStep() {
		attr4.code(9.0);
		attr4.code(1.0);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID1() {
		attr4.decode(attr4.getBinNumber());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID2() {
		attr4.decode(-1);
	}
	
	@Test(expectedExceptions = OutOfRangeException.class)
	public void test_code_outOfrange() {
		attr1.handle(101.0);
		attr1.handle(-1.0);
		attr1.handle(150.0);
	}
	
	@Test(expectedExceptions = OutOfRangeException.class)
	public void test_handle_outOfRange() {
		attr1.handle(101.0);
		attr1.handle(-1.0);
		attr1.handle(150.0);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_isInBinRange() {
		attr1.isInBinRange(1, -1);
		attr1.isInBinRange(1, 26);
	}
	
	@Test
	public void test_handle() {

		for(int i = 0; i <= attr1.getRange()[1]; i++) 
			attr1.handle(i * 1.0);
		
		Assert.assertEquals(100.0 ,attr1.getMaxValue());
		Assert.assertEquals(0.0 ,attr1.getMinValue());
		Assert.assertEquals(101 ,attr1.getCounts());
	}
	
}
