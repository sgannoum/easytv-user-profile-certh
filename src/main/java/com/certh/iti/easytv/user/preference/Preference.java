package com.certh.iti.easytv.user.preference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.BinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.ColorAttribute;
import com.certh.iti.easytv.user.preference.attributes.DoubleAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.LanguageAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class Preference implements Clusterable, Comparable<Preference> {

	protected static Map<String, Attribute> preferencesAttributes;
	
	static {
		init();
	}
	
    protected double[] points;
	protected String name = new String();
	protected Map<String, Object> preferences = new HashMap<String, Object>();
	protected JSONObject jsonObj = null;
	
	
	public static void init() {
		preferencesAttributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
	 
		{	
			//common
	     	put("http://registry.easytv.eu/common/volume", 									IntegerAttribute.Builder().setRange(new double[] {0.0, 100.0}).setStep(1.0).build());
			put("http://registry.easytv.eu/common/contrast", 								IntegerAttribute.Builder().setRange(new double[] {0.0, 100.0}).setStep(1.0).build());
			put("http://registry.easytv.eu/common/brightness", 								IntegerAttribute.Builder().setRange(new double[] {0.0, 100.0}).setStep(1.0).build());
		    put("http://registry.easytv.eu/common/content/audio/language", 					LanguageAttribute.Builder().enableDiscretization(false).enableFrequencyHistogram(false).build());
		    put("http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size", 	DoubleAttribute.Builder().setRange(new double[] {1.0, 2.0}).setStep(0.5).build());
		    put("http://registry.easytv.eu/common/display/screen/enhancement/cursor/color", ColorAttribute.Builder().build());
		    
		    //cs
		    put("http://registry.easytv.eu/application/tts/audio/language", 				LanguageAttribute.Builder().enableDiscretization(false).enableFrequencyHistogram(false).build());
		    put("http://registry.easytv.eu/application/tts/audio/speed", 					IntegerAttribute.Builder().setRange(new double[] {0.0, 100.0}).setStep(1.0).build());
		    put("http://registry.easytv.eu/application/tts/audio/volume", 					IntegerAttribute.Builder().setRange(new double[] {0.0, 100.0}).setStep(1.0).build());
		    put("http://registry.easytv.eu/application/tts/audio/voice", 					NominalAttribute.Builder().setState(new String[] {"male", "female"}).build());
			put("http://registry.easytv.eu/application/tts/audio/quality", 					IntegerAttribute.Builder().setRange(new double[] {1.0, 8.0}).setMissingValue(-1).build()); 
		    put("http://registry.easytv.eu/application/cs/cc/subtitles", 					SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/cc/subtitles/language", 			LanguageAttribute.Builder().enableDiscretization(false).enableFrequencyHistogram(false).build());
		    put("http://registry.easytv.eu/application/cs/cc/subtitles/font/size", 			IntegerAttribute.Builder().setRange(new double[] {0.0, 50.0}).setStep(1.0).build());
		    put("http://registry.easytv.eu/application/cs/cc/subtitles/font/color", 		ColorAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/cc/subtitles/background/color",	ColorAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/cc/audio/subtitle",  				SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS", 	SymmetricBinaryAttribute.Builder().build());	
		    put("http://registry.easytv.eu/application/cs/ui/text/size", 					OrdinalAttribute.Builder().setState(new String[] {"15", "20", "23"}).build());					
		    put("http://registry.easytv.eu/application/cs/ui/language", 					LanguageAttribute.Builder().enableDiscretization(false).enableFrequencyHistogram(false).build());	
		    put("http://registry.easytv.eu/application/cs/ui/vibration/touch", 				SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/ui/text/magnification/scale", 	SymmetricBinaryAttribute.Builder().build()); 				
		    
		    //audio equalizer
		    put("http://registry.easytv.eu/application/cs/audio/eq",  						SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/audio/eq/low/shelf/frequency", 	IntegerAttribute.Builder().setRange(new double[] {35.0,	 220.0}).build());
		    put("http://registry.easytv.eu/application/cs/audio/eq/low/shelf/gain", 		IntegerAttribute.Builder().setRange(new double[] {-50.0, 50.0}).setMissingValue(-51.0).build());
		    put("http://registry.easytv.eu/application/cs/audio/eq/low/pass/frequency", 	IntegerAttribute.Builder().setRange(new double[] {80.0,  1600.0}).setStep(10.0).build());
		    put("http://registry.easytv.eu/application/cs/audio/eq/low/pass/qFactor", 		DoubleAttribute.Builder().setRange(new double[]  {0.7,   12.0})	.setStep(0.1).build());
		    put("http://registry.easytv.eu/application/cs/audio/eq/high/pass/frequency", 	IntegerAttribute.Builder().setRange(new double[] {800.0, 5900.0}).setStep(100.0).build());
		    put("http://registry.easytv.eu/application/cs/audio/eq/high/pass/qFactor", 		DoubleAttribute.Builder().setRange(new double[]  {0.7,   12.0})	.setStep(0.1).build());
		    put("http://registry.easytv.eu/application/cs/audio/eq/high/shelf/frequency", 	IntegerAttribute.Builder().setRange(new double[] {2200.0, 4700.0}).setStep(100.0).build());
		    put("http://registry.easytv.eu/application/cs/audio/eq/high/shelf/gain", 		IntegerAttribute.Builder().setRange(new double[] {-50.0,  50.0}).setMissingValue(-51.0).build());
		  	put("http://registry.easytv.eu/application/cs/audio/volume", 					IntegerAttribute.Builder().setRange(new double[] {0.0,   100.0}).setStep(1.0).build());
		    put("http://registry.easytv.eu/application/cs/audio/track", 					LanguageAttribute.Builder().enableDiscretization(false).enableFrequencyHistogram(false).build());
		    
		    //control
		    put("http://registry.easytv.eu/application/control/voice", 										SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/control/csGazeAndGestureControlType", 				NominalAttribute.Builder().setState(new String[] {"none", "cursor_control", "gaze_control", "gesture_control", "mouse_control"}).build());
		    put("http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize",  	DoubleAttribute.Builder().setRange(new double[] {1.0, 2.0}).setStep(0.5).build());
		    put("http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage", 	LanguageAttribute.Builder().enableDiscretization(false).enableFrequencyHistogram(false).build());
		    
		    //accessibility
		    put("http://registry.easytv.eu/application/cs/accessibility/audio/description",  	SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/accessibility/detection/text/reader", SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/accessibility/detection/sound",  		SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/accessibility/detection/face",  		SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/accessibility/detection/text",  		SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/accessibility/detection/character",  	SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/accessibility/magnification", 		SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/cs/accessibility/magnification/scale", 	DoubleAttribute.Builder().setRange(new double[] {1.5, 3.5}).setStep(0.5).build());
		    put("http://registry.easytv.eu/application/cs/accessibility/sign/language", 		LanguageAttribute.Builder().enableDiscretization(false).enableFrequencyHistogram(false).build());
		    
		    //HbbTV
		    put("http://registry.easytv.eu/application/hbbtv/screen/reader",  				SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/hbbtv/screen/reader/speed",  		NominalAttribute.Builder().setState(new String[] {"slow", "normal", "fast"}).build());
		    put("http://registry.easytv.eu/application/hbbtv/cc/subtitles",  				SymmetricBinaryAttribute.Builder().build());
		    put("http://registry.easytv.eu/application/hbbtv/cc/subtitles/position", 		NominalAttribute.Builder().setState(new String[] {"up", "down"}).build());
		    put("http://registry.easytv.eu/application/hbbtv/cc/subtitles/language",  		LanguageAttribute.Builder().setState(new String[] {"ca", "en", "ar", "es"}).enableDiscretization(false).enableFrequencyHistogram(false).build());
		    put("http://registry.easytv.eu/application/hbbtv/cc/subtitles/font/size", 		IntegerAttribute.Builder().setRange(new double[] {1.0, 50.0}).build());
		    put("http://registry.easytv.eu/application/hbbtv/cc/subtitles/background",  	SymmetricBinaryAttribute.Builder().build());
	    }};
		
	}
	
	public Preference() {}
	
	public Preference(Random rand, Preference initPreference) {
		this.name = "default";
		
		String avoid = " ";
		preferences.putAll(initPreference.preferences);
		for(final Entry<String, Attribute> e : Preference.getAttributes().entrySet()) {
			String key = e.getKey();
			Attribute oprand = e.getValue();
			
			if(key.startsWith(avoid)) {
				
				preferences.put(key+" to be omitted", null);
				continue;
			}
			
			if(preferences.containsKey(key))
				continue;
			
			if(BinaryAttribute.class.isInstance(oprand)) {
				//get random value
				boolean res = (boolean) oprand.getRandomValue(rand);
				
				//add to preference
				preferences.put(key, res);
				
				//remove dependent preferences
				if(!res) avoid = key;
			} else {
				preferences.put(key, oprand.getRandomValue(rand));
			}
		}
		
		//Update points
		this.setPoint();
		
		//Update json
		jsonObj = null;
	}

	
	public Preference(String name, Map<String, Object> entries) throws UserProfileParsingException {
		this.name = name;
		this.setPreferences(entries);
		jsonObj = null;
	}
	
	public Preference(String name, JSONObject json) throws UserProfileParsingException {
		this.name = name;
		this.setJSONObject(json);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		jsonObj = null;
	}
	
	@Override
	public double[] getPoint() {	
		return points;
	}

	public Map<String, Object> getPreferences() {
		return preferences;
	}

	public void setPreferences(Map<String, Object> preferences) throws UserProfileParsingException {
		
		//clear up old preferences
		this.preferences.clear();
		
		for (Entry<String, Object> entries : preferences.entrySet()) {
			String key = entries.getKey();
			Object value = entries.getValue();
			Object handled_value;
			
			//Get preference attribute handler
			Attribute handler = preferencesAttributes.get(key);
			
			//Unknown preference throw an exception
			if(handler == null) {
				throw new UserProfileParsingException("Unknown preference: '"+ key+"'");
			} 

			//Handle preference value
			try 
			{
				handled_value = handler.handle(value);
			}
			catch(ClassCastException e) {	
				throw new UserProfileParsingException("Non compatible data value: '"+value+"' of preference '"+ key+"' "+e.getMessage());
			}
			catch(Exception e) {	
				throw new UserProfileParsingException( key+ " "+e.getMessage());
			}
			
			//Add
			this.preferences.put(key, handled_value);
		}
		
		//Update points
		this.setPoint();
		
		//Update json
		jsonObj = null;
	}

	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			JSONObject jsonPreferences = new JSONObject();
			
			for(Entry<String, Object> entry : preferences.entrySet()) 
				jsonPreferences.put(entry.getKey(), entry.getValue());
			
			jsonObj.put("preferences", jsonPreferences);
		}
		return jsonObj;
	}

	public void setJSONObject(JSONObject json) throws UserProfileParsingException {
		
		//set to this json
		this.jsonObj = json;
		
		if(!json.has("preferences")) 
			throw new UserProfileParsingException("Missing 'preferences' element.");
		
		
		JSONObject jsonPreference = json.getJSONObject("preferences");
		String[] fields = JSONObject.getNames(jsonPreference);
		
		//No default preferences case
		if(fields == null) 
			throw new UserProfileParsingException("Empty default preferences element.");
		
		//Convert to a map
		Map<String, Object> entries = new HashMap<String, Object>();
		for(int i = 0 ; i < fields.length; i++) {
			String key = fields[i];
			Object value = jsonPreference.get(key);
			entries.put(key, value);
		}
		
		//Update preferences
		this.setPreferences(entries);
	}
	
	private void setPoint() {
		List<Double> pointsList = new ArrayList<Double>();
		
		for(Entry<String, Attribute> entry : preferencesAttributes.entrySet()) {
			String prefKey = entry.getKey();
			Attribute handler = entry.getValue();
			
			//get preference value
			Object prefValue = preferences.get(prefKey);
			
			//get preference points
			double d = handler.getPoints(prefValue);
			
			pointsList.add(d);

		}
		
		//convert to double[]
		points = new double[pointsList.size()];
		for(int i = 0; i < pointsList.size(); i++) {
			points[i] = pointsList.get(i).doubleValue();
		}
	}

	/**
	 * Compare two preferences set
	 */
	public int compareTo(Preference o) {
		int i = 0;
		double[] points_1 = this.getPoint();
		double[] points_2 = o.getPoint();

		for(i = 0; i < 0 && points_1[i] == points_2[i] ; i++);
			
		return  (int) Math.ceil((points_1[i] - points_2[i]));
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(o == this) return true;
		if(!Preference.class.isInstance(o)) return false;
		
		Preference other = (Preference) o;
		
		return (other.name.equals(other.name) && this.compareTo(other) == 0);
	}
	
	
	@Override
	public String toString() {
		return this.getJSONObject().toString(4);
	}
	

	/**
	 * @return uris arrays
	 */
	public static Map<String, Attribute> getAttributes(){
		return Collections.unmodifiableMap(preferencesAttributes);
	}


}
