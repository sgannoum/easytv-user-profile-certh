package com.certh.iti.easytv.user.generator.operand;

import java.util.Random;

import com.certh.iti.easytv.user.preference.operand.NumericLiteral;

public class RandomDisplayContrastLiteral extends NumericLiteral {

	public RandomDisplayContrastLiteral(Random literal) {
		super(literal.nextInt(100), new double[]{0.0, 100.0});
	}

}
