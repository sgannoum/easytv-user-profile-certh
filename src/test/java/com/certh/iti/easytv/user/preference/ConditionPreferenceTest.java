package com.certh.iti.easytv.user.preference;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.UserProfileParsingException;

public class ConditionPreferenceTest {
	
	private JSONObject json = new JSONObject("{  \"user_preferences\": {\r\n" + 
			"	\"default\": {\r\n" + 
			"	  \"preferences\": {\r\n" +  
			"		\"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 13,\r\n" + 
			"		\"http://registry.easytv.eu/common/display/screen/enhancement/font/type\": \"sans-serif\",\r\n" + 
			"		\"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#ffffff\",\r\n" + 
			"		\"http://registry.easytv.eu/common/display/screen/enhancement/font/color\": \"#000000\"\r\n" + 
			"	  }\r\n" + 
			"	},\r\n" + 
			"	\"conditional\": [\r\n" + 
			"	  {\r\n" + 
			"		\"name\": \"condition_1\",\r\n" + 
			"		\"conditions\": [\r\n" + 
			"		 {\r\n" + 
			"			\"type\": \"and\",\r\n" + 
			"			\"operands\": [\r\n" + 
			"				  {\r\n" + 
			"					\"type\": \"eq\",\r\n" + 
			"					\"operands\": [\r\n" + 
			"						\"http://registry.easytv.eu/context/location\",\r\n" + 
			"						\"gr\"\r\n" + 
			"					]\r\n" + 
			"				  },\r\n" + 
			"				  {\r\n" + 
			"					\"type\": \"eq\",\r\n" + 
			"					\"operands\": [\r\n" + 
			"						\"http://registry.easytv.eu/context/location\",\r\n" + 
			"						\"gr\"\r\n" + 
			"					]\r\n" + 
			"				  }\r\n" + 
			"			]\r\n" + 
			"		   }\r\n" + 
			"		  ],\r\n" + 
			"					  \"preferences\": {\r\n" + 
			"		  \"http://registry.easytv.eu/common/volume\": 21\r\n" + 
			"		}\r\n" + 
			"		}\r\n" + 
			"	  ]\r\n" + 
			"	}" +
			" }");
	
	@Test
	public void test_conditions() throws JSONException, UserProfileParsingException {
		JSONObject jsonExpected = (JSONObject) json.getJSONObject("user_preferences").getJSONArray("conditional").get(0);
		
		//convert JSON preference to map
		ConditionalPreference preference1 = new ConditionalPreference(jsonExpected.getString("name"), jsonExpected);
		
		//check that the conditions size is bigger than zero
		Assert.assertTrue(preference1.getPreferences().size() > 0);
		
		//check that the conditions size is bigger than zero
		Assert.assertTrue(preference1.getConditions().size() > 0);
		
		//use it to create a new preference object
		ConditionalPreference preference2 = new ConditionalPreference("condition_1", preference1.getPreferences(), preference1.getConditions());
		JSONObject jsonFound = preference2.toJSON();
		
		Assert.assertTrue(jsonFound.similar(jsonExpected), "\nExpected: "+jsonExpected.toString(4)+"\n Found: "+jsonFound.toString(4)+"\n");
	}

}
