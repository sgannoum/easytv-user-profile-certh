package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;

public class LanguageLiteral extends OperandLiteral{
	
	private int language; 
	private static final String[] languagesStr = {"EN", "ES", "CA", "GR", "IT"};

	public LanguageLiteral(Object literal) {
		super(literal);
		language = indexOf((String) literal);
		
		if(language == -1)
			throw new IllegalStateException("Unknown language " + literal);
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public String toString() {
		return languagesStr[language];
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!LanguageLiteral.class.isInstance(obj)) return false;
		LanguageLiteral other = (LanguageLiteral) obj;
		
		return language == other.language;
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		LanguageLiteral other = (LanguageLiteral) op2;
		return Math.pow(language - other.language, 2);
	}
	
	
	protected static int indexOf(String language) {
		language = language.toUpperCase();
		for(int i = 0; i < languagesStr.length; i++) 
			if(language.equalsIgnoreCase(languagesStr[i])) 
				return i;
			
		return -1;
	}
	
	protected static String languageOf(int index) {
		if(index < 0 || index >= languagesStr.length)
			throw new IllegalStateException("Unknow integer representation of a language "+index);
		
		return languagesStr[index];
	}
	
	public double[] getPoint() {
		return new double[] {language};
	}
	
	@Override
	public OperandLiteral createFromJson(JSONObject jsonPreference, String field) {
		
		try {
			String obj = jsonPreference.getString(field);
			return new LanguageLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}
}
