package com.certh.iti.easytv.user.preference.attributes;

import java.io.IOException;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.UserContext;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute.IntegerConverter;
import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;

import junit.framework.Assert;

public class IntegerAttributeTest {
	
	IntegerAttribute attr1, attr2, attr3, attr4, attr5, attr6; 
	Discretization dist_attr1, dist_attr2, dist_attr3, dist_attr4, dist_attr6;

	@BeforeClass
	public void beforClass() throws IOException {

		attr1 = new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, 0);
		attr2 = new IntegerAttribute(new double[] {1.0, 8.0}, 1.0, 0.0);
		attr3 = new IntegerAttribute(new double[] {-15.0, 15.0}, 1.0, 10, 0);
		attr4 = new IntegerAttribute(new double[] {0.0, 100.0}, 2.0, 25, 0);
		attr5 = (IntegerAttribute) IntegerAttribute
				.Builder()
				.setRange(new double[] {0.0, 10.0})
				.setDiscretes(new Integer[][] {
			    	new  Integer[] {0, 3},
			    	new  Integer[] {4, 5},
			    	new  Integer[] {6,10}
			    })
				.setConverter(new IntegerConverter() {
					@Override
					public Integer valueOf(Object obj) {
						return Number.class.cast(obj).intValue();
					}
					
					@Override
					public boolean isInstance(Object obj) {
						return Number.class.isInstance(obj);
					}
				})
				.build();
		
		
		/*case http://registry.easytv.eu/context/device/screenSize/xdpi*/
		attr6 =  (IntegerAttribute) IntegerAttribute
				.Builder()
				.setRange(new double[] {150.0, 640.0})
				.setStep(1.0)
				.setDiscretes(UserContext.DENSITY_DISCRET)
				.setConverter(new IntegerConverter() {
					@Override
					public Integer valueOf(Object obj) {
						return Number.class.cast(obj).intValue();
					}
					
					@Override
					public boolean isInstance(Object obj) {
						return Number.class.isInstance(obj);
					}
				})
				.build();
		
		//handle proper values
		for(int i = 0; i < 101; i++) attr1.handle(i);
		for(int i = 1; i < 9; i++) attr2.handle(i);
		for(int i = -15; i < 16; i++) attr3.handle(i);
		for(int i = 0; i < 101; i+= 2) attr4.handle(i);
		
		//handle the only value
		attr6.handle(257.5769958496094);
		
