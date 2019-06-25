package com.certh.iti.easytv.user.preference.attributes;

public class NominalAttribute extends Attribute implements INominal {

	protected int state;
	protected long n = 0;
	protected String[] states;
	protected long[] counts;

	public NominalAttribute(String[] states) {
		super(new double[] { 0.0, states.length });
		this.states = states;
		this.counts = new long[states.length];
	}

	public NominalAttribute(double[] range, String[] states) {
		super(range);
		this.states = states;
		this.counts = new long[states.length];
	}

	public NominalAttribute(double operandMissingValue, String[] states) {
		super(new double[] { 0.0, states.length }, operandMissingValue);
		this.states = states;
		this.counts = new long[states.length];
	}

	public NominalAttribute(double[] range, double operandMissingValue, String[] states) {
		super(range, operandMissingValue);
		this.states = states;
		this.counts = new long[states.length];
	}

	public final long[] getStateCounts() {
		return counts;
	}

	public final String[] getStates() {
		return states;
	}

	@Override
	public String toString() {
		return states[state];
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
	public Object clone(Object value) {
		String str = String.valueOf(value);

		int state = orderOf(str);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + value);

		// increase counts
		counts[state]++;
		n++;

		return value;

	}

	protected int orderOf(String state) {
		for (int i = 0; i < states.length; i++)
			if (state.equalsIgnoreCase(states[i]))
				return i;

		return -1;
	}

	protected String stateOf(int index) {
		if (index < 0 || index >= states.length)
			return null;

		return states[index];
	}

}
