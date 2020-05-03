package com.certh.iti.easytv.user.preference.attributes;

import com.certh.iti.easytv.user.preference.attributes.discretization.BooleanDiscretization;

public class AsymmetricBinaryAttribute extends BinaryAttribute {
	
	public AsymmetricBinaryAttribute() {
		super(new double[] {0.0, 1.0 }, 0.0);
	}
	
	public AsymmetricBinaryAttribute(BooleanDiscretization discretization) {
		super(new double[] {0.0, 1.0 }, 0.0, discretization);
	}
	
	public static class AsymmetricBinaryBuilder extends BirnaryBuilder {
		
		public AsymmetricBinaryBuilder() {
			super(new AsymmetricBinaryAttribute());
		}
		
	}
	
	@Override
	public double getPoints(Object literal) {
		if(literal == null) {
			return missingValue;
		}
		
		double value = ((boolean) literal) == true ? 1.0 : 0.0;
		return value;
	}
	
	public static AsymmetricBinaryBuilder Builder() {
		return new AsymmetricBinaryBuilder();
	}
}
