package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;

public class SymmetricBooleanLiteral extends BooleanLiteral {

	public SymmetricBooleanLiteral(Object literal) {
		super(literal, Type.Symmetric_Binary);
	}

	@Override
	public OperandLiteral clone(Object value) {
		try {
			boolean obj = (Boolean) value;
			return new SymmetricBooleanLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}
}
