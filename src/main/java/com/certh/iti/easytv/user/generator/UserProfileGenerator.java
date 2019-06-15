package com.certh.iti.easytv.user.generator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.certh.iti.easytv.user.UserPreferences;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.generator.operand.RandomBooleanLiteral;
import com.certh.iti.easytv.user.generator.operand.RandomColorLiteral;
import com.certh.iti.easytv.user.generator.operand.RandomDisplayContrastLiteral;
import com.certh.iti.easytv.user.generator.operand.RandomFontLiteral;
import com.certh.iti.easytv.user.generator.operand.RandomImageMagnificationScaleLiteral;
import com.certh.iti.easytv.user.generator.operand.RandomIntLiteral;
import com.certh.iti.easytv.user.generator.operand.RandomLanguageLiteral;
import com.certh.iti.easytv.user.generator.operand.RandomTTSQualityLiteral;
import com.certh.iti.easytv.user.generator.operand.RandomTTSSpeed;
import com.certh.iti.easytv.user.generator.operand.RandomTTSVolume;
import com.certh.iti.easytv.user.preference.Preference;
import com.certh.iti.easytv.user.preference.operand.OperandLiteral;

public class UserProfileGenerator {
	
	private Random rand;
	
	protected static final Class<?>[] PREFERENCE_CLASSES = {  RandomIntLiteral.class,	 		//"audioVolume",
															  RandomLanguageLiteral.class,	 	//"audioLanguage",
															  RandomDisplayContrastLiteral.class, 	 		//"displayContrast",
															  RandomIntLiteral.class,	 		//"fontSize",
															  RandomFontLiteral.class, //font 
															  RandomLanguageLiteral.class,   	//"subtitles",
															  RandomLanguageLiteral.class,   	//"signLanguage", 
															  
															  RandomTTSSpeed.class,   			//"tts/speed", 
															  RandomTTSVolume.class,   			//"tts/volume", 
															  RandomLanguageLiteral.class,  	//"tts/language", 
															  RandomTTSQualityLiteral.class,  	//"tts/audioQuality",
															  
															  RandomImageMagnificationScaleLiteral.class,   		//"cs/accessibility/imageMagnification/scale",
															  RandomBooleanLiteral.class,   	//"cs/accessibility/textDetection", 
															  RandomBooleanLiteral.class,   	//"cs/accessibility/faceDetection", 
															  
															  RandomIntLiteral.class,   		//"cs/audio/volume", 
															  RandomLanguageLiteral.class,  	//"cs/audio/track", 
															  RandomBooleanLiteral.class,  		//"cs/audio/audioDescription", 
															  
															  RandomBooleanLiteral.class,   	//"cs/cc/audioSubtitles", 
															  RandomLanguageLiteral.class, 		//"cs/cc/subtitles/language", 
															  RandomIntLiteral.class,  	   		//"cs/cc/subtitles/fontSize",
															  
															  RandomColorLiteral.class,   		//"cs/cc/subtitles/fontColor", 
															  RandomColorLiteral.class,    		//"cs/cc/subtitles/backgroundColor"
															  RandomColorLiteral.class,   		//"fontColor", 
															  RandomColorLiteral.class    		//"backgroundColor"
															  };
	
	
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
				
			Map<String, OperandLiteral> map = new HashMap<String, OperandLiteral>();
			for(int j = 0 ; j < PREFERENCE_CLASSES.length; j++) {
				Class<?> cls = PREFERENCE_CLASSES[j];
				map.put(Preference.PREFERENCE_ATTRIBUTE[j], (OperandLiteral) cls.getConstructor(Random.class).newInstance(rand));
			}
			
			Preference defaultPreference = new Preference("default", map);
			List<Preference> preferences = new ArrayList<Preference>();
			UserPreferences userPreferences = new UserPreferences(defaultPreference, preferences);
			
			try {
				profiles.add(new UserProfile(userPreferences, false));
			} catch(Exception e) {}
		}
		return profiles;
	}

}
