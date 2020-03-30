package com.certh.iti.easytv.user;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.AttributesAggregator;
import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

public class OnlyPreferenceProfile extends Profile {
			
	protected static AttributesAggregator aggregator = new AttributesAggregator();

	static {
		logger = Logger.getLogger(OnlyPreferenceProfile.class.getName());
		aggregator.add(Preference.getAttributes());
	}
	
	public OnlyPreferenceProfile() {
		super();
	}
	
	public OnlyPreferenceProfile(int userId, Random rand) throws IOException, UserProfileParsingException {
		super(userId, rand);
	}
	
	public OnlyPreferenceProfile(OnlyPreferenceProfile other) throws IOException, UserProfileParsingException {
		super(other);
	}
	
	public OnlyPreferenceProfile(File file) throws IOException, JSONException, UserProfileParsingException {
		super(file);
	}
	
	public OnlyPreferenceProfile(JSONObject json) throws IOException, UserProfileParsingException {
		super(json);
	}
	
	public OnlyPreferenceProfile(int userId, UserProfile userProfile, UserContext userContext, UserContent userContent) throws IOException, UserProfileParsingException {
		super(userId, userProfile, userContext, userContent);
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
	 * Get the number of distinct items of the user preferences
	 * @return
	 */
	public static int getBinNumber() {
		return aggregator.getBinNumber();
	}
	
	/**
	 * Get bins associated values
	 * @return
	 */
	public static Vector<Bin> getBins() {
		return aggregator.getBins();
	}
	
	/**
	 * Get an item set representation of the usre profile 
	 * 
	 * @return
	 */
	@Override
	public int[] getAsItemSet() {
		int[] preferencesitemSet = userProfile.getAsItemSet();
		int[] itemSet = new int[preferencesitemSet.length];
			
		int base = 0, index = 0;
		for(int i = 0; i < preferencesitemSet.length; itemSet[index++] = preferencesitemSet[i++] + base );
		
		return itemSet;
	}
	
}
