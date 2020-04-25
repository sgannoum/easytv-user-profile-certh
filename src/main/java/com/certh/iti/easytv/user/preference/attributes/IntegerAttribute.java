package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.DummyLocalizable;

import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;
import com.certh.iti.easytv.user.preference.attributes.discretization.IntegerDiscretization;
import com.certh.iti.easytv.util.Table;
import com.certh.iti.easytv.util.Table.Position;

public class IntegerAttribute extends NumericAttribute {	
		
	protected int binsNum;
	protected Integer[][] discretes = null;
	protected TreeMap<Integer, Long> frequencyHistogram = new TreeMap<Integer, Long>();
	
	public IntegerAttribute(double[] range) {
		super(range);
		this.binsNum = -1;
	}

	public IntegerAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
		this.binsNum = -1;
	}
	
	public IntegerAttribute(double[] range, double step, double operandMissingValue) {
		super(range, step, operandMissingValue);
		this.binsNum = -1;
	}
	
	public IntegerAttribute(double[] range, double step, int binsNum, double operandMissingValue) {
		super(range, step, operandMissingValue);
		this.binsNum = binsNum;
	}
	
	public IntegerAttribute(double[] range, double step, double operandMissingValue, Discretization discretization) {
		super(range, step, operandMissingValue);
		this.discretization = discretization;
	}
	
	public IntegerAttribute(double[] range, double step, Integer[][] discretes) {
		super(range, step);
		this.binsNum = -1;
		this.discretes = discretes;
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

		// Increase histogram counts
		Long tmp = (tmp = frequencyHistogram.get(numericValue)) == null ? 1L : (tmp + 1L);
		frequencyHistogram.put(numericValue, tmp);
		
		//Increment the number of occurrences 
		if(discretization != null)
			discretization.handle(numericValue);

		// Set Min Max vlaue
		setMinMaxValue(numericValue);

		sum += numericValue;

		n++;
		
		return value;
	}
	
	@Override
	public double getStandardDeviation() {
		if(n == 0)
			return 0.0;
		
		if(standard_deviation == -1.0) {
			
			double var = 0.0;
			double mean = sum/n;
			
			for(Entry<Integer, Long> entry : frequencyHistogram.entrySet()) 
				var += entry.getValue() * Math.pow(entry.getKey() - mean, 2);
			
			standard_deviation = Math.sqrt(var/n);
		}
		
		return standard_deviation; 
	}
	
	@Override
	protected String getValueshistogram() {
		
		//Histogram table
		Table histTable = new Table(2, 11);
		Table.Row headerRow = histTable.createRow(1, Position.CENTER);		
		headerRow.addCell("Values histogram");
		histTable.addRow(headerRow);
		histTable.addRow(new Object[] {" Value", " Frequency"}, Position.CENTER);			
		for(Entry<Integer, Long> entry : frequencyHistogram.entrySet()) 
			histTable.addRow(new Object[] {entry.getKey().intValue(), entry.getValue().intValue()});
		
		return histTable.toString() +" \r\n";
	}
	
	@Override
	public Discretization getDiscretization() {
		if(discretization == null) {
			if(frequencyHistogram.isEmpty()) return null;
			else if(binsNum != -1)
				return new IntegerDiscretization(range, step, binsNum, frequencyHistogram);
			else if(discretes != null)
				return new IntegerDiscretization(range, step, discretes, frequencyHistogram);
			else
				return new IntegerDiscretization(range, step, frequencyHistogram);
		}
		else 
			return discretization;
	}
	
}
