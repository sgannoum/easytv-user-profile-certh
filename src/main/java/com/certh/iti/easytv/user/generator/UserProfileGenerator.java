package com.certh.iti.easytv.user.generator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.certh.iti.easytv.user.UserPreferences;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.preference.ConditionalPreference;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.BinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.ColorAttribute;

public class UserProfileGenerator {
	
	private Random rand;
	
	public UserProfileGenerator() {
		rand = new Random();
	}
	
	public UserProfileGenerator(long seed) {
		rand = new Random(seed);
	}
	
	/**
	 * Generate randomly initiated set of user profiles. 
	 * 
	 * @param num
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public List<UserProfile> generate(int num) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		
		List<UserProfile> profiles =  new ArrayList<UserProfile>(num);	
		for(int i = 0; i < num; i++) {
				
			Map<String, Object> map = new HashMap<String, Object>();

			for(final Entry<String, Attribute> e : Preference.preferencesAttributes.entrySet()) {
				Attribute oprand = e.getValue();
				double[] range = oprand.getRange();
				Object literal = null;
				
				if (ColorAttribute.class.isInstance(oprand)) {
					literal ="#"+Integer.toHexString(rand.nextInt(255))+Integer.toHexString(rand.nextInt(255))+Integer.toHexString(rand.nextInt(255));		
				}
				else if (BinaryAttribute.class.isInstance(oprand)) {
					literal = rand.nextInt(1) == 0 ? false : true;	
				}
				else {
					int root = (int) (range[1] - range[0]);
					literal = (root / Math.abs(root)) * rand.nextInt(Math.abs(root)) + range[0];
					
					double res = (double) literal;
					if(res < range[0] || res > range[1]) {
						throw new IllegalArgumentException("Value "+res+" out of range ["+range[0]+", "+range[1]+"]");
					}
				}
				
				map.put(e.getKey(), literal);
			}
			
			Preference defaultPreference = new Preference("default", map);
			List<ConditionalPreference> preferences = new ArrayList<ConditionalPreference>();
			UserPreferences userPreferences = new UserPreferences(defaultPreference, preferences);
			
			try {
				profiles.add(new UserProfile(userPreferences, false));
			} catch(Exception e) {}
		}
		return profiles;
	}

}
