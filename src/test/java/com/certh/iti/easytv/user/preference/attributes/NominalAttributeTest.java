package com.certh.iti.easytv.user.preference.attributes;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.preference.attributes.NominalAttribute.StringConverter;
import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;

import junit.framework.Assert;

public class NominalAttributeTest {
	
	NominalAttribute attr1, attr2, attr3; 
	Discretization dist_attr1, dist_attr2;
	
	@BeforeClass
	public void beforTest() {
		
		String[] states_1 = new String[] {"15", "20", "23"};
		String[] states_2 = new String[] {"none", "gaze_control", "gesture_control"};
		
		attr1 = (NominalAttribute) NominalAttribute.Builder().setState(states_1).build();
		attr2 = (NominalAttribute) NominalAttribute.Builder().setState(states_2).build();
		attr3 = (NominalAttribute) NominalAttribute.Builder()
	    		.setState(new String[] { "0.75", "1.0", "1.5", "2.0", "3.0", "4.0"})
	    		.setConverter(new StringConverter() {
	    			
	    			@Override
	    			public String valueOf(Object obj) {
	    				return String.valueOf(obj);
	    			}
	    			
	    			@Override
	    			public boolean isInstance(Object obj) {
	    				return Double.class.isInstance(obj);
	    			}
	    		})
	    		.build();
		
		//Load values
		for(int i = 0; i < states_1.length; i++)
			attr1.handle(states_1[i]);
		
		for(int i = 0; i < states_2.length; i++)
			attr2.handle(states_2[i]);
		
		//get discretization
		dist_attr1 = attr1.getDiscretization();
		dist_attr2 = attr2.getDiscretization();	
	}
	
	@Test
	public void test_BinNumber() {
		Assert.assertEquals(3, dist_attr1.getBinNumber());
		Assert.assertEquals(3, dist_attr2.getBinNumber());
	}
	
	@Test
	public void test_code_attribute1() {
		Assert.assertEquals(0, dist_attr1.code("15"));
		Assert.assertEquals(1, dist_attr1.code("20"));
		Assert.assertEquals(2, dist_attr1.code("23"));
	}
	
	@Test
	public void test_isInBinRange_attribute1() {
		Assert.assertFalse(dist_attr1.inRange("15", 2));
		Assert.assertTrue(dist_attr1.inRange("15", 0));
		Assert.assertTrue(dist_attr1.inRange("20", 1));
		Assert.assertTrue(dist_attr1.inRange("23", 2));
	}
	
	@Test
	public void test_decode_attribute1() {
		Assert.assertEquals("15", dist_attr1.decode(0));
		Assert.assertEquals("20", dist_attr1.decode(1));
		Assert.assertEquals("23", dist_attr1.decode(2));
	}
	
	@Test
	public void test_code_attribute2() {		
		Assert.assertEquals(0, dist_attr2.code("none"));
		Assert.assertEquals(1, dist_attr2.code("gaze_control"));
		Assert.assertEquals(2, dist_attr2.code("gesture_control"));
	}
	
	@Test
	public void test_decode_attribute2() {
		Assert.assertEquals("none", dist_attr2.decode(0));
		Assert.assertEquals("gaze_control", dist_attr2.decode(1));
		Assert.assertEquals("gesture_control", dist_attr2.decode(2));
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
	public void test_code_outOfrange() {
		dist_attr1.code("Null");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_handle_outOfRange() {
		dist_attr1.handle("Null");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_isInBinRange() {
		dist_attr1.inRange("15", -1);
		dist_attr1.inRange("15", 26);
	}
	
	@Test
	public void test_handel_attr3() {
		Assert.assertEquals(0.75, attr3.handle(0.75));
		Assert.assertEquals(1.0, attr3.handle(1.0));
		Assert.assertEquals(1.5, attr3.handle(1.5));
		Assert.assertEquals(2.0, attr3.handle(2.0));
		Assert.assertEquals(3.0, attr3.handle(3.0));
		Assert.assertEquals(4.0, attr3.handle(4.0));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_wrong_type_handel_attr3() {
		attr3.handle(75);
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void test_wrong_value_handel_attr3() {
		attr3.handle(0.80);
	}
	
}
