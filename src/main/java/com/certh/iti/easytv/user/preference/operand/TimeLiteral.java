package com.certh.iti.easytv.user.preference.operand;

import java.time.Instant;
import org.json.JSONException;


public class TimeLiteral extends NumericLiteral {

	private Object time;
	
	public TimeLiteral(Object literal) {
		super(Instant.parse( (CharSequence) literal ).toEpochMilli(), null);
	//	super(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse((String) literal).getTime(), null);
		time = literal;
	}
	
	@Override
	public Object getValue() {
		return time;
	}
	
	@Override
	public OperandLiteral clone(Object value) {
		
		try {
			String obj = String.valueOf(value);
			return new TimeLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}
	
	@Override
	protected void checkValue() {}

}
