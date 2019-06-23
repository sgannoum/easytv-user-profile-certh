package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;

public class OrdinalLiteral extends OperandLiteral{

	protected int state;
	protected String[] states;
	private double Maxvalue = Double.MIN_VALUE;
	private double Minvalue = Double.MAX_VALUE;

	public OrdinalLiteral(Object literal, String[] states) {
		super(literal, Type.Ordinal);
		this.states = states;
		
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
	
	public double getMaxValue() {
		return Maxvalue;
	}
	
	
	public double getMinValue() {
		return Minvalue;
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
		return new double[] {(state)/(states.length - 1)};
	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public OperandLiteral clone(Object value) {
		try {
			String obj = String.valueOf(value);
			
			
			//Set the min max value from all cloned attributes
			OrdinalLiteral res = new OrdinalLiteral(obj, states);;
			setMinMaxValue(res.state);
			
			return res;
		} catch (JSONException e) {}
		
		return null;
	}

}
