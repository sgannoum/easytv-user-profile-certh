package com.certh.iti.easytv.user.preference.operand;

import org.apache.commons.math3.ml.clustering.Clusterable;


/**
 * Each operand shall be either a URI (in accordance with IETF RFC 3986) � in which case it shall be interpreted as the value of the concept with the URI as key, a condition object (i.e. a nested condition), or a literal.
 * @author salgan
 *
 */
public abstract class OperandLiteral implements Clusterable{
	
	public static enum Type {Nominal, Symmetric_Binary, Asymetric_Binary, Ordinal, Numeric, Non};
	
	protected Object literal;
	protected Type type;
	protected double operandMissingValue = -1.0;
	protected double[] range;
	
	public OperandLiteral(Object literal, Type type, double[] range) {
		this.literal = literal;
		this.type = type;
		this.range = range;
	}
	
	public OperandLiteral(Object literal, Type type, double[] range, double operandMissingValue) {
		this.literal = literal;
		this.type = type;
		this.range = range;
		this.operandMissingValue = operandMissingValue;
	}
		
	public Object getValue() {
		return literal;
	}
	
	public Type getType() {
		return type;
	}
	
	public double[] getRange() {
		return range;
	}
	
	public double getOperandMissingValue() {
		return operandMissingValue;
	}
	
	public double[] getMissingPoint() {
		return new double[] {operandMissingValue};
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!OperandLiteral.class.isInstance(obj)) return false;
		OperandLiteral other = (OperandLiteral) obj;
		
		return literal.equals(other.literal);
	}
	
	
	@Override
	public String toString() {		
		return literal.toString();
	}
	
	/**
	 * Create on operand instance from the given value
	 * 
	 * @param value
	 * @return
	 */
	public abstract OperandLiteral clone(Object value);

}