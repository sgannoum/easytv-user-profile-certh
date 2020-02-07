package com.certh.iti.easytv.user.preference.attributes;

import java.math.BigDecimal;
import java.util.Random;

import org.apache.commons.math3.exception.OutOfRangeException;

import com.certh.iti.easytv.util.Table;
import com.certh.iti.easytv.util.Table.Position;

public abstract class Attribute {
	
	
	public static class Bin {
		public String label;
		public Object[] range;
		public Object center;
		public Attribute type;
		public int counts;
	}
	
	protected static final int MAX_BINS_NUMS = 10000;
	
	protected double missingValue = -1.0;
	protected double[] range;

	protected Bin[] bins;
	protected double step = 1.0;
	protected int binSize = 1;
	protected int remaining = 0;
	protected int binsNum = MAX_BINS_NUMS;
	
	
	public Attribute(double[] range) {
		this.range = range;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		int initBinNum = (int) (valueRange < MAX_BINS_NUMS ? valueRange : MAX_BINS_NUMS);
		if(valueRange > initBinNum) { 
			 remaining = (int) (valueRange % initBinNum);
			 binSize = (int) ((valueRange - remaining)  / initBinNum);
		}
		
		//recalculate the bins number
		this.binsNum = (int) Math.floor(valueRange < MAX_BINS_NUMS ? valueRange : valueRange / binSize);

		//call subclass initialization
		init();
		
	}
	
	public Attribute(double[] range, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		int initBinNum = (int) (valueRange < MAX_BINS_NUMS ? valueRange : MAX_BINS_NUMS);
		if(valueRange > initBinNum) { 
			 remaining = (int) (valueRange % initBinNum);
			 binSize = (int) ((valueRange - remaining)  / initBinNum);
		}
		
		//recalculate the bins number
		this.binsNum = (int) Math.floor(valueRange < MAX_BINS_NUMS ? valueRange : valueRange / binSize);

		//call subclass initialization
		init();
		
	}
	
	public Attribute(double[] range, double step, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;
		this.step = step;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		int initBinNum = (int) (valueRange < MAX_BINS_NUMS ? valueRange : MAX_BINS_NUMS);
		if(valueRange > initBinNum) { 
			 remaining = (int) (valueRange % initBinNum);
			 binSize = (int) ((valueRange - remaining)  / initBinNum);
		}
		
		//recalculate the bins number
		this.binsNum = (int) Math.floor(valueRange < MAX_BINS_NUMS ? valueRange : valueRange / binSize);

		//call subclass initialization
		init();
		
	}
	
	public Attribute(double[] range, double step, int binsNum, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;
		this.step = step;
		this.binsNum = binsNum;

		double valueRange = ((range[1] - range[0]) / step) + 1;
		if(valueRange > binsNum) {
			 remaining = (int) (valueRange % binsNum);
			 binSize = (int) ((valueRange - remaining)  / binsNum);
		}
		
		//call subclass initialization
		init();
		
	}
	
	/**
	 * Enforce implementation in subclasses, 
	 * Fill out the bin labels table
	 */
	protected abstract void init(); 
	
	/**
	 * Get a vector point representation of the given value
	 * @param literal
	 * @return
	 */
	public abstract double getPoints(Object literal);
	
		
	public double[] getRange() {
		return range;
	}
	
	public double getOperandMissingValue() {
		return missingValue;
	}
	
	public double[] getMissingPoint() {
		return new double[] {missingValue};
	}
	
	public double getStep() {
		return step;
	}
	
	public double getBinSize() {
		return binSize;
	}
	
	public int getBinNumber() {
		return binsNum;
	}
	
	public final Bin[] getBins() {
		return bins;
	}
	
	public String getXMLDataTypeURI() {
		return "http://www.w3.org/2001/XMLSchema#double";
	}
	
	/**
	 * Get an integer representation of the given value
	 * 
	 * @return
	 */
	public abstract int code(Object literal);
	

	/**
	 * Check whether the given object is in the given bin range
	 * 
	 * @param literal
	 * @param binId
	 * @return
	 */
	public abstract boolean isInBinRange(Object literal, int binId);
	
