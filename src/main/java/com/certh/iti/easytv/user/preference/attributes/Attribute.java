package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

import com.certh.iti.easytv.user.preference.attributes.discretization.Discrete;
import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;
import com.certh.iti.easytv.util.Table;
import com.certh.iti.easytv.util.Table.Position;

public abstract class Attribute {
			
	protected double missingValue = -1.0;
	protected double[] range;

	protected Discretization discretization = null;
	
	public Attribute(double[] range) {
		this.range = range;
	}
	
	public Attribute(double[] range, double operandMissingValue) {
		this.range = range;
		this.missingValue = operandMissingValue;
	}
	
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
	
	public int getBinSize(int index) {
		return discretization.getDiscreteSize(index);
	}
	
	public int getBinNumber() {
		return discretization.getBinNumber();
	}
	
	public final Discrete[] getBins() {
		return discretization.getBins();
	}
	
	public abstract String getXMLDataTypeURI();
	/**
	 * Get an integer representation of the given value
	 * 
	 * @return
	 */
	public int code(Object literal) {
		return discretization.code(literal);
	}
	

	/**
	 * Check whether the given object is in the given bin range
	 * 
	 * @param literal
	 * @param binId
	 * @return
	 */
	public boolean isInBinRange(Object literal, int binId) {
		return discretization.inRange(literal, binId);
	}
	
	
	/**
	 * Get the binId that the given number belongs to 
	 * 
	 * @param value
	 * @return
	 */
	public int getBinId(boolean value) {
		return discretization.getBinId(value);
	}
	
	/**
	 * Get the corresponding dimension value
	 * 
	 * @return
	 */
	public Object decode(int itemId) {
		return discretization.decode(itemId);
	}
	
	/**
	 * Get the corresponding dimension value
	 * 
	 * @return
	 */
	public Discretization getDiscretization() {
		return discretization;
	}

	/**
	 * Generate a random value 
	 * 
	 * @param rand
	 * @return
	 */
	public Object getRandomValue(Random rand) {
		double boundaries = 1 + (range[1] - range[0]);
		double ran = (double) rand.nextInt((int) Math.abs(boundaries));
		Object literal = range[0] + Math.rint(ran);

		//check range boundaries
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
		
		if(discretization.getBinNumber() > 0 ) return table.toString() + "\r\n" + getBinsHistogram() + "\r\n";
		else return table.toString() + "\r\n" ;
	}
	
	/**
	 * Print in the form of table the bins histogram
	 * 
	 * @return
	 */
	protected String getBinsHistogram() {
		return discretization.getBinsHistogram();
	}

}
