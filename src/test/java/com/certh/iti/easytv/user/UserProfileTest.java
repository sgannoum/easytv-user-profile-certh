package com.certh.iti.easytv.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.config.Config;

public class UserProfileTest {
	
	private JSONObject json;
	
	@BeforeClass
	public void beforClass() throws IOException {
		String line;
		BufferedReader reader = new BufferedReader(new FileReader(new File(getClass().getClassLoader().getResource(Config.path).getFile())));
		StringBuffer buff = new StringBuffer();
		
		while((line = reader.readLine()) != null) {
			buff.append(line);
		}
		
		json = new JSONObject(buff.toString());		
		reader.close();
		
	}
	
	@Test
	public void test_constructor() throws IOException {
		UserProfile userProfile1 = new UserProfile(json);
		UserProfile userProfile2 = new UserProfile( userProfile1.getUserPreferences(), false);
	
		Assert.assertTrue(userProfile2.getJSONObject().similar(json), "Expected: " + json.toString(4)+" \n but found: \n"+userProfile2.getJSONObject().toString(4));
	}
	
	@Test
	public void test_identifcal_profiles() throws IOException {
		
		JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/volume\": 100,\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 20,\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/language\": \"es\",\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#000000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		JSONObject jsonProfile2 = new JSONObject("{\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/volume\": 100,\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 20,\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/language\": \"es\",\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#000000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		UserProfile userProfile1 = new UserProfile(jsonProfile1);
		UserProfile userProfile2 = new UserProfile(jsonProfile2);
	
		Assert.assertEquals(userProfile1.distanceTo(userProfile2), 0.0);
	}
	
	@Test
	public void test_profiles_similarety_color_difference() throws IOException {
		
		JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/volume\": 100,\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 20,\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/language\": \"es\",\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#000000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		JSONObject jsonProfile2 = new JSONObject("{\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/volume\": 100,\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 20,\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/language\": \"es\",\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#080000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		UserProfile userProfile1 = new UserProfile(jsonProfile1);
		UserProfile userProfile2 = new UserProfile(jsonProfile2);
	
		Assert.assertEquals(userProfile1.distanceTo(userProfile2), 0.0);
	}
	
	@Test
	public void test_profiles_similarety_language_difference() throws IOException {
		
		JSONObject jsonProfile1 = new JSONObject("{\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/volume\": 100,\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 20,\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/language\": \"es\",\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#000000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		JSONObject jsonProfile2 = new JSONObject("{\r\n" + 
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/volume\": 100,\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 20,\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/language\": \"gr\",\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#000000\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		UserProfile userProfile1 = new UserProfile(jsonProfile1);
		UserProfile userProfile2 = new UserProfile(jsonProfile2);
	
		Assert.assertEquals(userProfile1.distanceTo(userProfile2), 0.0);
	}
	
	
	@Test
	public void test_getPoints() throws IOException {
		
		JSONObject jsonProfile1 = new JSONObject("{\r\n" +  
				"  \"user_preferences\": {\r\n" + 
				"    \"default\": {\r\n" + 
				"      \"preferences\": {\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/volume\": 100,\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/size\": 20,\r\n" + 
				"        \"http://registry.easytv.eu/common/content/audio/language\": \"es\",\r\n" +
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/font/color\": \"#030201\",\r\n" + 
				"        \"http://registry.easytv.eu/common/display/screen/enhancement/background\": \"#060504\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
		
		UserProfile userProfile1 = new UserProfile(jsonProfile1);
		
		double[] expectedPoints = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 2.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 6.0, 5.0, 4.0, 0.0, 0.0, 0.0, 100.0, 0.0, 0.0, 20.0, 0.0, 0.0, 0.0}; 
		
		double[] actualPoints = userProfile1.getPoint();
		Assert.assertEquals(actualPoints.length, expectedPoints.length);
		
		for(int i = 0 ; i < actualPoints.length; i++)
			Assert.assertEquals(actualPoints[i], expectedPoints[i]); 

	}

}
