package com.certh.iti.easytv.user.preference.attributes;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class IntegerAttributeTest {
	
	IntegerAttribute attr1, attr2, attr3, attr4; 
	
	@BeforeClass
	public void beforClass() {

		attr1 = new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, 0);
		attr2 = new IntegerAttribute(new double[] {1.0, 8.0}, 0);
		attr3 = new IntegerAttribute(new double[] {-15.0, 15.0}, 1.0, 10, 0);
		attr4 = new IntegerAttribute(new double[] {0.0, 100.0}, 2.0, 25, 0);
		
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
		Assert.assertEquals(0, attr1.code(0));
		Assert.assertEquals(0, attr1.code(1));
		Assert.assertEquals(0, attr1.code(2));
		Assert.assertEquals(0, attr1.code(3));
		Assert.assertEquals(0, attr1.code(4));
		
		//bin 1 has bin size elements = 4
		Assert.assertEquals(1, attr1.code(5));
		Assert.assertEquals(1, attr1.code(6));
		Assert.assertEquals(1, attr1.code(7));
		Assert.assertEquals(1, attr1.code(8));
		
		//bin 2 has bin size elements = 4
		Assert.assertEquals(2, attr1.code(9));
		Assert.assertEquals(2, attr1.code(10));
		Assert.assertEquals(2, attr1.code(11));
		Assert.assertEquals(2, attr1.code(12));
	}
	
	@Test
	public void test_decode_attribute1() {
		Assert.assertEquals(2, attr1.decode(0));
		Assert.assertEquals(7, attr1.decode(1));
		Assert.assertEquals(11, attr1.decode(2));
	}
	
	@Test
	public void test_code_attribute2() {		
		Assert.assertEquals(0, attr2.code(1));
		Assert.assertEquals(1, attr2.code(2));
		Assert.assertEquals(2, attr2.code(3));
		Assert.assertEquals(3, attr2.code(4));
		Assert.assertEquals(4, attr2.code(5));
		Assert.assertEquals(5, attr2.code(6));
		Assert.assertEquals(6, attr2.code(7));
		Assert.assertEquals(7, attr2.code(8));
	}
	
	@Test
	public void test_decode_attribute2() {
		Assert.assertEquals(1, attr2.decode(0));
		Assert.assertEquals(2, attr2.decode(1));
		Assert.assertEquals(3, attr2.decode(2));
	}
	
	@Test
	public void test_code_attribute3() {		
		//bin 0 bin size + 1 elements = 4
		Assert.assertEquals(0, attr3.code(-15));
		Assert.assertEquals(0, attr3.code(-14));
		Assert.assertEquals(0, attr3.code(-13));
		Assert.assertEquals(0, attr3.code(-12));
		
		//bin 1 has bin size elements = 3
		Assert.assertEquals(1, attr3.code(-11));
		Assert.assertEquals(1, attr3.code(-10));
		Assert.assertEquals(1, attr3.code(-9));

		//bin 2 has bin size elements = 3
		Assert.assertEquals(2, attr3.code(-8));
		Assert.assertEquals(2, attr3.code(-7));
		Assert.assertEquals(2, attr3.code(-6));
		
		//.
		//.
		//.
		
		//bin 9 has bin size elements = 3
		Assert.assertEquals(9, attr3.code(13));
		Assert.assertEquals(9, attr3.code(14));
		Assert.assertEquals(9, attr3.code(15));
	}
	
	@Test
	public void test_decode_attribute3() {
		Assert.assertEquals(-14, attr3.decode(0));
		Assert.assertEquals(-10, attr3.decode(1));
		Assert.assertEquals(-7, attr3.decode(2));
	}
	
	@Test
	public void test_code_attribute4() {
		//bin 0 
		Assert.assertEquals(0, attr4.code(0));
		Assert.assertEquals(0, attr4.code(2));
		Assert.assertEquals(0, attr4.code(4));

		//bin 1
		Assert.assertEquals(1, attr4.code(6));
		Assert.assertEquals(1, attr4.code(8));
		
		//bin 2 
		Assert.assertEquals(2, attr4.code(10));
		Assert.assertEquals(2, attr4.code(12));
		
		
		//.
		//.
		//.
		
		//bin 24 
		Assert.assertEquals(24, attr4.code(98));
		Assert.assertEquals(24, attr4.code(100));
	}
	
	@Test
	public void test_decode_attribute4() {
		Assert.assertEquals(2, attr4.decode(0));
		Assert.assertEquals(8, attr4.decode(1));
		Assert.assertEquals(12, attr4.decode(2));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_code_outOfStep() {
		attr4.code(9);
		attr4.code(1);
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
		attr1.handle(101);
		attr1.handle(-1);
		attr1.handle(150);
	}
	
	@Test(expectedExceptions = OutOfRangeException.class)
	public void test_handle_outOfRange() {
		attr1.handle(101);
		attr1.handle(-1);
		attr1.handle(150);
	}
	
	@Test
	public void test_handle() {

		for(int i = 0; i <= attr1.getRange()[1]; i++) 
			attr1.handle(i);
		
		Assert.assertEquals(100.0 ,attr1.getMaxValue());
		Assert.assertEquals(0.0 ,attr1.getMinValue());
		Assert.assertEquals(101 ,attr1.getCounts());
	}

	
}
