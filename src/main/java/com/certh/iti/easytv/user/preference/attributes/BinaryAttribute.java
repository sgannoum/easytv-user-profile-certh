package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

public abstract class BinaryAttribute extends Attribute {
	
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
		return  rand.nextBoolean();
	}

	@Override
	public int code(Object literal) {	
		
		if(!Boolean.class.isInstance(literal))
			throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into Boolean");
		
		//convert int to double
		double value = (boolean )literal == true ? 1.0 : 0.0;

		return super.code(value);
	}
	
	@Override
	public Object handle(Object value) {
		
		if(!Boolean.class.isInstance(value))
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " can't not be converted into Boolean");
		

		Boolean literal = (Boolean) value;
		
		//find bin index
		int binId = literal == false ? 0 : 1;
		
		//increase bin counter
		binsCounter[binId]++;
		
		return value;
	}
	
	@Override
	public String toString() {
		
		String binlables =  "", binsCounts = "", emplyLine = "",upperLine = "", middleLine = "";
		for(int i = 0 ; i < binsCounter.length; i++) {
			binlables += String.format("|%-7s", (boolean) binsCenter[i] ? "True" : "False");
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
	
	/**
	 * Fill out the bin label with the proper labels
	 */
	protected void init() {
		binslables = new String[binsNum];
		binsCounter = new int[binsNum];
		binsCenter = new Object[binsNum]; 
		
		binsCenter[0] = false;	
		binsCenter[1] = true;
		
		binslables[0] = "false";
		binslables[1] = "true";

	}

}
