package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;

import com.certh.iti.easytv.user.preference.Condition;


public class ConditionLiteral extends OperandLiteral {
	private Condition conditionalLiteral;
	
	public ConditionLiteral(Object obj) {
		super(new Condition((JSONObject) obj));
		conditionalLiteral = (Condition) literal;
	}
	
	public ConditionLiteral(JSONObject obj) {
		super(new Condition(obj));
		conditionalLiteral = (Condition) literal;
	}
	
	public ConditionLiteral(Condition literal) {
		super(literal);
		conditionalLiteral = literal;
	}

	@Override
	public JSONObject toJSON() {
		return conditionalLiteral.toJSON();
	}
	
	@Override
	public String toString() {
		return conditionalLiteral.toJSON().toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!ConditionLiteral.class.isInstance(obj)) return false;
		
		return conditionalLiteral.equals(((ConditionLiteral) obj).conditionalLiteral);
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		return conditionalLiteral.distanceTo(((ConditionLiteral) op2).conditionalLiteral);
	}
	
	public double[] getPoint() {
		//TO-DO implement a getPoint
		return null;
	}
	
	@Override
	public OperandLiteral createFromJson(JSONObject jsonPreference, String field) {
		
		try {
			String obj = jsonPreference.getString(field);
			return new ConditionLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}
	
}