		//get discretization
		dist_attr1 = attr1.getDiscretization();
		dist_attr2 = attr2.getDiscretization();
		dist_attr3 = attr3.getDiscretization();
		dist_attr4 = attr4.getDiscretization();
		dist_attr6 = attr6.getDiscretization();
		
	}
	
	@Test
	public void test_BinNumber() {
		Assert.assertEquals(25, dist_attr1.getBinNumber());
		Assert.assertEquals(8, dist_attr2.getBinNumber());
		Assert.assertEquals(10, dist_attr3.getBinNumber());
		Assert.assertEquals(25, dist_attr4.getBinNumber());
	}
	
	@Test
	public void test_Binsize_remaining() {
		Assert.assertEquals(5, dist_attr1.getDiscreteSize(0)); Assert.assertEquals(4, dist_attr1.getDiscreteSize(1));
		Assert.assertEquals(1, dist_attr2.getDiscreteSize(0));
		Assert.assertEquals(3, dist_attr3.getDiscreteSize(1));
		Assert.assertEquals(2, dist_attr4.getDiscreteSize(1));
	}
	
	@Test
	public void test_code_attribute1() {
		//bin 0 bin size + 1 elements = 5
		Assert.assertEquals(0, dist_attr1.code(0));
		Assert.assertEquals(0, dist_attr1.code(1));
		Assert.assertEquals(0, dist_attr1.code(2));
		Assert.assertEquals(0, dist_attr1.code(3));
		Assert.assertEquals(0, dist_attr1.code(4));
		
		//bin 1 has bin size elements = 4
		Assert.assertEquals(1, dist_attr1.code(5));
		Assert.assertEquals(1, dist_attr1.code(6));
		Assert.assertEquals(1, dist_attr1.code(7));
		Assert.assertEquals(1, dist_attr1.code(8));
		
		//bin 2 has bin size elements = 4
		Assert.assertEquals(2, dist_attr1.code(9));
		Assert.assertEquals(2, dist_attr1.code(10));
		Assert.assertEquals(2, dist_attr1.code(11));
		Assert.assertEquals(2, dist_attr1.code(12));
	}
	
	@Test
	public void test_isInBinRange_attribute1() {
		//bin 0 bin size + 1 elements = 5
		Assert.assertFalse(dist_attr1.inRange(-1, 0));
		Assert.assertTrue(dist_attr1.inRange(0, 0));
		Assert.assertTrue(dist_attr1.inRange(1, 0));
		Assert.assertTrue(dist_attr1.inRange(2, 0));
		Assert.assertTrue(dist_attr1.inRange(3, 0));
		Assert.assertTrue(dist_attr1.inRange(4, 0));
		Assert.assertFalse(dist_attr1.inRange(5, 0));

		
		//bin 1 has bin size elements = 4
		Assert.assertFalse(dist_attr1.inRange(4, 1));
		Assert.assertTrue(dist_attr1.inRange(5, 1));
		Assert.assertTrue(dist_attr1.inRange(6, 1));
		Assert.assertTrue(dist_attr1.inRange(7, 1));
		Assert.assertTrue(dist_attr1.inRange(8, 1));
		Assert.assertFalse(dist_attr1.inRange(9, 1));
		
		//bin 2 has bin size elements = 4
		Assert.assertTrue(dist_attr1.inRange(9, 2));
		Assert.assertTrue(dist_attr1.inRange(10, 2));
		Assert.assertTrue(dist_attr1.inRange(11, 2));
		Assert.assertTrue(dist_attr1.inRange(12, 2));
	}
	
	@Test
	public void test_decode_attribute1() {
		Assert.assertEquals(2, dist_attr1.decode(0));
		Assert.assertEquals(7, dist_attr1.decode(1));
		Assert.assertEquals(11, dist_attr1.decode(2));
	}
	
	@Test
	public void test_code_attribute2() {		
		Assert.assertEquals(0, dist_attr2.code(1));
		Assert.assertEquals(1, dist_attr2.code(2));
		Assert.assertEquals(2, dist_attr2.code(3));
		Assert.assertEquals(3, dist_attr2.code(4));
		Assert.assertEquals(4, dist_attr2.code(5));
		Assert.assertEquals(5, dist_attr2.code(6));
		Assert.assertEquals(6, dist_attr2.code(7));
		Assert.assertEquals(7, dist_attr2.code(8));
	}
	
	@Test
	public void test_isInBinRange_attribute2() {
		//bin 0 bin size + 1 elements = 5
		Assert.assertTrue(dist_attr2.inRange(1, 0));
		Assert.assertTrue(dist_attr2.inRange(2, 1));
		Assert.assertTrue(dist_attr2.inRange(3, 2));
		Assert.assertTrue(dist_attr2.inRange(4, 3));
		Assert.assertTrue(dist_attr2.inRange(5, 4));
		Assert.assertTrue(dist_attr2.inRange(6, 5));
		Assert.assertTrue(dist_attr2.inRange(7, 6));
		Assert.assertTrue(dist_attr2.inRange(8, 7));
		
		Assert.assertFalse(dist_attr2.inRange(1, 1));
		Assert.assertFalse(dist_attr2.inRange(2, 2));
		Assert.assertFalse(dist_attr2.inRange(3, 3));
		Assert.assertFalse(dist_attr2.inRange(4, 4));
		Assert.assertFalse(dist_attr2.inRange(5, 5));
		Assert.assertFalse(dist_attr2.inRange(6, 6));
		Assert.assertFalse(dist_attr2.inRange(7, 7));
	}
	
	@Test
	public void test_decode_attribute2() {
		Assert.assertEquals(1, dist_attr2.decode(0));
		Assert.assertEquals(2, dist_attr2.decode(1));
		Assert.assertEquals(3, dist_attr2.decode(2));
	}
	
	@Test
	public void test_code_attribute3() {		
		//bin 0 bin size + 1 elements = 4
		Assert.assertEquals(0, dist_attr3.code(-15));
		Assert.assertEquals(0, dist_attr3.code(-14));
		Assert.assertEquals(0, dist_attr3.code(-13));
		Assert.assertEquals(0, dist_attr3.code(-12));
		
		//bin 1 has bin size elements = 3
		Assert.assertEquals(1, dist_attr3.code(-11));
		Assert.assertEquals(1, dist_attr3.code(-10));
		Assert.assertEquals(1, dist_attr3.code(-9));

		//bin 2 has bin size elements = 3
		Assert.assertEquals(2, dist_attr3.code(-8));
		Assert.assertEquals(2, dist_attr3.code(-7));
		Assert.assertEquals(2, dist_attr3.code(-6));
		
		//.
		//.
		//.
		
		//bin 9 has bin size elements = 3
		Assert.assertEquals(9, dist_attr3.code(13));
		Assert.assertEquals(9, dist_attr3.code(14));
		Assert.assertEquals(9, dist_attr3.code(15));
	}
	
	@Test
	public void test_decode_attribute3() {
		Assert.assertEquals(-13, dist_attr3.decode(0));
		Assert.assertEquals(-10, dist_attr3.decode(1));
		Assert.assertEquals(-7, dist_attr3.decode(2));
	}
	
	@Test
	public void test_code_attribute4() {
		//bin 0 
		Assert.assertEquals(0, dist_attr4.code(0));
		Assert.assertEquals(0, dist_attr4.code(2));
		Assert.assertEquals(0, dist_attr4.code(4));

		//bin 1
		Assert.assertEquals(1, dist_attr4.code(6));
		Assert.assertEquals(1, dist_attr4.code(8));
		
		//bin 2 
		Assert.assertEquals(2, dist_attr4.code(10));
		Assert.assertEquals(2, dist_attr4.code(12));
		
		
		//.
		//.
		//.
		
		//bin 24 
		Assert.assertEquals(24, dist_attr4.code(98));
		Assert.assertEquals(24, dist_attr4.code(100));
	}
	
	@Test
	public void test_decode_attribute4() {
		Assert.assertEquals(2, dist_attr4.decode(0));
		Assert.assertEquals(8, dist_attr4.decode(1));
		Assert.assertEquals(12, dist_attr4.decode(2));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_code_outOfStep() {
		dist_attr4.code(9);
		dist_attr4.code(1);
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
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_isInBinRange() {
		dist_attr1.inRange(1, -1);
		dist_attr1.inRange(1, 26);
	}
	
	@Test
	public void test_handle() {
		Assert.assertEquals(100.0 ,attr1.getMaxValue());
		Assert.assertEquals(0.0 ,attr1.getMinValue());
		Assert.assertEquals(101 ,attr1.getCounts());
	}

	@Test
	public void test_handle_attr5() {
		Assert.assertEquals(0.0 ,attr5.handle(0.0));
		Assert.assertEquals(1.0 ,attr5.handle(1.0));
		Assert.assertEquals(2.0 ,attr5.handle(2.0));
		Assert.assertEquals(3.0 ,attr5.handle(3.0));
		Assert.assertEquals(4.0 ,attr5.handle(4.0));
	}
	
	@Test
	public void test_code_attr6() {
		Assert.assertEquals(0 , dist_attr6.code(257.5769958496094));
	}
	
	@Test
	public void test_decode_attr6() {
		Assert.assertEquals(251 , dist_attr6.decode(0));
	}
	
}
