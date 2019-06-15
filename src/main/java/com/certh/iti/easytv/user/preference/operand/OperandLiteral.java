package com.certh.iti.easytv.user.preference.operand;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;


/**
 * Each operand shall be either a URI (in accordance with IETF RFC 3986) � in which case it shall be interpreted as the value of the concept with the URI as key, a condition object (i.e. a nested condition), or a literal.
 * @author salgan
 *
 */
public abstract class OperandLiteral implements Clusterable{
	
	protected Object literal;
	
	public OperandLiteral(Object literal) {
		this.literal = literal;
	}
		
	public Object getValue() {
		return this.literal;
	}
	
	public abstract JSONObject toJSON();
	public abstract double distanceTo(OperandLiteral op2);
	
	public abstract OperandLiteral createFromJson(JSONObject jsonPreference, String field);
}