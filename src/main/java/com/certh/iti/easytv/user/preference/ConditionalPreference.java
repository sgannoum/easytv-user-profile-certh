package com.certh.iti.easytv.user.preference;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;



public class ConditionalPreference extends Preference {
	
	private List<Condition> conditions = new ArrayList<Condition>();;
	
	public ConditionalPreference() {
		super();
	}
	
	public ConditionalPreference(String name, Map<String, Object> entries, List<Condition> conditions) {
		super(name, entries);
		this.conditions.addAll(conditions);
		this.jsonObj = null;
	}
	
	public ConditionalPreference(String name, JSONObject json) {
		super();
		this.setName(name);
		this.setJSONObject(json);
	}
	
	public List<Condition> getConditions() {
		return conditions;
	}
	
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
		jsonObj = null;
	}

	@Override
	public void setJSONObject(JSONObject json) {
		//clear up old ones
		conditions.clear();
		
		//Add default preferences
		super.setJSONObject(json);
		
		//Add conditions
		JSONArray jsonConditions = json.getJSONArray("conditions");
		
		for(int i = 0 ; i < jsonConditions.length(); i++) 
			conditions.add(new Condition(jsonConditions.getJSONObject(i)));
		
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