	/**
	 * Get the binId that the given number belongs to 
	 * 
	 * @param value
	 * @return
	 */
	public int getBinId(double value) {
		
	    BigDecimal x = new BigDecimal( String.valueOf(value) );
	    BigDecimal bdVal = x.remainder( new BigDecimal( String.valueOf(step) ) ) ;
		if (bdVal.doubleValue() != 0)
			throw new IllegalArgumentException("The value " + value + " is not compatible with step: " + step);
		
		if(value < range[0] || value > range[1])
			throw new OutOfRangeException(value, range[0], range[1]);
		
		//the value position in the sequence of value ranges
		int binId = 0;
		int position = (int) ((value - range[0]) / step);
		int firstValueRange = remaining * (binSize + 1);
		
		if(position < firstValueRange)
			binId = (int) Math.floor(position / (binSize + 1));
		else 
			binId = (int) Math.floor((position - firstValueRange) / binSize) + remaining;
		
		if(binId < 0 || binId >= bins.length)
			throw new IllegalArgumentException("Out of Range bin id: " + binId+" ["+bins[binId].range[0]+","+bins[binId].range[1]+"]");
		
		//specify the itemId
		return binId;
	}
	
	/**
	 * Get the corresponding dimension value
	 * 
	 * @return
	 */
	public Object decode(int itemId) {
		int binId = itemId;
		
		if (binId >= binsNum || binId < 0)
			throw new IllegalArgumentException("Out of range bin id: " + binId);
						
		return bins[binId].center;
	}

	/**
	 * Generate a random value 
	 * 
	 * @param rand
	 * @return
	 */
	public Object getRandomValue(Random rand) {
		int root = (int) (range[1] - range[0]);
		Object literal = (root / Math.abs(root)) * rand.nextInt(Math.abs(root)) + range[0];
		
		double res = (double) literal;
		if(res < range[0] || res > range[1]) {
			throw new IllegalArgumentException("Value "+res+" out of range ["+range[0]+", "+range[1]+"]");
		}
		
		return literal;
	}
	
	/**
	 * Create on operand instance from the given value
	 * 
	 * @param value
	 * @return
	 */
	public Object handle(Object value) {
		return value;
	}
	
	@Override
	public String toString() {

		Table table = new Table(2, 20);
		Table.Row headerRow = table.createRow(1, Position.CENTER);		
		headerRow.addCell("Attribute properties");
		table.addRow(headerRow);
		table.addRow(new String[] {"Range", "Missing Value"}, Position.CENTER);
		table.addRow(new Object[] {String.format("[%-8.1f, %-8.1f]", range[0], range[1]), missingValue}, Position.CENTER);
		
		if(binsNum > 0 ) return table.toString() + "\n" + getBinsHistogram() + "\n";
		else return table.toString() + "\n" ;
	}
	
	/**
	 * Print in the form of table the bins histogram
	 * 
	 * @return
	 */
	protected String getBinsHistogram() {
		
		//find table columns width
		int maxColumnWdith = 6;
		for(int i = 0 ; i < binsNum; i++) {
			if(maxColumnWdith < bins[i].label.length())
				maxColumnWdith = bins[i].label.length();
		}
		

		Table table = new Table(binsNum + 1, maxColumnWdith);
		Table.Row headerRow = table.createRow(1, Position.CENTER);
		Table.Row binIdRow = table.createRow();
		Table.Row binRangeRow = table.createRow();
		Table.Row binCenterRow = table.createRow();
		Table.Row binCountsRow = table.createRow();
		
		headerRow.addCell("Bins histogram");
		binIdRow.addCell("Id");
		binRangeRow.addCell("Range");
		binCenterRow.addCell("Center");
		binCountsRow.addCell("Counts");
		
		for(int i = 0 ; i < binsNum; i++) {
			
			binIdRow.addCell(i);
			binRangeRow.addCell(bins[i].label);
			binCenterRow.addCell(bins[i].center);
			binCountsRow.addCell(bins[i].counts);
		}
		
		table.addRow(headerRow);
		table.addRow(binIdRow);
		table.addRow(binRangeRow);
		table.addRow(binCenterRow);
		table.addRow(binCountsRow);
		
		return table.toString() +"\n";
	}
}
