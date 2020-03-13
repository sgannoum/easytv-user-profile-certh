package com.certh.iti.easytv.user;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.config.Config;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;


public class UserPreferencesTest {

	private JSONObject json;
	
	@BeforeClass
	public void beforClass() throws IOException {
		json = Config.getProfile("userModel.json");
	}
	
	@Test
	public void test_constructor() throws JSONException, UserProfileParsingException {
		UserPreferences userPreferences = new UserPreferences(json.getJSONObject("user_preferences"));
		UserPreferences userPreferences2 = new UserPreferences(userPreferences.getDefaultPreference(), userPreferences.getConditionalPreferences());
			
		Assert.assertTrue(userPreferences.getJSONObject().similar(json.getJSONObject("user_preferences")));
	}
	
}
