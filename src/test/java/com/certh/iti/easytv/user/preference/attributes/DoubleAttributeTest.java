package com.certh.iti.easytv.user.preference.attributes;

import java.math.BigDecimal;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;

import junit.framework.Assert;

public class DoubleAttributeTest {
	
	DoubleAttribute attr1, attr2, attr3, attr4, qFactor, qFactor_withBins, qFactor_equals_bins, qFactor_not_equals_bins; 
	Discretization dist_attr1, dist_attr2, dist_attr3, dist_attr4, dist_qFactor, dist_qFactor_equals_bins, dist_qFactor_not_equals_bins;
	
	@BeforeClass
	public void beforTest() {
		
		attr1 = new DoubleAttribute(new double[] {0.0, 100.0}, 1.0, 25, -1);
		attr2 = new DoubleAttribute(new double[] {1.0, 8.0}, -1);
		attr3 = new DoubleAttribute(new double[] {1.0, 2.0}, 0.5, -1);
		attr4 = new DoubleAttribute(new double[] {1.5, 3.5}, 0.5, -1);
		qFactor = new DoubleAttribute(new double[] {0.7, 12.0}, 0.1, -1.0);
		qFactor_equals_bins = new DoubleAttribute(new double[] {0.7, 12.0}, 0.1, 38, -1.0);
		qFactor_not_equals_bins = new DoubleAttribute(new double[] {0.7, 12.0}, 0.1, 28, -1.0);
		
		//get discretization
		dist_attr1 = attr1.getDiscretization();
		dist_attr2 = attr2.getDiscretization();
		dist_attr3 = attr3.getDiscretization();
		dist_attr4 = attr4.getDiscretization();
		dist_qFactor = qFactor.getDiscretization();
		dist_qFactor_equals_bins = qFactor_equals_bins.getDiscretization();
		dist_qFactor_not_equals_bins = qFactor_not_equals_bins.getDiscretization();
	}
	
	@Test
	public void test_BinNumber() {
		Assert.assertEquals(25, dist_attr1.getBinNumber());
		Assert.assertEquals(8, dist_attr2.getBinNumber());
		Assert.assertEquals(3, dist_attr3.getBinNumber());
		Assert.assertEquals(5, dist_attr4.getBinNumber());
		Assert.assertEquals(114, dist_qFactor.getBinNumber());
		Assert.assertEquals(38, dist_qFactor_equals_bins.getBinNumber());
		Assert.assertEquals(28, dist_qFactor_not_equals_bins.getBinNumber());
	}
	
	@Test
	public void test_Binsize_remaining() {
		Assert.assertEquals(5, dist_attr1.getDiscreteSize(0)); Assert.assertEquals(4, dist_attr1.getDiscreteSize(1));
		Assert.assertEquals(1, dist_attr2.getDiscreteSize(0));
		Assert.assertEquals(1, dist_attr3.getDiscreteSize(0));
		Assert.assertEquals(1, dist_attr4.getDiscreteSize(0));
		Assert.assertEquals(1, dist_qFactor.getDiscreteSize(0));
		Assert.assertEquals(3, dist_qFactor_equals_bins.getDiscreteSize(0));
		Assert.assertEquals(5, dist_qFactor_not_equals_bins.getDiscreteSize(0)); Assert.assertEquals(5, dist_qFactor_not_equals_bins.getDiscreteSize(1)); Assert.assertEquals(4, dist_qFactor_not_equals_bins.getDiscreteSize(2));
	}
	
	@Test
	public void test_code_attribute1() {
		//bin 0 bin size + 1 elements = 5
		Assert.assertEquals(0, dist_attr1.code(0.0) );
		Assert.assertEquals(0, dist_attr1.code(1.0) );
		Assert.assertEquals(0, dist_attr1.code(2.0) );
		Assert.assertEquals(0, dist_attr1.code(3.0) );
		Assert.assertEquals(0, dist_attr1.code(4.0) );
		
		//bin 1 has bin size elements = 4
		Assert.assertEquals(1, dist_attr1.code(5.0) );
		Assert.assertEquals(1, dist_attr1.code(6.0) );
		Assert.assertEquals(1, dist_attr1.code(7.0) );
		Assert.assertEquals(1, dist_attr1.code(8.0) );
		
		//bin 2 has bin size elements = 4
		Assert.assertEquals(2, dist_attr1.code(9.0) );
		Assert.assertEquals(2, dist_attr1.code(10.0) );
		Assert.assertEquals(2, dist_attr1.code(11.0) );
		Assert.assertEquals(2, dist_attr1.code(12.0) );
	}
	
