package com.certh.iti.easytv.user.preference.operand;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;


public class NumericLiteral  extends OperandLiteral {
	private double value;
	private long n = 0;
	private double sum = 0.0;
	private double standard_deviation = -1.0;
	private Map<Double, Long> frequencyHistogram = new HashMap<Double, Long>();
	private double Maxvalue = Double.MIN_VALUE;
	private double Minvalue = Double.MAX_VALUE;
	
	public NumericLiteral(Object literal, double[] range) {
		super(literal, Type.Numeric, range);
		
		if(Double.class.isInstance(literal)) {
			value = Double.class.cast(literal);
		} else {
			value = Integer.class.cast(literal);
		}

		checkValue();
		setMinMaxValue(value);
	}
	
	public NumericLiteral(double literal, double[] range) {
		super(literal, Type.Numeric, range);
		value = literal;
		checkValue();
	}
	
	public NumericLiteral(long literal, double[] range) {
		super(literal, Type.Numeric, range);
		value = literal * 1.0;
		checkValue();
	}
	
	public NumericLiteral(int literal, double[] range) {
		super(literal, Type.Numeric, range);
		value = literal * 1.0;
		checkValue();
	}

	protected void checkValue() {
		if(value < range[0] || value > range[1]) {
			throw new IllegalArgumentException("Value out of range: "+ value);
		}
	}
	
	private void setMinMaxValue(double value) {
		
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
	
	@Override
	public String toString() {
		return String.valueOf(literal);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!NumericLiteral.class.isInstance(obj)) return false;
		NumericLiteral other = (NumericLiteral) obj;
		
		return value == other.value;
	}
	
	public double[] getPoint() {
		return new double[] {value};
	}

	@Override
	public OperandLiteral clone(Object value) {
		try {			
			//Set the min max value from all cloned attributes
			NumericLiteral res = new NumericLiteral(value, range);
			
			Long tmp = frequencyHistogram.get(res.value);
			if( tmp == null) {
				frequencyHistogram.put(res.value, 1L);
			} else {
				frequencyHistogram.put(res.value, tmp + 1);
			}
			
			setMinMaxValue(res.value);
			sum += res.value;
			n++;
			return res;
		} catch (JSONException e) {}
		
		return null;
	}

}