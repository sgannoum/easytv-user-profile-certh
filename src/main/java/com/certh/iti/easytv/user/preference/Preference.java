package com.certh.iti.easytv.user.preference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.AttributesAggregator;
import com.certh.iti.easytv.user.preference.attributes.ColorAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.LanguageAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;
import com.certh.iti.easytv.user.preference.attributes.DoubleAttribute;

public class Preference implements Clusterable, Comparable<Preference> {

	public static final LinkedHashMap<String, Attribute> preferencesAttributes  =  new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
 
	{
		put("http://registry.easytv.eu/common/volume", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, 0));
		put("http://registry.easytv.eu/common/contrast", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, 0));
	    put("http://registry.easytv.eu/common/content/audio/language", new LanguageAttribute());
	    put("http://registry.easytv.eu/common/display/screen/enhancement/cursor/Size", new DoubleAttribute(new double[] {1.0, 2.0}, 0.5, 0));
	    put("http://registry.easytv.eu/common/display/screen/enhancement/cursor/color", new ColorAttribute());
	    put("http://registry.easytv.eu/application/tts/audio/language", new LanguageAttribute());
	    put("http://registry.easytv.eu/application/tts/audio/speed", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, 0));
	    put("http://registry.easytv.eu/application/tts/audio/volume", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, 0));
	    put("http://registry.easytv.eu/application/tts/audio/voice", new NominalAttribute(new String[] {"male", "female"}));
		put("http://registry.easytv.eu/application/tts/audio/quality", new IntegerAttribute(new double[] {1.0, 8.0}, 0)); 
	    put("http://registry.easytv.eu/application/cs/cc/subtitles/language", new LanguageAttribute());
	    put("http://registry.easytv.eu/application/cs/cc/subtitles/font/size", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, 0));
	    put("http://registry.easytv.eu/application/cs/cc/subtitles/font/color", new ColorAttribute());
	    put("http://registry.easytv.eu/application/cs/cc/subtitles/background/color", new ColorAttribute());
	    put("http://registry.easytv.eu/application/cs/cc/audio/subtitle",  new SymmetricBinaryAttribute());
	    put("http://registry.easytv.eu/application/cs/ui/audioAssistanceBasedOnTTS",  new SymmetricBinaryAttribute());	
	    put("http://registry.easytv.eu/application/cs/ui/text/size", new OrdinalAttribute(new String[] {"15", "20", "23"}));					
	    put("http://registry.easytv.eu/application/cs/ui/language", new LanguageAttribute());	
	    put("http://registry.easytv.eu/application/cs/ui/vibration/touch", new SymmetricBinaryAttribute());
	    put("http://registry.easytv.eu/application/cs/ui/text/magnification/scale",  new SymmetricBinaryAttribute()); 					
	    put("http://registry.easytv.eu/application/cs/audio/eq/bass", new IntegerAttribute(new double[] {-15.0, 15.0}, 1.0, 10, -16.0));
	    put("http://registry.easytv.eu/application/cs/audio/eq/mids", new IntegerAttribute(new double[] {-15.0, 15.0}, 1.0, 10, -16.0));
	    put("http://registry.easytv.eu/application/cs/audio/eq/highs", new IntegerAttribute(new double[] {-15.0, 15.0}, 1.0, 10, -16.0));
	  	put("http://registry.easytv.eu/application/cs/audio/volume", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, 0));
	    put("http://registry.easytv.eu/application/cs/audio/track", new LanguageAttribute());
	    put("http://registry.easytv.eu/application/control/voice", new SymmetricBinaryAttribute());
	    put("http://registry.easytv.eu/application/control/csGazeAndGestureControlType", new NominalAttribute(new String[] {"none", "gaze_control", "gesture_control"}));
	    put("http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiTextSize",  new DoubleAttribute(new double[] {0.0, 3.0}, 0));
	    put("http://registry.easytv.eu/application/control/csGazeAndGestureControlCursorGuiLanguage", new LanguageAttribute());
	    put("http://registry.easytv.eu/application/cs/accessibility/enhancement/image/type",  new NominalAttribute(new String[] {"none", "face-detection", "image-magnification"}));
	    put("http://registry.easytv.eu/application/cs/accessibility/audio/description",  new SymmetricBinaryAttribute());
	    put("http://registry.easytv.eu/application/cs/accessibility/detection/sound",  new SymmetricBinaryAttribute());
	    put("http://registry.easytv.eu/application/cs/accessibility/detection/text",  new SymmetricBinaryAttribute());
	    put("http://registry.easytv.eu/application/cs/accessibility/detection/character",  new SymmetricBinaryAttribute());
	    put("http://registry.easytv.eu/application/cs/accessibility/magnification/scale", new DoubleAttribute(new double[] {1.5, 3.5}, 0.5, 0));
	    put("http://registry.easytv.eu/application/cs/accessibility/sign/language", new LanguageAttribute());

		
		//  put("http://registry.easytv.eu/common/display/screen/enhancement/background", new ColorAttribute());
		//  put("http://registry.easytv.eu/application/cs/accessibility/detection/face",  new SymmetricBinaryAttribute());
		//	put("http://registry.easytv.eu/application/control/gaze",  new SymmetricBinaryAttribute());
		//	put("http://registry.easytv.eu/common/display/screen/enhancement/font/size", new IntegerAttribute(new double[] {1.0, 50.0} , 1));
		//  put("http://registry.easytv.eu/common/display/screen/enhancement/font/type", new NominalAttribute(new String[] {"fantasy", "monospace", "sans-serif", "serif", "cursive"}));
		//  put("http://registry.easytv.eu/common/display/screen/enhancement/font/color", new ColorAttribute());
		//  put("http://registry.easytv.eu/application/cs/cc/subtitles/language", new LanguageAttribute());
		
    }};
    
    
	public static AttributesAggregator aggregator = new AttributesAggregator();
	static {
		aggregator.add(Preference.preferencesAttributes);
	}
	
    protected double[] points;
	protected String name = new String();
	protected Map<String, Object> preferences = new HashMap<String, Object>();
	protected JSONObject jsonObj = null;
	
	public Preference() {}
	
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
	
	/**
	 * Get an item set representation of the user profile 
	 * 
	 * @return
	 */
	public int[] getAsItemSet() {
		Collection<Entry<String, Attribute>> entries = preferencesAttributes.entrySet();
		int[] itemSet = new int[preferences.size()];
		int index = 0, base = 0;
		
		for(Entry<String, Attribute> entry : entries) {
			String key = entry.getKey();
			Attribute attributHandler = preferencesAttributes.get(key);
			Object value = preferences.get(key);
			
			//add only existing preferences
			if(attributHandler.getBinNumber() != 0 && value != null) 
				itemSet[index++] = attributHandler.code(value) + base;
			
			base += attributHandler.getBinNumber();
		}
		return itemSet;
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
				throw new UserProfileParsingException("Non compatible data value: '"+value+"' for preference '"+ key+"' "+e.getMessage());
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
			double[] d = handler.getPoints(prefValue);
			
			//add points
			for(int i = 0; i < d.length && pointsList.add(new Double(d[i])); i++);
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
	 * Get users profiles dimensional operands
	 * 
	 * @return 
	 */
	public static final AttributesAggregator getAttributesAggregator() {
		return aggregator;
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
}
