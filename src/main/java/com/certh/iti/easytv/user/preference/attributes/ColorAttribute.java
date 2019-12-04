package com.certh.iti.easytv.user.preference.attributes;

import java.awt.Color;
import java.util.Random;

public class ColorAttribute extends NumericAttribute {
	

	private NumericAttribute red = new IntegerAttributeWithoutBins(new double[] {0.0, 255.0}, -1);
	private NumericAttribute green = new IntegerAttributeWithoutBins(new double[] {0.0, 255.0}, -1);
	private NumericAttribute blue =  new IntegerAttributeWithoutBins(new double[] {0.0, 255.0}, -1);
	
	
	public ColorAttribute() {
		super(new double[] {0.0, 16777216.0});
	}

	public ColorAttribute(double[] range) {
		super(range);
	}
	
	/**
	 * Fill out the bin label with the proper labels
	 */
	@Override
	protected void init() {
				
		binslables = new String[binsNum];
		binsCounter = new int[binsNum];
		binsCenter = new Object[binsNum];
		
		int size = binSize + 1;
		double initialRange = 0;
		
		//second section with bins that has size of binSize 
		for(int i = 0; i < binsNum; i++) {

			if(i == remaining) {
				size = binSize;
				initialRange = remaining * step ;
			}
			
			//the bin middle value
			int firstValue = (int) (initialRange + (i * size * step) + range[0]);
			int lastValue =  (int) (initialRange + (((i + 1) * size * step) + range[0]) - step);
			int midValue = 0;
			
			//take the middle value
			if(binSize % 2 == 0) {
				midValue += firstValue + (size / 2) * step;
			} else {
				midValue += firstValue + ((size - 1) / 2) * step;
			}
			
			binsCenter[i] = midValue;
			binslables[i] = String.valueOf(firstValue) + ", " + String.valueOf(lastValue) ;
		}
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
	public double[] getPoints(Object literal) {
		if(literal == null) {
			//TODO: handle the missing value of a color, as one value
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
		int bindId = getBinId(numericValue);
		binsCounter[bindId]++;

		//sum += numericValue;
		n++;
		
		return value;
	}
	
	@Override
	public int code(Object literal) {		
		Color color = Color.decode((String) literal);
		
		double value = Math.abs(color.getRGB()) * 1.0;

		return super.code(value);
	}

	@Override 
	public Object decode(int itemId) {
		int binId = itemId - codeBase;
		int attributeId = itemId - binId;
		
		if (attributeId != codeBase)
			throw new IllegalArgumentException("Wrong attribute id: " + codeBase + " " + attributeId);
		
		if (binId >= MAX_BINS_NUMS)
			throw new IllegalArgumentException("Out of range bin id: " + binId);
		
		int binMidValue =  (int) ((binId * binSize * step) + range[0]);

		//take the middle value
		if(binSize % 2 == 0) {
			binMidValue += binSize / 2;
		} else {
			binMidValue += (binSize - 1) / 2;
		}
		
		return Color.decode(String.format("#%06X",binMidValue));
	}

}
