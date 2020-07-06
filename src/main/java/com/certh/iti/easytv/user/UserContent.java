package com.certh.iti.easytv.user;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

import com.certh.iti.easytv.user.exceptions.UserContentParsingException;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.MultiNominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.SymmetricBinaryAttribute;

public class UserContent implements Clusterable {

	private double[] points = new double[] { -1, -1, -1, -1, -1, -1}; 
	protected Map<String, Object> content  =  new HashMap<String, Object>();
    private JSONObject jsonObj = null;
    
	protected static Map<String, Attribute> content_attributes;
	
	static {
		init();
	}
    
	public static void init() {
		content_attributes  =  new LinkedHashMap<String, Attribute>() {
			private static final long serialVersionUID = 1L;
	 
		{
		    put("http://registry.easytv.eu/application/cs/accessibility/detection/face",  new SymmetricBinaryAttribute());
			put("http://registry.easytv.eu/application/cs/accessibility/detection/text",  new SymmetricBinaryAttribute());
			put("http://registry.easytv.eu/application/cs/accessibility/detection/sound",  new SymmetricBinaryAttribute());
		    put("http://registry.easytv.eu/application/cs/accessibility/detection/character",  new SymmetricBinaryAttribute());
		    put("http://registry.easytv.eu/application/cs/cc/subtitles/language", new MultiNominalAttribute(new String[] {"ca", "gr", "it", "es"}));
		    put("http://registry.easytv.eu/application/cs/audio/track", new MultiNominalAttribute(new String[] {"ca", "gr", "it", "es"}));
	    }};
		
	}
    
    
	public UserContent() {
		this.setPoint();
	}
	
	public UserContent(Random rand) {	
		
		for(final Entry<String, Attribute> e : UserContent.getAttributes().entrySet()) {
			Attribute oprand = e.getValue();
			content.put(e.getKey(), oprand.getRandomValue(rand));
		}
		
		//Update points
		this.setPoint();
		
		//Update json
		jsonObj = null;
		
	}
    
	public UserContent(JSONObject json) throws UserContentParsingException {
		setJSONObject(json);
	}
	
	public UserContent(Map<String, Object> content) throws UserContentParsingException {
		setContent(content);
	}
	
	public Map<String, Object> getContent(){
		return content;
	}
	
	public void setContent(Map<String, Object> content) throws UserContentParsingException {
		
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
				throw new UserContentParsingException("Unknown content: '"+ key+"'");
			} 

			//Handle preference value
			try {
				handled_value = handler.handle(value);
			} catch(ClassCastException e) {	
				throw new UserContentParsingException("Non compatible data value: '"+value+"' for preference '"+ key+"' "+e.getMessage());
			}			
			catch(Exception e) {	
				throw new UserContentParsingException( key+ " "+e.getMessage());
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
	
	public void setJSONObject(JSONObject json) throws UserContentParsingException {

		//clean up
		Map<String, Object> entries = new HashMap<String, Object>();
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/face")) {
			entries.put("http://registry.easytv.eu/application/cs/accessibility/detection/face", json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/face"));
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/text")) {
			entries.put("http://registry.easytv.eu/application/cs/accessibility/detection/text", json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/text"));
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/sound")) {
			entries.put("http://registry.easytv.eu/application/cs/accessibility/detection/sound", json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/sound"));
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/accessibility/detection/character")) {
			entries.put("http://registry.easytv.eu/application/cs/accessibility/detection/character", json.getBoolean("http://registry.easytv.eu/application/cs/accessibility/detection/character"));
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/cc/subtitles/language")) {			
			entries.put("http://registry.easytv.eu/application/cs/cc/subtitles/language", json.getJSONArray("http://registry.easytv.eu/application/cs/cc/subtitles/language"));
		}
		
		if(json.has("http://registry.easytv.eu/application/cs/audio/track")) {
			entries.put("http://registry.easytv.eu/application/cs/audio/track", json.getJSONArray("http://registry.easytv.eu/application/cs/audio/track"));
		}
		
		//Update context
		this.setContent(entries);
		
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
			
			points[index++] = handler.getPoints(prefValue);
		}
	}
	
	public static Map<String, Attribute> getAttributes(){
		return Collections.unmodifiableMap(content_attributes);
	}

	
}
