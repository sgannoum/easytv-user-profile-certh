package com.certh.iti.easytv.user;

import java.text.ParseException;
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
	    put("http://registry.easytv.eu/context/time", new TimeAttribute());
	    put("http://registry.easytv.eu/context/location", new NominalAttribute(new String[] {"ca", "gr", "it", "es"}));
    }};
	
	
	public UserContext() {}
    
	public UserContext(JSONObject json) throws UserContextParsingException {
		setJSONObject(json);
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
		
		if(json.has("http://registry.easytv.eu/context/device")) {
			context.put("http://registry.easytv.eu/context/device", json.getString("http://registry.easytv.eu/context/device"));
		}
		
		if(json.has("http://registry.easytv.eu/context/light")) {
			context.put("http://registry.easytv.eu/context/light", json.getInt("http://registry.easytv.eu/context/light"));
		}
		
		if(json.has("http://registry.easytv.eu/context/proximity")) {
			context.put("http://registry.easytv.eu/context/proximity", json.getInt("http://registry.easytv.eu/context/proximity"));
		}

		if(json.has("http://registry.easytv.eu/context/time")) {
			String timeStr = json.getString("http://registry.easytv.eu/context/time");
			
			//convert time
			try {
				TimeAttribute.convertDate(timeStr);
			} catch (ParseException e) {
				// TODO Add context exceptions
				throw new UserContextParsingException("Wrong context time format "+ timeStr);
			}
			
			context.put("http://registry.easytv.eu/context/time", timeStr);
		}
		
		if(json.has("http://registry.easytv.eu/context/location")) {
			context.put("http://registry.easytv.eu/context/location", json.getString("http://registry.easytv.eu/context/location"));
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