	@Test
	public void test_isInBinRange_attribute1() {
		//bin 0 bin size + 1 elements = 5
		Assert.assertFalse(dist_attr1.inRange(-10., 0));
		Assert.assertTrue(dist_attr1.inRange(0.0, 0));
		Assert.assertTrue(dist_attr1.inRange(1.0, 0));
		Assert.assertTrue(dist_attr1.inRange(2.0, 0));
		Assert.assertTrue(dist_attr1.inRange(3.0, 0));
		Assert.assertTrue(dist_attr1.inRange(4.0, 0));
		Assert.assertFalse(dist_attr1.inRange(5.0, 0));

		
		//bin 1 has bin size elements = 4
		Assert.assertFalse(dist_attr1.inRange(4.0, 1));
		Assert.assertTrue(dist_attr1.inRange(5.0, 1));
		Assert.assertTrue(dist_attr1.inRange(6.0, 1));
		Assert.assertTrue(dist_attr1.inRange(7.0, 1));
		Assert.assertTrue(dist_attr1.inRange(8.0, 1));
		Assert.assertFalse(dist_attr1.inRange(9.0, 1));
		
		//bin 2 has bin size elements = 4
		Assert.assertTrue(dist_attr1.inRange(9.0, 2));
		Assert.assertTrue(dist_attr1.inRange(10.0, 2));
		Assert.assertTrue(dist_attr1.inRange(11.0, 2));
		Assert.assertTrue(dist_attr1.inRange(12.0, 2));
	}
	
	@Test
	public void test_decode_attribute1() {
		Assert.assertEquals(2.0, dist_attr1.decode(0));
		Assert.assertEquals(7.0, dist_attr1.decode(1));
		Assert.assertEquals(11.0, dist_attr1.decode(2));
	}
	
	@Test
	public void test_code_attribute2() {		
		Assert.assertEquals(0, dist_attr2.code(1.0));
		Assert.assertEquals(1, dist_attr2.code(2.0));
		Assert.assertEquals(2, dist_attr2.code(3.0));
		Assert.assertEquals(3, dist_attr2.code(4.0));
		Assert.assertEquals(4, dist_attr2.code(5.0));
		Assert.assertEquals(5, dist_attr2.code(6.0));
		Assert.assertEquals(6, dist_attr2.code(7.0));
		Assert.assertEquals(7, dist_attr2.code(8.0));
	}
	
	@Test
	public void test_isInBinRange_attribute2() {
		//bin 0 bin size + 1 elements = 5
		Assert.assertTrue(dist_attr2.inRange(1.0, 0));
		Assert.assertTrue(dist_attr2.inRange(2.0, 1));
		Assert.assertTrue(dist_attr2.inRange(3.0, 2));
		Assert.assertTrue(dist_attr2.inRange(4.0, 3));
		Assert.assertTrue(dist_attr2.inRange(5.0, 4));
		Assert.assertTrue(dist_attr2.inRange(6.0, 5));
		Assert.assertTrue(dist_attr2.inRange(7.0, 6));
		Assert.assertTrue(dist_attr2.inRange(8.0, 7));
		
		Assert.assertFalse(dist_attr2.inRange(1.0, 1));
		Assert.assertFalse(dist_attr2.inRange(2.0, 2));
		Assert.assertFalse(dist_attr2.inRange(3.0, 3));
		Assert.assertFalse(dist_attr2.inRange(4.0, 4));
		Assert.assertFalse(dist_attr2.inRange(5.0, 5));
		Assert.assertFalse(dist_attr2.inRange(6.0, 6));
		Assert.assertFalse(dist_attr2.inRange(7.0, 7));
	}
	
	@Test
	public void test_decode_attribute2() {
		Assert.assertEquals(1.0, dist_attr2.decode(0));
		Assert.assertEquals(2.0, dist_attr2.decode(1));
		Assert.assertEquals(3.0, dist_attr2.decode(2));
	}

