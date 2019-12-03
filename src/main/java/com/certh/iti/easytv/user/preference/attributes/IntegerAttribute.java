package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.DummyLocalizable;

public class IntegerAttribute extends NumericAttribute {	
	
	public IntegerAttribute(double[] range) {
		super(range);
	}

	public IntegerAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
	}
	
	public IntegerAttribute(double[] range, double step, double operandMissingValue) {
		super(range, step, operandMissingValue);
	}
	
	public IntegerAttribute(double[] range, double step, int binsNum, double operandMissingValue) {
		super(range, step, binsNum, operandMissingValue);
	}
	
	/**
	 * Fill out the bin label with the proper labels
	 */
	@Override
	protected void init() {
				
		binslables = new String[binsNum];
		binsCounter = new int[binsNum];
		binsCenter = new Object[binsNum];
				
		for(int i = 0; i < binsNum; i++) {

			//the bin middle value
			int firstValue =  (int) ((i * binSize * step) + range[0]);
			int lastValue =  (int) ((((i + 1) * binSize * step) + range[0]) - step);
			if(lastValue > range[1]) lastValue = (int) range[1];
			
			int midValue = 0;
			
			//take the middle value
			if(binSize % 2 == 0) {
				midValue += firstValue + (binSize / 2) * step;
			} else {
				midValue += firstValue + ((binSize - 1) / 2) * step;
			}
			
			binsCenter[i] = midValue;
			binslables[i] = String.valueOf(firstValue) + ", " + String.valueOf(lastValue) ;
		}
	}
	
	@Override
	public Object getRandomValue(Random rand) {
		Double value = (Double) super.getRandomValue(rand);
		return value.intValue();
	}

	@Override
	public double[] getPoints(Object literal) {
		if (literal == null) {
			return new double[] { missingValue };
		}

		int value = (int) literal;
		return new double[] { value };
	}

	@Override
	public Object handle(Object value) {

		int numericValue = Integer.class.cast(value);
		
		if(numericValue < range[0] || numericValue > range[1])
			throw new OutOfRangeException(numericValue, range[0], range[1]);
		
		if(numericValue % step != 0) 
			throw new OutOfRangeException(new DummyLocalizable("Non compatible with step: " + step), numericValue, range[0], range[1]);

		// Increate histogram counts
		Double key = new Double(numericValue);
		Long tmp = (tmp = frequencyHistogram.get(key)) == null ? 1L : (tmp + 1L);
		frequencyHistogram.put(key, tmp);
		
		//Increment the number of occurrences 
		int bindId = getBinId(numericValue);
		binsCounter[bindId]++;

		// Set Min Max vlaue
		setMinMaxValue(numericValue);

		sum += numericValue;

		n++;
		
		return value;
	}
	
	@Override
	public int code(Object literal) {		
		//convert int to double
		double value = ((int) literal) * 1.0;

		return super.code(value);
	}

}
