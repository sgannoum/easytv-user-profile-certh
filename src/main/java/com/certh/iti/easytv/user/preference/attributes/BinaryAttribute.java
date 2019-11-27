package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

public abstract class BinaryAttribute extends Attribute {

	private String[] binsLable = new String[]{"False", "True"};
	private int[] binsCounter;
	
	public BinaryAttribute(double[] range) {
		super(range);
		binsCounter = new int[2];
	}
	
	public BinaryAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
		binsCounter = new int[2];
	}
	
	@Override
	public Object getRandomValue(Random rand) {		
		return  rand.nextInt(1) == 0 ? false : true;
	}

	@Override
	public int code(Object literal) {
		boolean value = (boolean) literal;
		
		//find out the bin id
		int binId = value == true ? 1 : 0;
		
		//Increment counts
		binsCounter[binId]++;
		
		return this.attributeCodeBase + binId;
	}

	@Override
	public Object decode(int itemId) {
		int binId = itemId - attributeCodeBase;
		int attributeId = itemId - binId;
		
		if (attributeId != attributeCodeBase)
			throw new IllegalArgumentException("Wrong attribute id: " + attributeCodeBase + " " + attributeId);
		
		if (binId != 1 && binId != 0)
			throw new IllegalArgumentException("Out of range bin id: " + binId);

		return binId == 1 ? true : false;
	}
	
	@Override
	public String toString() {
		
		String binlables =  "", binsCounts = "", emplyLine = "",upperLine = "", middleLine = "";
		for(int i = 0 ; i < binsCounter.length; i++) {
			binlables += String.format("|%-7s", binsLable[i]);
			binsCounts += String.format("|%-7d", binsCounter[i]);
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

}