	@Test
	public void test_code_attribute3() {		
		Assert.assertEquals(0, dist_attr3.code(1.0));
		Assert.assertEquals(1, dist_attr3.code(1.5));
		Assert.assertEquals(2, dist_attr3.code(2.0));
	}
	
	@Test
	public void test_decode_attribute3() {
		Assert.assertEquals(1.0, dist_attr3.decode(0));
		Assert.assertEquals(1.5, dist_attr3.decode(1));
		Assert.assertEquals(2.0, dist_attr3.decode(2));
	}
	
	@Test
	public void test_code_attribute4() {		
		Assert.assertEquals(0, dist_attr4.code(1.5));
		Assert.assertEquals(1, dist_attr4.code(2.0));
		Assert.assertEquals(2, dist_attr4.code(2.5));
		Assert.assertEquals(3, dist_attr4.code(3.0));
		Assert.assertEquals(4, dist_attr4.code(3.5));
	}
	
	@Test
	public void test_decode_attribute4() {
		Assert.assertEquals(1.5, dist_attr4.decode(0));
		Assert.assertEquals(2.0, dist_attr4.decode(1));
		Assert.assertEquals(2.5, dist_attr4.decode(2));
		Assert.assertEquals(3.0, dist_attr4.decode(3));
		Assert.assertEquals(3.5, dist_attr4.decode(4));

	}
	
	@Test
	public void test_code_qFactor() {
		Assert.assertEquals(0, dist_qFactor.code(0.7) );
		Assert.assertEquals(1, dist_qFactor.code(0.8) );
		Assert.assertEquals(2, dist_qFactor.code(0.9) );
		Assert.assertEquals(3, dist_qFactor.code(1.0) );
		Assert.assertEquals(4, dist_qFactor.code(1.1) );
	
		Assert.assertEquals(103, dist_qFactor.code(11.0) );
		Assert.assertEquals(112, dist_qFactor.code(11.9) );
		Assert.assertEquals(113, dist_qFactor.code(12.0) );
		
		int index = 0;
		BigDecimal x = new BigDecimal(String.valueOf("0.7"));
		for(double i = 0.7 ; i < 12.0; index++, i += 0.1) {
			Assert.assertEquals(index, dist_qFactor.code(x.doubleValue()));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}
	}
	
	@Test
	public void test_isInBinRange_qFactor() {
		
		int index = 0;
		BigDecimal x = new BigDecimal(String.valueOf("0.7"));
		for(double i = 0.7 ; i < 12.0; index++, i += 0.1) {
			Assert.assertTrue(dist_qFactor.inRange(x.doubleValue(), index));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}
	}
	
	@Test
	public void test_decode_qFactor() {		
		BigDecimal x = new BigDecimal(String.valueOf("0.7"));
		for(int bin = 0; bin < dist_qFactor.getBinNumber(); bin++) {
			Assert.assertEquals(x.doubleValue(), dist_qFactor.decode(bin));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}	
	}
	
	@Test
	public void test_code_qFactor_equals_bins() {
		Assert.assertEquals(0, dist_qFactor_equals_bins.code(0.7) );
		Assert.assertEquals(0, dist_qFactor_equals_bins.code(0.8) );
		Assert.assertEquals(0, dist_qFactor_equals_bins.code(0.9) );
		Assert.assertEquals(1, dist_qFactor_equals_bins.code(1.0) );
		Assert.assertEquals(1, dist_qFactor_equals_bins.code(1.1) );
		Assert.assertEquals(1, dist_qFactor_equals_bins.code(1.2) );
		Assert.assertEquals(37, dist_qFactor_equals_bins.code(11.8) );
		Assert.assertEquals(37, dist_qFactor_equals_bins.code(11.9) );
		Assert.assertEquals(37, dist_qFactor_equals_bins.code(12.0) );
		
		int index = 0;
		int expected_binId = 0;
		BigDecimal x = new BigDecimal(String.valueOf("0.7"));
		for(double i = 0.7 ; i < 12.0; index++, i += 0.1) {
			if(index % dist_qFactor_equals_bins.getDiscreteSize(expected_binId) == 0 && index != 0) expected_binId++;
			Assert.assertEquals(expected_binId, dist_qFactor_equals_bins.code(x.doubleValue()));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}
	}
	
