package com.certh.iti.easytv.user.preference.attributes;

public class DoubleAttribute extends NumericAttribute {

	public DoubleAttribute(double[] range) {
		super(range);
	}

	public DoubleAttribute(double[] range, double operandMissingValue) {
		super(range);
	}

	@Override
	public double[] getPoints(Object literal) {
		if (literal == null) {
			return new double[] { missingValue };
		}

		double value = (double) literal;
		return new double[] { value };
	}

	@Override
	public Object clone(Object value) {

		double numericValue = Double.class.cast(value);

		// Increate histogram counts
		Long tmp = frequencyHistogram.get(numericValue);
		if (tmp == null) {
			frequencyHistogram.put(numericValue, 1L);
		} else {
			frequencyHistogram.put(numericValue, tmp + 1);
		}

		// Set Min Max vlaue
		setMinMaxValue(numericValue);

		sum += numericValue;

		n++;
		return value;

	}

}
