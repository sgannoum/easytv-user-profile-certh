package com.certh.iti.easytv.user.preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.preference.operand.BooleanLiteral;
import com.certh.iti.easytv.user.preference.operand.OperandLiteral;
import com.certh.iti.easytv.user.preference.operand.ConditionLiteral;
import com.certh.iti.easytv.user.preference.operand.NumericLiteral;
import com.certh.iti.easytv.user.preference.operand.StringLiteral;
import com.certh.iti.easytv.user.preference.operand.SymmetricBooleanLiteral;

public class ConditionTest {
	
	
	@Test
	public void test_condition_construction() {

		List<OperandLiteral> operands_One = new ArrayList<OperandLiteral>();
		List<OperandLiteral> operands_Two = new ArrayList<OperandLiteral>();
		List<OperandLiteral> operands_MoreThanTwo= new ArrayList<OperandLiteral>();
		
		JSONObject jsonCondition1 = new JSONObject("{\r\n" + 
				"              \"type\": \"ge\",\r\n" + 
				"              \"operands\": [\r\n" + 
				"                \"time\",\r\n" + 
				"                \"NaN:NaN\"\r\n" + 
				"              ]\r\n" + 
				"            }");
		
		
		JSONObject jsonCondition2 = new JSONObject("{\r\n" + 
				"              \"type\": \"le\",\r\n" + 
				"              \"operands\": [\r\n" + 
				"                \"time\",\r\n" + 
				"                \"NaN:NaN\"\r\n" + 
				"              ]\r\n" + 
				"            }");
		
		JSONObject jsonCondition3 = new JSONObject("{\r\n" + 
				"          \"type\": \"ne\",\r\n" + 
				"          \"operands\": [\r\n" + 
				"            \"language_sign\",\r\n" + 
				"            \"spanish\"\r\n" + 
				"          ]\r\n" + 
				"        }");
		
		
		operands_One.add(new ConditionLiteral(jsonCondition1));
		
		operands_Two.add(new ConditionLiteral(jsonCondition1));
		operands_Two.add(new ConditionLiteral(jsonCondition2));
		
		operands_MoreThanTwo.add(new ConditionLiteral(jsonCondition1));
		operands_MoreThanTwo.add(new ConditionLiteral(jsonCondition2));
		operands_MoreThanTwo.add(new ConditionLiteral(jsonCondition3));


		String[] types =  { "NOT", "EQ" ,"NE","LT","LE","GT","GE","AND","OR","AP"};
		Object[] operands = {operands_One, operands_Two, operands_Two, operands_Two, operands_Two, operands_Two, operands_Two, operands_MoreThanTwo, operands_MoreThanTwo, operands_Two};

		for(int i = 0; i < types.length; i++) {
			new Condition(types[i], (List<OperandLiteral>) operands[i]);
		}
	}
	