	@Test
	public void test_isInBinRange_qFactor_equals_bins() {
		int index = 0;
		int bin = -1;
		BigDecimal x = new BigDecimal(String.valueOf("0.7"));
		for(double i = 0.7 ; i < 12.0; index++, i += 0.1) {
			if(index % dist_qFactor_equals_bins.getDiscreteSize(0) == 0) bin++;
			Assert.assertTrue(dist_qFactor_equals_bins.inRange(x.doubleValue(), bin));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}
	}
	
	@Test
	public void test_decode_qFactor_equals_bins() {
		BigDecimal x = new BigDecimal(String.valueOf("0.8"));
		for(int bin = 0; bin < dist_qFactor_equals_bins.getBinNumber(); bin++) {
			Assert.assertEquals(x.doubleValue(), dist_qFactor_equals_bins.decode(bin));
			x = x.add(new BigDecimal(String.valueOf("0.3")));
		}
	}
	
	@Test
	public void test_code_qFactor_not_equals_bins() {
		Assert.assertEquals(0, dist_qFactor_not_equals_bins.code(0.7) );
		Assert.assertEquals(0, dist_qFactor_not_equals_bins.code(0.8) );
		Assert.assertEquals(0, dist_qFactor_not_equals_bins.code(0.9) );
		Assert.assertEquals(0, dist_qFactor_not_equals_bins.code(1.0) );
		Assert.assertEquals(0, dist_qFactor_not_equals_bins.code(1.1) );
		
		Assert.assertEquals(1, dist_qFactor_not_equals_bins.code(1.2) );
		Assert.assertEquals(1, dist_qFactor_not_equals_bins.code(1.3) );
		Assert.assertEquals(1, dist_qFactor_not_equals_bins.code(1.4) );
		Assert.assertEquals(1, dist_qFactor_not_equals_bins.code(1.5) );
		Assert.assertEquals(1, dist_qFactor_not_equals_bins.code(1.6) );
		
		Assert.assertEquals(2, dist_qFactor_not_equals_bins.code(1.7) );
		
		int index = 0;
		int expected_binId = 1;
		BigDecimal x = new BigDecimal(String.valueOf("1.7"));
		for(double i = 1.7 ; i < 12.0; index++, i += 0.1) {
			if(index % dist_qFactor_not_equals_bins.getDiscreteSize(expected_binId) == 0) expected_binId++;
			Assert.assertEquals(expected_binId, dist_qFactor_not_equals_bins.code(x.doubleValue()));
			x = x.add(new BigDecimal(String.valueOf("0.1")));
		}
	}
	
	@Test
	public void test_isInBinRange_qFactor_not_equals_bins() {
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(0.7, 0));
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(0.8, 0));
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(0.9, 0));
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(1.0, 0));
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(1.1, 0));
		
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(1.2, 1));
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(1.3, 1));
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(1.4, 1));
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(1.5, 1));
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(1.6, 1));
		
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(1.7, 2));
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(1.8, 2));
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(1.9, 2));
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(2.0, 2));
		
		Assert.assertTrue(dist_qFactor_not_equals_bins.inRange(2.1, 3));
	}
	
	@Test
	public void test_decode_qFactor_not_equals_bins() {
		Assert.assertEquals(0.9, dist_qFactor_not_equals_bins.decode(0) );
		Assert.assertEquals(1.4, dist_qFactor_not_equals_bins.decode(1) );
		Assert.assertEquals(1.9, dist_qFactor_not_equals_bins.decode(2) );
		Assert.assertEquals(2.3, dist_qFactor_not_equals_bins.decode(3) );

		BigDecimal x = new BigDecimal(String.valueOf("2.3"));
		for(int bin = 3; bin < dist_qFactor_not_equals_bins.getBinNumber(); bin++) {
			Assert.assertEquals(x.doubleValue(), dist_qFactor_not_equals_bins.decode(bin));
			x = x.add(new BigDecimal(String.valueOf("0.4")));
		}
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_code_outOfStep() {
		dist_attr4.code(9.0);
		dist_attr4.code(1.0);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID1() {
		dist_attr4.decode(dist_attr4.getBinNumber());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID2() {
		dist_attr4.decode(-1);
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
		dist_attr1.inRange(1, -1);
		dist_attr1.inRange(1, 26);
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
