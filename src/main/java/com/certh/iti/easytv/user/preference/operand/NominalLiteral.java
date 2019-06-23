package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;

public class NominalLiteral extends OperandLiteral{
	
	protected int state;
	protected String[] states;
	protected long[] counts;

	public NominalLiteral(Object literal, String[] states) {
		super(literal, Type.Nominal);
		this.states = states;
		this.counts = new long[states.length];
		
		state = orderOf((String) literal);
		
		if(state == -1)
			throw new IllegalStateException("Unknown state " + literal);
		
	}
	
	private void increaseStateCounts(int state) {
		counts[state]++;
	}
	
	public final long[] getStatCounts() {
		return counts;
	}
	
	public final String[] getStates() {
		return states;
	}
	
	protected int orderOf(String state) {
		for(int i = 0; i < states.length; i++) 
			if(state.equalsIgnoreCase(states[i])) 
				return i;
			
		return -1;
	}
	
	protected String stateOf(int index) {
		if(index < 0 || index >= states.length)
			return null;
		
		return states[index];
	}
	
	@Override
	public String toString() {
		return states[state];
	}
	
	public double[] getPoint() {
		return new double[] {state};
	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperandLiteral clone(Object value) {
		try {
			String str = String.valueOf(value);
			NominalLiteral res = new NominalLiteral(str, states);
			
			//increase counts
			increaseStateCounts(res.state);
			
			return res;
		} catch (JSONException e) {}
		
		return null;
	}

}
