package com.certh.iti.easytv.user;

import org.json.JSONObject;
import org.testng.annotations.Test;
import org.testng.Assert;


public class GeneralTest {
	
	@Test
	public void test_constructor1() {
		
		JSONObject jsonObj = new JSONObject("{  \"general\": {\r\n" + 
											"    \"age\": 40,\r\n" + 
											"    \"gender\": \"male\"\r\n" + 
											"  }}");
		
		General acutalGeneral = new General(jsonObj.getJSONObject("general"));
		General expectedGeneral = new General(40, "MALE");

		Assert.assertEquals(acutalGeneral, expectedGeneral);
		
		//compare JSON representation
		Assert.assertTrue(jsonObj.getJSONObject("general").similar(expectedGeneral.toJSON()));
	}
	
	@Test
	public void test_constructor2() {
		
		JSONObject jsonObj = new JSONObject("{  \"general\": {\r\n" + 
											"    \"age\": 40,\r\n" + 
											"    \"gender\": \"female\"\r\n" + 
											"  }}");
		
		General acutalGeneral = new General(jsonObj.getJSONObject("general"));
		General expectedGeneral = new General(40, "female");

		Assert.assertEquals(acutalGeneral, expectedGeneral);
		
		//compare JSON representation
		Assert.assertTrue(jsonObj.getJSONObject("general").similar(expectedGeneral.toJSON()));
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void test_constructor_unknown_gender() {
		
		JSONObject jsonObj = new JSONObject("{  \"general\": {\r\n" + 
													"    \"age\": 40,\r\n" + 
													"    \"gender\": \"femaale\"\r\n" + 
													"  }}");

		new General(jsonObj.getJSONObject("general"));
	}
	
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void test_constructor_wrong_age1() {
		
		JSONObject jsonObj = new JSONObject("{  \"general\": {\r\n" + 
													"    \"age\": -1,\r\n" + 
													"    \"gender\": \"femaale\"\r\n" + 
													"  }}");

		new General(jsonObj.getJSONObject("general"));
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void test_constructor_wrong_age2() {
		
		JSONObject jsonObj = new JSONObject("{  \"general\": {\r\n" + 
													"    \"age\": 120,\r\n" + 
													"    \"gender\": \"femaale\"\r\n" + 
													"  }}");

		new General(jsonObj.getJSONObject("general"));
	}
}
