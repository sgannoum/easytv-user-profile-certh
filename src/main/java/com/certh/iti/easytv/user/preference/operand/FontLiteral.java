package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;

public class FontLiteral extends OperandLiteral {
	
	private int font; 

	private static final String[] fontsStr = {"fantasy", "monospace", "sans-serif", "serif", "cursive"};

	public FontLiteral(Object literal) {
		super(literal);
		font = indexOf((String) literal);
		
		if(font == -1)
			throw new IllegalStateException("Unknown Font type " + literal);
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public String toString() {
		return fontsStr[font];
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!FontLiteral.class.isInstance(obj)) return false;
		FontLiteral other = (FontLiteral) obj;
		
		return font == other.font;
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		FontLiteral other = (FontLiteral) op2;
		return Math.pow(font - other.font, 2);
	}
	
	
	protected static int indexOf(String language) {
		for(int i = 0; i < fontsStr.length; i++) 
			if(language.equalsIgnoreCase(fontsStr[i])) 
				return i;
			
		return -1;
	}
	
	protected static String fontOf(int index) {
		if(index < 0 || index >= fontsStr.length)
			throw new IllegalStateException("Unknow integer representation of a font "+index);
		
		return fontsStr[index];
	}
	
	public double[] getPoint() {
		return new double[] {font};
	}

	@Override
	public OperandLiteral createFromJson(JSONObject jsonPreference, String field) {
		
		try {
			String obj = jsonPreference.getString(field);
			return new FontLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}
}
