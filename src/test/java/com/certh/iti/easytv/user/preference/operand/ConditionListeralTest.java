package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConditionListeralTest {
	
	
	private JSONObject jsonCondition1 = new JSONObject("{\r\n" + 
			"              \"type\": \"ge\",\r\n" + 
			"              \"operands\": [\r\n" + 
			"                \"time\",\r\n" + 
			"                \"19:00\"\r\n" + 
			"              ]\r\n" + 
			"            }");
	
	
	private JSONObject jsonCondition2 = new JSONObject("{\r\n" + 
			"              \"type\": \"le\",\r\n" + 
			"              \"operands\": [\r\n" + 
			"                \"time\",\r\n" + 
			"                \"19:00\"\r\n" + 
			"              ]\r\n" + 
			"            }");
	
	
	private JSONObject jsonCondition3 = new JSONObject("{\r\n" + 
			"              \"type\": \"le\",\r\n" + 
			"              \"operands\": [\r\n" + 
			"                \"time\",\r\n" + 
			"                \"19:30\"\r\n" + 
			"              ]\r\n" + 
			"            }");
	
	@Test
	public void test_constructor() {
		new ConditionLiteral(jsonCondition1);
	}
	
	@Test(expectedExceptions=ClassCastException.class)
	public void test_wrong_constructor() {
		ConditionLiteral conditionLiteral1 = new ConditionLiteral(15);
	}
	
	@Test
	public void test_distanceTo1() {
		ConditionLiteral conditionLiteral1 = new ConditionLiteral(jsonCondition1);
		ConditionLiteral conditionLiteral2 = new ConditionLiteral(jsonCondition2);

		Assert.assertEquals(conditionLiteral1.distanceTo(conditionLiteral2), 0.0);
	}

	//@Test
	public void test_distanceTo2() {
		
		//TO=DO implement a test that compare two conditions after specifying how
		//actually to compare two conditions
		
		ConditionLiteral conditionLiteral1 = new ConditionLiteral(jsonCondition1);
		ConditionLiteral conditionLiteral2 = new ConditionLiteral(jsonCondition3);

		Assert.assertEquals(conditionLiteral1.distanceTo(conditionLiteral2), -1.0);
	}
	
	@Test
	public void test_equals() {
		ConditionLiteral conditionLiteral1 = new ConditionLiteral(jsonCondition1);
		ConditionLiteral conditionLiteral2 = new ConditionLiteral(jsonCondition2);

		Assert.assertEquals(conditionLiteral1, conditionLiteral2);
	}
	
	@Test
	public void test_not_equals() {
		ConditionLiteral conditionLiteral1 = new ConditionLiteral(jsonCondition1);
		ConditionLiteral conditionLiteral2 = new ConditionLiteral(jsonCondition3);

		Assert.assertNotEquals(conditionLiteral1, conditionLiteral2);
	}
	

}
