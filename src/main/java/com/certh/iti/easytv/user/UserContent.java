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
import com.certh.iti.easytv.user.preference.attributes.AttributesAggregator;
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
	
	public static AttributesAggregator aggregator = new AttributesAggregator();
	static {
		aggregator.add(UserContent.content_attributes);
	}
    
	public UserContent() {
		this.setPoint();
	}
    
	public UserContent(JSONObject json) throws UserContextParsingException {
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
	
	public void setJSONObject(JSONObject json) throws UserContextParsingException {

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
	
	/**
	 * Get context as a set of itemset 
	 * 
	 * @return
	 */
	public int[] getAsItemSet() {
		int index = 0, base = 0, size = 0;
		Collection<Entry<String, Attribute>> entries = content_attributes.entrySet();
		
		for(Attribute attributHandler : content_attributes.values()) 
			if(attributHandler.getBinNumber() != 0)  size++;
		
		int[] itemSet;
		if(content.size() > size)
			itemSet = new int[size];
		else itemSet = new int[content.size()];
		
		for(Entry<String, Attribute> entry : entries) {
			Attribute attributHandler =  entry.getValue();
			Object value = content.get(entry.getKey()); 
			
			//add only existing preferences
			if(attributHandler.getBinNumber() != 0 && value != null)
				itemSet[index++] = attributHandler.code(value) + base;
			
			base += attributHandler.getBinNumber();
		}
		return itemSet;
	}
	
	public static final AttributesAggregator getAttributesAggregator() {
		return aggregator;
	}

	
}
