package com.certh.iti.easytv.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONArray;
import org.json.JSONObject;

import com.certh.iti.easytv.user.preference.ConditionalPreference;
import com.certh.iti.easytv.user.preference.Preference;

public class UserPreferences implements Clusterable {
	
	private Preference defaultPreference;
	private List<ConditionalPreference> conditionalPreferences;
	private JSONObject jsonObj;
	
	public UserPreferences(JSONObject json) {
		this.setJSONObject(json);
	}
	
	public UserPreferences( Preference defaultPreference, List<ConditionalPreference> preferences) {
		this.setDefaultPreferences(defaultPreference);
		this.setConditionalPreferences(preferences);
		jsonObj = null;
	}

	public List<ConditionalPreference> getConditionalPreferences() {
		return conditionalPreferences;
	}
	
	public Preference getDefaultPreference() {
		return defaultPreference;
	}

	public void setDefaultPreferences(Preference preferences) {
		defaultPreference = preferences;
	}
	
	public void setConditionalPreferences(List<ConditionalPreference> preferences) {
		this.conditionalPreferences = preferences;
	}

	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			toJSON();
		}
		return jsonObj;
	}

	public void setJSONObject(JSONObject json) {
			
		defaultPreference = new Preference("default", json.getJSONObject("default"));
		conditionalPreferences = new ArrayList<ConditionalPreference>();

		if(json.has("conditional")) {
			
			//Get conditional preferences
			JSONArray conditional = json.getJSONArray("conditional");
			
			//Add conditional preferences
			for(int i = 0 ; i < conditional.length(); i++) {
				JSONObject condition =  conditional.getJSONObject(i);
				conditionalPreferences.add(new ConditionalPreference(condition.getString("name"), condition));
			}
		}
		
		this.jsonObj = json;
	}
	
	public JSONObject toJSON() {
		if(jsonObj == null) {

			jsonObj = new JSONObject();
			
			//add default preferences
			jsonObj.put("default", defaultPreference.toJSON());
			
			
			if(!conditionalPreferences.isEmpty()) {
				//prepare the conditional preference array
				JSONArray conditional = new JSONArray();
				
				for (int i = 0; i < conditionalPreferences.size(); i++) 
					conditional.put(conditionalPreferences.get(i).getJSONObject());
				
				//add conditional preferences
				jsonObj.put("conditional", conditional);
			}
		}
		return jsonObj;
	}

	public double[] getPoint() {
		return this.getDefaultPreference().getPoint();
		
		//TO-DO include the conditional preferences
	}

}
