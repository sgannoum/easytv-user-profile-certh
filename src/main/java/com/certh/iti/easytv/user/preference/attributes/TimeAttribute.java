package com.certh.iti.easytv.user.preference.attributes;

import java.time.Instant;

public class TimeAttribute extends Attribute{

	public TimeAttribute() {
		super(new double[] {0.0, Instant.parse("1970-01-01T00:00:00Z").toEpochMilli()});
	}
	
	public TimeAttribute(double[] range) {
		super(range);
	}
	
	public TimeAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
	}
	
	@Override
	public double[] getPoints(Object literal) {
		if(literal == null) {
			return new double[] {missingValue};
		}
		
		long ms = Instant.parse( (CharSequence) literal).toEpochMilli();
		return new double[] {ms};
	}

}
