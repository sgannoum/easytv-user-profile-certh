package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONException;
import org.json.JSONObject;


public class NumericLiteral  extends OperandLiteral {
	private double value;
	private double Maxvalue = Double.MIN_VALUE;
	private double Minvalue = Double.MAX_VALUE;
	
	public NumericLiteral(Object literal) {
		super(literal, Type.Numeric);
		
		if(Double.class.isInstance(literal)) {
			value = Double.class.cast(literal);
		} else {
			value = Integer.class.cast(literal);
		}

		setMinMaxValue(value);
	}
	
	public NumericLiteral(double literal) {
		super(literal, Type.Numeric);
		value = literal;
	}
	
	public NumericLiteral(long literal) {
		super(literal, Type.Numeric);
		value = literal * 1.0;
	}
	
	public NumericLiteral(int literal) {
		super(literal, Type.Numeric);
		value = literal * 1.0;
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
	
	@Override
	public JSONObject toJSON() {
		return null;
	}
	@Override
	public String toString() {
		return String.valueOf(literal);
	}
	

	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!NumericLiteral.class.isInstance(obj)) return false;
		NumericLiteral other = (NumericLiteral) obj;
		
		return value == other.value;
	}
	
	public double[] getPoint() {
		return new double[] {value};
	}

	@Override
	public OperandLiteral clone(Object value) {
		try {			
			//Set the min max value from all cloned attributes
			NumericLiteral res = new NumericLiteral(value);
			setMinMaxValue(res.value);
			
			return res;
		} catch (JSONException e) {}
		
		return null;
	}

}