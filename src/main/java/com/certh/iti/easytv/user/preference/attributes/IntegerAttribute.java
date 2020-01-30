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
			int firstValue = (int) (initialRange + (i * size * step) + range[0]);
			int lastValue =  (int) (initialRange + (((i + 1) * size * step) + range[0]) - step);
			int midValue = 0;
			
			//take the middle value
			if(binSize % 2 == 0) {
				midValue += firstValue + (size / 2) * step;
			} else {
				midValue += firstValue + ((size - 1) / 2) * step;
			}
			
			bins[i].center = midValue;
			bins[i].label = firstValue == lastValue ? String.valueOf(firstValue) : String.valueOf(firstValue) + ", " + String.valueOf(lastValue) ;
			bins[i].range = new Integer[] {firstValue, lastValue};
			bins[i].type = this;
		}
	}
	
	@Override
	public Object getRandomValue(Random rand) {
		Double value = (Double) super.getRandomValue(rand);
		return value.intValue();
	}
	
	@Override
	public String getXMLDataTypeURI() {
		return "http://www.w3.org/2001/XMLSchema#integer";
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
		
		int numericValue ;
		
		if(Integer.class.isInstance(value)) {
			numericValue = Integer.class.cast(value);
		} else
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " can't not be converted into Integer");
			
		
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
		
		return value;
	}
	
	@Override
	public int code(Object literal) {
		
		if(!Integer.class.isInstance(literal))
			throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into integer");
		
		int binId = getBinId(((int) literal) * 1.0);

		//check that the given value belongs to the bin range
		if(!isInBinRange(literal, binId)) 
			throw new IllegalArgumentException("Value " + literal + " is not in bin range ["+bins[binId].range[0]+","+bins[binId].range[1]+"]");
		
		return binId;
	}
	
	@Override
	public boolean isInBinRange(Object literal, int binId) {
		
		if(!Integer.class.isInstance(literal))
			throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into integer");
		
		if(binId < 0 || binId >= bins.length)
			throw new IllegalArgumentException("Out of Range bin id: " + binId+" ["+0+","+bins.length+"]");
		
		int value = (int) literal;
		Bin bin = bins[binId];
		
		return (value >= (int) bin.range[0] && value <= (int) bin.range[1]);
	}

}
