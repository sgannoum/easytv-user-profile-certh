package com.certh.iti.easytv.user;

import org.json.JSONObject;
import org.testng.annotations.Test;
import org.testng.Assert;

public class AuditoryTest {
	
	@Test
	public void test_constructor1() {
		
		JSONObject jsonObj = new JSONObject("{\"auditory\": {\r\n" + 
											"    \"quarterK\": 81,\r\n" + 
											"    \"halfK\": 35,\r\n" + 
											"    \"oneK\": 98,\r\n" + 
											"    \"twoK\": 18,\r\n" + 
											"    \"fourK\": 57,\r\n" + 
											"    \"eightK\": 27\r\n" + 
											"  	}"
											+ "}");
		
		Auditory acutalAuditory = new Auditory(jsonObj.getJSONObject("auditory"));
		Auditory expectedAuditory = new Auditory(81, 35, 98, 18, 57, 27);

		Assert.assertEquals(acutalAuditory, expectedAuditory);
		
		//compare JSON representation
		Assert.assertTrue(jsonObj.getJSONObject("auditory").similar(expectedAuditory.toJSON()));
	}
	
	@Test
	public void test_constructor2() {
		
		JSONObject jsonObj = new JSONObject("{\"auditory\": {\r\n" + 
											"    \"quarterK\": 82,\r\n" + 
											"    \"halfK\": 35,\r\n" + 
											"    \"oneK\": 98,\r\n" + 
											"    \"twoK\": 18,\r\n" + 
											"    \"fourK\": 57,\r\n" + 
											"    \"eightK\": 27\r\n" + 
											"  	}"
											+ "}");
		
		Auditory acutalAuditory = new Auditory(jsonObj.getJSONObject("auditory"));
		Auditory expectedAuditory = new Auditory(81, 35, 98, 18, 57, 27);

		Assert.assertNotEquals(acutalAuditory, expectedAuditory);
	}

}
