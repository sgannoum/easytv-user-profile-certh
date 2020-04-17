package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;
import com.certh.iti.easytv.user.preference.attributes.discretization.StringDiscretization;

public class NominalAttribute extends Attribute implements INominal {

	protected int state;
	protected long n = 0;
	protected String[] states;
	

	public NominalAttribute(String[] states) {
		super(new double[] { 0.0, states.length - 1 });
		this.states = states;
		discretization = new StringDiscretization(states);
	}
	
	protected NominalAttribute(String[] states, Discretization distrectization) {
		super(new double[] { 0.0, states.length - 1 });
		this.states = states;
		this.discretization = distrectization;
	}

	public NominalAttribute(double[] range, String[] states) {
		super(range);
		this.states = states;
		discretization = new StringDiscretization(states);
	}

	public NominalAttribute(double operandMissingValue, String[] states) {
		super(new double[] { 0.0, states.length - 1 }, operandMissingValue);
		this.states = states;
		discretization = new StringDiscretization(states);
	}

	public NominalAttribute(double[] range, double operandMissingValue, String[] states) {
		super(range, operandMissingValue);
		this.states = states;
		discretization = new StringDiscretization(states);
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
	public double getPoints(Object literal) {
		if (literal == null) {
			return missingValue;
		}

		int state = orderOf((String) literal);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + literal);

		return state;
	}

	@Override
	public Object handle(Object value) {
		
		if(!String.class.isInstance(value))
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " can't not be converted into String");
		
		// increase counts
		discretization.handle(value);
		
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

}
