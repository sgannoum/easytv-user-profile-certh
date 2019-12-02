package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

public abstract class Attribute {

	protected static int attributeCodeIndex = 100;
	protected static final int IncrCodeStep = 100;
	protected int attributeCodeBase;
	
	protected double missingValue = -1.0;
	protected double[] range;
	
	protected Object[] binsLable;
	protected int[] binsCounter;
	protected double step = 1.0;
	protected int binSize = 1;
	protected int binsNum = IncrCodeStep;
	
	public Attribute(double[] range) {
		this.range = range;
		this.attributeCodeBase = attributeCodeIndex;
		attributeCodeIndex += IncrCodeStep;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		int initBinNum = (int) (valueRange < IncrCodeStep ? valueRange : IncrCodeStep);
		
		if(valueRange > initBinNum) 
			 binSize = (int) Math.ceil(valueRange/initBinNum);
		
		this.binsNum = (int) (valueRange < IncrCodeStep ? valueRange : valueRange / binSize);

		init();
	}
	
	public Attribute(double[] range, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;
		this.attributeCodeBase = attributeCodeIndex;
		attributeCodeIndex += IncrCodeStep;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		int initBinNum = (int) (valueRange < IncrCodeStep ? valueRange : IncrCodeStep);
		
		if(valueRange > initBinNum) 
			 binSize = (int) Math.ceil(valueRange/initBinNum);
		
		this.binsNum = (int) (valueRange < IncrCodeStep ? valueRange : valueRange / binSize);

		init();
	}
	
	public Attribute(double[] range, double step, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;
		this.attributeCodeBase = attributeCodeIndex;
		this.step = step;
		attributeCodeIndex += IncrCodeStep;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		int initBinNum = (int) (valueRange < IncrCodeStep ? valueRange : IncrCodeStep);
		
		if(valueRange > initBinNum) 
			 binSize = (int) Math.ceil(valueRange/initBinNum);
		
		this.binsNum = (int) (valueRange < IncrCodeStep ? valueRange : valueRange / binSize);

		init();
	}
	
	public Attribute(double[] range, double step, int binsNum, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;
		this.attributeCodeBase = attributeCodeIndex;
		this.step = step;
		this.binsNum = binsNum;
		attributeCodeIndex += IncrCodeStep;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		if(valueRange > binsNum) 
			binSize = (int) Math.ceil(valueRange/binsNum);
		

		init();
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
	
	public double getStep() {
		return step;
	}
	
	public double getBinSize() {
		return binSize;
	}
	
	public double getBinNumber() {
		return binsNum;
	}
	
	/**
	 * Get a vector point representation of the given value
	 * @param literal
	 * @return
	 */
	public abstract double[] getPoints(Object literal);
	
	
	/**
	 * Enforce implementation in subclasses, 
	 * Fill out the bin labels table
	 */
	protected abstract void init(); 
	
	/**
	 * Get an integer representation of the given value
	 * @return
	 */
	public int code(Object literal) {		
		double value = (double) literal;
		
		//specify the itemId
		return attributeCodeBase + getBinId(value);
	}
	
	/**
	 * Get the binId that the given number belongs to 
	 * 
	 * @param value
	 * @return
	 */
	protected int getBinId(double value) {
		//the value position in the sequence of value ranges
		int position = (int) ((value - range[0]) / step);
		
		int binId = (int) (position / binSize);
		
		//specify the itemId
		return binId;
	}
	
	/**
	 * Get the corresponding dimension value
	 * @return
	 */
	public Object decode(int itemId) {
		int binId = itemId - attributeCodeBase;
		int attributeId = itemId - binId;
		
		if (attributeId != attributeCodeBase)
			throw new IllegalArgumentException("Wrong attribute id: " + attributeCodeBase + " " + attributeId);
		
		if (binId >= binsNum)
			throw new IllegalArgumentException("Out of range bin id: " + binId);
						
		return binsLable[binId];
	}

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
