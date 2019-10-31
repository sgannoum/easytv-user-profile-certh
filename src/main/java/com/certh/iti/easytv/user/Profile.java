package com.certh.iti.easytv.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class Profile implements Clusterable {
	
	private static long num_profiles = 0;
	
	private double[] points = null;
	private int userId = -1;
	private UserContext userContext = null;
	private UserContent userContent = null;
	private UserProfile userProfile = new UserProfile();
	private JSONObject jsonObj = null;
	
	public Profile() {		
		num_profiles++;
	}
	
	public Profile(Profile other) throws IOException, UserProfileParsingException {
		jsonObj = null;
		setJSONObject(other.getJSONObject());
		
		num_profiles++;
	}
	
	public Profile(File file) throws IOException, JSONException, UserProfileParsingException {
		jsonObj = null;
		
		String line;
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuffer buff = new StringBuffer();
		
		while((line = reader.readLine()) != null) 
			buff.append(line);

		//close file
		reader.close();
		
		setJSONObject( new JSONObject(buff.toString()));
		
		num_profiles++;
	}
	
	public Profile(JSONObject json) throws IOException, UserProfileParsingException {
		jsonObj = null;
		setJSONObject(json);
		
		num_profiles++;
	}
	
	public Profile(int userId, UserProfile userProfile, UserContext userContext, UserContent userContent) throws IOException, UserProfileParsingException {
		
		this.userId = userId;
		this.userProfile = userProfile;
		this.userContext = userContext;
		this.userContent = userContent;

		//initialize json
		jsonObj = this.getJSONObject();

		num_profiles++;
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public UserProfile getUserProfile() {
		return this.userProfile;
	}
	
	public UserContext getUserContext() {
		return this.userContext;
	}
	
	public UserContent getUserContent() {
		return this.userContent;
	}
	
	/**
	 * @return a vector of points that represent the user profile.
	 */
	@Override
	public double[] getPoint() {
		return points;
	}
	
	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();

			jsonObj.put("user_id", userId);
			jsonObj.put("user_profile", userProfile.getJSONObject());
			if(userContext != null) jsonObj.put("user_Context", userContext.getJSONObject());
			if(userContent != null) jsonObj.put("user_Content", userContent.getJSONObject());
		}
		
		return jsonObj;
	}
	
	public void setJSONObject(JSONObject json) throws UserProfileParsingException {	
		
		if(!json.has("user_id")) 
			throw new UserProfileParsingException("Missing 'user_id' element.");
		
		
		if(!json.has("user_profile")) 
			throw new UserProfileParsingException("Missing 'user_profile' element.");
		
		
		userId = json.getInt("user_id");
		userProfile.setJSONObject(json.getJSONObject("user_profile"));
		
		if(json.has("user_context")) 
			userContext = new UserContext(json.getJSONObject("user_context")); 		
		
		if(json.has("user_content")) 
			userContent = new UserContent(json.getJSONObject("user_content"));		

		//Update points
		this.setPoints();
		
		//Update json
		jsonObj = json;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!Profile.class.isInstance(obj)) return false;
		Profile other = (Profile) obj;
		return getJSONObject().similar(other.getJSONObject());
	}
	

	private void setPoints() {

		int index = 0;
		double[] profile_points, context_points, content_points;
		
		//user profile points
		profile_points = userProfile.getPoint();	
		context_points = userContext == null ? new UserContext().getPoint() : userContext.getPoint();
		content_points = userContent == null ? new UserContent().getPoint() : userContent.getPoint();
		
		 
		//initialize points
		points = new double[profile_points.length + context_points.length + content_points.length];		

		for(int i = 0; i < profile_points.length; points[index] = profile_points[i], i++, index++ );
		for(int i = 0; i < context_points.length; points[index] = context_points[i], i++, index++ );
		for(int i = 0; i < content_points.length; points[index] = content_points[i], i++, index++ );
	}
	
	/**
	 * @return Total number of loaded profiles
	 */
	public static long getProfilesCounts() {
		return num_profiles;
	}
}
