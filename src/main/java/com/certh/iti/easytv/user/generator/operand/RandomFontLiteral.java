package com.certh.iti.easytv.user.generator.operand;

import java.util.Random;

import com.certh.iti.easytv.user.preference.operand.FontLiteral;

public class RandomFontLiteral extends FontLiteral {

	public RandomFontLiteral(Random rand) {
		super(FontLiteral.fontsStr[rand.nextInt(FontLiteral.fontsStr.length)]);
	}

}
