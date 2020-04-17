package com.certh.iti.easytv.user.preference.attributes;

import java.math.BigDecimal;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.DummyLocalizable;

import com.certh.iti.easytv.user.preference.attributes.discretization.DoubleDiscretization;

public class DoubleAttribute extends NumericAttribute {
	
	
	public DoubleAttribute(double[] range) {
		super(range);
		
		discretization = new DoubleDiscretization(range);
	}

	public DoubleAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
		
		discretization = new DoubleDiscretization(range);
	}
	
	public DoubleAttribute(double[] range, double step, double operandMissingValue) {
		super(range, step, operandMissingValue);
		
		discretization = new DoubleDiscretization(range, step);
	}
	
	public DoubleAttribute(double[] range, double step, int binsNum, double operandMissingValue) {
		super(range, step, operandMissingValue);
		
		discretization = new DoubleDiscretization(range, step, binsNum);

	}

	@Override
	public double getPoints(Object literal) {
		if (literal == null) {
			return missingValue;
		}

		double value = (double) literal;
		return value;
	}
	
	@Override
	public String getXMLDataTypeURI() {
		return "http://www.w3.org/2001/XMLSchema#double";
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
		
		
	    BigDecimal x = new BigDecimal( String.valueOf(numericValue) );
	    BigDecimal bdVal = x.remainder( new BigDecimal( String.valueOf(step) ) ) ;
		if(bdVal.doubleValue() != 0.0) 
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
		
		return numericValue;
	}

}
