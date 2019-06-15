package com.certh.iti.easytv.user.generator.operand;

import java.util.Random;

import com.certh.iti.easytv.user.preference.operand.ColorLiteral;


public class RandomColorLiteral extends ColorLiteral {

	public RandomColorLiteral(Random rand) {
		super("#"+Integer.toHexString(rand.nextInt(255))+Integer.toHexString(rand.nextInt(255))+Integer.toHexString(rand.nextInt(255)));
	}

}
