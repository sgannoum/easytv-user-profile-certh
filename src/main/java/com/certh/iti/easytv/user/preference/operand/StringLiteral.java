package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;


public class StringLiteral extends OperandLiteral {
	private String str;

	public StringLiteral(Object literal) {
		super(literal, Type.Non);
		str = (String) literal;
	}
	
	public StringLiteral(String literal) {
		super(literal, Type.Non);
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
	
	public double[] getPoint() {
		return new double[] {str.length()};
	}

	@Override
	public OperandLiteral clone(Object value) {
		try {
			String obj = String.valueOf(value);
			return new StringLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}
}