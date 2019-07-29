package com.certh.iti.easytv.user.preference.attributes;

public class IntegerAttribute extends NumericAttribute {

	public IntegerAttribute(double[] range) {
		super(range);
	}

	public IntegerAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
	}

	@Override
	public double[] getPoints(Object literal) {
		if (literal == null) {
			return new double[] { missingValue };
		}

		int value = (int) literal;
		return new double[] { value };
	}

	@Override
	public Object handle(Object value) {

		int numericValue = Integer.class.cast(value);

		// Increate histogram counts
		Long tmp = frequencyHistogram.get(numericValue);
		if (tmp == null) {
			frequencyHistogram.put((double) numericValue, 1L);
		} else {
			frequencyHistogram.put((double) numericValue, tmp + 1);
		}

		// Set Min Max vlaue
		setMinMaxValue(numericValue);

		sum += numericValue;

		n++;
		return value;
	}

}
