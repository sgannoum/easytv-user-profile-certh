package com.certh.iti.easytv.user.preference.attributes;

public class OrdinalAttribute extends NominalAttribute implements INumeric, INominal {

	private double sum = 0.0;
	private double Maxvalue = Double.MIN_VALUE;
	private double Minvalue = Double.MAX_VALUE;

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

	private void setMinMaxValue(double value) {

		if (value > Maxvalue) {
			Maxvalue = value;
		}

		if (value < Minvalue) {
			Minvalue = value;
		}
	}

	public double getMaxValue() {
		return Maxvalue;
	}

	public double getMinValue() {
		return Minvalue;
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
	public double[][] getEntriesCounts() {

		double[][] statesCounts = new double[states.length][1];

		for (int i = 0; i < states.length; i++)
			statesCounts[i][0] = counts[i];

		return statesCounts;
	}

	@Override
	public double getStandardDeviation() {
		double var = 0.0;
		double mean = sum / n;

		for (int i = 0; i < states.length; i++)
			var += counts[i] * Math.pow(i - mean, 2);

		return Math.sqrt(var / n);
	}

	@Override
	public double[] getPoints(Object literal) {
		if (literal == null) {
			return new double[] { missingValue };
		}

		int state = orderOf((String) literal);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + literal);

		return new double[] { state };
	}
	

	@Override
	public Object handle(Object value) {
		String str = String.valueOf(value);

		int state = orderOf(str);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + value);

		// set Min Max value
		setMinMaxValue(state);

		// increase counts
		counts[state]++;

		sum += state;

		return value;
	}

}
