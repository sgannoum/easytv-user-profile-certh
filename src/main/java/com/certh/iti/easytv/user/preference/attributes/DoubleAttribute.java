package com.certh.iti.easytv.user.preference.attributes;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.DummyLocalizable;

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
		bins = new Bin[binsNum];
		
		int size = binSize + 1;
		double initialRange = 0;
		
		//second section with bins that has size of binSize 
		for(int i = 0; i < binsNum; i++) {
			bins[i] = new Bin();

			if(i == remaining) {
				size = binSize;
				initialRange = remaining * step ;
			}
			
			//the bin middle value
			double firstValue = initialRange + (i * size * step) + range[0];
			double lastValue =  initialRange + (((i + 1) * size * step) + range[0]) - step;
			double midValue = 0;
			
			//take the middle value
			if(binSize % 2 == 0) {
				midValue += firstValue + (size / 2) * step;
			} else {
				midValue += firstValue + ((size - 1) / 2) * step;
			}
			
			bins[i].center = midValue;
			bins[i].label = firstValue == lastValue ? String.valueOf(firstValue) : String.valueOf(firstValue) + ", " + String.valueOf(lastValue) ;
			bins[i].range = new Double[] {firstValue, lastValue};
			bins[i].type = this;
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
		} else if(Double.class.isInstance(value)) { 
			numericValue = Double.class.cast(value);
		} else
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " can't not be converted into Double");
			
		
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
		bins[bindId].counts++;
		
		// Set Min Max vlaue
		setMinMaxValue(numericValue);

		sum += numericValue;

		n++;
		
		return numericValue;
	}
	
	/**
	 * Get an integer representation of the given value
	 * 
	 * @return
	 */
	public int code(Object literal) {
		
		if(!Double.class.isInstance(literal))
			throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into Double");
		
		int binId = getBinId((double) literal);
		
		//check that the given value belongs to the bin range
		if(!isInBinRange(literal, binId)) 
			throw new IllegalArgumentException("Value " + literal + " is not in bin range ["+bins[binId].range[0]+","+bins[binId].range[1]+"]");
		
		//specify the itemId
		return binId;
	}
	
	@Override
	public boolean isInBinRange(Object literal, int binId) {
		
		if(!Double.class.isInstance(literal))
			throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into Double");
		
		if(binId < 0 || binId >= bins.length)
			throw new IllegalArgumentException("Out of Range bin id: " + binId+" ["+0+","+bins.length+"]");
		
		double value = (double) literal;
		Bin bin = bins[binId];
		
		return (value >= (double) bin.range[0] && value <= (double) bin.range[1]);
	}

}
