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
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute.IntegerConverter;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute.StringConverter;
import com.certh.iti.easytv.user.preference.attributes.OrdinalAttribute;
import com.certh.iti.easytv.user.preference.attributes.TimeAttribute;


public class UserContext implements Clusterable{
	
    private double[] points = new double[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}; 
    private Map<String, Object> context  =  new HashMap<String, Object>();
    private JSONObject jsonObj = null;
    private static final Integer[][] SCREEN_WIDTH_HEIGHT_DISCRET = 
    new Integer[][] {  
    	new  Integer[] {1,94},
    	new  Integer[] {95,109},
    	new  Integer[] {110,125},
    	new  Integer[] {126,149},
    	new  Integer[] {150,163},
    	new  Integer[] {164,188},
    	new  Integer[] {189,216},
    	new  Integer[] {217,320},
    	new  Integer[] {321,360},
    	new  Integer[] {361,480},
    	new  Integer[] {481,540},
    	new  Integer[] {541,568},
    	new  Integer[] {569,600},
    	new  Integer[] {601,640},
    	new  Integer[] {641,720},
    	new  Integer[] {721,750},
    	new  Integer[] {751,768},
    	new  Integer[] {769,800},
    	new  Integer[] {801,828},
    	new  Integer[] {829,854},
    	new  Integer[] {855,900},
    	new  Integer[] {901,960},
    	new  Integer[] {961,1024},
    	new  Integer[] {1025,1050},
    	new  Integer[] {1051,1080},
    	new  Integer[] {1081,1125},
    	new  Integer[] {1126,1136},
    	new  Integer[] {1137,1200},
    	new  Integer[] {1201,1242},
    	new  Integer[] {1243,1280},
    	new  Integer[] {1281,1334},
    	new  Integer[] {1335,1360},
    	new  Integer[] {1361,1366},
    	new  Integer[] {1367,1440},
    	new  Integer[] {1441,1536},
    	new  Integer[] {1537,1600},
    	new  Integer[] {1601,1680},
    	new  Integer[] {1681,1700},
    	new  Integer[] {1701,1792},
    	new  Integer[] {1793,1800},
    	new  Integer[] {1801,1824},
    	new  Integer[] {1825,1920},
    	new  Integer[] {1921,2000},
    	new  Integer[] {2001,2048},
    	new  Integer[] {2049,2160},
    	new  Integer[] {2161,2304},
    	new  Integer[] {2305,2436},
    	new  Integer[] {2437,2560},
    	new  Integer[] {2561,2688},
    	new  Integer[] {2689,2732},
    	new  Integer[] {2733,2880},
    	new  Integer[] {2881,2960},
    	new  Integer[] {2961,3000},
    	new  Integer[] {3001,3840},
    	new  Integer[] {3841,4096},
    	new  Integer[] {4097,5120}
    };
    
    private static final Integer[][] DENSITY_DISCRET = 
    new Integer[][] {
    	new  Integer[] {121,140},
    	new  Integer[] {141,160},
    	new  Integer[] {161,160},
    	new  Integer[] {161,180},
    	new  Integer[] {181,200},
    	new  Integer[] {201,220},
    	new  Integer[] {221,231},
    	new  Integer[] {232,240},
    	new  Integer[] {241,260},
    	new  Integer[] {261,280},
    	new  Integer[] {281,300},
    	new  Integer[] {301,320},
    	new  Integer[] {321,340},
    	new  Integer[] {341,360},
    	new  Integer[] {361,400},
    	new  Integer[] {401,420},
    	new  Integer[] {421,440},
    	new  Integer[] {441,450},
    	new  Integer[] {451,480},
    	new  Integer[] {481,560},
    	new  Integer[] {561,600},
    	new  Integer[] {601,640}
    };
    
	protected static Map<String, Attribute> contextAttributes  =  new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
	{
	    put("http://registry.easytv.eu/context/device", 
	    		new NominalAttribute(new String[] {"pc", "mobile", "tablet"}));
	    
		put("http://registry.easytv.eu/context/proximity", 
				new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, -1));
		
	    put("http://registry.easytv.eu/context/location", 
	    		new NominalAttribute(new String[] {"ca", "gr", "it", "es"}));
	    
	    put("http://registry.easytv.eu/context/time", 
	    		new TimeAttribute());
	    
	    put("http://registry.easytv.eu/context/device/soundMeter", 
	    		new NominalAttribute(new String[] { 
	    			"Breathing", "Mosquito", "Whisper", "Park", "Quiet office", "Normal conversation", "Busy traffic", "Red level"
	    		}));
	    
	    put("http://registry.easytv.eu/context/device/screenSize/width", 
	    		new IntegerAttribute(new double[] {1.0, 5120.0}, 1.0, SCREEN_WIDTH_HEIGHT_DISCRET));
	    
	    put("http://registry.easytv.eu/context/device/screenSize/height", 
	    		new IntegerAttribute(new double[] {1.0, 5120.0}, 1.0, SCREEN_WIDTH_HEIGHT_DISCRET));
	    
	    //The exact physical pixels per inch of the screen in the X dimension.
	    put("http://registry.easytv.eu/context/device/screenSize/xdpi", 		
	    		IntegerAttribute
				.Builder()
				.setRange(new double[] {150.0, 640.0})
				.setDiscretes(DENSITY_DISCRET)
				.setConverter(new IntegerConverter() {
					@Override
					public Integer valueOf(Object obj) {
						return ((Double) obj).intValue();
					}
					
					@Override
					public boolean isInstance(Object obj) {
						return Double.class.isInstance(obj);
					}
				})
				.build());			

	   //The exact physical pixels per inch of the screen in the Y dimension.
	    put("http://registry.easytv.eu/context/device/screenSize/ydpi", 		
	    		IntegerAttribute
				.Builder()
				.setRange(new double[] {150.0, 640.0})
				.setDiscretes(DENSITY_DISCRET)
				.setConverter(new IntegerConverter() {
					@Override
					public Integer valueOf(Object obj) {
						return ((Double) obj).intValue();
					}
					
					@Override
					public boolean isInstance(Object obj) {
						return Double.class.isInstance(obj);
					}
				})
				.build());
	    
	    put("http://registry.easytv.eu/context/device/screenSize/diameter", 
	    		new DoubleAttribute(new double[] {2.5, 31.0}, 0.5, -1));
	    
	    put("http://registry.easytv.eu/context/device/screenSize/densityValue", 
	    		NominalAttribute
				.Builder()
	    		.setState(new String[] { "0.75", "1.0", "1.5", "2.0", "3.0", "4.0"})
	    		.setConverter(new StringConverter() {
	    				    			
	    			@Override
	    			public String valueOf(Object obj) {
	    				String tmp = obj.toString();
	    				if(!tmp.contains(".")) tmp += ".0";
	    				return tmp;
	    			}
	    			
	    			@Override
	    			public boolean isInstance(Object obj) {
	    				return Number.class.isInstance(obj);
	    			}
	    		})
	    		.build());
	    
	    put("http://registry.easytv.eu/context/device/screenSize/densityBucket", 
	    		new NominalAttribute(new String[] { 
	    			"ldpi", "mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi"
	    		}));
	    
		put("http://registry.easytv.eu/context/light", 
				new OrdinalAttribute(new String[] {
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
