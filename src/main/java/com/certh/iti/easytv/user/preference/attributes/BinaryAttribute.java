package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

public abstract class BinaryAttribute extends Attribute {
	
	public BinaryAttribute(double[] range) {
		super(range);
	}
	
	public BinaryAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
	}
	
	
	/**
	 * Fill out the bin label with the proper labels
	 */
	@Override
	protected void init() {
		bins = new Bin[binsNum];
		bins[0] = new Bin();
		bins[1] = new Bin();

		bins[0].range = new Boolean[] {false};
		bins[1].range = new Boolean[] {true};
		
		bins[0].center = false;	
		bins[1].center = true;
		
		bins[0].label = "false";
		bins[1].label = "true";
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
		
		//get bin Id
		int binId = getBinId(value);
		
		//check that the given value belongs to the bin range
		if(!isInBinRange(literal, binId)) 
			throw new IllegalArgumentException("Value " + literal + " is not in bin range ["+bins[binId].range[0]+"]");

		return binId;
	}
	
	@Override
	public boolean isInBinRange(Object literal, int binId) {
		
		if(!Boolean.class.isInstance(literal))
			throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into Double");
		
		if(binId < 0 || binId >= bins.length)
			throw new IllegalArgumentException("Out of Range bin id: " + binId+" ["+0+","+bins.length+"]");
		
		boolean value = (boolean) literal;
		Bin bin = bins[binId];
		
		return (value == (boolean) bin.range[0]);
	}
	
	@Override
	public Object handle(Object value) {
		
		if(!Boolean.class.isInstance(value))
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " can't not be converted into Boolean");
		

		Boolean literal = (Boolean) value;
		
		//find bin index
		int binId = literal == false ? 0 : 1;
		
		//increase bin counter
		bins[binId].counts++;
		
		return value;
	}
}
