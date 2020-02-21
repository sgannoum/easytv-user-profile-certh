package com.certh.iti.easytv.user.preference.attributes;

public class AsymmetricBinaryAttribute extends BinaryAttribute {
	
	public AsymmetricBinaryAttribute() {
		super(new double[] {0.0, 1.0 }, 0.0);
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
