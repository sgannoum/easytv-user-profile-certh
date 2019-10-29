package com.certh.iti.easytv.user.preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;

public class ConditionTest {
	
	
	@Test
	public void test_condition_construction() throws UserProfileParsingException {

		List<Object> operands_One = new ArrayList<Object>();
		List<Object> operands_Two = new ArrayList<Object>();
		List<Object> operands_MoreThanTwo= new ArrayList<Object>();
		
		JSONObject jsonCondition1 = new JSONObject("{" + 
				"              \"type\": \"ge\"," + 
				"              \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/time\"," + 
				"                \"2019-11-30T09:47:47.619Z\"," + 
				"              ]" + 
				"            }");
		
		
		JSONObject jsonCondition2 = new JSONObject("{" + 
				"              \"type\": \"le\"," + 
				"              \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/time\"," + 
				"                \"2019-11-30T09:47:47.619Z\"," + 
				"              ]" + 
				"            }");
		
		JSONObject jsonCondition3 = new JSONObject("{" + 
				"          \"type\": \"ne\"," + 
				"          \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/location\"," + 
				"            \"es\"" + 
				"          ]" + 
				"        }");
		
		
		operands_One.add(new Condition(jsonCondition1));
		
		operands_Two.add(new Condition(jsonCondition1));
		operands_Two.add(new Condition(jsonCondition2));
		
		operands_MoreThanTwo.add(new Condition(jsonCondition1));
		operands_MoreThanTwo.add(new Condition(jsonCondition2));
		operands_MoreThanTwo.add(new Condition(jsonCondition3));


		String[] types =  { "NOT", "EQ" ,"NE","LT","LE","GT","GE","AND","OR","AP"};
		Object[] operands = {operands_One, operands_Two, operands_Two, operands_Two, operands_Two, operands_Two, operands_Two, operands_MoreThanTwo, operands_MoreThanTwo, operands_Two};

		for(int i = 0; i < types.length; i++) {
			new Condition(types[i], (List<Object>) operands[i]);
		}
	}
	
