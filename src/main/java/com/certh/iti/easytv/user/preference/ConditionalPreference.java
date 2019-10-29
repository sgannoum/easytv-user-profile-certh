package com.certh.iti.easytv.user.preference;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;



public class ConditionalPreference extends Preference {
	
	private List<Condition> conditions = new ArrayList<Condition>();;
	
	public ConditionalPreference() {
		super();
	}
	
	public ConditionalPreference(String name, Map<String, Object> entries, List<Condition> conditions) throws UserProfileParsingException {
		super(name, entries);
		this.conditions.addAll(conditions);
		this.jsonObj = null;
	}
	
	public ConditionalPreference(String name, JSONObject json) throws UserProfileParsingException {
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
	public void setJSONObject(JSONObject json) throws UserProfileParsingException {
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
	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			
			//Convert the condition preference section first
			super.getJSONObject();
			
			//Add condition name
			jsonObj.put("name", name);
			
			//Add condition section
			JSONArray jsonConditions = new JSONArray();
			for(int i = 0; i < conditions.size(); i++) 
				jsonConditions.put(conditions.get(i).geJSONObject());
			
			//Add condition section to the JSON file
			jsonObj.put("conditions", jsonConditions);
		}
		
		return jsonObj;
	}
	
	@Override
	public String toString() {
		return this.getJSONObject().toString(4);
	}
	
}