	@Test
	public void test_incompatibility_type_condition() {

		List<IllegalStateException> expectedExceptions = new ArrayList<IllegalStateException>();
		List<IllegalStateException> actualExceptions = new ArrayList<IllegalStateException>();

		List<OperandLiteral> operands_One = new ArrayList<OperandLiteral>();
		List<OperandLiteral> operands_Two = new ArrayList<OperandLiteral>();
		List<OperandLiteral> operands_MoreThanTwo= new ArrayList<OperandLiteral>();
		
		JSONObject jsonCondition1 = new JSONObject("{\r\n" + 
				"              \"type\": \"ge\",\r\n" + 
				"              \"operands\": [\r\n" + 
				"                \"time\",\r\n" + 
				"                \"NaN:NaN\"\r\n" + 
				"              ]\r\n" + 
				"            }");
		
		
		JSONObject jsonCondition2 = new JSONObject("{\r\n" + 
				"              \"type\": \"le\",\r\n" + 
				"              \"operands\": [\r\n" + 
				"                \"time\",\r\n" + 
				"                \"NaN:NaN\"\r\n" + 
				"              ]\r\n" + 
				"            }");
		
		JSONObject jsonCondition3 = new JSONObject("{\r\n" + 
				"          \"type\": \"ne\",\r\n" + 
				"          \"operands\": [\r\n" + 
				"            \"language_sign\",\r\n" + 
				"            \"spanish\"\r\n" + 
				"          ]\r\n" + 
				"        }");
		
		
		operands_One.add(new ConditionLiteral(jsonCondition1));
		
		operands_Two.add(new ConditionLiteral(jsonCondition1));
		operands_Two.add(new ConditionLiteral(jsonCondition2));
		
		operands_MoreThanTwo.add(new ConditionLiteral(jsonCondition1));
		operands_MoreThanTwo.add(new ConditionLiteral(jsonCondition2));
		operands_MoreThanTwo.add(new ConditionLiteral(jsonCondition3));


		String[] types =  { "NOT", "EQ" ,"NE","LT","LE","GT","GE","AND","OR","AP"};
		Object[] operands = {operands_Two, operands_One, operands_One, operands_One, operands_One, operands_MoreThanTwo, operands_One, operands_One, operands_One, operands_MoreThanTwo};

		for (int i = 0; i < types.length; i++)
			try {
				new Condition(types[i], (List<OperandLiteral>) operands[i]);
			} catch (IllegalStateException e) {
				actualExceptions.add(e);
			}
		
		
		expectedExceptions.add(new IllegalStateException("A condition of type NOT must have only one operand, given 2"));
		expectedExceptions.add(new IllegalStateException("A condition of type EQ must have exaclty two operands, given 1"));
		expectedExceptions.add(new IllegalStateException("A condition of type NE must have exaclty two operands, given 1"));
		expectedExceptions.add(new IllegalStateException("A condition of type LT must have exaclty two operands, given 1"));
		expectedExceptions.add(new IllegalStateException("A condition of type LE must have exaclty two operands, given 1"));
		expectedExceptions.add(new IllegalStateException("A condition of type GT must have exaclty two operands, given 3"));
		expectedExceptions.add(new IllegalStateException("A condition of type GE must have exaclty two operands, given 1"));
		expectedExceptions.add(new IllegalStateException("A condition of type AND must have at least two operands, given 1"));
		expectedExceptions.add(new IllegalStateException("A condition of type OR must have at least two operands, given 1"));
		expectedExceptions.add(new IllegalStateException("A condition of type AP must have exaclty two operands, given 3"));

		for(int i = 0 ; i < actualExceptions.size(); i++)
			Assert.assertEquals(actualExceptions.get(i).getMessage(), expectedExceptions.get(i).getMessage());
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void test_unknown_condition() {

		List<OperandLiteral> operands_One = new ArrayList<OperandLiteral>();
		
		JSONObject jsonCondition1 = new JSONObject("{\r\n" + 
				"              \"type\": \"ge\",\r\n" + 
				"              \"operands\": [\r\n" + 
				"                \"time\",\r\n" + 
				"                \"NaN:NaN\"\r\n" + 
				"              ]\r\n" + 
				"            }");
		
		operands_One.add(new ConditionLiteral(jsonCondition1));
	
		String[] types =  { "NON"};
		Object[] operands = {operands_One};

		for(int i = 0; i < types.length; i++) {
			new Condition(types[i], (List<OperandLiteral>) operands[i]);
		}
	}
	
	@Test
	public void test_condition_equality_without_inner_conditions() {

		List<Condition> actualConditions= new ArrayList<Condition>();
		List<Condition> expectedConditions= new ArrayList<Condition>();
		List<OperandLiteral> expectedOperands = new ArrayList<OperandLiteral>();
		
		JSONObject jsonCondition1 = new JSONObject("{\"conditions\": [\r\n" + 
				"        {\r\n" + 
				"          \"type\": \"and\",\r\n" + 
				"          \"operands\": [\r\n" + 
				"                \"time\",\r\n" + 
				"                \"19:00\",\r\n" + 
				"                \"contrast\",\r\n" + 
				"                100,\r\n" + 
				"                \"color_temperature\",\r\n" + 
				"                0.008,\r\n" + 
				"                \"avatar\",\r\n" + 
				"                true\r\n" + 
				"          ]\r\n" + 
				"        }\r\n" + 
				"      ]}");
		
		
		JSONArray jsonConditions = jsonCondition1.getJSONArray("conditions");
		for(int i = 0 ; i < jsonConditions.length(); i++) 
			actualConditions.add(new Condition(jsonConditions.getJSONObject(i)));
		
		expectedConditions.add(new Condition("AND", new ArrayList<OperandLiteral>(Arrays.asList(new StringLiteral("time"), new StringLiteral("19:00"), 
																								  new StringLiteral("contrast"), new NumericLiteral(100),
																								  new StringLiteral("color_temperature"), new NumericLiteral(0.008),
																								  new StringLiteral("avatar"), new SymmetricBooleanLiteral(true)
																								  ))));
	
		Assert.assertEquals(actualConditions, expectedConditions);
	}
	
	@Test
	public void test_condition_proper_construction() {

		List<Condition> actualConditions= new ArrayList<Condition>();
		List<Condition> expectedConditions= new ArrayList<Condition>();
		List<OperandLiteral> expectedOperands = new ArrayList<OperandLiteral>();
		
		JSONObject jsonCondition1 = new JSONObject("{\"conditions\": [\r\n" + 
													"        {\r\n" + 
													"          \"type\": \"and\",\r\n" + 
													"          \"operands\": [\r\n" + 
													"            {\r\n" + 
													"              \"type\": \"ge\",\r\n" + 
													"              \"operands\": [\r\n" + 
													"                \"time\",\r\n" + 
													"                \"19:00\"\r\n" + 
													"              ]\r\n" + 
													"            },\r\n" + 
													"            {\r\n" + 
													"              \"type\": \"le\",\r\n" + 
													"              \"operands\": [\r\n" + 
													"                \"contrast\",\r\n" + 
													"                100\r\n" + 
													"              ]\r\n" + 
													"            },\r\n" + 
													"            {\r\n" + 
													"              \"type\": \"le\",\r\n" + 
													"              \"operands\": [\r\n" + 
													"                \"color_temperature\",\r\n" + 
													"                0.008\r\n" + 
													"              ]\r\n" + 
													"            },\r\n" + 
													"            {\r\n" + 
													"              \"type\": \"le\",\r\n" + 
													"              \"operands\": [\r\n" + 
													"                \"avatar\",\r\n" + 
													"                true\r\n" + 
													"              ]\r\n" + 
													"            }\r\n" + 
													"          ]\r\n" + 
													"        }\r\n" + 
													"      ]"
													+ "}");
		
		
		JSONArray jsonConditions = jsonCondition1.getJSONArray("conditions");
		for(int i = 0 ; i < jsonConditions.length(); i++) 
			actualConditions.add(new Condition(jsonConditions.getJSONObject(i)));
		
		expectedOperands.add(new ConditionLiteral(new Condition("ge", new ArrayList<OperandLiteral>(Arrays.asList(new StringLiteral("time"), new StringLiteral("19:00"))))));
		expectedOperands.add(new ConditionLiteral(new Condition("lt", new ArrayList<OperandLiteral>(Arrays.asList(new StringLiteral("contrast"), new NumericLiteral(100))))));
		expectedOperands.add(new ConditionLiteral(new Condition("lt", new ArrayList<OperandLiteral>(Arrays.asList(new StringLiteral("color_temperature"), new NumericLiteral(0.008))))));
		expectedOperands.add(new ConditionLiteral(new Condition("lt", new ArrayList<OperandLiteral>(Arrays.asList(new StringLiteral("avatar"), new SymmetricBooleanLiteral(true))))));
		expectedConditions.add(new Condition("and", expectedOperands));
	
		Assert.assertEquals(actualConditions, expectedConditions);
	}
	
	@Test
	public void test_toJSON() {
		
		JSONObject jsonCondition1 = new JSONObject("        {\r\n" + 
				"          \"type\": \"and\",\r\n" + 
				"          \"operands\": [\r\n" + 
				"            {\r\n" + 
				"              \"type\": \"ge\",\r\n" + 
				"              \"operands\": [\r\n" + 
				"                \"time\",\r\n" + 
				"                \"19:00\"\r\n" + 
				"              ]\r\n" + 
				"            },\r\n" + 
				"            {\r\n" + 
				"              \"type\": \"le\",\r\n" + 
				"              \"operands\": [\r\n" + 
				"                \"contrast\",\r\n" + 
				"                100\r\n" + 
				"              ]\r\n" + 
				"            },\r\n" + 
				"            {\r\n" + 
				"              \"type\": \"le\",\r\n" + 
				"              \"operands\": [\r\n" + 
				"                \"color_temperature\",\r\n" + 
				"                0.008\r\n" + 
				"              ]\r\n" + 
				"            },\r\n" + 
				"            {\r\n" + 
				"              \"type\": \"le\",\r\n" + 
				"              \"operands\": [\r\n" + 
				"                \"avatar\",\r\n" + 
				"                true\r\n" + 
				"              ]\r\n" + 
				"            }\r\n" + 
				"          ]\r\n" + 
				"        }"
				+ "}");
		
		Condition condition1 = new Condition(jsonCondition1);
		Condition condition2 = new Condition(condition1.getType(), condition1.getOperands());
				
		Assert.assertTrue(condition2.toJSON().similar(jsonCondition1));
	}

}
