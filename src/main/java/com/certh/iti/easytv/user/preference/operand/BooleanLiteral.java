package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONObject;

public abstract class BooleanLiteral extends OperandLiteral {
	private boolean booleanLiteral;
	
	public BooleanLiteral(Object literal, Type type) {
		super(literal, type);
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

	public double[] getPoint() {
		return new double[] {booleanLiteral ? 1.0 : 0.0};
	}
	
}