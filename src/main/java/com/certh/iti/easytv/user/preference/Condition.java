package com.certh.iti.easytv.user.preference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.user.preference.operand.ConditionLiteral;
import com.certh.iti.easytv.user.preference.operand.NominalLiteral;
import com.certh.iti.easytv.user.preference.operand.OperandLiteral;
import com.certh.iti.easytv.user.preference.operand.StringLiteral;
import com.certh.iti.easytv.user.preference.operand.TimeLiteral;
import com.certh.iti.easytv.user.preference.operand.NumericLiteral;

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
	
	
	protected static final String CONTEXT_PREFIX = "http://registry.easytv.eu/context/";

	
	public static final LinkedHashMap<String, OperandLiteral> contextToOperand  =  new LinkedHashMap<String, OperandLiteral>() {
		private static final long serialVersionUID = 1L;

	{
		put(CONTEXT_PREFIX + "time",  new TimeLiteral("2019-05-30T09:47:47.619Z"));
		put(CONTEXT_PREFIX + "location", new NominalLiteral("gr", new String[] {"gr", "fr", "sp", "it"}));
		put(CONTEXT_PREFIX + "contrast", new NumericLiteral(0, new double[] {0.0, 100.0}));

    }};
	
	
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
				jsonOperands.put(operands.get(i).getValue());
					
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
		
		if(other.type != type) return false;
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
			
			// Specify entry type
			// try convert to JSONObject
			try {
				JSONObject obj = jsonOperands.getJSONObject(i);
				operands.add( new ConditionLiteral(obj));
				continue;
			} catch (JSONException e1) {}	
			
			
			//get URI
			String uri = jsonOperands.getString(i++);
			operands.add(new StringLiteral(uri));
			
			//get Value
			OperandLiteral literal = contextToOperand.get(uri);
			operands.add(literal.clone(jsonOperands.get(i)));
		}
		return operands;
	}

}
