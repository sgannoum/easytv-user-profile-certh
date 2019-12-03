package com.certh.iti.easytv.user.preference.attributes;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.DummyLocalizable;
import org.apache.commons.math3.exception.util.Localizable;

public class DoubleAttribute extends NumericAttribute {
	
	public DoubleAttribute(double[] range) {
		super(range);
	}

	public DoubleAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
	}
	
	public DoubleAttribute(double[] range, double step, double operandMissingValue) {
		super(range, step, operandMissingValue);
	}
	
	public DoubleAttribute(double[] range, double step, int binsNum, double operandMissingValue) {
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
			double firstValue =  (i * binSize * step) + range[0];
			double lastValue =  (((i + 1) * binSize * step) + range[0]) - step;
			double midValue = 0.0;
			
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
	public double[] getPoints(Object literal) {
		if (literal == null) {
			return new double[] { missingValue };
		}

		double value = (double) literal;
		return new double[] { value };
	}

	@Override
	public Object handle(Object value) {

		double numericValue;
		
		if(Integer.class.isInstance(value)) {
			numericValue = Integer.class.cast(value);
		} else 
			numericValue = Double.class.cast(value);
		
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
		
		return numericValue;
	}

}
