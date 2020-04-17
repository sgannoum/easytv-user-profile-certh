package com.certh.iti.easytv.user.preference.attributes;

import java.awt.Color;
import java.util.Random;

import com.certh.iti.easytv.user.preference.attributes.discretization.NoDiscretization;

public class ColorAttribute extends IntegerAttribute {
	
	private NumericAttribute red = new IntegerAttribute(new double[] {0.0, 255.0}, 1, 0, new NoDiscretization());
	private NumericAttribute green = new IntegerAttribute(new double[] {0.0, 255.0}, 1, 0, new NoDiscretization());
	private NumericAttribute blue =  new IntegerAttribute(new double[] {0.0, 255.0}, 1, 0, new NoDiscretization());
	
	private static int MAX_BIN_SIZE = 100;
	
	public ColorAttribute() {
		super(new double[] {0X000000, 0Xffffff}, 1.0, MAX_BIN_SIZE, 0);
	}
	
	public ColorAttribute(double step, int binNum) {
		super(new double[] {0X000000, 0Xffffff}, step, binNum, 0);
	}

	public ColorAttribute(double[] range) {
		super(range, 1.0, MAX_BIN_SIZE, 0);
	}
	
	public ColorAttribute(double[] range, double step) {
		super(range, step, MAX_BIN_SIZE);
	}
	
	public ColorAttribute(double[] range, double step, int binNum) {
		super(range, step, binNum, 0);
	}
	
	public ColorAttribute(double[] range, double step, Integer[][] discretes) {
		super(range, step, discretes);
	}
	
	public ColorAttribute(Integer[][] discretes) {
		super(new double[] {0X000000, 0Xffffff}, 1.0, discretes);
	}
	
	@Override
	public double[] getMissingPoint() {
		return new double[] {missingValue, missingValue, missingValue};
	}
	
	@Override
	public Object getRandomValue(Random rand) {
		return String.format("#%02X%02X%02X", rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
	}
	
	@Override
	public double getPoints(Object literal) {
		if(literal == null) 
			return missingValue;
		
		Color color = Color.decode((String) literal);
		return color.getRGB();
	}
	
	public NumericAttribute[] getDimensions() {
		return new NumericAttribute[] {red, green, blue};
	}
	
	@Override
	public Object handle(Object value) {

		Color color = Color.decode((String) value);
		
		int numericValue = color.getRGB() & 0X00ffff;

		// Increase histogram counts
		Double key = new Double(numericValue);
		Long tmp = (tmp = frequencyHistogram.get(key)) == null ? 1L : (tmp + 1L);
		frequencyHistogram.put(key, tmp);
		
		//handle red dimension
		red.handle(color.getRed());

		//handle alpha dimension
		green.handle(color.getGreen());
		
		//handle blue dimension
		blue.handle(color.getBlue());

		// Set Min Max vlaue
		setMinMaxValue(numericValue);
		
		//Increment the number of occurrences 
		discretization.handle(numericValue);

		//sum += numericValue;
		n++;
		
		return value;
	}
	
	@Override
	public int code(Object literal) {	
		
		if(!String.class.isInstance(literal))
			throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into Color");
		
		Color color = Color.decode((String) literal);
		
		//remove alpha 8 bits value
		return discretization.getBinId(color.getRGB() & 0x00ffffff);
	}

	@Override 
	public Object decode(int itemId) {
		return "#"+Integer.toHexString((int) super.decode(itemId));
	}

	@Override
	protected String getValueshistogram() {
		return "";
	}
}
