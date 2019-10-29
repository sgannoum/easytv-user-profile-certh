package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

public abstract class BinaryAttribute extends Attribute {

	public BinaryAttribute(double[] range) {
		super(range);
	}
	
	public BinaryAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
	}
	
	@Override
	public Object getRandomValue(Random rand) {		
		return  rand.nextInt(1) == 0 ? false : true;
	}

}
