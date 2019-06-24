package com.certh.iti.easytv.user.generator.operand;

import java.util.Random;

import com.certh.iti.easytv.user.preference.operand.NumericLiteral;


public class RandomTTSSpeed extends NumericLiteral {

	public RandomTTSSpeed(Random literal) {
		super(literal.nextInt(20) - 10, new double[] {-10.0, 10.0});
	}

}
