package com.certh.iti.easytv.user.preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

import com.certh.iti.easytv.user.preference.operand.ColorLiteral;
import com.certh.iti.easytv.user.preference.operand.FontLiteral;
import com.certh.iti.easytv.user.preference.operand.LanguageLiteral;
import com.certh.iti.easytv.user.preference.operand.NumericLiteral;
import com.certh.iti.easytv.user.preference.operand.OperandLiteral;
import com.certh.iti.easytv.user.preference.operand.SymmetricBooleanLiteral;

public class Preference implements Clusterable, Comparable<Preference> {

	
	protected static final String COMMON_PREFIX = "http://registry.easytv.eu/common/";
	protected static final String APPLICATION_PREFIX = "http://registry.easytv.eu/application/";
	
	public static final HashMap<String, OperandLiteral> preferencesToOperand  =  new HashMap<String, OperandLiteral>() {{
		
		put(COMMON_PREFIX + "content/audio/volume",  new NumericLiteral(0));
		put(COMMON_PREFIX + "content/audio/language", new LanguageLiteral("en"));
		put(COMMON_PREFIX + "display/screen/enhancement/font/size", new NumericLiteral(0));
		put(COMMON_PREFIX + "display/screen/enhancement/font/type", new FontLiteral("fantasy"));
		put(COMMON_PREFIX + "display/screen/enhancement/font/color", new ColorLiteral("#000000"));
		put(COMMON_PREFIX + "display/screen/enhancement/background", new ColorLiteral("#000000"));
		put(COMMON_PREFIX + "subtitles", new LanguageLiteral("en"));
		put(COMMON_PREFIX + "signLanguage", new LanguageLiteral("en"));
		put(COMMON_PREFIX + "displayContrast", new NumericLiteral(0));

		put(APPLICATION_PREFIX + "tts/speed", new NumericLiteral(0));
		put(APPLICATION_PREFIX + "tts/volume", new NumericLiteral(0));
		put(APPLICATION_PREFIX + "tts/language",  new LanguageLiteral("en"));
		put(APPLICATION_PREFIX + "tts/audioQuality", new NumericLiteral(0));
		put(APPLICATION_PREFIX + "cs/accessibility/imageMagnification/scale", new NumericLiteral(0));
		put(APPLICATION_PREFIX + "cs/accessibility/textDetection", new SymmetricBooleanLiteral(false));
		put(APPLICATION_PREFIX + "cs/accessibility/faceDetection", new SymmetricBooleanLiteral(false));
		put(APPLICATION_PREFIX + "cs/audio/volume",  new NumericLiteral(0));
		put(APPLICATION_PREFIX + "cs/audio/track", new LanguageLiteral("en"));
		put(APPLICATION_PREFIX + "cs/audio/audioDescription", new SymmetricBooleanLiteral(false));
		put(APPLICATION_PREFIX + "cs/cc/audioSubtitles", new SymmetricBooleanLiteral(false));
		put(APPLICATION_PREFIX + "cs/cc/subtitles/language", new LanguageLiteral("en"));
		put(APPLICATION_PREFIX + "cs/cc/subtitles/fontSize", new NumericLiteral(0));
		put(APPLICATION_PREFIX + "cs/cc/subtitles/fontColor", new ColorLiteral("#000000"));
		put(APPLICATION_PREFIX + "cs/cc/subtitles/backgroundColor", new ColorLiteral("#000000"));
		
    }};
	
	protected String name;
	protected Map<String, OperandLiteral> preferences;
	protected JSONObject jsonObj;
	
	public Preference(String name, Map<String, OperandLiteral> entries) {
		this.name = name;
		this.preferences = new HashMap<String, OperandLiteral>();
		this.setPreferences(entries);
		jsonObj = null;
	}
	
	public Preference(String name, JSONObject json) {
		this.name = name;
		this.preferences = new HashMap<String, OperandLiteral>();
		setJSONObject(json);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, OperandLiteral> getPreferences() {
		return preferences;
	}

	public void setPreferences(Map<String, OperandLiteral> entries) {
		this.preferences = entries;
	}

	public JSONObject getJSONObject() {
		return toJSON();
	}

	public void setJSONObject(JSONObject json) {
		JSONObject jsonPreference = json.getJSONObject("preferences");
		String[] fields = JSONObject.getNames(jsonPreference);
		
		for(int i = 0 ; i < fields.length; i++) {
			String preferenceUri = fields[i];
			OperandLiteral instance = preferencesToOperand.get(preferenceUri);
			
			if(instance == null)
				throw new IllegalStateException("Unknown preference type");
			
			preferences.put(preferenceUri, (OperandLiteral) instance.clone(jsonPreference.get(preferenceUri)));
		}
		
		this.jsonObj = json;
	}
	
	public JSONObject toJSON() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			JSONObject jsonPreferences = new JSONObject();
			
			Iterator<java.util.Map.Entry<String, OperandLiteral>> interator = preferences.entrySet().iterator();
			while(interator.hasNext()) {
				Map.Entry<String, OperandLiteral> entry = interator.next();
				jsonPreferences.put(entry.getKey(), entry.getValue().getValue());
			}
			jsonObj.put("preferences", jsonPreferences);
		}
		return jsonObj;
	}
	
	public double[] getPoint() {
		List<Double> pointsList = new ArrayList<Double>();
		
		Iterator<Entry<String, OperandLiteral>> iter = preferencesToOperand.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, OperandLiteral> entry = iter.next();
			OperandLiteral operand = preferences.get(entry.getKey());
			double[] d;
			
			if(operand == null) {
				d = entry.getValue().getPoint();
			} else {
				d = operand.getPoint();
			}
			
			for(int i = 0; i < d.length; i++)
				pointsList.add(new Double(d[i]));
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
	
	public static final OperandLiteral[] getPreferencesOperands(){
		return 	(OperandLiteral[]) preferencesToOperand.values().toArray() ;
	}
	
}
