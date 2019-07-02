package com.certh.iti.easytv.user.preference.attributes;

public abstract class Attribute {

	protected double missingValue = -1.0;
	protected double[] range;
	
	public Attribute(double[] range) {
		this.range = range;
	}
	
	public Attribute(double[] range, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;
	}
		
	public double[] getRange() {
		return range;
	}
	
	public double getOperandMissingValue() {
		return missingValue;
	}
	
	public double[] getMissingPoint() {
		return new double[] {missingValue};
	}
	
	public abstract double[] getPoints(Object literal);
	
	/**
	 * Create on operand instance from the given value
	 * 
	 * @param value
	 * @return
	 */
	public Object handle(Object value) {
		return value;
	}
	
	public int getDimensionsNumber() {
		return 1;
	}

}
