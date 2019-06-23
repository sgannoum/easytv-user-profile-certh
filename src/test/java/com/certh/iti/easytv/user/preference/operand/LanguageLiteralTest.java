package com.certh.iti.easytv.user.preference.operand;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LanguageLiteralTest {
	
	@Test
	public void test_constructor1() {
		
		final String[] languagesStr = {"EN", "ES", "CA", "GR", "IT"};
		
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
	public void test_equals() {
		LanguageLiteral languageLiteral1 = new LanguageLiteral("es");
		LanguageLiteral languageLiteral2 = new LanguageLiteral("es");

		Assert.assertEquals(languageLiteral1, languageLiteral2);
	}
	
	@Test
	public void test_not_equals() {
		LanguageLiteral languageLiteral1 = new LanguageLiteral("es");
		LanguageLiteral languageLiteral2 = new LanguageLiteral("gr");

		Assert.assertNotEquals(languageLiteral1, languageLiteral2);
	}

}
