package com.certh.iti.easytv.user.preference.operand;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;


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
	
	public OperandLiteral(Object literal, Type type) {
		this.literal = literal;
		this.type = type;
	}
	
	public OperandLiteral(Object literal, Type type, double operandMissingValue) {
		this.literal = literal;
		this.type = type;
		this.operandMissingValue = operandMissingValue;
	}
		
	public Object getValue() {
		return literal;
	}
	
	public Type getType() {
		return type;
	}
	
	public double getOperandMissingValue() {
		return operandMissingValue;
	}
	
	public abstract JSONObject toJSON();
	
	public abstract OperandLiteral clone(Object value);

}