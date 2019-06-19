package com.certh.iti.easytv.user.preference;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.certh.iti.easytv.user.preference.operand.OperandLiteral;



public class ConditionalPreference extends Preference {
	
	private List<Condition> conditions;
	
	public ConditionalPreference(String name, Map<String, OperandLiteral> entries, List<Condition> conditions) {
		super(name, entries);
		this.conditions = new ArrayList<Condition>(conditions);
		this.jsonObj = null;
	}
	
	public ConditionalPreference(String name, JSONObject json) {
		super(name, json);
		setJSONObject(json);
	}
	
	public List<Condition> getConditions() {
		return conditions;
	}
	
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public void setJSONObject(JSONObject json) {
		//Add preferences
		super.setJSONObject(json);
		
		//Add conditions
		JSONArray jsonConditions = json.getJSONArray("conditions");
		this.conditions = new ArrayList<Condition>();
		for(int i = 0 ; i < jsonConditions.length(); i++) {
			this.conditions.add(new Condition(jsonConditions.getJSONObject(i)));
		}
		
		this.jsonObj = json;
	}
	
	@Override
	public JSONObject toJSON() {
		if(jsonObj == null) {
			
			//Convert the preference section
			super.toJSON();
			
			//Add condition name
			jsonObj.put("name", name);
			
			//Add condition section
			JSONArray jsonConditions = new JSONArray();
			for(int i = 0; i < conditions.size(); i++) 
				jsonConditions.put(conditions.get(i).toJSON());
			
			//Add condition section to the JSON file
			jsonObj.put("conditions", jsonConditions);
		}
		
		return jsonObj;
	}
	
	@Override
	public String toString() {
		return this.toJSON().toString(4);
	}
	
}
