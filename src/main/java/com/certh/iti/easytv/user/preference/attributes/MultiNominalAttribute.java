package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

import org.json.JSONArray;

public class MultiNominalAttribute extends NominalAttribute{

	public MultiNominalAttribute(String[] states) {
		super(states);
	}

	public MultiNominalAttribute(double[] range, String[] states) {
		super(range, states);
	}

	public MultiNominalAttribute(double operandMissingValue, String[] states) {
		super(operandMissingValue, states);
	}

	public MultiNominalAttribute(double[] range, double operandMissingValue, String[] states) {
		super(range, operandMissingValue, states);
	}
	
	
	@Override
	public Object getRandomValue(Random rand) {
		JSONArray values = new JSONArray();
		
		for(int i = 0; i < states.length; i++) {
			if(rand.nextBoolean())
				values.put(this.states[i]);
		}
		
		return values;	
	}

	@Override
	public double[] getPoints(Object literal) {
		
		if (literal == null) 
			return new double[] { missingValue };
		
		JSONArray values = (JSONArray) literal;
		int mask = 0;
		for(int i = 0; i < values.length(); i++) {
			int state = orderOf(values.getString(i));
			if (state == -1)
				throw new IllegalStateException("Unknown state " + literal);
			
			mask |= (int) Math.pow(2, state);
		}
		
		return new double[] { mask };
	}
	
	@Override
	public Object handle(Object value) {
		
		JSONArray values = (JSONArray) value;
		
		//Process the states
		for(int i = 0; i < values.length(); i++) {
		
			String str = values.getString(i);
	
			int state = orderOf(str);
			if (state == -1)
				throw new IllegalStateException("Unknown state " + value);
	
			// increase counts
			counts[state]++;
			n++;
		}
		return value;
	}
}
