package com.certh.iti.easytv.user.preference.attributes;

import java.math.BigDecimal;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class DoubleAttributeTest {
	
	DoubleAttribute attr1, attr2, attr3, attr4, qFactor, qFactor_withBins, qFactor_equals_bins, qFactor_not_equals_bins; 
	
	@BeforeClass
	public void beforTest() {
		
		attr1 = new DoubleAttribute(new double[] {0.0, 100.0}, 1.0, 25, -1);
		attr2 = new DoubleAttribute(new double[] {1.0, 8.0}, -1);
		attr3 = new DoubleAttribute(new double[] {1.0, 2.0}, 0.5, -1);
		attr4 = new DoubleAttribute(new double[] {1.5, 3.5}, 0.5, -1);
		qFactor = new DoubleAttribute(new double[] {0.7, 12.0}, 0.1, -1.0);
		qFactor_equals_bins = new DoubleAttribute(new double[] {0.7, 12.0}, 0.1, 38, -1.0);
		qFactor_not_equals_bins = new DoubleAttribute(new double[] {0.7, 12.0}, 0.1, 28, -1.0);

		System.out.println("\n\nBefore Class");
		System.out.println(attr1.toString());
		System.out.println(attr2.toString());
		System.out.println(attr3.toString());
		System.out.println(attr4.toString());
		System.out.println(qFactor.toString());
		System.out.println(qFactor_equals_bins.toString());
		System.out.println(qFactor_not_equals_bins.toString());
	}
	
	@AfterClass
	public void afterClass() {
		
		System.out.println("\n\nAfter Class");
		System.out.println(attr1.toString());
		System.out.println(attr2.toString());
		System.out.println(attr3.toString());
		System.out.println(attr4.toString());
		System.out.println(qFactor.toString());
		System.out.println(qFactor_equals_bins.toString());
		System.out.println(qFactor_not_equals_bins.toString());
	}
	
	@Test
	public void test_BinNumber() {
		Assert.assertEquals(25, attr1.getBinNumber());
		Assert.assertEquals(8, attr2.getBinNumber());
		Assert.assertEquals(3, attr3.getBinNumber());
		Assert.assertEquals(5, attr4.getBinNumber());
		Assert.assertEquals(114, qFactor.getBinNumber());
		Assert.assertEquals(38, qFactor_equals_bins.getBinNumber());
		Assert.assertEquals(28, qFactor_not_equals_bins.getBinNumber());
	}
	
	@Test
	public void test_Binsize_remaining() {
		Assert.assertEquals(1, attr1.getRemaining());  Assert.assertEquals(4, attr1.getBinSize());
		Assert.assertEquals(0, attr2.getRemaining()); Assert.assertEquals(1, attr2.getBinSize());
		Assert.assertEquals(0, attr3.getRemaining()); Assert.assertEquals(1, attr3.getBinSize());
		Assert.assertEquals(0, attr4.getRemaining()); Assert.assertEquals(1, attr4.getBinSize());
		Assert.assertEquals(0, qFactor.getRemaining()); Assert.assertEquals(1, qFactor.getBinSize());
		Assert.assertEquals(0, qFactor_equals_bins.getRemaining()); Assert.assertEquals(3, qFactor_equals_bins.getBinSize());
		Assert.assertEquals(2, qFactor_not_equals_bins.getRemaining()); Assert.assertEquals(4, qFactor_not_equals_bins.getBinSize());
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
	
	@Test
	public void test_code_qFactor() {
		Assert.assertEquals(0, qFactor.code(0.7) );
		Assert.assertEquals(1, qFactor.code(0.8) );
		Assert.assertEquals(2, qFactor.code(0.9) );
		Assert.assertEquals(3, qFactor.code(1.0) );
		Assert.assertEquals(4, qFactor.code(1.1) );
	
		Assert.assertEquals(103, qFactor.code(11.0) );
		Assert.assertEquals(112, qFactor.code(11.9) );
		Assert.assertEquals(113, qFactor.code(12.0) );
		
		int index = 0;
		BigDecimal x = new BigDecimal(String.valueOf("0.7"));
		for(double i = 0.7 ; i < 12.0; index++, i += 0.1) {
			Assert.assertEquals(index, qFactor.code(x.doubleValue()));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}
	}
	
	@Test
	public void test_isInBinRange_qFactor() {
		
		int index = 0;
		BigDecimal x = new BigDecimal(String.valueOf("0.7"));
		for(double i = 0.7 ; i < 12.0; index++, i += 0.1) {
			Assert.assertTrue(qFactor.isInBinRange(x.doubleValue(), index));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}
	}
	
	@Test
	public void test_decode_qFactor() {		
		BigDecimal x = new BigDecimal(String.valueOf("0.7"));
		for(int bin = 0; bin < qFactor.getBinNumber(); bin++) {
			Assert.assertEquals(x.doubleValue(), qFactor.decode(bin));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}	
	}
	
	@Test
	public void test_code_qFactor_equals_bins() {
		Assert.assertEquals(0, qFactor_equals_bins.code(0.7) );
		Assert.assertEquals(0, qFactor_equals_bins.code(0.8) );
		Assert.assertEquals(0, qFactor_equals_bins.code(0.9) );
		Assert.assertEquals(1, qFactor_equals_bins.code(1.0) );
		Assert.assertEquals(1, qFactor_equals_bins.code(1.1) );
		Assert.assertEquals(1, qFactor_equals_bins.code(1.2) );
		Assert.assertEquals(37, qFactor_equals_bins.code(11.8) );
		Assert.assertEquals(37, qFactor_equals_bins.code(11.9) );
		Assert.assertEquals(37, qFactor_equals_bins.code(12.0) );
		
		int index = 0;
		int bin = -1;
		BigDecimal x = new BigDecimal(String.valueOf("0.7"));
		for(double i = 0.7 ; i < 12.0; index++, i += 0.1) {
			if(index % qFactor_equals_bins.getBinSize() == 0) bin++;
			Assert.assertEquals(bin, qFactor_equals_bins.code(x.doubleValue()));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}
	}
	
	@Test
	public void test_isInBinRange_qFactor_equals_bins() {
		int index = 0;
		int bin = -1;
		BigDecimal x = new BigDecimal(String.valueOf("0.7"));
		for(double i = 0.7 ; i < 12.0; index++, i += 0.1) {
			if(index % qFactor_equals_bins.getBinSize() == 0) bin++;
			Assert.assertTrue(qFactor_equals_bins.isInBinRange(x.doubleValue(), bin));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}
	}
	
	@Test
	public void test_decode_qFactor_equals_bins() {
		BigDecimal x = new BigDecimal(String.valueOf("0.8"));
		for(int bin = 0; bin < qFactor_equals_bins.getBinNumber(); bin++) {
			Assert.assertEquals(x.doubleValue(), qFactor_equals_bins.decode(bin));
			x = x.add(new BigDecimal(String.valueOf("0.3")));
		}
	}
	
	@Test
	public void test_code_qFactor_not_equals_bins() {
		Assert.assertEquals(0, qFactor_not_equals_bins.code(0.7) );
		Assert.assertEquals(0, qFactor_not_equals_bins.code(0.8) );
		Assert.assertEquals(0, qFactor_not_equals_bins.code(0.9) );
		Assert.assertEquals(0, qFactor_not_equals_bins.code(1.0) );
		Assert.assertEquals(0, qFactor_not_equals_bins.code(1.1) );
		
		Assert.assertEquals(1, qFactor_not_equals_bins.code(1.2) );
		Assert.assertEquals(1, qFactor_not_equals_bins.code(1.3) );
		Assert.assertEquals(1, qFactor_not_equals_bins.code(1.4) );
		Assert.assertEquals(1, qFactor_not_equals_bins.code(1.5) );
		Assert.assertEquals(1, qFactor_not_equals_bins.code(1.6) );
		
		Assert.assertEquals(2, qFactor_not_equals_bins.code(1.7) );

		int index = 0;
		int bin = 1;
		BigDecimal x = new BigDecimal(String.valueOf("1.7"));
		for(double i = 1.7 ; i < 12.0; index++, i += 0.1) {
			if(index % qFactor_not_equals_bins.getBinSize() == 0) bin++;
			Assert.assertEquals(bin, qFactor_not_equals_bins.code(x.doubleValue()));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}
	}
	
	@Test
	public void test_isInBinRange_qFactor_not_equals_bins() {
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(0.7, 0));
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(0.8, 0));
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(0.9, 0));
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(1.0, 0));
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(1.1, 0));
		
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(1.2, 1));
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(1.3, 1));
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(1.4, 1));
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(1.5, 1));
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(1.6, 1));
		
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(1.7, 2));
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(1.8, 2));
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(1.9, 2));
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(2.0, 2));
		
		Assert.assertTrue(qFactor_not_equals_bins.isInBinRange(2.1, 3));
	}
	
	@Test
	public void test_decode_qFactor_not_equals_bins() {
		Assert.assertEquals(0.9, qFactor_not_equals_bins.decode(0) );
		Assert.assertEquals(1.4, qFactor_not_equals_bins.decode(1) );
		Assert.assertEquals(1.9, qFactor_not_equals_bins.decode(2) );
		Assert.assertEquals(2.3, qFactor_not_equals_bins.decode(3) );

		BigDecimal x = new BigDecimal(String.valueOf("2.3"));
		for(int bin = 3; bin < qFactor_not_equals_bins.getBinNumber(); bin++) {
			Assert.assertEquals(x.doubleValue(), qFactor_not_equals_bins.decode(bin));
			x = x.add(new BigDecimal(String.valueOf("0.4")));
		}
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
