package com.certh.iti.easytv.user;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

import com.certh.iti.easytv.user.exceptions.UserContextParsingException;
import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
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
	    put("http://registry.easytv.eu/context/time", new TimeAttribute());
	    put("http://registry.easytv.eu/context/location", new NominalAttribute(new String[] {"ca", "gr", "it", "es"}));
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


}
