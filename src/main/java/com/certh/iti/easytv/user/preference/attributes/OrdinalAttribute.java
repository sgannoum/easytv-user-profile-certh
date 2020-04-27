package com.certh.iti.easytv.user.preference.attributes;

import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;
import com.certh.iti.easytv.user.preference.attributes.discretization.StringDiscretization;

public class OrdinalAttribute extends NominalAttribute implements INumeric, INominal {

	private double sum = 0.0;
	private double Maxvalue = Double.MIN_VALUE;
	private double Minvalue = Double.MAX_VALUE;
	
	public static class OrdinalBuilder extends NominalBuilder{
		
		public OrdinalBuilder() {
			super(new OrdinalAttribute());
		}
	}

	protected OrdinalAttribute() {
		super();
	}
	
	public OrdinalAttribute(String[] states) {
		super(states);
		this.states = states;
	}

	public OrdinalAttribute(double[] range, String[] states) {
		super(range, states);
		this.states = states;
	}

	public OrdinalAttribute(double[] range, double operandMissingValue, String[] states) {
		super(range, operandMissingValue, states);
		this.states = states;
	}
	public OrdinalAttribute(double[] range, double operandMissingValue, String[] states, String[][] discretes) {
		super(range, operandMissingValue, states);
		this.states = states;
		this.discretes = discretes;
	}

	private void setMinMaxValue(double value) {

		if (value > Maxvalue) {
			Maxvalue = value;
		}

		if (value < Minvalue) {
			Minvalue = value;
		}
	}

	public double getMaxValue() {
		return Maxvalue != Minvalue && Maxvalue != Double.MIN_VALUE ? Maxvalue : range[1];
	}
	
	public double getMinValue() {
		return Maxvalue != Minvalue && Minvalue != Double.MAX_VALUE ? Minvalue : range[0];
	}

	@Override
	public long getCounts() {
		return n;
	}

	@Override
	public double getSum() {
		return sum;
	}

	@Override
	public double getMean() {
		return sum / n;
	}

	@Override
	public double getStandardDeviation() {
		double var = 0.0;
		double mean = sum / n;

		for (int i = 0; i < states.length; i++)
			var += discretization.getBins()[i].getCounts() * Math.pow(i - mean, 2);

		return Math.sqrt(var / n);
	}

	@Override
	public double getPoints(Object literal) {
		if (literal == null) {
			return missingValue;
		}

		String convertedValue = converter.valueOf(literal);

		int state = orderOf(convertedValue);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + literal);

		return state;
	}
	

	@Override
	public Object handle(Object value) {
		
		if(!converter.isInstance(value))
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " can't not be converted into String");
		
		String convertedValue = converter.valueOf(value);

		int state = orderOf(convertedValue);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + value);
		
		// Increase histogram counts
		String key = (String) value;
		Long tmp = (tmp = frequencyHistogram.get(key)) == null ? 1L : (tmp + 1L);
		frequencyHistogram.put(key, tmp);

		// set Min Max value
		setMinMaxValue(state);

		// increase counts
		if(discretization != null)
			discretization.handle(value);

		sum += state;

		return value;
	}
	
	@Override
	public Discretization getDiscretization() {
		if(discretization == null) {
			if(frequencyHistogram.isEmpty()) return null;
			else if(discretes == null)
				return new StringDiscretization(range, frequencyHistogram);
			else
				return new StringDiscretization(range, discretes, frequencyHistogram);
		}
		else 
			return discretization;
	}
	
	public static OrdinalBuilder Builder() {
		return new OrdinalBuilder();
	}

}
