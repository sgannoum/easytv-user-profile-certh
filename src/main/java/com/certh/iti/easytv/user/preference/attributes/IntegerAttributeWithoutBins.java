package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.DummyLocalizable;

public class IntegerAttributeWithoutBins extends NumericAttribute {	
	
	public IntegerAttributeWithoutBins(double[] range) {
		super(range);
	}

	public IntegerAttributeWithoutBins(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
	}
	
	/**
	 * Fill out the bin label with the proper labels
	 */
	@Override
	protected void init() {
		binsCenter = null;
		binsCounter = null;
		binSize = 0;
		binsNum = 0;
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
	
	
	@Override
	public String toString() {
		
		String separtingLine1 = String.format("%76s", " ").replaceAll(" ", "+");

		String attributeProperties = super.toString();
		String statisticalData = String.format("%s\n"+
												 "|%-74s|\n"+
												 "%s\n"+
										 		 "|%-10s|%-10s|%-19s|%-10s|%-10s|%-10s|\n"+
										 		 "%s\n"+
												 "|%-10d|%-10.1f|%-19.1f|%-10.1f|%-10.1f|%-10.1f|\n"+
												 "%s\n\n"
												 
											 	, separtingLine1
											 	, String.format("%26s", " ") + "Statistical data"
											 	, separtingLine1
												, "   Total", "   sum", "Standard deviation", "    Mean",     "    Min",   "    Max"
												, separtingLine1
											    , n,       sum, getStandardDeviation(), getMean(), getMinValue(), getMaxValue() 
											    , separtingLine1);
		

		
		return attributeProperties + statisticalData;
	}
	
	
}
