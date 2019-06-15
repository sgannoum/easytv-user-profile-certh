package com.certh.iti.easytv.user.preference.operand;

import java.time.LocalTime;

import org.json.JSONException;
import org.json.JSONObject;


public class TimeLiteral extends OperandLiteral{

	private LocalTime time;
	
	public TimeLiteral(Object literal) {
		super(literal);
		time = LocalTime.parse((String) literal);
	}


	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public String toString() {
		return time.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!TimeLiteral.class.isInstance(obj)) return false;
		TimeLiteral other = (TimeLiteral) obj;
		
		return time.equals(other.time);
	}

	@Override
	public double distanceTo(OperandLiteral op2) {
		TimeLiteral other = (TimeLiteral) op2;
		System.out.println(time.getMinute() - other.time.getMinute());

		return Math.pow(time.getHour() - other.time.getHour(), 2) + Math.pow((time.getMinute() - other.time.getMinute())/60.0, 2);
	}
	
	public double[] getPoint() {
		return new double[] {time.toNanoOfDay()};
	}

	@Override
	public OperandLiteral createFromJson(JSONObject jsonPreference, String field) {
		
		try {
			String obj = jsonPreference.getString(field);
			return new TimeLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}

}
