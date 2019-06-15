package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;


public class NumericLiteral  extends OperandLiteral {
	private double numericLiteral;
	
	public NumericLiteral(Object literal) {
		super(literal);
		
		if(Double.class.isInstance(literal)) {
			numericLiteral = Double.class.cast(literal);
		} else {
			numericLiteral = Integer.class.cast(literal);
		}
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
		if(!NumericLiteral.class.isInstance(obj)) return false;
		NumericLiteral other = (NumericLiteral) obj;
		
		return numericLiteral == other.numericLiteral;
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		NumericLiteral other = (NumericLiteral) op2;
		return Math.pow(numericLiteral - other.numericLiteral, 2);
	}
	
	public double[] getPoint() {
		return new double[] {numericLiteral};
	}

	@Override
	public OperandLiteral createFromJson(JSONObject jsonPreference, String field) {
		
		try {
			Number obj = jsonPreference.getNumber(field);
			return new NumericLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}

}