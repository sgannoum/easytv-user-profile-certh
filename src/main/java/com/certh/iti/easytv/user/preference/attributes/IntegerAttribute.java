package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.DummyLocalizable;

import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;
import com.certh.iti.easytv.user.preference.attributes.discretization.IntegerDiscretization;

public class IntegerAttribute extends NumericAttribute {	
		
	public IntegerAttribute(double[] range) {
		super(range);
		
		discretization = new IntegerDiscretization(range);
	}

	public IntegerAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
		
		discretization = new IntegerDiscretization(range);
	}
	
	public IntegerAttribute(double[] range, double step, double operandMissingValue) {
		super(range, step, operandMissingValue);
		
		discretization = new IntegerDiscretization(range, step);
	}
	
	public IntegerAttribute(double[] range, double step, int binsNum, double operandMissingValue) {
		super(range, step, operandMissingValue);
		
		discretization = new IntegerDiscretization(range, step, binsNum);
	}
	
	public IntegerAttribute(double[] range, double step, double operandMissingValue, Discretization discretization) {
		super(range, step, operandMissingValue);
		
		this.discretization = discretization;
	}
	
	public IntegerAttribute(double[] range, double step, Integer[][] discretes) {
		super(range, step);
		
		this.discretization = new IntegerDiscretization(range, step, discretes);
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
	public double getPoints(Object literal) {
		if (literal == null) {
			return missingValue;
		}

		int value = (int) literal;
		return value;
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
		discretization.handle(numericValue);

		// Set Min Max vlaue
		setMinMaxValue(numericValue);

		sum += numericValue;

		n++;
		
		return value;
	}
	

}
