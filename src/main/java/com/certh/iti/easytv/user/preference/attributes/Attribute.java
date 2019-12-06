package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

import org.apache.commons.math3.exception.OutOfRangeException;

public abstract class Attribute {

	protected static final int MAX_BINS_NUMS = 100;
	
	protected double missingValue = -1.0;
	protected double[] range;
	
	protected String[] binslables = null;
	protected Object[] binsCenter = null;
	protected int[] binsCounter = null;
	protected double step = 1.0;
	protected int binSize = 1;
	protected int remaining = 0;
	protected int binsNum = MAX_BINS_NUMS;
	
	
	public Attribute(double[] range) {
		this.range = range;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		int initBinNum = (int) (valueRange < MAX_BINS_NUMS ? valueRange : MAX_BINS_NUMS);
		if(valueRange > initBinNum) { 
			 remaining = (int) (valueRange % initBinNum);
			 binSize = (int) ((valueRange - remaining)  / initBinNum);
		}
		
		//recalculate the bins number
		this.binsNum = (int) Math.floor(valueRange < MAX_BINS_NUMS ? valueRange : valueRange / binSize);

		//call subclass initialization
		init();
		
	}
	
	public Attribute(double[] range, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		int initBinNum = (int) (valueRange < MAX_BINS_NUMS ? valueRange : MAX_BINS_NUMS);
		if(valueRange > initBinNum) { 
			 remaining = (int) (valueRange % initBinNum);
			 binSize = (int) ((valueRange - remaining)  / initBinNum);
		}
		
		//recalculate the bins number
		this.binsNum = (int) Math.floor(valueRange < MAX_BINS_NUMS ? valueRange : valueRange / binSize);

		//call subclass initialization
		init();
		
	}
	
	public Attribute(double[] range, double step, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;
		this.step = step;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		int initBinNum = (int) (valueRange < MAX_BINS_NUMS ? valueRange : MAX_BINS_NUMS);
		if(valueRange > initBinNum) { 
			 remaining = (int) (valueRange % initBinNum);
			 binSize = (int) ((valueRange - remaining)  / initBinNum);
		}
		
		//recalculate the bins number
		this.binsNum = (int) Math.floor(valueRange < MAX_BINS_NUMS ? valueRange : valueRange / binSize);

		//call subclass initialization
		init();
		
	}
	
	public Attribute(double[] range, double step, int binsNum, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;
		this.step = step;
		this.binsNum = binsNum;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		if(valueRange > binsNum) {
			 remaining = (int) (valueRange % binsNum);
			 binSize = (int) ((valueRange - remaining)  / binsNum);
		}
		
		//call subclass initialization
		init();
		
	}
	
	/**
	 * Enforce implementation in subclasses, 
	 * Fill out the bin labels table
	 */
	protected abstract void init(); 
	
	/**
	 * Get a vector point representation of the given value
	 * @param literal
	 * @return
	 */
	public abstract double[] getPoints(Object literal);
	
		
	public double[] getRange() {
		return range;
	}
	
	public double getOperandMissingValue() {
		return missingValue;
	}
	
	public double[] getMissingPoint() {
		return new double[] {missingValue};
	}
	
	public double getStep() {
		return step;
	}
	
	public double getBinSize() {
		return binSize;
	}
	
	public int getBinNumber() {
		return binsNum;
	}
	
	public int[] getBinsCounter() {
		return binsCounter;
	}
	
	public Object[] getBinsValues() {
		return binsCenter;
	}
	
	public String[] getBinsLabel() {
		return binslables;
	}
	
	/**
	 * Get an integer representation of the given value
	 * 
	 * @return
	 */
	public int code(Object literal) {
		
		if(!Double.class.isInstance(literal))
			throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into Double");
		
		//specify the itemId
		return getBinId((double) literal);
	}
	
	/**
	 * Get the binId that the given number belongs to 
	 * 
	 * @param value
	 * @return
	 */
	public int getBinId(double value) {
		
		if (value % step != 0)
			throw new IllegalArgumentException("The value " + value + " is not compatible with step: " + step);
		
		if(value < range[0] || value > range[1])
			throw new OutOfRangeException(value, range[0], range[1]);
		
		//the value position in the sequence of value ranges
		int binId = 0;
		int position = (int) ((value - range[0]) / step);
		int firstValueRange = remaining * (binSize + 1);
		
		if(position < firstValueRange)
			binId = (int) Math.floor(position / (binSize + 1));
		else 
			binId = (int) Math.floor((position - firstValueRange) / binSize) + remaining;

		//specify the itemId
		return binId;
	}
	
	/**
	 * Get the corresponding dimension value
	 * 
	 * @return
	 */
	public Object decode(int itemId) {
		int binId = itemId;
		
		if (binId >= binsNum || binId < 0)
			throw new IllegalArgumentException("Out of range bin id: " + binId);
						
		return binsCenter[binId];
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
