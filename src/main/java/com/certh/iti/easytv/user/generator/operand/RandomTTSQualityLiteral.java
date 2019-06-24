package com.certh.iti.easytv.user.generator.operand;

import java.util.Random;

import com.certh.iti.easytv.user.preference.operand.NumericLiteral;

public class RandomTTSQualityLiteral extends NumericLiteral {

	public RandomTTSQualityLiteral(Random literal) {
		super(literal.nextInt(8), new double[] {0.0, 8.0});
	}

}
