package com.certh.iti.easytv.user.preference.attributes;

import java.util.Random;

import org.json.JSONArray;

import com.certh.iti.easytv.user.preference.attributes.discretization.NoDiscretization;

public class MultiNominalAttribute extends NominalAttribute {

	public MultiNominalAttribute(String[] states) {
		super(states, new NoDiscretization());
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
	public double getPoints(Object literal) {
		
		if (literal == null) 
			return missingValue;
		
		JSONArray values = (JSONArray) literal;
		int mask = 0;
		for(int i = 0; i < values.length(); i++) {
			int state = orderOf(values.getString(i));
			if (state == -1)
				throw new IllegalStateException("Unknown state " + literal);
			
			mask |= (int) Math.pow(2, state);
		}
		
		return mask;
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
			discretization.handle(value);
			
			n++;
		}
		return value;
	}
}
