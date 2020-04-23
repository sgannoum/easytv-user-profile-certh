package com.certh.iti.easytv.user;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

import com.certh.iti.easytv.user.exceptions.UserContextParsingException;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.DoubleAttribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.TimeAttribute;


public class UserContext implements Clusterable{
	
    private double[] points = new double[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}; 
    private Map<String, Object> context  =  new HashMap<String, Object>();
    private JSONObject jsonObj = null;
    
	protected static Map<String, Attribute> contextAttributes  =  new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
	{
	    put("http://registry.easytv.eu/context/device", new NominalAttribute(new String[] {"pc", "modile", "tablet"}));
		put("http://registry.easytv.eu/context/proximity", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, -1));
	    put("http://registry.easytv.eu/context/location", new NominalAttribute(new String[] {"ca", "gr", "it", "es"}));
	    put("http://registry.easytv.eu/context/time", new TimeAttribute());
	    put("http://registry.easytv.eu/context/soundMeter", new IntegerAttribute(new double[] {0.0, 120.0}, 1.0, 11,-1));
	    put("http://registry.easytv.eu/context/device/screenSize/diameter", new DoubleAttribute(new double[] {2.5, 31.0}, 1.0, -1));
	    put("http://registry.easytv.eu/context/device/screenSize/xdpi", new DoubleAttribute(new double[] {150.0, 640.0}, 1.0, -1));
	    put("http://registry.easytv.eu/context/device/screenSize/ydpi", new DoubleAttribute(new double[] {150.0, 640.0}, 1.0, -1));
	    put("http://registry.easytv.eu/context/device/screenSize/densityValue", new DoubleAttribute(new double[] {0.0, 10.0}, 1.0, -1));
	    put("http://registry.easytv.eu/context/device/screenSize/width", new NominalAttribute(new String[] {
	    		"94","96","100","102","109","110","113","125","127","128","130","131","132","135","149","150",
	    		"154","157","163","165","169","170","171","188","189","190","216","217","218","219","220","320",
	    		"360","480","540","568","600","640","720","750","768","800","828","854","854","900","960","1024",
	    		"1050","1080","1125","1136","1200","1242","1280","1334","1360","1366","1440","1536","1600","1680",
	    		"1700","1792","1800","1824","1920","1921","2000","2048","2160","2304","2436","2560","2688","2732",
	    		"2880","2960","3000","3840","4096","5120"}));
	    
	    put("http://registry.easytv.eu/context/device/screenSize/height", new NominalAttribute(new String[] {
	    		"94","96","100","102","109","110","113","125","127","128","130","131","132","135","149","150",
	    		"154","157","163","165","169","170","171","188","189","190","216","217","218","219","220","320",
	    		"360","480","540","568","600","640","720","750","768","800","828","854","854","900","960","1024",
	    		"1050","1080","1125","1136","1200","1242","1280","1334","1360","1366","1440","1536","1600","1680",
	    		"1700","1792","1800","1824","1920","1921","2000","2048","2160","2304","2436","2560","2688","2732",
	    		"2880","2960","3000","3840","4096","5120"}));

	    put("http://registry.easytv.eu/context/device/screenSize/densityBucket", new NominalAttribute(new String[] {
	    		"ldpi", "mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi"
	    		}));
	    
		put("http://registry.easytv.eu/context/light", new OrdinalAttribute(new String[] {
				"dark", "dark surroundings", "living room", "hallway", "overcast day",
				"home", "class", "workplace", "sunrise", "grocery", "supermarket",
				"theater", "detailed work", "visual task", "demanding visual task",
				"full daylight", "direct sun"
				}));
    }};
	
	
	public UserContext() {}
	
	public UserContext(Random rand) {	
		
		//Create user context
		for(final Entry<String, Attribute> e : UserContext.getAttributes().entrySet()) {
			Attribute oprand = e.getValue();
			context.put(e.getKey(), oprand.getRandomValue(rand));
		}
		
		//Update points
		this.setPoint();
		
		//Update json
		jsonObj = null;
		
	}
    
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
			catch(Exception e) {	
				throw new UserContextParsingException( key+ " "+e.getMessage());
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
			
			points[index++] = handler.getPoints(prefValue);
		}
	}
	
	public static Map<String, Attribute> getAttributes(){
		return Collections.unmodifiableMap(contextAttributes);
	}

}
