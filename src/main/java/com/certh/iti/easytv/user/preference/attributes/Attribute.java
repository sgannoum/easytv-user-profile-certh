package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

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
	 * Generate a random value 
	 * 
	 * @param rand
	 * @return
	 */
	public Object getRandomValue(Random rand) {
		int root = (int) (range[1] - range[0]);
		Object literal = (root / Math.abs(root)) * rand.nextInt(Math.abs(root)) + range[0];
		
		double res = (double) literal;
		if(res < range[0] || res > range[1]) {
			throw new IllegalArgumentException("Value "+res+" out of range ["+range[0]+", "+range[1]+"]");
		}
		
		return literal;
	}
	
	/**
	 * Create on operand instance from the given value
	 * 
	 * @param value
	 * @return
	 */
	public Object handle(Object value) {
		return value;
	}
}
