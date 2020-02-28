package com.certh.iti.easytv.user.preference.attributes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.certh.iti.easytv.util.Table;
import com.certh.iti.easytv.util.Table.Position;

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
	
	public NumericAttribute(double[] range, double step, double operandMissingValue) {
		super(range, step, operandMissingValue);
	}
	
	public NumericAttribute(double[] range, double step, int binsNum, double operandMissingValue) {
		super(range, step, binsNum, operandMissingValue);
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
		distTable.addRow(new Object[] {"Bins number", "Bin Size", "Remaining", "Step"}, Position.CENTER);
		distTable.addRow(new Object[] {binsNum, binSize, remaining, step});
		
		
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
