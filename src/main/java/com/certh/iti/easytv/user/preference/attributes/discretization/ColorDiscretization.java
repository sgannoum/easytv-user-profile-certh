package com.certh.iti.easytv.user.preference.attributes.discretization;

import java.awt.Color;
import java.util.TreeMap;

public class ColorDiscretization extends IntegerDiscretization{
	
	public ColorDiscretization(double[] range) {
		super(range, 1.0);
	}
	
	public ColorDiscretization(double[] range, double step) {
		super(range, step);
	}

	public ColorDiscretization(double[] range, double step, int binsNum) {
		super(range, step, binsNum);
	}
	
	public ColorDiscretization(double[] range, double step, Integer[][] discretes) {
		super(range, step, discretes);
	}
	
	public ColorDiscretization(double[] range, double step, int binNums,  TreeMap<Integer, Long> values) {
		super(range, step, binNums, values);
	}
	
	public ColorDiscretization(double[] range, double step, Integer[][] discretes, TreeMap<Integer, Long> values) {
		super(range, step, discretes, values);
	}

	@Override
	public int code(Object literal) {	
		
		if(!String.class.isInstance(literal))
			throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into Color");
		
		Color color = Color.decode((String) literal);
		
		//remove alpha 8 bits value
		return super.getBinId(color.getRGB() & 0x00ffffff);
	}

	@Override 
	public Object decode(int itemId) {
		return "#"+Integer.toHexString((int) super.decode(itemId));
	}

}