	@Test
	public void test_incompatibility_type_condition() throws UserProfileParsingException {

		List<IllegalStateException> expectedExceptions = new ArrayList<IllegalStateException>();
		List<IllegalStateException> actualExceptions = new ArrayList<IllegalStateException>();

		List<Object> operands_One = new ArrayList<Object>();
		List<Object> operands_Two = new ArrayList<Object>();
		List<Object> operands_MoreThanTwo= new ArrayList<Object>();
		
		JSONObject jsonCondition1 = new JSONObject("{" + 
				"              \"type\": \"ge\"," + 
				"              \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/time\"," + 
				"                \"2019-11-30T09:47:47.619Z\"," + 
				"              ]" + 
				"            }");
		
		
		JSONObject jsonCondition2 = new JSONObject("{" + 
				"              \"type\": \"le\"," + 
				"              \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/time\"," + 
				"                \"2019-11-30T09:47:47.619Z\"," + 
				"              ]" + 
				"            }");
		
		JSONObject jsonCondition3 = new JSONObject("{" + 
				"          \"type\": \"ne\"," + 
				"          \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/location\"," + 
				"            \"es\"" + 
				"          ]" + 
				"        }");
		
		
		operands_One.add(new Condition(jsonCondition1));
		
		operands_Two.add(new Condition(jsonCondition1));
		operands_Two.add(new Condition(jsonCondition2));
		
		operands_MoreThanTwo.add(new Condition(jsonCondition1));
		operands_MoreThanTwo.add(new Condition(jsonCondition2));
		operands_MoreThanTwo.add(new Condition(jsonCondition3));


		String[] types =  { "NOT", "EQ" ,"NE","LT","LE","GT","GE","AND","OR","AP"};
		Object[] operands = {operands_Two, operands_One, operands_One, operands_One, operands_One, operands_MoreThanTwo, operands_One, operands_One, operands_One, operands_MoreThanTwo};

		for (int i = 0; i < types.length; i++)
			try {
				new Condition(types[i], (List<Object>) operands[i]);
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
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void test_unknown_condition() throws UserProfileParsingException {

		List<Object> operands_One = new ArrayList<Object>();
		
		JSONObject jsonCondition1 = new JSONObject("{" + 
				"              \"type\": \"ge\"," + 
				"              \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/time\"," + 
				"                \"2019-11-30T09:47:47.619Z\"" + 
				"              ]" + 
				"            }");
		
		operands_One.add(new Condition(jsonCondition1));
	
		String[] types =  { "NON"};
		Object[] operands = {operands_One};

		for(int i = 0; i < types.length; i++) {
			new Condition(types[i], (List<Object>) operands[i]);
		}
	}
	
	@Test
	public void test_condition_equality_without_inner_conditions() throws JSONException, UserProfileParsingException {

		List<Condition> actualConditions= new ArrayList<Condition>();
		List<Condition> expectedConditions= new ArrayList<Condition>();
		List<Object> expectedOperands = new ArrayList<Object>();
		
		JSONObject jsonCondition1 = new JSONObject("{\"conditions\": [" + 
				"        {" + 
				"          \"type\": \"and\"," + 
				"          \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/time\"," + 
				"                \"09:47:47\"," + 
				"                \"http://registry.easytv.eu/context/location\"," + 
				"                \"gr\"" + 
				"          ]" + 
				"        }" + 
				"      ]}");
		
		
		JSONArray jsonConditions = jsonCondition1.getJSONArray("conditions");
		for(int i = 0 ; i < jsonConditions.length(); i++) 
			actualConditions.add(new Condition(jsonConditions.getJSONObject(i)));
		
		expectedConditions.add(new Condition("AND", Arrays.asList("http://registry.easytv.eu/context/time", "09:47:47", 
																	"http://registry.easytv.eu/context/location", "gr")
																								  ));

		Assert.assertEquals(actualConditions, expectedConditions);
	}
	
	@Test
	public void test_condition_proper_construction() throws JSONException, UserProfileParsingException {

		List<Condition> actualConditions= new ArrayList<Condition>();
		List<Condition> expectedConditions= new ArrayList<Condition>();
		List<Object> expectedOperands = new ArrayList<Object>();
		
		JSONObject jsonCondition1 = new JSONObject("{\"conditions\": [" + 
													"        {" + 
													"          \"type\": \"and\"," + 
													"          \"operands\": [" + 
													"            {" + 
													"              \"type\": \"ge\"," + 
													"              \"operands\": [" + 
													"                \"http://registry.easytv.eu/context/time\"," + 
													"                \"2019-01-30T09:47:47.619Z\"," + 
													"              ]" + 
													"            }," + 
													"            {" + 
													"              \"type\": \"le\"," + 
													"              \"operands\": [" + 
													"                \"http://registry.easytv.eu/context/time\"," + 
													"                \"2019-11-30T09:47:47.619Z\"," + 
													"              ]" + 
													"            }," + 
													"            {" + 
													"              \"type\": \"ge\"," + 
													"              \"operands\": [" + 
													"                \"http://registry.easytv.eu/context/contrast\"," + 
													"                10," + 
													"              ]" + 
													"            }," + 
													"            {" + 
													"              \"type\": \"le\"," + 
													"              \"operands\": [" + 
													"                \"http://registry.easytv.eu/context/contrast\"," + 
													"                90," + 
													"              ]" + 
													"            }" + 
													"          ]" + 
													"        }" + 
													"      ]"
													+ "}");
		
		
		JSONArray jsonConditions = jsonCondition1.getJSONArray("conditions");
		for(int i = 0 ; i < jsonConditions.length(); i++) 
			actualConditions.add(new Condition(jsonConditions.getJSONObject(i)));
		
		expectedOperands.add(new Condition("ge", new ArrayList<Object>(Arrays.asList("http://registry.easytv.eu/context/time", "2019-01-30T09:47:47.619Z"))));
		expectedOperands.add(new Condition("le", new ArrayList<Object>(Arrays.asList("http://registry.easytv.eu/context/time", "2019-11-30T09:47:47.619Z"))));
		expectedOperands.add(new Condition("ge", new ArrayList<Object>(Arrays.asList("http://registry.easytv.eu/context/contrast",  10))));
		expectedOperands.add(new Condition("le", new ArrayList<Object>(Arrays.asList("http://registry.easytv.eu/context/contrast",  90))));
		expectedConditions.add(new Condition("and", expectedOperands));
	
		Assert.assertEquals(actualConditions, expectedConditions);
	}
	
	@Test
	public void test_toJSON() throws UserProfileParsingException {
		
		JSONObject jsonCondition1 = new JSONObject(" {" + 
				"          \"type\": \"and\"," + 
				"          \"operands\": [" + 
				"            {" + 
				"              \"type\": \"ge\"," + 
				"              \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/time\"," + 
				"                \"12:00:00\"," + 
				"              ]" + 
				"            }," + 
				"            {" + 
				"              \"type\": \"le\"," + 
				"              \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/time\"," + 
				"                \"12:00:00\"," + 
				"              ]" + 
				"            }," + 
				"            {" + 
				"              \"type\": \"ge\"," + 
				"              \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/contrast\"," + 
				"                10," + 
				"              ]" + 
				"            }," + 
				"            {" + 
				"              \"type\": \"le\"," + 
				"              \"operands\": [" + 
				"                \"http://registry.easytv.eu/context/contrast\"," + 
				"                90," + 
				"              ]" + 
				"            }" + 
				"          ]" + 
				"        }");
		
		Condition condition1 = new Condition(jsonCondition1);
		Condition condition2 = new Condition(condition1.getType(), condition1.getOperands());
		
		System.out.println(condition2.geJSONObject().toString(4));
		
		Assert.assertTrue(condition2.geJSONObject().similar(jsonCondition1));
	}
	
	@Test
	public void test_to() throws UserProfileParsingException {
		
		
		JSONObject jsonCondition1 = new JSONObject("{\"conditions\": [" + 
													"        {" + 
													"          \"type\": \"and\"," + 
													"          \"operands\": [" + 
													"            {" + 
													"              \"type\": \"ge\"," + 
													"              \"operands\": [" + 
													"                \"http://registry.easytv.eu/context/time\"," + 
													"                \"2019-01-30T09:47:47.619Z\"," + 
													"              ]" + 
													"            }," + 
													"            {" + 
													"              \"type\": \"le\"," + 
													"              \"operands\": [" + 
													"                \"http://registry.easytv.eu/context/time\"," + 
													"                \"2019-11-30T09:47:47.619Z\"," + 
													"              ]" + 
													"            }," + 
													"            {" + 
													"              \"type\": \"ge\"," + 
													"              \"operands\": [" + 
													"                \"http://registry.easytv.eu/context/contrast\"," + 
													"                10," + 
													"              ]" + 
													"            }," + 
													"            {" + 
													"              \"type\": \"le\"," + 
													"              \"operands\": [" + 
													"                \"http://registry.easytv.eu/context/contrast\"," + 
													"                90," + 
													"              ]" + 
													"            }" + 
													"          ]" + 
													"        }" + 
													"      ]"
													+ "}");
		
		List<Object> list = jsonCondition1.getJSONArray("conditions").toList();
		System.out.println(list.toString());
		HashMap<String, Object> map = (HashMap<String, Object>) list.get(0);
		System.out.println(map.get("operands"));
	}

}
