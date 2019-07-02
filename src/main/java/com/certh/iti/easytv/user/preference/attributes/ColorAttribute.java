package com.certh.iti.easytv.user.preference.attributes;

import java.awt.Color;

public class ColorAttribute extends NumericAttribute {

	private NumericAttribute alpha = new IntegerAttribute(new double[] {0.0, 255.});
	private NumericAttribute red_MinMax = new IntegerAttribute(new double[] {0.0, 255.});
	private NumericAttribute green_MinMax = new IntegerAttribute(new double[] {0.0, 255.});
	private NumericAttribute blue_MinMax =  new IntegerAttribute(new double[] {0.0, 255.});
	
	
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
		return new double[] {color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()};
	}
	
	@Override
	public int getDimensionsNumber() {
		return 4;
	}
	
	public NumericAttribute[] getDimensions() {
		return new NumericAttribute[] {alpha, red_MinMax, green_MinMax, blue_MinMax};
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

		//handle alpha dimension
		alpha.handle(color.getAlpha());
		
		//handle red dimension
		red_MinMax.handle(color.getRed());

		//handle alpha dimension
		green_MinMax.handle(color.getGreen());
		
		//handle blue dimension
		blue_MinMax.handle(color.getBlue());

		// Set Min Max vlaue
		setMinMaxValue(numericValue);

		//sum += numericValue;

		n++;
		return value;
	}


}
