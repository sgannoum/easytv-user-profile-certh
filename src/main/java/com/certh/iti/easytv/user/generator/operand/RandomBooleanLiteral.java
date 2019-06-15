package com.certh.iti.easytv.user.generator.operand;

import java.util.Random;

import com.certh.iti.easytv.user.preference.operand.BooleanLiteral;

public class RandomBooleanLiteral extends BooleanLiteral {

	public RandomBooleanLiteral(Random rand) {
		super(rand.nextBoolean());
	}
}
