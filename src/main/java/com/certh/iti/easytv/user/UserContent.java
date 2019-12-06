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
	
	/**
	 * Get context as a set of itemset 
	 * 
	 * @return
	 */
	public static final int[] getItemsCounts() {
		int index = 0, size = 0;
		Collection<Attribute> entries = content_attributes.values();
		
		for(Attribute attributHandler : content_attributes.values()) 
			size += attributHandler.getBinNumber();
				
		//create a table to hold all counts
		int[] counts = new int[size];

		//get bin frequency counts
		for(Object entry : entries) {
			Attribute attributHandler = (Attribute) entry;
			
			int[] binCounter = attributHandler.getBinsCounter();
			
			if(attributHandler.getBinNumber() == 0) continue;
			
			for(int j = 0; j < binCounter.length; j++)
				counts[index++] = binCounter[j];
		}
		
		return counts;
	}
	
	/**
	 * Get the bins corresponding values 
	 * @return
	 */
	public static final Object[] getItemsValues() {
		int index = 0, size = 0;
		Collection<Attribute> entries = content_attributes.values();
		
		for(Attribute attributHandler : content_attributes.values()) 
			size += attributHandler.getBinNumber();
		
		//create a table to hold all counts
		Object[] labels = new Object[size];

		//get bin frequency counts
		for(Object entry : entries) {
			Attribute attributHandler = (Attribute) entry;
			
			Object[] binsLabels = attributHandler.getBinsValues();
			
			if(binsLabels == null || attributHandler.getBinNumber() == 0) continue;
			
			for(int j = 0; j < binsLabels.length; labels[index++] = binsLabels[j++]);
		}
		
		return labels;
	}
	
	/**
	 * Get the bins corresponding labels 
	 * @return
	 */
	public static final String[] getItemsLabels() {
		int index = 0, size = 0;
		Collection<Entry<String, Attribute>> entries = content_attributes.entrySet();
		
		for(Attribute attributHandler : content_attributes.values()) 
			size += attributHandler.getBinNumber();
		
		//create a table to hold all counts
		String[] labels = new String[size];

		//get bin frequency counts
		for(Entry<String, Attribute> entry : entries) {

			String[] binsLabels = entry.getValue().getBinsLabel();
			
			if(binsLabels == null || entry.getValue().getBinNumber() == 0) continue;
			
			for(int j = 0; j < binsLabels.length; j++)
				labels[index++] = String.format("%s - %s", entry.getKey(), binsLabels[j]);
		}
		
		return labels;
	}
	
	/**
	 * Get users profiles dimensional operands
	 * 
	 * @return 
	 */
	public static final Attribute[] getOperands() {
		Collection<Entry<String, Attribute>> entries = content_attributes.entrySet();
		Attribute[] operandsLiteral = new Attribute[entries.size()];
		int index = 0;		
			
		for(Entry<String, Attribute> entry: entries) 
			operandsLiteral[index++] = entry.getValue();
		
		return 	operandsLiteral;
	}
	
	/**
	 * @return uris arrays
	 */
	public static String[] getUris(){
		Collection<Entry<String, Attribute>> entries = content_attributes.entrySet();
		String[] uris = new String[entries.size()];
		int index = 0;		
		
		for(Entry<String, Attribute> entry: entries) 
			uris[index++] = entry.getKey();
		
		return 	uris;
	}
}
