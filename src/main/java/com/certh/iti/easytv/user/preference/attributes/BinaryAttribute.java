package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

import com.certh.iti.easytv.user.preference.attributes.discretization.BooleanDiscretization;

public abstract class BinaryAttribute extends Attribute {
	
	public BinaryAttribute(double[] range) {
		super(range);
		
		discretization = new BooleanDiscretization();
	}
	
	public BinaryAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
		
		discretization = new BooleanDiscretization();
	}
	
	@Override
	public Object getRandomValue(Random rand) {		
		return  rand.nextBoolean();
	}
	
	@Override
	public String getXMLDataTypeURI() {
		return "http://www.w3.org/2001/XMLSchema#boolean";
	}
	
	@Override
	public Object handle(Object value) {
		
		if(!Boolean.class.isInstance(value))
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " can't not be converted into Boolean");
		

		Boolean literal = (Boolean) value;
			
		//increase bin counter
		discretization.handle(literal);
		
		return value;
	}
}
