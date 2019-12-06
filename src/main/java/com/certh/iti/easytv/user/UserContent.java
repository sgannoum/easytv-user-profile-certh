package com.certh.iti.easytv.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

import com.certh.iti.easytv.user.exceptions.UserContextParsingException;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.MultiNominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class UserContent implements Clusterable {

	private double[] points = new double[] { -1, -1, -1, -1, -1, -1}; 
	protected Map<String, Object> content  =  new HashMap<String, Object>();
    private JSONObject jsonObj = null;
    
	public static final LinkedHashMap<String, Attribute> content_attributes  =  new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
 
	{
	    put("http://registry.easytv.eu/application/cs/accessibility/detection/face",  new SymmetricBinaryAttribute());
		put("http://registry.easytv.eu/application/cs/accessibility/detection/text",  new SymmetricBinaryAttribute());
		put("http://registry.easytv.eu/application/cs/accessibility/detection/sound",  new SymmetricBinaryAttribute());
	    put("http://registry.easytv.eu/application/cs/accessibility/detection/character",  new SymmetricBinaryAttribute());
	    put("http://registry.easytv.eu/application/cs/cc/subtitles/language", new MultiNominalAttribute(new String[] {"ca", "gr", "it", "es"}));
	    put("http://registry.easytv.eu/application/cs/audio/track", new MultiNominalAttribute(new String[] {"ca", "gr", "it", "es"}));
    }};
	
	public UserContent() {
		this.setPoint();
	}
    
	public UserContent(JSONObject json) {
		setJSONObject(json);
	}
	
	public UserContent(Map<String, Object> content) throws UserContextParsingException {
		setContent(content);
	}
	
	public void setContent(Map<String, Object> content) throws UserContextParsingException {
		
		//clear up old preferences
		this.content.clear();
		
		for (Entry<String, Object> entries : content.entrySet()) {
			String key = entries.getKey();
			Object value = entries.getValue();
			Object handled_value;
			
			//Get preference attribute handler
			Attribute handler = content_attributes.get(key);
			
			//Unknown preference throw an exception
			if(handler == null) {
				throw new UserContextParsingException("Unknown context: '"+ key+"'");
			} 

			//Handle preference value
			try {
				handled_value = handler.handle(value);
			} catch(ClassCastException e) {	
				throw new UserContextParsingException("Non compatible data value: '"+value+"' for preference '"+ key+"' "+e.getMessage());
			}
			
			//Add
			this.content.put(key, handled_value);
		}
		
		//Update points
		this.setPoint();
		
		//Update json
		jsonObj = null;
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
		this.setPoint();
		
		//Update json
		jsonObj = null;
		
	}

	/**
	 * Initialize Points vector
	 */
	private void setPoint() {
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
	
	/**
	 * Get context as a set of itemset 
	 * 
	 * @return
	 */
	public int[] getAsItemSet() {
		int index = 0, size = 0;
		Collection<Entry<String, Object>> entries = content.entrySet();
		
		for(Attribute attributHandler : content_attributes.values()) 
			size += attributHandler.getBinNumber();
		
		int[] itemSet = new int[size];
		
		for(Entry<String, Object> entry : entries) {
			Attribute attributHandler = content_attributes.get(entry.getKey());
			
			if(attributHandler.getBinNumber() != 0 )
				itemSet[index++] = attributHandler.code(entry.getValue());
		}
		
		return itemSet;
	}
	
}
