package com.certh.iti.easytv.user.preference.operand;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NumericLiteralTest {
	
	@Test
	public void test_constructor() {
		new NumericLiteral(1);
	}
	
	@Test(expectedExceptions=ClassCastException.class)
	public void test_wrong_constructor() {
		new NumericLiteral("Not numberic");
	}
	
	@Test
	public void test_equals() {
		NumericLiteral numericLiteral1 = new NumericLiteral(1.0);
		NumericLiteral numericLiteral2 = new NumericLiteral(1);

		Assert.assertEquals(numericLiteral1, numericLiteral2);
	}
	
	@Test
	public void test_not_equals() {
		NumericLiteral numericLiteral1 = new NumericLiteral(1.0);
		NumericLiteral numericLiteral2 = new NumericLiteral(2);

		Assert.assertNotEquals(numericLiteral1, numericLiteral2);
	}
	
	@Test
	public void test_cloned_states_counts() {
		NumericLiteral numericLiteral = new NumericLiteral(0);
		
		numericLiteral.clone(1);
		numericLiteral.clone(1);

		numericLiteral.clone(2);
		numericLiteral.clone(2);


		Assert.assertEquals(numericLiteral.getMaxValue(), 2.0);
		Assert.assertEquals(numericLiteral.getMinValue(), 1.0);
		
	}
	
	@Test
	public void test_entries_counts() {
		NumericLiteral numericLiteral = new NumericLiteral(0);
		
		numericLiteral.clone(1);
		numericLiteral.clone(1);

		numericLiteral.clone(2);
		numericLiteral.clone(2);
		
		numericLiteral.clone(3);
		numericLiteral.clone(3);
	
		
		Assert.assertEquals(numericLiteral.getEntriesCounts(), new double[][]{{1, 2}, 
																			  {2, 2},
																			  {3, 2}});
	}


}
