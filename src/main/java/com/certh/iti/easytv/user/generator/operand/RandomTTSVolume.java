package com.certh.iti.easytv.user.generator.operand;

import java.util.Random;

import com.certh.iti.easytv.user.preference.operand.NumericLiteral;

public class RandomTTSVolume extends NumericLiteral {

	public RandomTTSVolume(Random literal) {
		super(literal.nextInt(100));
	}

}
