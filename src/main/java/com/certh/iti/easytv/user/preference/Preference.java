package com.certh.iti.easytv.user.preference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.ColorAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.LanguageAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class Preference implements Clusterable, Comparable<Preference> {

	public static final LinkedHashMap<String, Attribute> preferencesAttributes  =  new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
 
	{
		put("http://registry.easytv.eu/common/content/audio/volume",  new IntegerAttribute(new double[] {0.0, 100.0}, 0));
		put("http://registry.easytv.eu/common/content/audio/language", new LanguageAttribute());
		put("http://registry.easytv.eu/common/display/screen/enhancement/font/size", new IntegerAttribute(new double[] {1.0, 50.0} , 1));
		put("http://registry.easytv.eu/common/display/screen/enhancement/font/type", new NominalAttribute(new String[] {"fantasy", "monospace", "sans-serif", "serif", "cursive"}));
		put("http://registry.easytv.eu/common/display/screen/enhancement/font/color", new ColorAttribute());
		put("http://registry.easytv.eu/common/display/screen/enhancement/background", new ColorAttribute());
		put("http://registry.easytv.eu/common/subtitles",  new LanguageAttribute());
		put("http://registry.easytv.eu/common/signLanguage", new LanguageAttribute());
		put("http://registry.easytv.eu/common/displayContrast", new IntegerAttribute(new double[] {0.0, 100.0}, 0));

		put("http://registry.easytv.eu/application/tts/speed", new IntegerAttribute(new double[] {0.0, 100.0}, 0));
		put("http://registry.easytv.eu/application/tts/volume", new IntegerAttribute(new double[] {0.0, 100.0}, 0));
		put("http://registry.easytv.eu/application/tts/language",   new LanguageAttribute());
		put("http://registry.easytv.eu/application/tts/audioQuality", new IntegerAttribute(new double[] {0.0, 100.0}, 0));
		put("http://registry.easytv.eu/application/cs/accessibility/imageMagnification/scale", new IntegerAttribute(new double[] {0.0, 100.0}, 0));
		put("http://registry.easytv.eu/application/cs/accessibility/textDetection", new SymmetricBinaryAttribute());
		put("http://registry.easytv.eu/application/cs/accessibility/faceDetection", new SymmetricBinaryAttribute());
		put("http://registry.easytv.eu/application/cs/audio/volume",  new IntegerAttribute(new double[] {0.0, 100.0}, 0));
		put("http://registry.easytv.eu/application/cs/audio/track",  new LanguageAttribute());
		put("http://registry.easytv.eu/application/cs/audio/audioDescription", new SymmetricBinaryAttribute());
		put("http://registry.easytv.eu/application/cs/cc/audioSubtitles", new SymmetricBinaryAttribute());
		put("http://registry.easytv.eu/application/cs/cc/subtitles/language",  new LanguageAttribute());
		put("http://registry.easytv.eu/application/cs/cc/subtitles/fontSize", new IntegerAttribute(new double[] {0.0, 100.0}, 0));
		put("http://registry.easytv.eu/application/cs/cc/subtitles/fontColor", new ColorAttribute());
		put("http://registry.easytv.eu/application/cs/cc/subtitles/backgroundColor", new ColorAttribute());
		
    }};
	
	protected String name = new String();
	protected Map<String, Object> preferences = new HashMap<String, Object>();
	protected JSONObject jsonObj = null;
	
	public Preference() {
		
	}
	
	public Preference(String name, Map<String, Object> entries) {
		this.name = name;
		this.setPreferences(entries);
		jsonObj = null;
	}
	
	public Preference(String name, JSONObject json) {
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

	public Map<String, Object> getPreferences() {
		return preferences;
	}

	public void setPreferences(Map<String, Object> entries) {
		this.preferences.putAll(entries);
		jsonObj = null;
	}

	public JSONObject getJSONObject() {
		return toJSON();
	}

	public void setJSONObject(JSONObject json) {
		this.jsonObj = json;
		
		//clear up old preferences
		preferences.clear();
		
		JSONObject jsonPreference = json.getJSONObject("preferences");
		String[] fields = JSONObject.getNames(jsonPreference);
		
		//no preferences case
		if(fields == null) 
			return;
		
		for(int i = 0 ; i < fields.length; i++) {
			String preferenceUri = fields[i];
			Attribute attributeHandler = preferencesAttributes.get(preferenceUri);
			
			if(attributeHandler == null)
				throw new IllegalStateException("Unknown preference type");
			
			preferences.put(preferenceUri, attributeHandler.handle(jsonPreference.get(preferenceUri)));
		}
			
	}
	
	public JSONObject toJSON() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			JSONObject jsonPreferences = new JSONObject();
			
			for(Entry<String, Object> entry : preferences.entrySet()) 
				jsonPreferences.put(entry.getKey(), entry.getValue());
			
			jsonObj.put("preferences", jsonPreferences);
		}
		return jsonObj;
	}
	
	public double[] getPoint() {
		List<Double> pointsList = new ArrayList<Double>();
		
		for(Entry<String, Attribute> entry : preferencesAttributes.entrySet()) {
			Attribute attributeHandler = entry.getValue();
			
			//get the corresponding points
			double[] d = attributeHandler.getPoints(preferences.get(entry.getKey()));
			
			for(int i = 0; i < d.length && pointsList.add(new Double(d[i])); i++);
		}
		
		//convert to double[]
		double[] points = new double[pointsList.size()];
		for(int i = 0; i < pointsList.size(); i++) {
			points[i] = pointsList.get(i).doubleValue();
		}
		
		return points;
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
		return this.toJSON().toString(4);
	}
	
	/**
	 * Get users profiles dimensional operands
	 * 
	 * @return 
	 */
	public static final Attribute[] getOperands() {
		Collection<Attribute> values = preferencesAttributes.values();
		Attribute[] operandsLiteral = new Attribute[values.size()];
		int index = 0;		
		
		Iterator<java.util.Map.Entry<String, Attribute>> interator = preferencesAttributes.entrySet().iterator();
		while(interator.hasNext()) 
			operandsLiteral[index++] = interator.next().getValue();
		
		return 	operandsLiteral;
	}
	
	/**
	 * @return uris arrays
	 */
	public static String[] getUris(){
		Collection<String> keys = preferencesAttributes.keySet();
		String[] uris = new String[keys.size()];
		int index = 0;		
		
		Iterator<java.util.Map.Entry<String, Attribute>> interator = preferencesAttributes.entrySet().iterator();
		while(interator.hasNext()) 
			uris[index++] = interator.next().getKey();
		
		return 	uris;
	}
	
	
}
