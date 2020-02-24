package com.certh.iti.easytv.user;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.config.Config;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;
import com.certh.iti.easytv.util.Table;
import com.certh.iti.easytv.util.Table.Position;

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
	
	/**
	 * Test that the user profile points corresponds to those produced by preference attributes
	 */
	@Test
	public void test_getPoints() throws IOException, UserProfileParsingException {
		
		JSONObject jsonProfile1 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"                        \"http://registry.easytv.eu/common/volume\": 27," + 
				"                        \"http://registry.easytv.eu/common/content/audio/language\": it," +
				"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
				"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/shelf/gain\": -50," + 
				"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"none\"," + 
				"                        \"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"" + 
				"}}}}");
				
		UserProfile profile1 = new UserProfile(jsonProfile1);

		int index = 0;
		double[] actual = profile1.getPoint();
		Map<String, Object> pref = profile1.getUserPreferences().getDefaultPreference().getPreferences();

		//compare profile preferences with returned points
		for(Entry<String, Attribute> entry :  Preference.getAttributes().entrySet()) {
			Object actualValue = pref.get(entry.getKey());
			double excpected = entry.getValue().getPoints(actualValue);
			
			if(actualValue != null)
				Assert.assertEquals(actual[index], excpected, entry.getKey());
			else 
				Assert.assertEquals(actual[index], entry.getValue().getMissingPoint()[0], entry.getKey());

			index++;
		}
	}
	
	/**
	 * Test that the user profile points corresponds to those produced by preference attributes
	 */
	@Test
	public void test_getItemset() throws IOException, UserProfileParsingException {
		
		JSONObject jsonProfile1 = new JSONObject("{\"user_preferences\": {\"default\": {\"preferences\": {" + 
				"                        \"http://registry.easytv.eu/common/volume\": 27," + 
				"                        \"http://registry.easytv.eu/common/content/audio/language\": it," +
				"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor\": 1.2," + 
				"                        \"http://registry.easytv.eu/application/cs/audio/eq/low/shelf/gain\": -50," + 
				"                        \"http://registry.easytv.eu/application/control/csGazeAndGestureControlType\": \"none\"," + 
				"                        \"http://registry.easytv.eu/application/cs/ui/text/size\": \"15\"" + 
				"}}}}");
				
		UserProfile profile1 = new UserProfile(jsonProfile1);

		int index = 0;
		double[] actual = profile1.getPoint();
		Map<String, Object> pref = profile1.getUserPreferences().getDefaultPreference().getPreferences();

		//compare profile preferences with returned points
		for(Entry<String, Attribute> entry :  Preference.getAttributes().entrySet()) {
			Object actualValue = pref.get(entry.getKey());
			double excpected = entry.getValue().getPoints(actualValue);
			
			if(actualValue != null)
				Assert.assertEquals(actual[index], excpected, entry.getKey());
			else 
				Assert.assertEquals(actual[index], entry.getValue().getMissingPoint()[0], entry.getKey());

			index++;
		}
	}
	
	//@Test
	public void test_print_bins() throws IOException, UserProfileParsingException {	
		
		Vector<Bin> profileBins = Profile.getBins();		
		JSONObject preferences = json.getJSONObject("user_profile").getJSONObject("user_preferences").getJSONObject("default").getJSONObject("preferences");
		
		int index = 0;
		for(Bin bin : profileBins) {
			System.out.println(String.format("%d: %-100s    %d", index++, bin.label, bin.counts));
		}
	}
	
	//@Test
	public void test_print_itemSet() throws IOException, UserProfileParsingException {
		
		Vector<Bin> profileBins = Profile.getBins();
		int[] itemset = profile1.getAsItemSet();
		
		JSONObject preferences = json.getJSONObject("user_profile").getJSONObject("user_preferences").getJSONObject("default").getJSONObject("preferences");
		
		System.out.println();
		for(int i : itemset) {
			Bin bin = profileBins.get(i);
			
			String[] lable = bin.label.split(" - ");
			if(preferences.has(lable[0])) {
				Object value = preferences.get(lable[0]);
	
				System.out.println(String.format("%d: %-100s    %d %s", i, lable[0], bin.counts, value));
			}
		}
	}

	//@Test
	public void test_print_item_to_inforamtion() throws IOException, UserProfileParsingException {
		
		Vector<Bin> profileBins = Profile.getBins();
		int[] itemset = profile1.getAsItemSet();
		
		JSONObject preferences = json.getJSONObject("user_profile").getJSONObject("user_preferences").getJSONObject("default").getJSONObject("preferences");

		Table.CellFormat[] cells = new Table.CellFormat[] {new Table.CellFormat(5),
														   new Table.CellFormat(90),
														   new Table.CellFormat(20),
														   new Table.CellFormat(15)};
		Table table = new Table(cells);
		table.addRow(new Object[]{"item","url", "range", "value"}, Position.CENTER);
		
		for(int i : itemset) {
			Bin bin = profileBins.get(i);
			String[] lable = bin.label.split(" - ");
			
			if(preferences.has(lable[0])) {
				Object initValue = preferences.get(lable[0]);
							
				table.addRow(new Object[]{i, lable[0], lable[1], initValue});
			}

		}
		
		System.out.println(table.toString());
	}


}
