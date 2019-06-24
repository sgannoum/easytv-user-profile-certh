package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;


public class StringLiteral extends OperandLiteral {

	public StringLiteral(Object literal) {
		super(literal, Type.Non, null);
	}
	
	public StringLiteral(String literal) {
		super(literal, Type.Non, null);
	}
	
	public double[] getPoint() {
		return new double[] {0.0};
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