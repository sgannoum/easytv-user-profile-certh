package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;

public class NominalLiteral extends OperandLiteral{
	
	protected int state;
	protected long n = 0;
	protected String[] states;
	protected long[] counts;

	public NominalLiteral(Object literal, String[] states) {
		super(literal, Type.Nominal, new double[] {0.0, states.length} );
		this.states = states;
		this.counts = new long[states.length];
		
		state = orderOf((String) literal);
		
		if(state == -1)
			throw new IllegalStateException("Unknown state " + literal);
		
	}
	
	public NominalLiteral(Object literal, String[] states, double[] range) {
		super(literal, Type.Nominal, range);
		this.states = states;
		this.counts = new long[states.length];
		
		state = orderOf((String) literal);
		
		if(state == -1)
			throw new IllegalStateException("Unknown state " + literal);
		
	}
	
	public final long[] getStateCounts() {
		return counts;
	}
	
	public final String[] getStates() {
		return states;
	}
	
	@Override
	public String toString() {
		return states[state];
	}
	
	public double[] getPoint() {
		return new double[] {state};
	}

	@Override
	public OperandLiteral clone(Object value) {
		try {
			String str = String.valueOf(value);
			NominalLiteral res = new NominalLiteral(str, states);
			
			//increase counts
			counts[res.state]++;
			n++;
			
			return res;
		} catch (JSONException e) {}
		
		return null;
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

}
