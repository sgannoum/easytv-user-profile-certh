package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class NominalAttribute extends Attribute implements INominal {

	protected int state;
	protected long n = 0;
	protected String[] states;

	public NominalAttribute(String[] states) {
		super(new double[] { 0.0, states.length - 1 });
		this.states = states;
		
		//Discretization properties
		this.bins = new Bin[states.length];
		for(int i = 0; i < states.length; i++) {
			bins[i] = new Bin();
			bins[i].center = states[i];
			bins[i].range = new String[] {states[i]};
			bins[i].label = states[i];
			bins[i].type = this;
		}
	}

	public NominalAttribute(double[] range, String[] states) {
		super(range);
		this.states = states;
		
		//Discretization properties
		this.bins = new Bin[states.length];
		for(int i = 0; i < states.length; i++) {
			bins[i] = new Bin();
			bins[i].center = states[i];
			bins[i].range = new String[] {states[i]};
			bins[i].label = states[i];
			bins[i].type = this;
		}
	}

	public NominalAttribute(double operandMissingValue, String[] states) {
		super(new double[] { 0.0, states.length - 1 }, operandMissingValue);
		this.states = states;
		
		//Discretization properties
		this.bins = new Bin[states.length];
		for(int i = 0; i < states.length; i++) {
			bins[i] = new Bin();
			bins[i].center = states[i];
			bins[i].range = new String[] {states[i]};
			bins[i].label = states[i];
			bins[i].type = this;
		}
	}

	public NominalAttribute(double[] range, double operandMissingValue, String[] states) {
		super(range, operandMissingValue);
		this.states = states;
		
		//Discretization properties
		this.bins = new Bin[states.length];
		for(int i = 0; i < states.length; i++) {
			bins[i] = new Bin();
			bins[i].center = states[i];
			bins[i].range = new String[] {states[i]};
			bins[i].label = states[i];
			bins[i].type = this;
		}
	}
	
	public NominalAttribute(double[] range, double operandMissingValue, double step, int binNum, String[] states) throws UserProfileParsingException {
		super(range, step, binNum, operandMissingValue);
		this.states = states;
		
		//Discretization properties
		this.bins = new Bin[states.length];
		for(int i = 0; i < states.length; i++) {
			bins[i] = new Bin();
			bins[i].center = states[i];
			bins[i].range = new String[] {states[i]};
			bins[i].label = states[i];
			bins[i].type = this;
		}
	}
	
	/**
	 * Fill out the bin label with the proper labels
	 */
	@Override
	protected void init() {	
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
	public String getXMLDataTypeURI() {
		return "http://www.w3.org/2001/XMLSchema#string";
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
		
		if(!String.class.isInstance(value))
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " can't not be converted into String");
		
		String str = String.valueOf(value);

		int state = orderOf(str);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + value);

		// increase counts
		bins[state].counts++;
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
		
		if(!String.class.isInstance(literal))
			throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into String");
		
		String str = String.valueOf(literal);

		int binId = orderOf(str);
		if (binId == -1)
			throw new IllegalStateException("Unknown state " + literal);
		
		//check that the given value belongs to the bin range
		if(!isInBinRange(literal, binId)) 
			throw new IllegalArgumentException("Value " + literal + " is not in bin range ["+bins[binId].range[0]+"]");
		
		return binId;
	}
	
	@Override
	public boolean isInBinRange(Object literal, int binId) {
		
		if(!String.class.isInstance(literal))
			throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into integer");
		
		if(binId < 0 || binId >= bins.length)
			throw new IllegalArgumentException("Out of Range bin id: " + binId+" ["+0+","+bins.length+"]");
		
		String value = (String) literal;
		Bin bin = bins[binId];
		
		return (value.equalsIgnoreCase((String) bin.range[0]));
	}

}
