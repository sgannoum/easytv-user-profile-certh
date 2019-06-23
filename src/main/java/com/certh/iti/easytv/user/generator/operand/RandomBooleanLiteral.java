package com.certh.iti.easytv.user.generator.operand;

import java.util.Random;

import com.certh.iti.easytv.user.preference.operand.SymmetricBooleanLiteral;

public class RandomBooleanLiteral extends SymmetricBooleanLiteral {

	public RandomBooleanLiteral(Random rand) {
		super(rand.nextBoolean());
	}
}
