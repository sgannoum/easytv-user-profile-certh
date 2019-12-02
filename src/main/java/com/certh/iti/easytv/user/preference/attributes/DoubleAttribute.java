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
	
	
	@Override
	public String toString() {
		
		String binlables =  "", binsCounts = "", emplyLine = "", upperLine = "", middleLine = "";
		
		for(int i = 0 ; i < binsCounter.length; i++) {
			binlables += String.format("|%-5.1f", binsLable[i]);
			binsCounts += String.format("|%-5d", binsCounter[i]);
		}
		
		binlables += "|";
		binsCounts += "|";
		
		emplyLine = String.format("%"+binlables.length()+"s", " ");
		upperLine = emplyLine.replaceAll(" ", "+");
		middleLine = emplyLine.replaceAll(" ", "-");
		
		return super.toString() + String.format("%s\n"
				+ "|%-"+(upperLine.length() - 2)+"s|\n"
				+ "%s\n"
				+ "%s\n"
				+ "%s\n"
				+ "%s\n"
				+ "%s\n",
				upperLine,
				"Bins histogram",
				upperLine, 
				binlables, 
				middleLine, 
				binsCounts, 
				upperLine);
	}
	
	/**
	 * Fill out the bin label with the proper labels
	 */
	protected void init() {
				
		binsCounter = new int[binsNum];
		binsLable = new Object[binsNum];
		
		for(int i = 0; i < binsNum; i++) {

			//the bin midell value
			double binMidValue =  (i * binSize * step) + range[0];

			//take the middle value
			if(binSize % 2 == 0) {
				binMidValue += binSize / 2;
			} else {
				binMidValue += (binSize - 1) / 2;
			}
			
			binsLable[i] = binMidValue;
		}
	}

}
