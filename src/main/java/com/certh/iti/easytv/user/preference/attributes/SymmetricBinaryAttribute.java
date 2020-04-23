package com.certh.iti.easytv.user.preference.attributes;

import com.certh.iti.easytv.user.preference.attributes.discretization.BooleanDiscretization;

public class SymmetricBinaryAttribute extends BinaryAttribute {
	
	public SymmetricBinaryAttribute() {
		super(new double[] {0.0, 1.0 }, 0.0);
	}
	
	public SymmetricBinaryAttribute(BooleanDiscretization discretization) {
		super(new double[] {0.0, 1.0 }, 0.0, discretization);
	}
	
	@Override
	public double getPoints(Object literal) {
		if(literal == null) {
			return missingValue;
		}
		
		double value = ((boolean) literal) == true ? 1.0 : 0.0;
		return value;
	}

}
