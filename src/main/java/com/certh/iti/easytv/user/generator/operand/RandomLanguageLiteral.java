package com.certh.iti.easytv.user.generator.operand;

import java.util.Random;

import com.certh.iti.easytv.user.preference.operand.LanguageLiteral;

public class RandomLanguageLiteral extends LanguageLiteral {

	public RandomLanguageLiteral(Random rand) {
		super(LanguageLiteral.languagesStr[rand.nextInt(LanguageLiteral.languagesStr.length)]);
	}

}
