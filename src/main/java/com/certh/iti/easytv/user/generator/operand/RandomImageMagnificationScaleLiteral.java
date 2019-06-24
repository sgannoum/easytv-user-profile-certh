package com.certh.iti.easytv.user.generator.operand;

import java.util.Random;

import com.certh.iti.easytv.user.preference.operand.NumericLiteral;

public class RandomImageMagnificationScaleLiteral extends NumericLiteral {

	public RandomImageMagnificationScaleLiteral(Random literal) {
		super(literal.nextInt(20), new double[] {0.0, 20.0});
	}

}
