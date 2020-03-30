package com.certh.iti.easytv.user;

import java.io.File;
import java.io.IOException;
import java.util.Map;
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

public class DynamicProfile extends Profile {
	
	protected static AttributesAggregator aggregator = new AttributesAggregator();

			
	static {
		logger = Logger.getLogger(DynamicProfile.class.getName());
	}
	
	public DynamicProfile() {
		super();
	}
	
	public DynamicProfile(int userId, Random rand) throws IOException, UserProfileParsingException {
		super(userId, rand);
	}
	
	public DynamicProfile(DynamicProfile other) throws IOException, UserProfileParsingException {
		super(other);
	}
	
	public DynamicProfile(File file) throws IOException, JSONException, UserProfileParsingException {
		super(file);
	}
	
	public DynamicProfile(JSONObject json) throws IOException, UserProfileParsingException {
		super(json);
	}
	
	public DynamicProfile(int userId, UserProfile userProfile, UserContext userContext, UserContent userContent) throws IOException, UserProfileParsingException {
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
	 * @return uris arrays
	 */
	public static void setAttributes(Map<String, Attribute> preferencesAttributes, Map<String, Attribute> contextAttributes, Map<String, Attribute> contentAttributes){
		logger.info("change profile attribute..");
		
		DynamicProfile.aggregator.reset();
		if(preferencesAttributes != null) {
			Preference.setAttributes(preferencesAttributes);
			DynamicProfile.aggregator.add(preferencesAttributes);
		}
		if(contextAttributes != null) {
			UserContext.setAttributes(contextAttributes);
			DynamicProfile.aggregator.add(contextAttributes);
		}
		if(contentAttributes != null) {
			UserContent.setAttributes(contentAttributes);
			DynamicProfile.aggregator.add(contentAttributes);
		}
	}
	
}
