package com.certh.iti.easytv.user.preference.operand;

import org.testng.Assert;
import org.testng.annotations.Test;

public class OrdinalLiteralTest {
	
	@Test
	public void test_cloned_states_counts() {
		OrdinalLiteral ordenalLiteral = new OrdinalLiteral("1", new String[] {"1", "2", "3"});
		
		ordenalLiteral.clone("1");
		ordenalLiteral.clone("1");

		ordenalLiteral.clone("2");
		ordenalLiteral.clone("2");


		Assert.assertEquals(ordenalLiteral.getStatCounts(), new long[] {2, 2, 0});
	}


	@Test
	public void test_cloned_states_counts_1() {
		OrdinalLiteral ordinalLiteral = new OrdinalLiteral("1", new String[] {"1", "2", "3"});
		
		ordinalLiteral.clone("1");
		ordinalLiteral.clone("1");

		ordinalLiteral.clone("2");
		ordinalLiteral.clone("2");
		ordinalLiteral.clone("2");


		Assert.assertNotEquals(ordinalLiteral.getStatCounts(), new long[] {2, 2, 0});
	}
	
	@Test
	public void test_Min_Max_states() {
		OrdinalLiteral ordinalLiteral = new OrdinalLiteral("1", new String[] {"1", "2", "3"});
		
		ordinalLiteral.clone("1");
		ordinalLiteral.clone("1");

		ordinalLiteral.clone("2");
		ordinalLiteral.clone("2");
		ordinalLiteral.clone("2");


		Assert.assertEquals(ordinalLiteral.getMaxValue(), 1.0);
		Assert.assertEquals(ordinalLiteral.getMinValue(), 0.0);

	}

}
