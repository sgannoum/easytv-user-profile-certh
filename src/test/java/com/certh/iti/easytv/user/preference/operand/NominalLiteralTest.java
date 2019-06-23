package com.certh.iti.easytv.user.preference.operand;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NominalLiteralTest {
	
	@Test
	public void test_cloned_states_counts() {
		NominalLiteral nominalLiteral = new NominalLiteral("1", new String[] {"1", "2", "3"});
		
		nominalLiteral.clone("1");
		nominalLiteral.clone("1");

		nominalLiteral.clone("2");
		nominalLiteral.clone("2");


		Assert.assertEquals(nominalLiteral.getStatCounts(), new long[] {2, 2, 0});
	}


	@Test
	public void test_cloned_states_counts_1() {
		NominalLiteral nominalLiteral = new NominalLiteral("1", new String[] {"1", "2", "3"});
		
		nominalLiteral.clone("1");
		nominalLiteral.clone("1");

		nominalLiteral.clone("2");
		nominalLiteral.clone("2");
		nominalLiteral.clone("2");


		Assert.assertNotEquals(nominalLiteral.getStatCounts(), new long[] {2, 2, 0});
	}
}
