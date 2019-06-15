package com.certh.iti.easytv.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

public class UserProfile implements Clusterable {
	
	private final double NoiseReduction = 0.2;
	private Boolean _IsAbstract = true;
	private File _File;
	private UserPreferences userPreferences = null;
	private JSONObject jsonObj = null;
	
	public UserProfile() {
		_IsAbstract = true;
		_File = null;
		userPreferences = null;
	}
	
	public UserProfile(UserProfile other) throws IOException {
		_IsAbstract = true;
		this._File = null;
		jsonObj = null;
		setJSONObject(other.getJSONObject());
	}
	
	public UserProfile(File _File) throws IOException {
		_IsAbstract = false;
		this._File = _File;
		jsonObj = null;
		ReadProfileJSON(_File);
	}
	
	public UserProfile(JSONObject json) throws IOException {
		_IsAbstract = false;
		_File = null;
		jsonObj = null;
		setJSONObject(json);
	}
	
	public UserProfile( UserPreferences userPreferences) throws IOException {
		_File = null;
		_IsAbstract = true;
		jsonObj = null;
		this.setUserPreferences(userPreferences);
	}
	
	public UserProfile(UserPreferences userPreferences, boolean isAbstract) throws IOException {
		_File = null;
		_IsAbstract = isAbstract;
		jsonObj = null;
		this.setUserPreferences(userPreferences);
	}
	
	public Boolean IsAbstract() {
		return _IsAbstract;
	}

	public File get_File() {
		return _File;
	}

	public double distanceTo(UserProfile other) {
		return 0.0
				
/*				visualCapabilities.distanceTo(other.visualCapabilities)+
					auditoryCapabilities.distanceTo(other.auditoryCapabilities) + 
					 	userPreferences.distanceTo(other.userPreferences)*/
					;
	}
	
	public void setJSONObject(JSONObject json) {		
		
		if(userPreferences == null)
			userPreferences = new UserPreferences(json.getJSONObject("user_preferences"));
		else
			userPreferences.setJSONObject(json.getJSONObject("user_preferences"));
		
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
	}
	
	public double[] getPoint() {
		double[] defaultPreferencePoints = userPreferences.getPoint();
		
		int size = defaultPreferencePoints.length;
		double [] userProfilePoints = new double[size];
		int index = 0;
		
		for(int i = 0; i < defaultPreferencePoints.length; i++, index++)
			userProfilePoints[index] = defaultPreferencePoints[i];
		
		return userProfilePoints;
	}
	
	private void ReadProfileJSON(File file) throws IOException {
		System.out.println("Reading profile: " + file.getAbsolutePath() + "");
		
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		StringBuffer json = new StringBuffer();
		while((line = reader.readLine()) != null) {
			json.append(line);
		}
		
		this.setJSONObject(new JSONObject(json.toString()));
	}
}
