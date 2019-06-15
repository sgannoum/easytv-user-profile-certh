package com.certh.iti.easytv.user.preference;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PreferenceTests {
	
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
			"						\"fr\"\r\n" + 
			"					]\r\n" + 
			"				  },\r\n" + 
			"				  {\r\n" + 
			"					\"type\": \"eq\",\r\n" + 
			"					\"operands\": [\r\n" + 
			"						\"http://registry.easytv.eu/context/location\",\r\n" + 
			"						\"fr\"\r\n" + 
			"					]\r\n" + 
			"				  }\r\n" + 
			"			]\r\n" + 
			"		   }\r\n" + 
			"		  ],\r\n" + 
			"					  \"preferences\": {\r\n" + 
			"		  \"http://registry.easytv.eu/common/content/audio/volume\": 21\r\n" + 
			"		}\r\n" + 
			"		}\r\n" + 
			"	  ]\r\n" + 
			"	}" +
			" }");
	 
	
	@Test
	public void test_preference() {
		JSONObject jsonExpected = json.getJSONObject("user_preferences").getJSONObject("default");

		//convert JSON preference to map
		Preference preference1 = new Preference("default", jsonExpected);
		
		Assert.assertTrue(preference1.getPreferences().size() > 0);
		
		//use it to create a new preference object
		Preference preference2 = new Preference("default", preference1.getPreferences());
		JSONObject jsonFound = preference2.toJSON();
		
		Assert.assertTrue(jsonFound.similar(jsonExpected), "\nExpected: "+jsonExpected.toString(4)+"\n Found: "+jsonFound.toString(4)+"\n");
	}
	
	

}
