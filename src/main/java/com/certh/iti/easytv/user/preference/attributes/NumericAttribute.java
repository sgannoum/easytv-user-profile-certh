package com.certh.iti.easytv.user.preference.attributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.DummyLocalizable;

import com.certh.iti.easytv.util.Table;
import com.certh.iti.easytv.util.Table.Position;

public abstract class NumericAttribute extends Attribute implements INumeric {
	
	protected long n = 0;
	protected double sum = 0.0;
	protected double standard_deviation = -1.0;
	protected double Maxvalue = Double.MIN_VALUE;
	protected double Minvalue = Double.MAX_VALUE;
	
	protected double step = 1.0;
	
	protected Map<Double, Long> frequencyHistogram = new HashMap<Double, Long>();

	public NumericAttribute(double[] range) {
		super(range);
	}
	
	public NumericAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
	}
	
	public NumericAttribute(double[] range, double step, double operandMissingValue) {
		super(range, operandMissingValue);
		this.step = step;
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
		return Maxvalue != Minvalue && Maxvalue != Double.MIN_VALUE ? Maxvalue : range[1];
	}
	
	public double getMinValue() {
		return Maxvalue != Minvalue && Minvalue != Double.MAX_VALUE ? Minvalue : range[0];
	}
	
	public long getCounts() {
		return n;
	}
	
	public double getSum() {
		return sum;
	}
	
	public double getMean() {
		return n!= 0 ? sum/n : 0;
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
		if(n == 0)
			return 0.0;
		
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
	
	/**
	 * Generate a random value 
	 * 
	 * @param rand
	 * @return
	 */
	@Override
	public Object getRandomValue(Random rand) {
		double boundaries = 1 + (range[1] - range[0]) / step;
		double ran = (double) rand.nextInt((int) Math.abs(boundaries));
		Object literal = range[0] + Math.rint(step * ran);

		//check range boundaries
		double res = (double) literal;
		if(res < range[0] || res > range[1]) {
			throw new IllegalArgumentException("Value "+res+" out of range ["+range[0]+", "+range[1]+"]");
		}
		
		//check step boundaries validity
	    BigDecimal x = new BigDecimal( String.valueOf(res) );
	    BigDecimal bdVal = x.remainder( new BigDecimal( String.valueOf(step) )) ;
		if(bdVal.doubleValue() != 0.0) 
			throw new OutOfRangeException(new DummyLocalizable("Non compatible with step: " + step), res, range[0], range[1]);
		
		return literal;
	}
	
	@Override
	public String toString() {

		
		//Statistical table
		Table statTable = new Table(6, 10);
		Table.Row headerRow = statTable.createRow(1, Position.CENTER);		
		headerRow.addCell("Statistical data");
		statTable.addRow(headerRow);
		statTable.addRow(new Object[] {"Total", "sum", "Stand dev", "Mean",  "Min", "Max"}, Position.CENTER);
		statTable.addRow(new Object[] {n, sum, getStandardDeviation(), getMean(), getMinValue(), getMaxValue() });	
		
		
		//Histogram table
		Table distTable = new Table(4, 11);
		headerRow = distTable.createRow(1, Position.CENTER);		
		headerRow.addCell("Discretization properties");
		distTable.addRow(headerRow);
		distTable.addRow(new Object[] {"Bins number", "Bin Size", "Step"}, Position.CENTER);
		distTable.addRow(new Object[] {discretization.getBinNumber(), discretization.getDiscreteSize(0), step});
		
		
		return super.toString() + statTable.toString()+ "\r\n" + distTable.toString() + "\r\n" + getValueshistogram();
	}
	
	protected String getValueshistogram() {
		
		//Histogram table
		Table histTable = new Table(2, 11);
		Table.Row headerRow = histTable.createRow(1, Position.CENTER);		
		headerRow.addCell("Values histogram");
		histTable.addRow(headerRow);
		histTable.addRow(new Object[] {" Value", " Frequency"}, Position.CENTER);
		double[][] entriesCounts = getEntriesCounts();
		for(int i = 0 ; i < entriesCounts.length; i++)
			histTable.addRow(new Object[] {entriesCounts[i][0], (int) entriesCounts[i][1]});
		
		return histTable.toString() +" \r\n";
	}
}
