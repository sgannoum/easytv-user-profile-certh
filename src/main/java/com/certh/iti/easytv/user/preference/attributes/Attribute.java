package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

public abstract class Attribute {

	protected static int attributeCodeIndex = 100;
	protected static final int IncrCodeStep = 100;
	protected int attributeCodeBase;
	
	protected double missingValue = -1.0;
	protected double[] range;
	
	public Attribute(double[] range) {
		this.range = range;
		this.attributeCodeBase = attributeCodeIndex;
		
		attributeCodeIndex += IncrCodeStep;
	}
	
	public Attribute(double[] range, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;
		this.attributeCodeBase = attributeCodeIndex;
		
		attributeCodeIndex += IncrCodeStep;
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
	
	public int getAttributeCodeBase() {
		return attributeCodeBase;
	}
	
	/**
	 * Get a vector point representation of the given value
	 * @param literal
	 * @return
	 */
	public abstract double[] getPoints(Object literal);
	
	/**
	 * Get an integer representation of the given value
	 * @return
	 */
	public abstract int code(Object literal);
	
	/**
	 * Get the corresponding dimension value
	 * @return
	 */
	public abstract Object decode(int literal);

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
	
	@Override
	public String toString() {
		String separtingLine = String.format("%43s", " ").replaceAll(" ", "+");
		
		return String.format("%s\n" +
							 "|%-41s|\n" +
							 "%s\n" +
							 "|%-20s|%-20s|\n"+
							 "%s\n" +
							 "|[%-8.1f, %-8.1f]|%-20.1f|\n" +
							 "%s\n\n" 
							 , 
							 separtingLine,
							 String.format("%10s", " ") + "Atrribute properties", 
							 separtingLine,
							 String.format("%8s", " ") + "Range", String.format("%4s", " ") + "Missing Value", 
							 separtingLine,
							 range[0], range[1], missingValue,
							 separtingLine);
	}
}
