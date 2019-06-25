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


}
