package com.certh.iti.easytv.user.preference;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class ConditionPreferenceTest {
	
	private JSONObject json = new JSONObject("{  \"user_preferences\": {" + 
			"	\"default\": {" + 
			"	  \"preferences\": {" +  
			"        \"http://registry.easytv.eu/common/volume\": 100," + 
			"        \"http://registry.easytv.eu/common/content/audio/language\": \"es\"" +
			"	  }" + 
			"	}," + 
			"	\"conditional\": [" + 
			"	  {" + 
			"		\"name\": \"condition_1\"," + 
			"		\"conditions\": [" + 
			"		 {" + 
			"			\"type\": \"and\"," + 
			"			\"operands\": [" + 
			"				  {" + 
			"					\"type\": \"eq\"," + 
			"					\"operands\": [" + 
			"						\"http://registry.easytv.eu/context/location\"," + 
			"						\"gr\"" + 
			"					]" + 
			"				  }," + 
			"				  {" + 
			"					\"type\": \"eq\"," + 
			"					\"operands\": [" + 
			"						\"http://registry.easytv.eu/context/location\"," + 
			"						\"gr\"" + 
			"					]" + 
			"				  }" + 
			"			]" + 
			"		   }" + 
			"		  ]," + 
			"					  \"preferences\": {" + 
			"		  \"http://registry.easytv.eu/common/volume\": 21" + 
			"		}" + 
			"		}" + 
			"	  ]" + 
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
		JSONObject jsonFound = preference2.getJSONObject();
		
		Assert.assertTrue(jsonFound.similar(jsonExpected), "\nExpected: "+jsonExpected.toString(4)+"\n Found: "+jsonFound.toString(4)+"\n");
	}

}
