package com.certh.iti.easytv.user.preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.user.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.attributes.Attribute;
import com.certh.iti.easytv.user.preference.attributes.IntegerAttribute;
import com.certh.iti.easytv.user.preference.attributes.NominalAttribute;
import com.certh.iti.easytv.user.preference.attributes.TimeAttribute;

/**
 * Each condition object shall have exactly one type (type string) and an operands object with one or more operand objects.
 * @author salgan
 *
 */
public class Condition {
	
	protected String type = new String();
	protected List<Object> operands = new ArrayList<Object>();;
	protected JSONObject jsonObj = null;
	
	public static final LinkedHashMap<String, Attribute> contextToOperand  =  new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;

	{
		put("http://registry.easytv.eu/context/device", new NominalAttribute( new String[] {"pc", "mobile", "tablet"}));
		put("http://registry.easytv.eu/context/light", new IntegerAttribute(new double[] {0.0, 100.0}, -1));
		put("http://registry.easytv.eu/context/proximity", new IntegerAttribute(new double[] {0.0, 500.0}, -1));
		put("http://registry.easytv.eu/context/time",  new TimeAttribute());
		put("http://registry.easytv.eu/context/location", new NominalAttribute( new String[] {"gr", "es", "ca", "it"}));
		put("http://registry.easytv.eu/context/contrast", new IntegerAttribute(new double[] {0.0, 100.0}, -1));

    }};
	
	public Condition(JSONObject json) throws UserProfileParsingException {
		this.setJSONObject(json);
	}
	
	public Condition(String type, List<Object> operands) throws UserProfileParsingException {

		//Check type and operands compatibility
		checkTypeOperandCompatibility(type, operands);
		
		this.type = type;
		this.setOperands(operands);
		
		jsonObj =  null;
	}
	
	public String getType() {
		return type;
	}

	public List<Object> getOperands() {
		return operands;
	}

	public void setOperands(List<Object> operands) {
		this.operands = operands;
		jsonObj = null;
	}
	
	public JSONObject geJSONObject() {
		return jsonObj;
	}

	/**
	 * @brief Set the object value given JSON object. 
	 * 
	 * @param jsonObj
	 * @throws UserProfileParsingException 
	 */
	public void setJSONObject(JSONObject jsonObj) throws UserProfileParsingException {
		
		if(!jsonObj.has("type"))
			throw new UserProfileParsingException("Wrong JSON: Missing condition 'type' element."+ jsonObj.toString());
		
		if(!jsonObj.has("operands"))
			throw new UserProfileParsingException("Wrong JSON: Missing condition 'operands' element: "+ jsonObj.toString());
		
		type = jsonObj.getString("type");
		
		//extract and add operands
		addOperands(operands, jsonObj.getJSONArray("operands"));
		
		//Check that operands size and type
		checkTypeOperandCompatibility(type, operands);

		this.jsonObj = jsonObj;
	}
	
	/**
	 * Convert to JSON
	 * 
	 * @return
	 */
	public JSONObject toJSON() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			JSONArray jsonOperands = new JSONArray();

			for(Object operand : operands) 
				if(Condition.class.isInstance(operand)) 
					jsonOperands.put(((Condition) operand).toJSON());
				else jsonOperands.put(operand);
					
			jsonObj.put("type", type.toLowerCase());
			jsonObj.put("operands", jsonOperands);
		}
		return jsonObj;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!Condition.class.isInstance(obj)) return false;
		Condition other = (Condition) obj;
		
		if(!other.type.equalsIgnoreCase(type)) return false;
		if(other.operands.size() != operands.size()) return false;
		
		int i = 0;
		for(i = 0 ; i < operands.size() && other.operands.contains(operands.get(i)); i++);

		return i == operands.size();
	}
	
	/**
	 * @brief Check type and operands compatibility
	 * 
	 * If type is "not", operands shall have exactly one element.
	 * If type is "eq", "ne", "lt", "le", "gt", "ge", or "ap", operands shall have exactly two elements.
	 * If type is "and" or "or", operands shall have at least two elements.
	 * 
	 * @param type
	 * @param operands
	 */
	private void checkTypeOperandCompatibility(String type, List<Object> operands) {
		int index = 0;
		final String[] StrTypes = { "NOT", "AND","OR", "EQ" ,"NE","LT","LE","GT","GE","AP"};
		for(String str : StrTypes) {
			if(type.equalsIgnoreCase(str))
				break;
			index++;
		}
		
		if(index == StrTypes.length) {
			throw new IllegalArgumentException("Unknown operand type: " + type);
		}
		else if(index == 0 && operands.size() != 1) {
			throw new IllegalStateException("A condition of type NOT must have only one operand, given "+ operands.size());
		} 
		else if((index == 1 || index == 2) && operands.size() < 2) {
			throw new IllegalStateException("A condition of type "+ type +" must have at least two operands, given "+ operands.size());
		} 
		else if( index > 2 && operands.size() != 2) {
			throw new IllegalStateException("A condition of type "+ type +" must have exaclty two operands, given "+ operands.size());
		}
	}
	
	/**
	 * Convert JSONArray into a list of operand literals
	 * 
	 * @param jsonOperands
	 * @return A list of operand literals
	 * @throws UserProfileParsingException 
	 */
	private List<Object> addOperands(List<Object> operands , JSONArray jsonOperands) throws UserProfileParsingException {
		for(int i = 0; i < jsonOperands.length(); i++) {
			
			// handle condition case
			try {
				JSONObject obj = jsonOperands.getJSONObject(i);
				operands.add( new Condition(obj));
				continue;
			} catch (JSONException e1) {}	
			
			//get URI
			String uri = jsonOperands.getString(i++);
			operands.add(uri);
			
			//get Value 
			Attribute handler = contextToOperand.get(uri);
			if(handler == null) 
				throw new IllegalArgumentException("Unknown context Uri: '" + uri +"'");
			
			
			operands.add(handler.handle(jsonOperands.get(i)));
		}
		
		return operands;
	}
	
	
	public void JSONArrayToListConverter(List<Object> operands, List<Object> jsonArray) throws UserProfileParsingException {
		

		for(int i = 0; i < jsonArray.size(); i++) {
			
			Object operand = jsonArray.get(i);
			

			if(HashMap.class.isInstance(operand)) {
					HashMap<String, Object> conditionOperands = (HashMap<String, Object>) operand;
					
					if(!conditionOperands.containsKey("type"))
						throw new UserProfileParsingException("Wrong JSON: Missing condition 'type' element.");
					
					if(!conditionOperands.containsKey("operands"))
						throw new UserProfileParsingException("Wrong JSON: Missing condition 'operands' element.");
					
					String entry_type = (String) conditionOperands.get("type");
					List<Object> entry_operands = (List<Object>) conditionOperands.get("operands");
					
					
					//new condition
					Condition condition = new Condition(entry_type, entry_operands);
				
					//add it to the operand list
					operands.add(condition);
			} else if(String.class.isInstance(operand)){
					//get URI
					String uri = (String) operand;
					Object value = jsonArray.get(++i);
				
					//get Value 
					Attribute handler = contextToOperand.get(uri);
					if(handler == null) 
						throw new IllegalArgumentException("Unknown context Uri: " + uri);

					//Handle contxt
					handler.handle(value);
						
					//add
					operands.add(uri);
					operands.add(value);
			}
		}

	}

}
