package com.certh.iti.easytv.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.config.Config;


public class UserPreferencesTest {

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
	public void test_constructor() throws JSONException, UserProfileParsingException {
		UserPreferences userPreferences = new UserPreferences(json.getJSONObject("user_preferences"));
		UserPreferences userPreferences2 = new UserPreferences(userPreferences.getDefaultPreference(), userPreferences.getConditionalPreferences());
			
		Assert.assertTrue(userPreferences.getJSONObject().similar(json.getJSONObject("user_preferences")));
	}
	
	
}
