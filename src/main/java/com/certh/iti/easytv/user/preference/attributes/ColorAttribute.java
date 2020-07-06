package com.certh.iti.easytv.user.preference.attributes;

import java.awt.Color;
import java.util.Random;
import java.util.regex.Pattern;

import com.certh.iti.easytv.user.preference.attributes.discretization.ColorDiscretization;
import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;
import com.certh.iti.easytv.user.preference.attributes.discretization.NoDiscretization;

public class ColorAttribute extends IntegerAttribute {
	
	private static double[] default_range = new double[] {0X000000, 0Xffffff};

	private NumericAttribute red = new IntegerAttribute(new double[] {0.0, 255.0}, 1, 0, new NoDiscretization());
	private NumericAttribute green = new IntegerAttribute(new double[] {0.0, 255.0}, 1, 0, new NoDiscretization());
	private NumericAttribute blue =  new IntegerAttribute(new double[] {0.0, 255.0}, 1, 0, new NoDiscretization());
	protected IntegerConverter converter = new IntegerConverter() {
		
		private Pattern colorPatter = Pattern.compile("#[0-9a-fA-F]{1,6}");

		@Override
		public boolean isInstance(Object obj) {
			if(!String.class.isInstance(obj))
				return false;
			
			return colorPatter.matcher((CharSequence) obj).matches();
		}

		@Override
		public Integer valueOf(Object obj) {
			Color color = Color.decode((String) obj);	
			return color.getRGB() & 0x00ffffff;
		}
		
	};
	
	public static class ColorBuilder extends IntegerBuilder {
		
		public ColorBuilder() {
			super(new ColorAttribute());
		}
	}
	
	public ColorAttribute() {
		super(default_range, 1.0, 0X000000);
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
		
		return converter.valueOf(literal);
	}
	
	public NumericAttribute[] getDimensions() {
		return new NumericAttribute[] {red, green, blue};
	}
	
	@Override
	public Object handle(Object value) {
		
		if(!converter.isInstance(value)) 
			throw new IllegalArgumentException("Value " + value + " can't not be converted into Color");
		
		int numericValue = converter.valueOf(value);

		// Increase histogram counts
		if(enableFrequencyHistogram) {
			Long tmp = (tmp = frequencyHistogram.get(numericValue)) == null ? 1L : (tmp + 1L);
			frequencyHistogram.put(numericValue, tmp);
			
			if(tmp > mostFrequentCount) {
				mostFrequentCount = tmp;
				mostFrequentValue = value;
			}
		}
		
		//handle red dimension
		red.handle((numericValue & 0x00ff0000) >> 16);

		//handle alpha dimension
		green.handle((numericValue & 0x0000ff00) >> 8);
		
		//handle blue dimension
		blue.handle(numericValue & 0x000000ff);

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
		if(!enableDiscretization) 
			return null;
		
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
	
	public static ColorBuilder Builder() {
		return new ColorBuilder();
	}
	
}
