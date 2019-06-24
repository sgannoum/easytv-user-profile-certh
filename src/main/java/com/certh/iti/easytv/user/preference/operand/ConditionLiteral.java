package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.user.preference.Condition;


public class ConditionLiteral extends OperandLiteral {
	
	public ConditionLiteral(Object obj) {
		super(new Condition((JSONObject) obj), Type.Non, null);
	}
	
	public ConditionLiteral(JSONObject obj) {
		super(new Condition(obj), Type.Non, null);
	}
	
	public ConditionLiteral(Condition literal) {
		super(literal, Type.Non, null);
	}
	
	@Override
	public String toString() {
		return ((Condition) literal).toJSON().toString();
	}
	
	@Override
	public Object getValue() {
		return ((Condition) literal).toJSON();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!ConditionLiteral.class.isInstance(obj)) return false;
		
		return ((Condition) literal).equals(((ConditionLiteral) obj).literal);
	}

	
	public double[] getPoint() {
		//TO-DO implement a getPoint
		return null;
	}
	
	@Override
	public OperandLiteral clone(Object value) {
		try {
			String obj = String.valueOf(value);
			return new ConditionLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}
	
}