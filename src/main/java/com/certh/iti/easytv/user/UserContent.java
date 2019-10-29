package com.certh.iti.easytv.user;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class UserContent implements Clusterable {

	private double[] points = new double[] { -1, -1, -1, -1}; 
	protected Map<String, Object> content  =  new HashMap<String, Object>();
    private JSONObject jsonObj = null;
    
	public static final LinkedHashMap<String, Attribute> content_attributes  =  new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
 
	{
	    put("http://registry.easytv.eu/application/cs/accessibility/detection/face",  new SymmetricBinaryAttribute());
		put("http://registry.easytv.eu/application/cs/accessibility/detection/text",  new SymmetricBinaryAttribute());
		put("http://registry.easytv.eu/application/cs/accessibility/detection/sound",  new SymmetricBinaryAttribute());
	    put("http://registry.easytv.eu/application/cs/accessibility/detection/character",  new SymmetricBinaryAttribute());
	//  put("http://registry.easytv.eu/application/cs/cc/subtitles/language", new NominalAttribute(new String[] {"ca", "gr", "it", "es"}));
   //   put("http://registry.easytv.eu/application/cs/audio/track", new NominalAttribute(new String[] {"ca", "gr", "it", "es"}));
    }};
	
	public UserContent() {
	}
    
	public UserContent(JSONObject json) {
		setJSONObject(json);
	}
	
	@Override
	public double[] getPoint() {
		return points;
	}

	
	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			
			for(Entry<String, Object> entry : content.entrySet()) 
				jsonObj.put(entry.getKey(), entry.getValue());
		}
		
		return jsonObj;
	}
	
	public void setJSONObject(JSONObject json) {

		//clean up
		content.clear();
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/face")) {
			content.put("http://registry.easytv.eu/application/cs/accessibility/detection/face", json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/face"));
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/text")) {
			content.put("http://registry.easytv.eu/application/cs/accessibility/detection/text", json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/text"));
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/sound")) {
			content.put("http://registry.easytv.eu/application/cs/accessibility/detection/sound", json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/sound"));
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/character")) {
			content.put("http://registry.easytv.eu/application/cs/accessibility/detection/character", json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/character"));
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/cc/subtitles/language")) {			
			content.put("http://registry.easytv.eu/application/cs/cc/subtitles/language", json.getJSONArray("http://registry.easytv.eu/application/cs/cc/subtitles/language"));
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/audio/track")) {
			content.put("http://registry.easytv.eu/application/cs/audio/track", json.getJSONArray("http://registry.easytv.eu/application/cs/audio/track"));
		}
		
		//Update points 
		this.setPoints();
		
		//Update json
		jsonObj = json;
		
	}

	/**
	 * Initialize Points vector
	 */
	private void setPoints() {
		int index = 0;		
		for(Entry<String, Attribute> entry : content_attributes.entrySet()) {
			String prefKey = entry.getKey();
			Attribute handler = entry.getValue();
			
			//get preference value
			Object prefValue = content.get(prefKey);
			
			//get preference points
			double[] d = handler.getPoints(prefValue);
			
			points[index++] = d[0];
		}
	}
}
