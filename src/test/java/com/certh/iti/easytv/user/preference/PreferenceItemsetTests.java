package com.certh.iti.easytv.user.preference;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class PreferenceItemsetTests {
	
	//hold initial attributes
	private Map<String, Attribute> initialAttributes;
	
	private Attribute integerAttribute = new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, -1);
	private Attribute integerAttribute_withBins = new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, -1);
	private Attribute nominalAttribute = new NominalAttribute(new String[] {"male", "female"});
	private Attribute symmetricBinaryAttribute = new SymmetricBinaryAttribute();
	private Attribute ordinalAttribute = new OrdinalAttribute(new String[] {"15", "20", "23"});

	
	//define new preferences
	private Map<String, Attribute> newAttributes = new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;

		{	
	     	put("IntegerAttribute", integerAttribute);
	     	put("IntegerAttribute_withBins", integerAttribute_withBins);
		    put("NominalAttribute", nominalAttribute);
		    put("SymmetricBinaryAttribute", symmetricBinaryAttribute);
		    put("OrdinalAttribute", ordinalAttribute);					

		}};

	@BeforeClass
	public void beforeTest() {
		initialAttributes = Preference.getAttributes();
		Preference.setAttributes(newAttributes);
	}
	 
	@AfterClass
	public void afterTest() {
		for(Attribute attr : newAttributes.values())
			System.out.println(attr.toString());
		
		Preference.setAttributes(initialAttributes);
	}
	
	@Test
	public void test_BinNumber() {
		Assert.assertEquals(Preference.getBinNumber(), 133);
	}
	
	@Test
	public void test_getAsItemset_all_first_bin() throws UserProfileParsingException {
		

		JSONObject json = new JSONObject("{  \"user_preferences\": {" + 
				"	\"default\": {" + 
				"	  \"preferences\": {" +  
				"        \"IntegerAttribute\": 0," +
				"        \"IntegerAttribute_withBins\": 0," + 
				"        \"NominalAttribute\": \"male\"," +
				"        \"SymmetricBinaryAttribute\": false," +
				"        \"OrdinalAttribute\": \"15\"," +
				"	  }}}}");
		
		
		//change initial preferences
		Preference.setAttributes(newAttributes);
		JSONObject jsonExpected = json.getJSONObject("user_preferences").getJSONObject("default");

		//convert JSON preference to map
		Preference preference = new Preference("default", jsonExpected);
		
		int index = 0;
		int[] actual = preference.getAsItemSet();
		int[] expected = new int[] {
				0, 
				0 + integerAttribute.getBinNumber(), 
				0 + integerAttribute.getBinNumber() +  integerAttribute_withBins.getBinNumber(), 
				0 + integerAttribute.getBinNumber() +  integerAttribute_withBins.getBinNumber() + nominalAttribute.getBinNumber(), 
				0 + integerAttribute.getBinNumber() +  integerAttribute_withBins.getBinNumber() + nominalAttribute.getBinNumber() + symmetricBinaryAttribute.getBinNumber()};

		for(String key : Preference.getAttributes().keySet()) {
			Assert.assertEquals(actual[index], expected[index], key);
			index++;
		}
	}
	
	@Test
	public void test_getAsItemset_all_last_bin() throws UserProfileParsingException {
		
		JSONObject json = new JSONObject("{  \"user_preferences\": {" + 
				"	\"default\": {" + 
				"	  \"preferences\": {" +  
				"        \"IntegerAttribute\": 100," +
				"        \"IntegerAttribute_withBins\": 100," + 
				"        \"NominalAttribute\": \"female\"," +
				"        \"SymmetricBinaryAttribute\": true," +
				"        \"OrdinalAttribute\": \"23\"," +
				"	  }}}}");
		
		
		//change initial preferences
		Preference.setAttributes(newAttributes);
		JSONObject jsonExpected = json.getJSONObject("user_preferences").getJSONObject("default");

		//convert JSON preference to map
		Preference preference = new Preference("default", jsonExpected);
		
		int index = 0;
		int[] actual = preference.getAsItemSet();
		int[] expected = new int[] {
				integerAttribute.getBinNumber() - 1, 
				integerAttribute.getBinNumber() + integerAttribute_withBins.getBinNumber() - 1, 
				integerAttribute.getBinNumber() +  integerAttribute_withBins.getBinNumber() + nominalAttribute.getBinNumber() - 1, 
				integerAttribute.getBinNumber() +  integerAttribute_withBins.getBinNumber() + nominalAttribute.getBinNumber() + symmetricBinaryAttribute.getBinNumber() - 1, 
				integerAttribute.getBinNumber() +  integerAttribute_withBins.getBinNumber() + nominalAttribute.getBinNumber() + symmetricBinaryAttribute.getBinNumber() + ordinalAttribute.getBinNumber() - 1};

		for(String key : Preference.getAttributes().keySet()) {
			Assert.assertEquals(actual[index], expected[index], key);
			index++;
		}
	}
	
	@Test
	public void test_getAsItemset_second_bin() throws UserProfileParsingException {
		
		JSONObject json = new JSONObject("{  \"user_preferences\": {" + 
				"	\"default\": {" + 
				"	  \"preferences\": {" +  
				"        \"IntegerAttribute\": 5," +
				"        \"IntegerAttribute_withBins\": 1," + 
				"        \"NominalAttribute\": \"female\"," +
				"        \"SymmetricBinaryAttribute\": true," +
				"        \"OrdinalAttribute\": \"20\"," +
				"	  }}}}");
		
		
		//change initial preferences
		Preference.setAttributes(newAttributes);
		JSONObject jsonExpected = json.getJSONObject("user_preferences").getJSONObject("default");

		//convert JSON preference to map
		Preference preference = new Preference("default", jsonExpected);
		
		int index = 0;
		int[] actual = preference.getAsItemSet();
		int[] expected = new int[] {
				1, 
				1 + integerAttribute.getBinNumber(), 
				1 + integerAttribute.getBinNumber() +  integerAttribute_withBins.getBinNumber(), 
				1 + integerAttribute.getBinNumber() +  integerAttribute_withBins.getBinNumber() + nominalAttribute.getBinNumber(), 
				1 + integerAttribute.getBinNumber() +  integerAttribute_withBins.getBinNumber() + nominalAttribute.getBinNumber() + symmetricBinaryAttribute.getBinNumber()};

		
		for(String key : Preference.getAttributes().keySet()) {
			Assert.assertEquals(actual[index], expected[index], key);
			index++;
		}
	}
	
}
