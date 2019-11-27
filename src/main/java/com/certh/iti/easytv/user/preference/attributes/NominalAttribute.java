package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

public class NominalAttribute extends Attribute implements INominal {

	protected int state;
	protected long n = 0;
	protected String[] states;
	protected long[] counts;
	
	protected double step = 1.0;
	protected int binNum = 1;
	protected int binsNum = IncrCodeStep;

	public NominalAttribute(String[] states) {
		super(new double[] { 0.0, states.length - 1 });
		this.states = states;
		this.counts = new long[states.length];
		
		double valueRange = (range[1] - range[0]) / step;
		if(valueRange > binsNum) 
			 binNum = (int) Math.ceil(valueRange/binsNum);
	}

	public NominalAttribute(double[] range, String[] states) {
		super(range);
		this.states = states;
		this.counts = new long[states.length];
		
		double valueRange = (range[1] - range[0]) / step;
		if(valueRange > binsNum) 
			 binNum = (int) Math.ceil(valueRange/binsNum);
	}

	public NominalAttribute(double operandMissingValue, String[] states) {
		super(new double[] { 0.0, states.length - 1 }, operandMissingValue);
		this.states = states;
		this.counts = new long[states.length];
		
		double valueRange = (range[1] - range[0]) / step;
		if(valueRange > binsNum) 
			 binNum = (int) Math.ceil(valueRange/binsNum);
	}

	public NominalAttribute(double[] range, double operandMissingValue, String[] states) {
		super(range, operandMissingValue);
		this.states = states;
		this.counts = new long[states.length];
		
		double valueRange = (range[1] - range[0]) / step;
		if(valueRange > binsNum) 
			 binNum = (int) Math.ceil(valueRange/binsNum);
	}
	
	public NominalAttribute(double[] range, double operandMissingValue, int binNum, String[] states) {
		super(range, operandMissingValue);
		this.states = states;
		this.binNum = binNum;
		this.counts = new long[states.length];

		if(binNum > states.length)
			throw new IllegalArgumentException("Bin number "+binNum+" can't be bigger than available states"+states.length);
		
		double valueRange = (range[1] - range[0]) / step;
		if(valueRange > binsNum) 
			 binNum = (int) Math.ceil(valueRange/binsNum);
	}

	public final long[] getStateCounts() {
		return counts;
	}

	public final String[] getStates() {
		return states;
	}
	
	@Override
	public Object getRandomValue(Random rand) {
		int state = rand.nextInt((int) this.range[1]);
		return this.states[state];	
	}

	@Override
	public double[] getPoints(Object literal) {
		if (literal == null) {
			return new double[] { missingValue };
		}

		int state = orderOf((String) literal);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + literal);

		return new double[] { state };
	}

	@Override
	public Object handle(Object value) {
		String str = String.valueOf(value);

		int state = orderOf(str);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + value);

		// increase counts
		counts[state]++;
		n++;

		return value;

	}

	protected int orderOf(String state) {
		for (int i = 0; i < states.length; i++)
			if (state.equalsIgnoreCase(states[i]))
				return i;

		return -1;
	}

	protected String stateOf(int index) {
		if (index < 0 || index >= states.length)
			return null;

		return states[index];
	}

	@Override
	public int code(Object literal) {		
		String str = String.valueOf(literal);

		int state = orderOf(str);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + literal);
		
		int itemId = this.attributeCodeBase + state;

		return itemId;
	}

	@Override
	public Object decode(int itemId) {

		int binId = itemId - attributeCodeBase;
		int attributeId = itemId - binId;
		
		if (attributeId != attributeCodeBase)
			throw new IllegalArgumentException("Wrong attribute id: " + attributeCodeBase + " " + attributeId);
		
		if (binId >= states.length)
			throw new IllegalArgumentException("Out of range bin index: " + binId);

		return states[binId];
	}
	
	
	@Override
	public String toString() {
		
		String binlables =  "", binsCounts = "", emplyLine = "",upperLine = "", middleLine = "";
		for(int i = 0 ; i < states.length; i++) {
			int fieldLength = states[i].length() + 2;
			binlables += String.format("|%-"+fieldLength+"s", states[i]);
			binsCounts += String.format("|%-"+fieldLength+"d", counts[i]);
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
