package com.certh.iti.easytv.user.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.certh.iti.easytv.user.UserPreferences;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;

public class UserProfileGenerator {
	
	private Random rand;
	
	public UserProfileGenerator() {
		rand = new Random();
	}
	
	public UserProfileGenerator(long seed) {
		rand = new Random(seed);
	}
	
	public void setSeed(long seed) {
		rand.setSeed(seed);
	}
	
	public UserProfile getNextProfile() throws UserProfileParsingException, IOException{
		
			Map<String, Object> map = new HashMap<String, Object>();

			for(final Entry<String, Attribute> e : Preference.preferencesAttributes.entrySet()) {
				Attribute oprand = e.getValue();
				map.put(e.getKey(), oprand.getRandomValue(rand));
			}
			
			Preference defaultPreference = new Preference("default", map);
			List<Preference> preferences = new ArrayList<Preference>();
			UserPreferences userPreferences = new UserPreferences(defaultPreference, preferences);
		
		return new UserProfile(userPreferences);
	}
	
	/**
	 * Generate profiles to output directory
	 * 	
	 * @param num
	 * @param outDir
	 * @throws UserProfileParsingException
	 * @throws IOException
	 */
	public void generateProfiles(int num, File outDir) throws UserProfileParsingException, IOException{
		
		for(int i = 0; i < num; i++) {
			
			UserProfile userProfile = getNextProfile();
			
			String fileName = outDir.getPath() + File.separatorChar + "userProfile_" + i + ".json";
			File file = new File(fileName);
			
			if(!file.exists())
				file.createNewFile();
			
			PrintWriter writer = new PrintWriter(file);
			userProfile.getJSONObject().write(writer, 4, 0);
			writer.close();
			
			System.out.println("User profile: "+ fileName +" has been created");
		}
	}
	
	
	/**
	 * Generate randomly initiated set of user profiles. 
	 * 
	 * @param num
	 * @return
	 * @throws UserProfileParsingException 
	 * @throws IOException 
	 */
	public List<UserProfile> getProfiles(int num) throws UserProfileParsingException, IOException{
		
		List<UserProfile> profiles =  new ArrayList<UserProfile>(num);	
		for(int i = 0; i < num; i++) {
				
			Map<String, Object> map = new HashMap<String, Object>();

			for(final Entry<String, Attribute> e : Preference.preferencesAttributes.entrySet()) {
				Attribute oprand = e.getValue();
				map.put(e.getKey(), oprand.getRandomValue(rand));
			}
			
			Preference defaultPreference = new Preference("default", map);
			List<Preference> preferences = new ArrayList<Preference>();
			UserPreferences userPreferences = new UserPreferences(defaultPreference, preferences);
			
			
			profiles.add(new UserProfile(userPreferences));

		}
		
		return profiles;
	}

}
