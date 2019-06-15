package com.certh.iti.easytv.user.generator.operand;

import java.util.Random;

import com.certh.iti.easytv.user.preference.operand.NumericLiteral;

public class RandomDoubleLiteral extends NumericLiteral {

	public RandomDoubleLiteral(Random literal) {
		super(literal.nextDouble());
	}

}