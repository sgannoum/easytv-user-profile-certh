package com.certh.iti.easytv.user.preference.operand;

public abstract class BooleanLiteral extends OperandLiteral {
	private boolean booleanLiteral;
	
	public BooleanLiteral(Object literal, Type type) {
		super(literal, type, new double[]{0.0, 1.0});
		booleanLiteral = (Boolean) literal;
	}
	
	public BooleanLiteral(Object literal, Type type, double[] range) {
		super(literal, type, range);
		booleanLiteral = (Boolean) literal;
	}
	
	@Override
	public String toString() {
		return String.valueOf(literal);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!BooleanLiteral.class.isInstance(obj)) return false;
		
		return literal.equals(((BooleanLiteral)obj).getValue());
	}

	public double[] getPoint() {
		return new double[] {booleanLiteral ? 1.0 : 0.0};
	}
	
}