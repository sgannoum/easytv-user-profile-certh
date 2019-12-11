package com.certh.iti.easytv.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.AttributesAggregator;

public class Profile implements Clusterable {
	
	private static long num_profiles = 0;
	private static AttributesAggregator aggregator = new AttributesAggregator();
	
	static {
		aggregator.add(Preference.preferencesAttributes);
		aggregator.add(UserContext.contextAttributes);
		aggregator.add(UserContent.content_attributes);
	}
	
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
	
	/**
	 * Get an item set representation of the usre profile 
	 * 
	 * @return
	 */
	public int[] getAsItemSet() {
		int index = 0;
		int[] preferencesitemSet = userProfile.getPreferencesAsItemSet();
		int[] contextItemSet = userContext.getAsItemSet();
		int[] contentItemSet = userContent.getAsItemSet();
		
		int[] itemSet = new int[preferencesitemSet.length + contextItemSet.length + contentItemSet.length];
		
		for(int i = 0; i < preferencesitemSet.length; itemSet[index++] = preferencesitemSet[i++]);
		for(int i = 0; i < contextItemSet.length; itemSet[index++] = contextItemSet[i++]);
		for(int i = 0; i < contentItemSet.length; itemSet[index++] = contentItemSet[i++]);
		
		return itemSet;
	}
	
	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();

			jsonObj.put("user_id", userId);
			jsonObj.put("user_profile", userProfile.getJSONObject());
			if(userContext != null) jsonObj.put("user_context", userContext.getJSONObject());
			if(userContent != null) jsonObj.put("user_content", userContent.getJSONObject());
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
	
	/**
	 * Get users profiles dimensional operands
	 * 
	 * @return 
	 */
	public static final Vector<Attribute> getOperands() {
		return aggregator.getOperands();
	}
	
	/**
	 * @return uris arrays
	 */
	public static final Vector<String> getUris(){
		return 	aggregator.getUris();
	}
	
	/**
	 * Print the collected statistical data
	 */
	public static String getStatistics() {
		String output = "";
		Set<Entry<String, Attribute>> preferenceEntrySet = Preference.preferencesAttributes.entrySet();
		
		for(Entry<String, Attribute> entry : preferenceEntrySet) {
			output += String.format("%s\n%s\n" , entry.getKey(), entry.getValue().toString());
		}
		
		return output;
	}
	
	/**
	 * Get the number of distinct items of the user preferences
	 * @return
	 */
	public static int getPreferencesDistinctItems() {
		return aggregator.getSize();
	}
	
	/**
	 * Get the frequency counts of the occurred items
	 * @return
	 */
	public static final Vector<Integer> getPreferencesDistinctItemsCounts() {
		return aggregator.getBinsCounts();
	}
	
	/**
	 * Get bins associated values
	 * @return
	 */
	public static final Vector<Object> getPreferencesDistinctItemsValues() {
		return aggregator.getBinsValues();
	}
	
	/**
	 * Get bins associated labels
	 * @return
	 */
	public static final Vector<String> getPreferencesDistinctItemsLabels() {
		return aggregator.getBinsLables();
	}
	
}
