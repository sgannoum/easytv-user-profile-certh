package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;

public class FontLiteral extends NominalLiteral {
	
	protected static final String[] fontsStr = {"fantasy", "monospace", "sans-serif", "serif", "cursive"};

	public FontLiteral(Object literal) {
		super(literal, fontsStr);
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!FontLiteral.class.isInstance(obj)) return false;
		FontLiteral other = (FontLiteral) obj;
		
		return state == other.state;
	}
	
	@Override
	public OperandLiteral clone(Object value) {
		try {
			String str = String.valueOf(value);
			return new FontLiteral(str);
		} catch (JSONException e) {}
		
		return null;
	}
}
