package com.certh.iti.easytv.user.generator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	
	protected static final String COMMON_PREFIX = "http://registry.easytv.eu/common/";
	protected static final String APPLICATION_PREFIX = "http://registry.easytv.eu/application/";
	
	private Random rand;
	
	public static final HashMap<String, Class<?>> preferencesToRandOperand  =  new HashMap<String, Class<?>>() {{
		
		put(COMMON_PREFIX + "content/audio/volume",   RandomIntLiteral.class);
		put(COMMON_PREFIX + "content/audio/language", RandomLanguageLiteral.class);
		put(COMMON_PREFIX + "displayContrast", RandomDisplayContrastLiteral.class);
		put(COMMON_PREFIX + "display/screen/enhancement/font/size", RandomIntLiteral.class);
		put(COMMON_PREFIX + "display/screen/enhancement/font/type", RandomFontLiteral.class);
		put(COMMON_PREFIX + "subtitles", RandomLanguageLiteral.class);
		put(COMMON_PREFIX + "signLanguage", RandomLanguageLiteral.class);
		put(COMMON_PREFIX + "display/screen/enhancement/font/color", RandomColorLiteral.class);
		put(COMMON_PREFIX + "display/screen/enhancement/background", RandomColorLiteral.class);

		put(APPLICATION_PREFIX + "tts/speed", RandomTTSSpeed.class);
		put(APPLICATION_PREFIX + "tts/volume",  RandomTTSVolume.class);
		put(APPLICATION_PREFIX + "tts/language",  RandomLanguageLiteral.class);
		put(APPLICATION_PREFIX + "tts/audioQuality", RandomTTSQualityLiteral.class);
		put(APPLICATION_PREFIX + "cs/accessibility/imageMagnification/scale", RandomImageMagnificationScaleLiteral.class);
		put(APPLICATION_PREFIX + "cs/accessibility/textDetection", RandomBooleanLiteral.class);
		put(APPLICATION_PREFIX + "cs/accessibility/faceDetection", RandomBooleanLiteral.class);
		put(APPLICATION_PREFIX + "cs/audio/volume",  RandomIntLiteral.class);
		put(APPLICATION_PREFIX + "cs/audio/track", RandomLanguageLiteral.class);
		put(APPLICATION_PREFIX + "cs/audio/audioDescription", RandomBooleanLiteral.class);
		put(APPLICATION_PREFIX + "cs/cc/audioSubtitles", RandomBooleanLiteral.class);
		put(APPLICATION_PREFIX + "cs/cc/subtitles/language", RandomLanguageLiteral.class);
		put(APPLICATION_PREFIX + "cs/cc/subtitles/fontSize", RandomIntLiteral.class);
		put(APPLICATION_PREFIX + "cs/cc/subtitles/fontColor", RandomColorLiteral.class);
		put(APPLICATION_PREFIX + "cs/cc/subtitles/backgroundColor", RandomColorLiteral.class);
		
    }};
	
	
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
			Iterator<Map.Entry<String, Class<?>>> interator = preferencesToRandOperand.entrySet().iterator();
			while(interator.hasNext()) {
				Entry<String, Class<?>> entry = interator.next();
				Class<?> cls = entry.getValue();
				map.put(entry.getKey(), (OperandLiteral) cls.getConstructor(Random.class).newInstance(rand));
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
