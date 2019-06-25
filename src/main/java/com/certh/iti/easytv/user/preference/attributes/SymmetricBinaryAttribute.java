package com.certh.iti.easytv.user.preference.attributes;

public class SymmetricBinaryAttribute extends BinaryAttribute {
	
	public SymmetricBinaryAttribute() {
		super(new double[] {0.0, 1.0 });
	}
	
	@Override
	public double[] getPoints(Object literal) {
		if(literal == null) {
			return new double[] {missingValue};
		}
		
		double value = ((boolean) literal) == true ? 1.0 : 0.0;
		return new double[] {value};
	}

}
