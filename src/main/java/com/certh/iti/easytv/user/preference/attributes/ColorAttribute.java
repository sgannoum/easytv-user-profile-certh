package com.certh.iti.easytv.user.preference.attributes;

import java.awt.Color;
import java.util.Random;

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
	
	@Override
	public int code(Object literal) {		
		Color color = Color.decode((String) literal);
		
		int value = Math.abs(color.getRGB());
		
		//the value position in the sequence of value ranges
		int position = (int) ((value - range[0]) / step);
		
		//specify the itemId
		int binId = attributeCodeBase + (int) (position / binSize);

		return binId;
	}

	@Override 
	public Object decode(int itemId) {
		int binId = itemId - attributeCodeBase;
		int attributeId = itemId - binId;
		
		if (attributeId != attributeCodeBase)
			throw new IllegalArgumentException("Wrong attribute id: " + attributeCodeBase + " " + attributeId);
		
		if (binId >= IncrCodeStep)
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
