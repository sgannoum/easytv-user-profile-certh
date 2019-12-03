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
		
		String histogram = "";
		double[][] entriesCounts = getEntriesCounts();
		for(int i = 0 ; i < entriesCounts.length; i++)
			histogram += String.format("|%-11.1f|%-11d|\n", entriesCounts[i][0], (int) entriesCounts[i][1]);
		

		String separtingLine1 = String.format("%76s", " ").replaceAll(" ", "+");
		String separtingLine2 = String.format("%25s", " ").replaceAll(" ", "+");
		String separtingLine3 = String.format("%35s", " ").replaceAll(" ", "+");
		
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
		
		String histogramValues = String.format("%s\n"+
												 "|%-23s|\n"+
												 "%s\n"+
												 "|%-11s|%-11s| \n"+
												 "%s\n"+
												 "%s" + 
												 "%s\n\n"
												
											    , separtingLine2
											    , "  Values histogram"
											    , separtingLine2
											    , " Value", " Frequency"
											    , separtingLine2
											    , histogram
											    , separtingLine2);
		
		String discretizationProperties = String.format( "%s\n" + 
														 "|%-33s|\n"+
														 "%s\n" + 
														 "|%-10s|%-10s|%-10s|\n"+
														 "%s\n"+
														 "|%-11d|%-10d|%-10.1f|\n"+
														 "%s\n\n"
													    
													    , separtingLine3
													    , "    Discretization properties"
													    , separtingLine3
													    , "Bins number", " Bin Size", "   Step"
													    , separtingLine3
													    , binsNum, binSize, step
													    , separtingLine3);

		
		return attributeProperties + statisticalData + discretizationProperties + histogramValues;
	}
	
}
