package com.certh.iti.easytv.user.preference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	protected static final String[] StrTypes = { "NOT", "EQ" ,"NE","LT","LE","GT","GE","AND","OR","AP"};

	protected String type;
	protected List<Object> operands;
	protected JSONObject jsonObj;
	
	
	protected static final String CONTEXT_PREFIX = "http://registry.easytv.eu/context/";

	
	public static final LinkedHashMap<String, Attribute> contextToOperand  =  new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;

	{
		put("http://registry.easytv.eu/context/time",  new TimeAttribute());
		put("http://registry.easytv.eu/context/location", new NominalAttribute( new String[] {"gr", "fr", "sp", "it"}));
		put("http://registry.easytv.eu/context/contrast", new IntegerAttribute(new double[] {0.0, 100.0}, -1));

    }};
	
	
	public Condition(JSONObject json) {
		this.setJSONObject(json);
	}
	
	public Condition(String type, List<Object> operands) {
		this.setType(type);
		this.setOperands(operands);
		
		//Check type and operands compatibility
		isTypeAndOperandCompatibility(this.type, this.operands);
		
		jsonObj =  null;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
		if(indexOf(type) == -1) 
			throw new IllegalStateException("A condition with unknown type "+ type);
	}


	public List<Object> getOperands() {
		return operands;
	}

	public void setOperands(List<Object> operands) {
		this.operands = operands;
	}
	
	public JSONObject geJSONObject() {
		return jsonObj;
	}

	/**
	 * @brief Set the object value given JSON object. 
	 * 
	 * @param jsonObj
	 */
	public void setJSONObject(JSONObject jsonObj) {
		this.type = jsonObj.getString("type");
		this.operands = toOperands(jsonObj.getJSONArray("operands"));
		
		//Check that operands size and type
		isTypeAndOperandCompatibility(type, operands);
		
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
	private void isTypeAndOperandCompatibility(String type, List<Object> operands) {
		int int_type = indexOf(type);
		if(int_type == 0 && operands.size() != 1) {
			throw new IllegalStateException("A condition of type NOT must have only one operand, given "+ operands.size());
		} else if((int_type == 7 || int_type == 8) && operands.size() < 2) {
			throw new IllegalStateException("A condition of type "+ type +" must have at least two operands, given "+ operands.size());
		} else if((int_type != 0 && int_type != 7 && int_type != 8) && operands.size() != 2) {
			throw new IllegalStateException("A condition of type "+ type +" must have exaclty two operands, given "+ operands.size());
		}
	}
	
	/**
	 * Convert a string into a Type representation 
	 * 
	 * @param type
	 * @return The type representation
	 */
	private int indexOf(String type) {
		type = type.toUpperCase();
		for(int i = 0; i < StrTypes.length; i++) 
			if(type.equals(StrTypes[i])) 
				return i;
			
		return -1;
	}
	
	/**
	 * Convert JSONArray into a list of operand literals
	 * 
	 * @param jsonOperands
	 * @return A list of operand literals
	 */
	private List<Object> toOperands(JSONArray jsonOperands) {
		List<Object> operands = new ArrayList<Object>();
		for(int i = 0; i < jsonOperands.length(); i++) {
			
			// Specify entry type
			// try convert to JSONObject
			try {
				JSONObject obj = jsonOperands.getJSONObject(i);
				operands.add( new Condition(obj));
				continue;
			} catch (JSONException e1) {}	
			
			
			//get URI
			String uri = jsonOperands.getString(i++);
			operands.add(uri);
			
			//get Value
			Attribute attributeHandler = contextToOperand.get(uri);
			if(attributeHandler == null) 
				throw new IllegalArgumentException("Unknown context Uri: " + uri);
			
			
			operands.add(attributeHandler.handle(jsonOperands.get(i)));
		}
		return operands;
	}

}
