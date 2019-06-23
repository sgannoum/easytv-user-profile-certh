package com.certh.iti.easytv.user.preference;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.user.preference.operand.BooleanLiteral;
import com.certh.iti.easytv.user.preference.operand.ConditionLiteral;
import com.certh.iti.easytv.user.preference.operand.NumericLiteral;
import com.certh.iti.easytv.user.preference.operand.OperandLiteral;
import com.certh.iti.easytv.user.preference.operand.StringLiteral;
import com.certh.iti.easytv.user.preference.operand.SymmetricBooleanLiteral;


/**
 * Each condition object shall have exactly one type (type string) and an operands object with one or more operand objects.
 * @author salgan
 *
 */
public class Condition {
	
	protected static final String[] StrTypes = { "NOT", "EQ" ,"NE","LT","LE","GT","GE","AND","OR","AP"};

	protected int type;
	protected List<OperandLiteral> operands;
	protected JSONObject jsonObj;
	
	
	public Condition(JSONObject json) {
		this.setJSONObject(json);
	}
	
	public Condition(String type, List<OperandLiteral> operands) {
		this.setType(type);
		this.setOperands(operands);
		
		//Check type and operands compatibility
		isTypeAndOperandCompatibility(this.type, this.operands);
		
		jsonObj =  null;
	}
	
	public String getType() {
		return StrTypes[type];
	}
	
	public void setType(String type) {
		this.type = indexOf(type);
		
		if(this.type == -1) 
			throw new IllegalStateException("A condition with unknown type "+ type);
		
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<OperandLiteral> getOperands() {
		return operands;
	}

	public void setOperands(List<OperandLiteral> operands) {
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
		this.type = indexOf(jsonObj.getString("type"));
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
			
			for(int i = 0 ; i < operands.size(); i++) 
				jsonOperands.put(operands.get(i).toJSON());
					
			jsonObj.put("type", StrTypes[type].toLowerCase());
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
		
		if(other.operands.size() != operands.size()) return false;
		
		boolean equalOperands = true;
		for(int i = 0 ; i < operands.size() && equalOperands; i++) {
			equalOperands = false;
			for(int j = 0 ; j < other.operands.size() && !equalOperands; j++)
				if(other.operands.get(j).equals(operands.get(i))) 
					equalOperands = true;
		}
		return equalOperands;
	}
	
	public double distanceTo(Condition other) {
		//TO-DO specify how to calculate distance between conditions
		
		if(this.type == other.type) {
			
		}
		
		return 0;
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
	private void isTypeAndOperandCompatibility(int type, List<OperandLiteral> operands) {
		
		if(type == 0 && operands.size() != 1) {
			throw new IllegalStateException("A condition of type NOT must have only one operand, given "+ operands.size());
		} else if((type == 7 || type == 8) && operands.size() < 2) {
			throw new IllegalStateException("A condition of type "+StrTypes[type]+" must have at least two operands, given "+ operands.size());
		} else if((type != 0 && type != 7 && type != 8) && operands.size() != 2) {
			throw new IllegalStateException("A condition of type "+StrTypes[type]+" must have exaclty two operands, given "+ operands.size());
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
	private List<OperandLiteral> toOperands(JSONArray jsonOperands) {
		List<OperandLiteral> operands = new ArrayList<OperandLiteral>();
		for(int i = 0; i < jsonOperands.length(); i++) {
			boolean found = false;
			
			// Specify entry type
			// try convert to JSONObject
			try {
				JSONObject obj = jsonOperands.getJSONObject(i);
				operands.add( new ConditionLiteral(obj));
				found = true;
			} catch (JSONException e1) {}	
			
			// try convert to string
			if(!found)
			try {
				String obj = jsonOperands.getString(i);
				operands.add( new StringLiteral(obj));
				found = true;
			} catch (JSONException e2) {}
			
			// try convert to number
			if(!found)
			try {
				double obj = jsonOperands.getDouble(i);
				operands.add( new NumericLiteral(obj));
				found = true;
			} catch (JSONException e3) {}
		
			// try convert to boolean
			if(!found)
			try {
				boolean obj = jsonOperands.getBoolean(i);
				operands.add( new SymmetricBooleanLiteral(obj));
				found = true;
			} catch (JSONException e4) {}
			
		}
		return operands;
	}

}
