package com.certh.iti.easytv.user.preference.operand;

import java.awt.Color;

import org.json.JSONException;
import org.json.JSONObject;

public class ColorLiteral extends OperandLiteral{

	private Color color;
	
	public ColorLiteral(Object literal) {
		super(literal, Type.Numeric);
		color = Color.decode((String) literal);
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!ColorLiteral.class.isInstance(obj)) return false;
		ColorLiteral other = (ColorLiteral) obj;
		
		return color.equals(other.color);
	}
	
	public double[] getPoint() {
		return new double[] {color.getRed(), color.getGreen(), color.getBlue()};
	}

	@Override
	public OperandLiteral clone(Object value) {
		try {
			String obj = String.valueOf(value);
			return new ColorLiteral(obj);
		} catch (JSONException e) {}
		
		return null;
	}

}
