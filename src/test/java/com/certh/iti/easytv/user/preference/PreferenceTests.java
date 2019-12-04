package com.certh.iti.easytv.user.preference;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class PreferenceTests {
	
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
	public void test_preference() throws UserProfileParsingException {
		JSONObject jsonExpected = json.getJSONObject("user_preferences").getJSONObject("default");

		//convert JSON preference to map
		Preference preference1 = new Preference("default", jsonExpected);
		
		Assert.assertTrue(preference1.getPreferences().size() > 0);
		
		//use it to create a new preference object
		Preference preference2 = new Preference("default", preference1.getPreferences());
		JSONObject jsonFound = preference2.getJSONObject();
		
		Assert.assertTrue(jsonFound.similar(jsonExpected), "\nExpected: "+jsonExpected.toString(4)+"\n Found: "+jsonFound.toString(4)+"\n");
	}
	
	@Test
	public void test_preference_ItemSet() throws UserProfileParsingException {

		String[] labels = Preference.getPreferencesDistinctItemsLabels();
		
		
		System.out.println("Distinct itemSet: " +Preference.getPreferencesDistinctItems());
		for(int i = 0; i < labels.length; i++) {
			System.out.println(labels[i]);
		}	
	}
	
}
