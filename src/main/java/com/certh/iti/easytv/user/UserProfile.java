package com.certh.iti.easytv.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

public class UserProfile implements Clusterable {
	
	private static long num_profiles = 0;
	
	private double[] points = null;
	private UserPreferences userPreferences = new UserPreferences();
	private JSONObject jsonObj = null;
	
	public UserProfile() {		
		num_profiles++;
	}
	
	public UserProfile(UserProfile other) throws IOException {
		jsonObj = null;
		setJSONObject(other.getJSONObject());
		
		num_profiles++;
	}
	
	public UserProfile(File file) throws IOException {
		jsonObj = null;
		
		String line;
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuffer buff = new StringBuffer();
		
		while((line = reader.readLine()) != null) 
			buff.append(line);

		setJSONObject( new JSONObject(buff.toString()));
		
		num_profiles++;
	}
	
	public UserProfile(JSONObject json) throws IOException {
		jsonObj = null;
		setJSONObject(json);
		
		num_profiles++;
	}
	
	public UserProfile( UserPreferences userPreferences) throws IOException {
		jsonObj = null;
		this.setUserPreferences(userPreferences);		
		
		num_profiles++;
	}
	
	public UserProfile(UserPreferences userPreferences, boolean isAbstract) throws IOException {
		jsonObj = null;
		this.setUserPreferences(userPreferences);
		
		num_profiles++;
	}
	
	
	public void setJSONObject(JSONObject json) {		
		userPreferences.setJSONObject(json.getJSONObject("user_preferences"));
		points = null;
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
	
	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			jsonObj.put("user_preferences", userPreferences.getJSONObject());
		}
		return jsonObj;
	}
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
		points = null;
		jsonObj = null;
	}
	
	/**
	 * @return Total number of loaded profiles
	 */
	public static long getProfilesCounts() {
		return num_profiles;
	}
	

	/**
	 * @return a vector of points that represent the user profile.
	 */
	public double[] getPoint() {

		//return already calculated vector
		if(points != null) return points;
		
		double[] defaultPreferencePoints = userPreferences.getPoint();
				
		points = new double[defaultPreferencePoints.length];		
		for(int i = 0; i < defaultPreferencePoints.length; i++)
			points[i] = defaultPreferencePoints[i];
		
		return points;
	}
}
