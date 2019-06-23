package com.certh.iti.easytv.user.preference.operand;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;


public class TimeLiteral extends NumericLiteral{

	
	public TimeLiteral(Object literal) {
		super(LocalTime.parse((String) literal, DateTimeFormatter.ISO_DATE_TIME).toNanoOfDay());
	}
	
	@Override
	public OperandLiteral clone(Object value) {
		
		try {
			String obj = String.valueOf(value);
			return new TimeLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}

}
