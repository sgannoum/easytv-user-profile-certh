package com.certh.iti.easytv.user.preference.attributes;

import java.awt.Color;

public class ColorAttribute extends NumericAttribute {

	private NumericAttribute red = new IntegerAttribute(new double[] {0.0, 255.0});
	private NumericAttribute green = new IntegerAttribute(new double[] {0.0, 255.0});
	private NumericAttribute blue =  new IntegerAttribute(new double[] {0.0, 255.0});
	
	
	public ColorAttribute() {
		super(new double[] {0.0, 16777215.0});
	}

	public ColorAttribute(double[] range) {
		super(range);
	}
	
	@Override
	public double[] getMissingPoint() {
		return new double[] {missingValue, missingValue, missingValue};
	}
	
	@Override
	public double[] getPoints(Object literal) {
		if(literal == null) {
			return new double[] {missingValue, missingValue, missingValue};
		}
		
		Color color = Color.decode((String) literal);
		return new double[] {color.getRGB()};
	}
	
	public NumericAttribute[] getDimensions() {
		return new NumericAttribute[] {red, green, blue};
	}
	
	@Override
	public Object handle(Object value) {

		Color color = Color.decode((String) value);
		
		int numericValue = Math.abs(color.getRGB());

		// Increate histogram counts
		Long tmp = frequencyHistogram.get(numericValue);
		if (tmp == null) {
			frequencyHistogram.put((double) numericValue, 1L);
		} else {
			frequencyHistogram.put((double) numericValue, tmp + 1);
		}
		
		//handle red dimension
		red.handle(color.getRed());

		//handle alpha dimension
		green.handle(color.getGreen());
		
		//handle blue dimension
		blue.handle(color.getBlue());

		// Set Min Max vlaue
		setMinMaxValue(numericValue);

		//sum += numericValue;

		n++;
		return value;
	}


}
