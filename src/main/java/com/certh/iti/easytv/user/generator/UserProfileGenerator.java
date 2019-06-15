package com.certh.iti.easytv.user.generator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.certh.iti.easytv.user.Auditory;
import com.certh.iti.easytv.user.UserPreferences;
import com.certh.iti.easytv.user.UserProfile;
import com.certh.iti.easytv.user.Visual;
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
	
	private int[] age_range;
	private int[] gender_range;
	
	private int[] visualAcuity_range;
	private int[] contrastSensitivity_range;
	private int[] colorBlindness_range;
	
    private int[] quarterK_range;
    private int[] halfK_range;
    private int[] oneK_range;
    private int[] twoK_range;
    private int[] fourK_range;
    private int[] eightK_range; 
	
	
	public UserProfileGenerator() {
		rand = new Random();
		this.initateAllRanges();
	}
	
	public UserProfileGenerator(long seed) {
		rand = new Random(seed);
		this.initateAllRanges();
	}
	
	/**
	 * Initiate the limits of the different variables.
	 */
	private void initateAllRanges() {
		age_range = new int[] {20, 100};
		gender_range = new int[] {0, 1};
		
		visualAcuity_range = new int[] {1, 20};
		contrastSensitivity_range = new int[] {1, 20};
		colorBlindness_range = new int[] {0, 6};
		
	    quarterK_range = new int[] {0, 90};
	    halfK_range = new int[] {0, 90};
	    oneK_range = new int[] {0, 90};
	    twoK_range = new int[] {0, 90};
	    fourK_range = new int[] {0, 90};
	    eightK_range = new int[] {0, 90}; 
	}
	
	public void setAgeRange(int[] age) {
		this.age_range = age;
	}

	public void setVisualAcuityRange(int[] visualAcuity) {
		this.visualAcuity_range = visualAcuity;
	}

	public void setContrastSensitivityRange(int[] contrastSensitivity) {
		this.contrastSensitivity_range = contrastSensitivity;
	}

	public void setColorBlindnessRange(int[] colorBlindness) {
		this.colorBlindness_range = colorBlindness;
	}

	public void setQuarterKRange(int[] quarterK) {
		this.quarterK_range = quarterK;
	}

	public void setHalfKRange(int[] halfK) {
		this.halfK_range = halfK;
	}

	public void setOneKRange(int[] oneK) {
		this.oneK_range = oneK;
	}

	public void setTwoKRange(int[] twoK) {
		this.twoK_range = twoK;
	}

	public void setFourKRange(int[] fourK) {
		this.fourK_range = fourK;
	}

	public void setEightKRange(int[] eightK) {
		this.eightK_range = eightK;
	}
	
	public void setGenderRange(int[] gender_range) {
		this.gender_range = gender_range;
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
				
			Visual visualCapabilities = new Visual(rand.nextInt(visualAcuity_range[1] - visualAcuity_range[0]) + visualAcuity_range[0], 
												   rand.nextInt(contrastSensitivity_range[1] - contrastSensitivity_range[0]) + contrastSensitivity_range[0], 
												   Visual.getColorBlindness(rand.nextInt(colorBlindness_range[1] - colorBlindness_range[0]) + colorBlindness_range[0]));
			
			Auditory auditoryCapabilities = new Auditory(rand.nextInt(quarterK_range[1] - quarterK_range[0]) + quarterK_range[0], 
														 rand.nextInt(halfK_range[1] - halfK_range[0]) + halfK_range[0], 
														 rand.nextInt(oneK_range[1] - oneK_range[0]) + oneK_range[0], 
														 rand.nextInt(twoK_range[1] - twoK_range[0]) + twoK_range[0], 
														 rand.nextInt(fourK_range[1] - fourK_range[0]) + fourK_range[0], 
														 rand.nextInt(eightK_range[1] - eightK_range[0]) + eightK_range[0]);
				
			Map<String, OperandLiteral> map = new HashMap<String, OperandLiteral>();
			for(int j = 0 ; j < PREFERENCE_CLASSES.length; j++) {
				Class<?> cls = PREFERENCE_CLASSES[j];
				map.put(Preference.PREFERENCE_ATTRIBUTE[j], (OperandLiteral) cls.getConstructor(Random.class).newInstance(rand));
			}
			
			Preference defaultPreference = new Preference("default", map);
			List<Preference> preferences = new ArrayList<Preference>();
			UserPreferences userPreferences = new UserPreferences(defaultPreference, preferences);
			
			try {
				profiles.add(new UserProfile(visualCapabilities, auditoryCapabilities, userPreferences, false));
			} catch(Exception e) {}
		}
		return profiles;
	}

}
