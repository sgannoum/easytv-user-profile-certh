package com.certh.iti.easytv.user.preference.operand;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConditionListeralTest {
	
	
	private JSONObject jsonCondition1 = new JSONObject("{\r\n" + 
			"              \"type\": \"ge\",\r\n" + 
			"              \"operands\": [\r\n" + 
			"                \"http://registry.easytv.eu/context/time\",\r\n" + 
			"                \"2019-11-30T09:47:47.619Z\",\r\n" + 
			"              ]\r\n" + 
			"            }");
	
	
	private JSONObject jsonCondition2 = new JSONObject("{\r\n" + 
			"              \"type\": \"ge\",\r\n" + 
			"              \"operands\": [\r\n" + 
			"                \"http://registry.easytv.eu/context/time\",\r\n" + 
			"                \"2019-11-30T09:47:47.619Z\",\r\n" + 
			"              ]\r\n" + 
			"            }");
	
	
	private JSONObject jsonCondition3 = new JSONObject("{\r\n" + 
			"              \"type\": \"le\",\r\n" + 
			"              \"operands\": [\r\n" + 
			"                \"http://registry.easytv.eu/context/time\",\r\n" + 
			"                \"2019-10-30T09:47:47.619Z\",\r\n" + 
			"              ]\r\n" + 
			"            }");
	
	private JSONObject jsonCondition4 = new JSONObject("{\r\n" + 
			"              \"type\": \"ge\",\r\n" + 
			"              \"operands\": [\r\n" + 
			"                \"http://registry.easytv.eu/context/time\",\r\n" + 
			"                \"2019-10-30T09:47:47.619Z\",\r\n" + 
			"              ]\r\n" + 
			"            }");
	
	@Test
	public void test_constructor() {
		new ConditionLiteral(jsonCondition1);
	}
	
	@Test(expectedExceptions=ClassCastException.class)
	public void test_wrong_constructor() {
		new ConditionLiteral(15);
	}
	
	@Test
	public void test_equals() {
		ConditionLiteral conditionLiteral1 = new ConditionLiteral(jsonCondition1);
		ConditionLiteral conditionLiteral2 = new ConditionLiteral(jsonCondition2);

		Assert.assertEquals(conditionLiteral1, conditionLiteral2);
	}
	
	@Test
	public void test_different_condition_types() {
		ConditionLiteral conditionLiteral1 = new ConditionLiteral(jsonCondition1);
		ConditionLiteral conditionLiteral2 = new ConditionLiteral(jsonCondition3);
		
		Assert.assertNotEquals(conditionLiteral1, conditionLiteral2);
	}
	
	@Test
	public void test_different_condition_values() {
		ConditionLiteral conditionLiteral1 = new ConditionLiteral(jsonCondition1);
		ConditionLiteral conditionLiteral2 = new ConditionLiteral(jsonCondition4);
		
		Assert.assertNotEquals(conditionLiteral1, conditionLiteral2);
	}
	

}
