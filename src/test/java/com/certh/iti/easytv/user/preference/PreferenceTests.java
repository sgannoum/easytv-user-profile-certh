package com.certh.iti.easytv.user.preference;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.preference.operand.ColorLiteral;
import com.certh.iti.easytv.user.preference.operand.FontLiteral;
import com.certh.iti.easytv.user.preference.operand.LanguageLiteral;
import com.certh.iti.easytv.user.preference.operand.NumericLiteral;
import com.certh.iti.easytv.user.preference.operand.OperandLiteral;
import com.certh.iti.easytv.user.preference.operand.SymmetricBooleanLiteral;

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
	
	
	@Test
	public void test_operands_literal_order() {

		OperandLiteral[] actual = Preference.getOperands();
		OperandLiteral[] expected = {  new NumericLiteral(0.0, new double[] {0.0, 100.0}),
									 new LanguageLiteral("en"),
									 new NumericLiteral(1.0 , new double[] {1.0, 50.0}),
									 new FontLiteral("fantasy"),
									 new ColorLiteral("#000000"),
									 new ColorLiteral("#000000"),
									 new LanguageLiteral("en"),
									 new LanguageLiteral("en"),
									 new NumericLiteral(0, new double[] {0.0, 100.0}),
									 new NumericLiteral(0, new double[] {0.0, 100.0}),
									 new NumericLiteral(0, new double[] {0.0, 100.0}),
									 new LanguageLiteral("en"),
									 new NumericLiteral(0, new double[] {0.0, 100.0}),
									 new NumericLiteral(0, new double[] {0.0, 100.0}),
									 new SymmetricBooleanLiteral(false),
									 new SymmetricBooleanLiteral(false),
									 new NumericLiteral(0, new double[] {0.0, 100.0}),
									 new LanguageLiteral("en"),
									 new SymmetricBooleanLiteral(false),
									 new SymmetricBooleanLiteral(false),
									 new LanguageLiteral("en"),
									 new NumericLiteral(0, new double[] {0.0, 100.0}),
									 new ColorLiteral("#000000"),
									 new ColorLiteral("#000000") };
		
		Assert.assertEquals(actual.length, expected.length);
		Assert.assertEquals(actual, expected);
	}
	
	

}
