package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

public class IntegerAttribute extends NumericAttribute {	
	
	private int[] binsLable;
	private int[] binsCounter;
	
	public IntegerAttribute(double[] range) {
		super(range);
		binsCounter = new int[binsNum];
		binsLable = new int[binsNum]; 
		setBinsLables();
	}

	public IntegerAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
		binsCounter = new int[binsNum];
		binsLable = new int[binsNum]; 
		setBinsLables();
	}
	
	public IntegerAttribute(double[] range, double step, double operandMissingValue) {
		super(range, step, operandMissingValue);
		binsCounter = new int[binsNum];
		binsLable = new int[binsNum]; 
		setBinsLables();
	}
	
	public IntegerAttribute(double[] range, double step, int binsNum, double operandMissingValue) {
		super(range, step, binsNum, operandMissingValue);
		binsCounter = new int[binsNum];
		binsLable = new int[binsNum]; 
		setBinsLables();
	}
	
	@Override
	public Object getRandomValue(Random rand) {
		Double value = (Double) super.getRandomValue(rand);
		return value.intValue();
	}

	@Override
	public double[] getPoints(Object literal) {
		if (literal == null) {
			return new double[] { missingValue };
		}

		int value = (int) literal;
		return new double[] { value };
	}

	@Override
	public Object handle(Object value) {

		int numericValue = Integer.class.cast(value);

		// Increate histogram counts
		Long tmp = frequencyHistogram.get(numericValue);
		if (tmp == null) {
			frequencyHistogram.put((double) numericValue, 1L);
		} else {
			frequencyHistogram.put((double) numericValue, tmp + 1);
		}

		// Set Min Max vlaue
		setMinMaxValue(numericValue);

		sum += numericValue;

		n++;
		return value;
	}
	
	@Override
	public int code(Object literal) {		
		int value = (int) literal;
		
		//the value position in the sequence of value ranges
		int position = (int) ((value - range[0]) / step);
		
		//specify the itemId
		int binId = (int) (position / binSize);
		
		//Increment the number of occurrences 
		binsCounter[binId]++;

		return attributeCodeBase + binId;
	}

	@Override 
	public Object decode(int itemId) {
		int binId = itemId - attributeCodeBase;
		int attributeId = itemId - binId;
		
		if (attributeId != attributeCodeBase)
			throw new IllegalArgumentException("Wrong attribute id: " + attributeCodeBase + " " + attributeId);
		
		if (binId >= binsNum)
			throw new IllegalArgumentException("Out of range bin id: " + binId);
				
		return binsLable[binId];
	}
	
	@Override
	public String toString() {
		
		String binlables =  "", binsCounts = "", emplyLine = "",upperLine = "", middleLine = "";
		for(int i = 0 ; i < binsCounter.length; i++) {
			binlables += String.format("|%-5d", binsLable[i]);
			binsCounts += String.format("|%-5d", binsCounter[i]);
		}
		
		binlables += "|";
		binsCounts += "|";
		
		emplyLine = String.format("%"+binlables.length()+"s", " ");
		upperLine = emplyLine.replaceAll(" ", "+");
		middleLine = emplyLine.replaceAll(" ", "-");
		
		return super.toString() + String.format("%s\n"
											+ "|%-"+(upperLine.length() - 2)+"s|\n"
											+ "%s\n"
											+ "%s\n"
											+ "%s\n"
											+ "%s\n"
											+ "%s\n",
											upperLine,
											 "Bins histogram",
											upperLine, 
											binlables, 
											middleLine, 
											binsCounts, 
											upperLine);
	}
	

	/**
	 * Fill out the bin label with the proper labels
	 */
	private void setBinsLables() {
				
		for(int i = 0; i < binsNum; i++) {

			//the bin midell value
			int binMidValue =  (int) ((i * binSize * step) + range[0]);

			//take the middle value
			if(binSize % 2 == 0) {
				binMidValue += binSize / 2;
			} else {
				binMidValue += (binSize - 1) / 2;
			}
			
			binsLable[i] = binMidValue;
		}
	}

}
