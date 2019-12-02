package com.certh.iti.easytv.user;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.config.Config;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;

public class ProfileTest {
		
	private JSONObject json;
	private Profile profile1;
	
	@BeforeClass
	public void beforClass() throws IOException, UserProfileParsingException {
		json = Config.getProfile("profile_with_context_1.json");
		profile1 = new Profile(json);
	}
	
	@Test
	public void test_constructor() throws IOException, UserProfileParsingException {
				
		Profile profile2 = new Profile(profile1.getUserId(), profile1.getUserProfile(), profile1.getUserContext(), profile1.getUserContent());
	
		//compare user_id
		Assert.assertEquals(profile1.getUserId(), json.getInt("user_id"), "User ids are not equals");

		//compare user_profile
		Assert.assertTrue(profile1.getUserProfile().getJSONObject().similar(json.getJSONObject("user_profile")), 
							"Expected: " + json.getJSONObject("user_profile").toString(4)+" \n but found: \n"+profile1.getUserProfile().getJSONObject().toString(4));

		//compare user_profile
		Assert.assertTrue(profile1.getUserProfile().getUserPreferences().getJSONObject().similar(json.getJSONObject("user_profile").getJSONObject("user_preferences")), 
							"Expected: " + json.getJSONObject("user_profile").toString(4)+" \n but found: \n"+profile1.getUserProfile().getJSONObject().toString(4));
		
		//TOOD Some preferences have incompatible value object which cause this assertion to fail
		//compare user_profile
		Assert.assertTrue(profile1.getUserProfile().getUserPreferences().getDefaultPreference().getJSONObject().similar(json.getJSONObject("user_profile").getJSONObject("user_preferences").getJSONObject("default")), 
							"Expected: " + json.getJSONObject("user_profile").getJSONObject("user_preferences").getJSONObject("default").toString(4)+" \n but found: \n"+profile1.getUserProfile().getUserPreferences().getDefaultPreference().getJSONObject().toString(4));
		
		
		//compare user_context
		Assert.assertTrue(profile2.getUserContext().getJSONObject().similar(json.getJSONObject("user_context")), 
							"Expected: " + json.getJSONObject("user_context").toString(4)+" \n but found: \n"+profile2.getUserContext().getJSONObject().toString(4));

		//compare whole json
		Assert.assertTrue(profile2.getJSONObject().similar(json), 
							"Expected: " + json.toString(4)+" \n but found: \n"+profile2.getJSONObject().toString(4));
	}
	
	@Test
	public void test_getPoints() throws IOException, UserProfileParsingException {
				
		int index = 0;
		double[] actualPoints = profile1.getPoint();
		Map<String, Object> pref = profile1.getUserProfile().getUserPreferences().getDefaultPreference().getPreferences();
		
		//compare profile preferences with returned points
		for(Entry<String, Attribute> entry :  Preference.preferencesAttributes.entrySet()) {
			Object value = pref.get(entry.getKey());
			
			//Get the 
			double d = entry.getValue().getPoints(value)[0];
			Assert.assertEquals(d, actualPoints[index], entry.getKey());
			//To check dimensions and points
			//System.out.println(entry.getKey()+" "+d +"  "+ actualPoints[index]);
			index++;
		}

	}

}
