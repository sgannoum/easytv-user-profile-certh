package com.certh.iti.easytv.user.preference.attributes;

import java.awt.Color;

public class ColorAttribute extends NumericAttribute {

	public ColorAttribute() {
		super(new double[] {0.0, 12777215.0});
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
		return new double[] {color.getRed(), color.getGreen(), color.getBlue()};
	}
	
	@Override
	public Object handle(Object value) {

		Color color = Color.decode((String) value);
		
		int numericValue = Math.abs(color.getRGB());

/*		// Increate histogram counts
		Long tmp = frequencyHistogram.get(numericValue);
		if (tmp == null) {
			frequencyHistogram.put((double) numericValue, 1L);
		} else {
			frequencyHistogram.put((double) numericValue, tmp + 1);
		}
*/

		// Set Min Max vlaue
		setMinMaxValue(numericValue);

		//sum += numericValue;

		n++;
		return value;
	}


}
