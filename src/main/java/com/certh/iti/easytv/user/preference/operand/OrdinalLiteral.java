package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;

public class OrdinalLiteral extends OperandLiteral{

	protected int state;
	protected String[] states;
	protected long[] counts;
	private double Maxvalue = Double.MIN_VALUE;
	private double Minvalue = Double.MAX_VALUE;

	public OrdinalLiteral(Object literal, String[] states) {
		super(literal, Type.Ordinal, new double[] {0.0, states.length});
		this.states = states;
		this.counts = new long[states.length];
		
		state = orderOf((String) literal);
		
		if(state == -1)
			throw new IllegalStateException("Unknown state " + literal);
		
	}
	
	public OrdinalLiteral(Object literal, String[] states, double[] range) {
		super(literal, Type.Ordinal, range);
		this.states = states;
		this.counts = new long[states.length];
		
		state = orderOf((String) literal);
		
		if(state == -1)
			throw new IllegalStateException("Unknown state " + literal);
		
	}
	
	private void setMinMaxValue(double value) {
		
		if(value > Maxvalue ) {
			Maxvalue = value;
		}
		
		if(value < Minvalue) {
			Minvalue = value;
		}
	}
	
	private void increaseStateCounts(int state) {
		counts[state]++;
	}
	
	public double getMaxValue() {
		return Maxvalue;
	}
	
	public double getMinValue() {
		return Minvalue;
	}
	
	public final String[] getStates() {
		return states;
	}
	
	public final long[] getStatCounts() {
		return counts;
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
		return new double[] {(state)/(states.length - 1)};
	}
	
	@Override
	public OperandLiteral clone(Object value) {
		try {
			String obj = String.valueOf(value);
			
			
			//Set the min max value from all cloned attributes
			OrdinalLiteral res = new OrdinalLiteral(obj, states);
			setMinMaxValue(res.state);
			increaseStateCounts(res.state);
			
			return res;
		} catch (JSONException e) {}
		
		return null;
	}

}
