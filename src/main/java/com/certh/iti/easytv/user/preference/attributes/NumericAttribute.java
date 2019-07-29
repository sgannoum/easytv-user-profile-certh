package com.certh.iti.easytv.user.preference.attributes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class NumericAttribute extends Attribute implements INumeric {
	
	protected long n = 0;
	protected double sum = 0.0;
	protected double standard_deviation = -1.0;
	protected double Maxvalue = Double.MIN_VALUE;
	protected double Minvalue = Double.MAX_VALUE;
	protected Map<Double, Long> frequencyHistogram = new HashMap<Double, Long>();

	public NumericAttribute(double[] range) {
		super(range);
	}
	
	public NumericAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
	}
	
	protected void setMinMaxValue(double value) {
		
		if(value > Maxvalue ) {
			Maxvalue = value;
		}
		
		if(value < Minvalue) {
			Minvalue = value;
		}
	}
	
	public double getMaxValue() {
		return Maxvalue;
	}
	
	public double getMinValue() {
		return Minvalue;
	}
	
	public long getCounts() {
		return n;
	}
	
	public double getSum() {
		return sum;
	}
	
	public double getMean() {
		return sum/n;
	}
	
	public double[][] getEntriesCounts() {
		int size = frequencyHistogram.keySet().size();
		int index = 0;
		double[][] entriesCounts = new double[size][2];
		
		Iterator<Entry<Double, Long>> iter = frequencyHistogram.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<Double, Long> entry = iter.next();
			entriesCounts[index][0] = entry.getKey().doubleValue();
			entriesCounts[index][1] = entry.getValue().doubleValue();
			index++;
		}

		return entriesCounts;
	}
	
	public double getStandardDeviation() {
		if(standard_deviation == -1.0) {
			
			double var = 0.0;
			double mean = sum/n;
			
			Iterator<Entry<Double, Long>> iter = frequencyHistogram.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<Double, Long> entry = iter.next();
				var += entry.getValue() * Math.pow(entry.getKey() - mean, 2);
			}
			
			standard_deviation = Math.sqrt(var/n);
		}
		
		return standard_deviation; 
	}

}
