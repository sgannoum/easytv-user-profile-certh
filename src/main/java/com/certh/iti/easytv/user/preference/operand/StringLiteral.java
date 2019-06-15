package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;


public class StringLiteral extends OperandLiteral {
	private String str;

	public StringLiteral(Object literal) {
		super(literal);
		str = (String) literal;
	}
	
	public StringLiteral(String literal) {
		super(literal);
		str = (String) literal;
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!StringLiteral.class.isInstance(obj)) return false;
		
		return str.equals(((StringLiteral) obj).str);
	}
	
	@Override
	public String toString() {
		return str;
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		StringLiteral other = (StringLiteral) op2;
		return str.compareToIgnoreCase(other.str);
	}
	
	public double[] getPoint() {
		return new double[] {str.length()};
	}
	
	@Override
	public OperandLiteral createFromJson(JSONObject jsonPreference, String field) {
		
		try {
			String obj = jsonPreference.getString(field);
			return new StringLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}
}