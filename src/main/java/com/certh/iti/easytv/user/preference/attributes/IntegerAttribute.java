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
	
	@Override
	public String toString() {
		
		String binlables =  "", binsCounts = "", emplyLine = "",upperLine = "", middleLine = "", binId = "";
		for(int i = 0 ; i < binsNum; i++) {
			binId += String.format("|%-5d", i);
			binlables += String.format("|%-5d", binsLable[i]);
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
											+ "%s\n"
											+ "%s\n"
											+ "%s\n",
											upperLine,
											 "Bins histogram",
											upperLine, 
											binId, 
											middleLine, 
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

			//the bin middle value
			int binMidValue =  (int) ((i * binSize * step) + range[0]);

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
