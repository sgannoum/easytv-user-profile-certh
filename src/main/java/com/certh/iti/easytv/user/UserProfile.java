package com.certh.iti.easytv.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

public class UserProfile implements Clusterable {
	
	private UserPreferences userPreferences = new UserPreferences();
	private JSONObject jsonObj = null;
	
	public UserProfile() {		}
	
	public UserProfile(UserProfile other) throws IOException, UserProfileParsingException {
		jsonObj = null;
		setJSONObject(other.getJSONObject());
	}
	
	public UserProfile(File file) throws IOException, JSONException, UserProfileParsingException {
		jsonObj = null;
		
		String line;
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuffer buff = new StringBuffer();
		
		while((line = reader.readLine()) != null) 
			buff.append(line);

		setJSONObject( new JSONObject(buff.toString()));
		
		//close file
		reader.close();
		
	}
	
	public UserProfile(JSONObject json) throws IOException, UserProfileParsingException {
		jsonObj = null;
		setJSONObject(json);
	}
	
	public UserProfile( UserPreferences userPreferences) throws IOException {
		jsonObj = null;
		this.setUserPreferences(userPreferences);		
	}
	
	public UserProfile(UserPreferences userPreferences, boolean isAbstract) throws IOException {
		jsonObj = null;
		this.setUserPreferences(userPreferences);
	}
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
		jsonObj = null;
	}

	/**
	 * @return a vector of points that represent the user profile.
	 */
	@Override
	public double[] getPoint() {
		return userPreferences.getPoint();
	}
	
	public int[] getAsItemSet() {
		return this.userPreferences.getAsItemSet();
	}

	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			jsonObj.put("user_preferences", userPreferences.getJSONObject());
		}
		return jsonObj;
	}
	
	
	public void setJSONObject(JSONObject json) throws UserProfileParsingException {	
		
		if(!json.has("user_preferences")) 
			throw new UserProfileParsingException("Missing 'user_preferences' element.");
		
		//Update preferences
		userPreferences.setJSONObject(json.getJSONObject("user_preferences"));
		
		//Update json
		jsonObj = json;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!UserProfile.class.isInstance(obj)) return false;
		UserProfile other = (UserProfile) obj;
		return getJSONObject().similar(other.getJSONObject());
	}
	
	/**
	 * Get the number of distinct items of the user preferences
	 * @return
	 */
	public static int getBinNumber() {
		return UserPreferences.getBinNumber();
	}
	
	/**
	 * Get bins associated values
	 * @return
	 */
	public static Vector<Bin> getBins() {
		return UserPreferences.getBins();
	}

	
}
