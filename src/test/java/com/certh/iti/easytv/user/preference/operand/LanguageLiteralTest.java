package com.certh.iti.easytv.user.preference.operand;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LanguageLiteralTest {
	
	@Test
	public void test_constructor1() {
		
		final String[] languagesStr = {"ENGLISH", "SPANISH", "CATALAN", "GREEK", "ITALIAN"};
		
		for(int i = 0; i < languagesStr.length; i++) {
			LanguageLiteral languageLiteral = new LanguageLiteral(languagesStr[i]);
			Assert.assertEquals(languageLiteral.getValue(), languagesStr[i]);
		}
			
	}

	@Test(expectedExceptions=IllegalStateException.class)
	public void test_wrong_constructor() {
		new LanguageLiteral("gibilish");
	}
	
	@Test
	public void test_distanceTo1() {
		LanguageLiteral languageLiteral1 = new LanguageLiteral("ENGLISH");
		LanguageLiteral languageLiteral2 = new LanguageLiteral("ENGLISH");

		Assert.assertEquals(languageLiteral1.distanceTo(languageLiteral2), 0.0);
	}
	
	@Test
	public void test_distanceTo2() {
		LanguageLiteral languageLiteral1 = new LanguageLiteral("ENGLISH");
		LanguageLiteral languageLiteral2 = new LanguageLiteral("SPANISH");

		Assert.assertEquals(languageLiteral1.distanceTo(languageLiteral2), 1.0);
	}
	
	@Test
	public void test_distanceTo3() {
		LanguageLiteral languageLiteral1 = new LanguageLiteral("SPANISH");
		LanguageLiteral languageLiteral2 = new LanguageLiteral("GREEK");

		Assert.assertEquals(languageLiteral1.distanceTo(languageLiteral2), 4.0);
	}
	
	@Test
	public void test_equals() {
		LanguageLiteral languageLiteral1 = new LanguageLiteral("SPANISH");
		LanguageLiteral languageLiteral2 = new LanguageLiteral("SPANISH");

		Assert.assertEquals(languageLiteral1, languageLiteral2);
	}
	
	@Test
	public void test_not_equals() {
		LanguageLiteral languageLiteral1 = new LanguageLiteral("SPANISH");
		LanguageLiteral languageLiteral2 = new LanguageLiteral("GREEK");

		Assert.assertNotEquals(languageLiteral1, languageLiteral2);
	}

}
