package com.certh.iti.easytv.user;

import org.json.JSONObject;
import org.testng.annotations.Test;
import org.testng.Assert;


public class VisualTest {

	@Test
	public void test_constructor1() {
		
		JSONObject jsonObj = new JSONObject("{\"visual\": {\r\n" + 
												"    \"visual_acuity\": 8,\r\n" + 
												"    \"contrast_sensitivity\": 24,\r\n" + 
												"    \"color_blindness\": \"deuteranomaly\"\r\n" + 
												"  }}");
		
		Visual acutalVisual = new Visual(jsonObj.getJSONObject("visual"));
		Visual expectedVisual = new Visual(8, 24, "deuteranomaly");

		Assert.assertEquals(acutalVisual, expectedVisual);
		
		//compare JSON representation
		Assert.assertTrue(jsonObj.getJSONObject("visual").similar(expectedVisual.toJSON()));
	}
	
	@Test
	public void test_colorBlindness() {
		 String[] ColorBlindessStrTypes = { "NORMAL", "deuteranomaly" ,"deuteranopia","protanomaly","protanopia","tritanomaly","tritanopia"};
		 
		 for(int i = 0; i < ColorBlindessStrTypes.length; i++)
			 new Visual(8, 24, ColorBlindessStrTypes[i]);
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void test_wrong_colorBlindness() {		 
		 new Visual(8, 24, "UNEXISTING");
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void test_wrong_visual_acuitys1() {		 
		 new Visual(-1, 24, "UNEXISTING");
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void test_wrong_visual_acuitys2() {		 
		 new Visual(20, 24, "UNEXISTING");
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void test_wrong_constrast_sensitivity1() {		 
		 new Visual(4, -1, "UNEXISTING");
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void test_wrong_constrast_sensitivity2() {		 
		 new Visual(2, 24, "UNEXISTING");
	}
}
