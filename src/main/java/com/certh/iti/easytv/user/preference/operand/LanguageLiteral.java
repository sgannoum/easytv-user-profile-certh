package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;

public class LanguageLiteral extends NominalLiteral {
	
	protected final static String[] languagesStr = {"EN", "ES", "CA", "GR", "IT"};

	public LanguageLiteral(Object literal) {
		super(literal, languagesStr);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!LanguageLiteral.class.isInstance(obj)) return false;
		LanguageLiteral other = (LanguageLiteral) obj;
		
		return state == other.state;
	}
	
	@Override
	public OperandLiteral clone(Object value) {
		try {
			String str = String.valueOf(value);
			return new LanguageLiteral(str);
		} catch (JSONException e) {}
		
		return null;
	}
}
