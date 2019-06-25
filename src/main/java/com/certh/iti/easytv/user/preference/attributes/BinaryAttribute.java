package com.certh.iti.easytv.user.preference.attributes;

public abstract class BinaryAttribute extends Attribute {

	public BinaryAttribute(double[] range) {
		super(range);
	}
	
	public BinaryAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
	}

}
