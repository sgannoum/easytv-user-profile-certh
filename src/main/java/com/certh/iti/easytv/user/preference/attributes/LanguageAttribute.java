package com.certh.iti.easytv.user.preference.attributes;

public class LanguageAttribute extends NominalAttribute {

	private static final String[] LANGUAGES = new String[] {"EN", "ES", "CA", "GR", "IT"};
	
	public LanguageAttribute() {
		super(-1,  LANGUAGES);
		this.enableDiscretization = false;
		this.enableFrequencyHistogram = false;
	}
	
	public LanguageAttribute(String[] langs) {
		super(-1,  langs);
		this.enableDiscretization = false;
		this.enableFrequencyHistogram = false;
	}
	
	public static class LanguageBuilder extends NominalBuilder{
		
		public LanguageBuilder() {
			super(new LanguageAttribute());
		}
		
	}
	
	public static LanguageBuilder Builder() {
		return new LanguageBuilder();
	}
	
}
