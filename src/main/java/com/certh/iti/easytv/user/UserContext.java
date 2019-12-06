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
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.TimeAttribute;

public class UserContext implements Clusterable{
	
    private double[] points = new double[] {-1, -1, -1, -1, -1}; 
    private Map<String, Object> context  =  new HashMap<String, Object>();
    private JSONObject jsonObj = null;
    
	public static final LinkedHashMap<String, Attribute> contextAttributes  =  new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
 
	{
	    put("http://registry.easytv.eu/context/device", new NominalAttribute(new String[] {"pc", "modile", "tablet"}));
		put("http://registry.easytv.eu/context/light", new IntegerAttribute(new double[] {0.0, 100.0}, -1));
		put("http://registry.easytv.eu/context/proximity", new IntegerAttribute(new double[] {0.0, 100.0}, -1));
	    put("http://registry.easytv.eu/context/location", new NominalAttribute(new String[] {"ca", "gr", "it", "es"}));
	    put("http://registry.easytv.eu/context/time", new TimeAttribute());
    }};
	
	
	public UserContext() {}
    
	public UserContext(JSONObject json) throws UserContextParsingException {
		setJSONObject(json);
	}
	
	public UserContext(Map<String, Object> context) throws UserContextParsingException {
		setContext(context);
	}
	
	public void setContext(Map<String, Object> context) throws UserContextParsingException {
		
		//clear up old preferences
		this.context.clear();
		
		for (Entry<String, Object> entries : context.entrySet()) {
			String key = entries.getKey();
			Object value = entries.getValue();
			Object handled_value;
			
			//Get preference attribute handler
			Attribute handler = contextAttributes.get(key);
			
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
			this.context.put(key, handled_value);
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
	
	public Map<String, Object> getContext(){
		return context;
	}

	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			
			for(Entry<String, Object> entry : context.entrySet()) 
				jsonObj.put(entry.getKey(), entry.getValue());
		}
		
		return jsonObj;
	}
	
	public void setJSONObject(JSONObject json) throws UserContextParsingException {	
		
		//clean up
		context.clear();
		
		//convert to map
		String[] fields = JSONObject.getNames(json);
		
		//Convert to a map
		Map<String, Object> entries = new HashMap<String, Object>();
		for(int i = 0 ; i < fields.length; i++) {
			String key = fields[i];
			Object value = json.get(key);
			entries.put(key, value);
		}
		
		//Update context
		this.setContext(entries);
	}
	
	/**
	 * Initialize Points vector
	 */
	private void setPoint() {
		int index = 0;
		for(Entry<String, Attribute> entry : contextAttributes.entrySet()) {
			String prefKey = entry.getKey();
			Attribute handler = entry.getValue();
			
			//get preference value
			Object prefValue = context.get(prefKey);
			
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
		Collection<Entry<String, Object>> entries = context.entrySet();
		
		for(Attribute attributHandler : contextAttributes.values()) 
			size += attributHandler.getBinNumber();
		
		int[] itemSet = new int[size];
		
		for(Entry<String, Object> entry : entries) {
			Attribute attributHandler = contextAttributes.get(entry.getKey());
			
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
		Collection<Attribute> entries = contextAttributes.values();
		
		for(Attribute attributHandler : contextAttributes.values()) 
			size += attributHandler.getBinNumber();
				
		//create a table to hold all counts
		int[] counts = new int[size];

		//get bin frequency counts
		for(Object entry : entries) {
			Attribute attributHandler = (Attribute) entry;
			
			int[] binCounter = attributHandler.getBinsCounter();
			
			if(binCounter == null) continue;
			
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
		Collection<Attribute> entries = contextAttributes.values();
		
		for(Attribute attributHandler : contextAttributes.values()) 
			size += attributHandler.getBinNumber();
		
		//create a table to hold all counts
		Object[] labels = new Object[size];

		//get bin frequency counts
		for(Object entry : entries) {
			Attribute attributHandler = (Attribute) entry;
			
			Object[] binsLabels = attributHandler.getBinsValues();
			
			if(binsLabels == null) continue;
			
			for(int j = 0; j < binsLabels.length; j++)
				labels[index++] = binsLabels[j];
		}
		
		return labels;
	}
	
	/**
	 * Get the bins corresponding labels 
	 * @return
	 */
	public static final String[] getItemsLabels() {
		int index = 0, size = 0;
		Collection<Entry<String, Attribute>> entries = contextAttributes.entrySet();
		
		for(Attribute attributHandler : contextAttributes.values()) 
			size += attributHandler.getBinNumber();
		
		//create a table to hold all counts
		String[] labels = new String[size];

		//get bin frequency counts
		for(Entry<String, Attribute> entry : entries) {

			String[] binsLabels = entry.getValue().getBinsLabel();
			
			if(binsLabels == null) continue;
			
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
		Collection<Entry<String, Attribute>> entries = contextAttributes.entrySet();
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
		Collection<Entry<String, Attribute>> entries = contextAttributes.entrySet();
		String[] uris = new String[entries.size()];
		int index = 0;		
		
		for(Entry<String, Attribute> entry: entries) 
			uris[index++] = entry.getKey();
		
		return 	uris;
	}


}
