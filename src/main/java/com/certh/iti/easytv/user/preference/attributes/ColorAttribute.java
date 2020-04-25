package com.certh.iti.easytv.user.preference.attributes;

import java.awt.Color;
import java.util.Random;
import com.certh.iti.easytv.user.preference.attributes.discretization.ColorDiscretization;
import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;
import com.certh.iti.easytv.user.preference.attributes.discretization.NoDiscretization;

public class ColorAttribute extends IntegerAttribute {
	
	private NumericAttribute red = new IntegerAttribute(new double[] {0.0, 255.0}, 1, 0, new NoDiscretization());
	private NumericAttribute green = new IntegerAttribute(new double[] {0.0, 255.0}, 1, 0, new NoDiscretization());
	private NumericAttribute blue =  new IntegerAttribute(new double[] {0.0, 255.0}, 1, 0, new NoDiscretization());
	
	private static double[] default_range = new double[] {0X000000, 0Xffffff};
	
	public ColorAttribute() {
		super(default_range, 1.0, -1);
	}
	
	public ColorAttribute(int binNum) {
		super(default_range, 1.0, binNum, 0X000000);
	}
	
	public ColorAttribute(double step, int binNum) {
		super(default_range, step, binNum, 0X000000);
	}
	
	public ColorAttribute(double[] range, double step, int binNum) {
		super(range, step,  binNum, 0X000000);
	}
	
	public ColorAttribute(double[] range, double step, Integer[][] discretes) {
		super(range, step, discretes);
	}
	
	public ColorAttribute(Integer[][] discretes) {
		super(default_range, 1.0, discretes);
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
		
		int numericValue = color.getRGB() & 0x00ffffff;

		// Increase histogram counts
		Long tmp = (tmp = frequencyHistogram.get(numericValue)) == null ? 1L : (tmp + 1L);
		frequencyHistogram.put(numericValue, tmp);
		
		//handle red dimension
		red.handle(color.getRed());

		//handle alpha dimension
		green.handle(color.getGreen());
		
		//handle blue dimension
		blue.handle(color.getBlue());

		// Set Min Max vlaue
		setMinMaxValue(numericValue);
		
		//Increment the number of occurrences 
		if(discretization != null)
			discretization.handle(numericValue);

		//sum += numericValue;
		n++;
		
		return value;
	}

	@Override
	protected String getValueshistogram() {
		return "";
	}
	
	@Override
	public double getStandardDeviation() {
		return 0.0;
	}
	
	@Override
	public Discretization getDiscretization() {
		if(discretization == null) {
			if(frequencyHistogram.isEmpty()) return null;
			else if(binsNum != -1)
				return new ColorDiscretization(range, step, binsNum, frequencyHistogram);
			else if(discretes != null)
				return new ColorDiscretization(range, step, discretes, frequencyHistogram);
			else 
				return new ColorDiscretization(range, step, frequencyHistogram);
		}
		else 
			return discretization;
	}
	
}
