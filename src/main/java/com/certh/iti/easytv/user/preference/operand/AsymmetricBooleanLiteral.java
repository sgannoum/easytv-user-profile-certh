package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;

public class AsymmetricBooleanLiteral extends BooleanLiteral {

	public AsymmetricBooleanLiteral(Object literal) {
		super(literal, Type.Asymetric_Binary);
	}

	@Override
	public OperandLiteral clone(Object value) {
		try {
			boolean obj = (Boolean) value;
			return new AsymmetricBooleanLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}
}
