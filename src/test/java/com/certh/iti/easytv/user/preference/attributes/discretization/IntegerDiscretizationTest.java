package com.certh.iti.easytv.user.preference.attributes.discretization;

import java.util.TreeMap;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class IntegerDiscretizationTest {
	
	IntegerDiscretization attr1, attr2, attr3, attr4; 
	
	@BeforeClass
	public void beforClass() {

		attr1 = new IntegerDiscretization(new double[] {0.0, 100.0}, 1.0, 25);
		attr2 = new IntegerDiscretization(new double[] {1.0, 8.0}, 1.0);
		attr3 = new IntegerDiscretization(new double[] {-15.0, 15.0}, 1.0, 10);
		attr4 = new IntegerDiscretization(new double[] {0.0, 100.0}, 2.0, 25);
		
	}
	
	@Test
	public void test_BinNumber() {
		Assert.assertEquals(25, attr1.getBinNumber());
		Assert.assertEquals(8, attr2.getBinNumber());
		Assert.assertEquals(10, attr3.getBinNumber());
		Assert.assertEquals(25, attr4.getBinNumber());
	}
	
	@Test
	public void test_Binsize_remaining() {
		Assert.assertEquals(1, attr1.getRemaining()); Assert.assertEquals(5, attr1.getDiscreteSize(0)); Assert.assertEquals(4, attr1.getDiscreteSize(1));
		Assert.assertEquals(0, attr2.getRemaining()); Assert.assertEquals(1, attr2.getDiscreteSize(0));
		Assert.assertEquals(1, attr3.getRemaining()); Assert.assertEquals(3, attr3.getDiscreteSize(1));
		Assert.assertEquals(1, attr4.getRemaining()); Assert.assertEquals(2, attr4.getDiscreteSize(1));
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
	public void test_isInBinRange_attribute1() {
		//bin 0 bin size + 1 elements = 5
		Assert.assertFalse(attr1.inRange(-1, 0));
		Assert.assertTrue(attr1.inRange(0, 0));
		Assert.assertTrue(attr1.inRange(1, 0));
		Assert.assertTrue(attr1.inRange(2, 0));
		Assert.assertTrue(attr1.inRange(3, 0));
		Assert.assertTrue(attr1.inRange(4, 0));
		Assert.assertFalse(attr1.inRange(5, 0));

		
		//bin 1 has bin size elements = 4
		Assert.assertFalse(attr1.inRange(4, 1));
		Assert.assertTrue(attr1.inRange(5, 1));
		Assert.assertTrue(attr1.inRange(6, 1));
		Assert.assertTrue(attr1.inRange(7, 1));
		Assert.assertTrue(attr1.inRange(8, 1));
		Assert.assertFalse(attr1.inRange(9, 1));
		
		//bin 2 has bin size elements = 4
		Assert.assertTrue(attr1.inRange(9, 2));
		Assert.assertTrue(attr1.inRange(10, 2));
		Assert.assertTrue(attr1.inRange(11, 2));
		Assert.assertTrue(attr1.inRange(12, 2));
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
	public void test_isInBinRange_attribute2() {
		//bin 0 bin size + 1 elements = 5
		Assert.assertTrue(attr2.inRange(1, 0));
		Assert.assertTrue(attr2.inRange(2, 1));
		Assert.assertTrue(attr2.inRange(3, 2));
		Assert.assertTrue(attr2.inRange(4, 3));
		Assert.assertTrue(attr2.inRange(5, 4));
		Assert.assertTrue(attr2.inRange(6, 5));
		Assert.assertTrue(attr2.inRange(7, 6));
		Assert.assertTrue(attr2.inRange(8, 7));
		
		Assert.assertFalse(attr2.inRange(1, 1));
		Assert.assertFalse(attr2.inRange(2, 2));
		Assert.assertFalse(attr2.inRange(3, 3));
		Assert.assertFalse(attr2.inRange(4, 4));
		Assert.assertFalse(attr2.inRange(5, 5));
		Assert.assertFalse(attr2.inRange(6, 6));
		Assert.assertFalse(attr2.inRange(7, 7));
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
		Assert.assertEquals(-13, attr3.decode(0));
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
	
	@Test
	public void test_dynamic_discretization_from_histogram_with_discretization() {
		TreeMap<Integer, Long> values  =  new TreeMap<Integer, Long>() {
			private static final long serialVersionUID = 1L;
		{
			put(0, 1L);
			put(1, 1L);
			put(2, 1L);
			put(3, 1L);
			put(7, 1L);
			put(8, 1L);
			put(9, 1L);
		}};
		
		IntegerDiscretization attr1 = new IntegerDiscretization(new double[] {0.0, 9.0}, 1.0, new Integer[][] {new Integer[]{0, 3}, 
																											   new Integer[]{4, 6},
																											   new Integer[]{7, 9}}, values);
		//check bins number found
		Assert.assertEquals(2, attr1.getBinNumber());
		
		//check the value range size of each one
		Assert.assertEquals(4, attr1.getDiscreteSize(0)); Assert.assertEquals(3, attr1.getDiscreteSize(1));
		
		//check code values
		Assert.assertEquals(0, attr1.code(0)); Assert.assertEquals(0, attr1.code(1)); Assert.assertEquals(0, attr1.code(2)); Assert.assertEquals(0, attr1.code(3));
		Assert.assertEquals(1, attr1.code(7)); Assert.assertEquals(1, attr1.code(8)); Assert.assertEquals(1, attr1.code(9)); 
		
		//check the occurrences counts of each bin
		Assert.assertEquals(4 ,attr1.getBins()[0].getCounts());
		Assert.assertEquals(3 ,attr1.getBins()[1].getCounts());
		
		//check decode value
		Assert.assertEquals(2, attr1.decode(0)); Assert.assertEquals(8, attr1.decode(1));
	}
	
	@Test
	public void test_dynamic_discretization_from_histogram_and_bins_number() {
		TreeMap<Integer, Long> values  =  new TreeMap<Integer, Long>() {
			private static final long serialVersionUID = 1L;
		{
			put(0, 1L);
			put(1, 1L);
			put(2, 1L);
			put(3, 1L);
			put(7, 1L);
			put(8, 1L);
			put(9, 1L);
		}};
		
		IntegerDiscretization attr1 = new IntegerDiscretization(new double[] {0.0, 9.0}, 1.0, 3, values);
				
		//check bins number found
		Assert.assertEquals(2, attr1.getBinNumber());
		
		//check the value range size of each one
		Assert.assertEquals(4, attr1.getDiscreteSize(0)); Assert.assertEquals(3, attr1.getDiscreteSize(1));
		
		//check code values
		Assert.assertEquals(0, attr1.code(0)); Assert.assertEquals(0, attr1.code(1)); Assert.assertEquals(0, attr1.code(2)); Assert.assertEquals(0, attr1.code(3));
		Assert.assertEquals(1, attr1.code(7)); Assert.assertEquals(1, attr1.code(8)); Assert.assertEquals(1, attr1.code(9)); 
		
		//check the occurrences counts of each bin
		Assert.assertEquals(4 ,attr1.getBins()[0].getCounts());
		Assert.assertEquals(3 ,attr1.getBins()[1].getCounts());
		
		//check decode value
		Assert.assertEquals(2, attr1.decode(0)); Assert.assertEquals(8, attr1.decode(1));
	}

	@Test
	public void test_dynamic_discretization_from_histogram_only() {
		TreeMap<Integer, Long> values  =  new TreeMap<Integer, Long>() {
			private static final long serialVersionUID = 1L;
		{
			put(0, 1L);
			put(1, 1L);
			put(2, 1L);
			put(3, 1L);
			put(7, 1L);
			put(8, 1L);
			put(9, 1L);
		}};
		
		IntegerDiscretization attr1 = new IntegerDiscretization(new double[] {0.0, 9.0}, 1.0, -1, values);
						
		//check bins number found
		Assert.assertEquals(7, attr1.getBinNumber());
		
		//check the value range size of each one
		Assert.assertEquals(1, attr1.getDiscreteSize(0)); Assert.assertEquals(1, attr1.getDiscreteSize(1));
		
		//check code values
		Assert.assertEquals(0, attr1.code(0)); Assert.assertEquals(1, attr1.code(1)); Assert.assertEquals(2, attr1.code(2)); Assert.assertEquals(3, attr1.code(3));
		Assert.assertEquals(4, attr1.code(7)); Assert.assertEquals(5, attr1.code(8)); Assert.assertEquals(6, attr1.code(9)); 
		
		//check the occurrences counts of each bin
		Assert.assertEquals(1 ,attr1.getBins()[0].getCounts());
		Assert.assertEquals(1 ,attr1.getBins()[1].getCounts());
		
		//check decode value
		Assert.assertEquals(0, attr1.decode(0)); Assert.assertEquals(1, attr1.decode(1)); Assert.assertEquals(2, attr1.decode(2)); Assert.assertEquals(3, attr1.decode(3));
		Assert.assertEquals(7, attr1.decode(4)); Assert.assertEquals(8, attr1.decode(5)); Assert.assertEquals(9, attr1.decode(6)); 
	}
	
}
