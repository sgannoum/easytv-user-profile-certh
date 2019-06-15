package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;

public class BooleanLiteral extends OperandLiteral {
	private boolean booleanLiteral;
	
	public BooleanLiteral(Object literal) {
		super(literal);
		booleanLiteral = (Boolean) literal;
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public String toString() {
		return String.valueOf(literal);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!BooleanLiteral.class.isInstance(obj)) return false;
		
		return literal.equals(((BooleanLiteral)obj).getValue());
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		BooleanLiteral other = (BooleanLiteral) op2;
		return booleanLiteral == other.booleanLiteral ? 0 : 1.0;
	}

	public double[] getPoint() {
		return new double[] {booleanLiteral ? 1.0 : 0.0};
	}
	
	@Override
	public OperandLiteral createFromJson(JSONObject jsonPreference, String field) {
		
		try {
			boolean obj = jsonPreference.getBoolean(field);
			return new BooleanLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}
}