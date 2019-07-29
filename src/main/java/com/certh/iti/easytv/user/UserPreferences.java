package com.certh.iti.easytv.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONArray;
import org.json.JSONObject;

import com.certh.iti.easytv.user.preference.ConditionalPreference;
import com.certh.iti.easytv.user.preference.Preference;

public class UserPreferences implements Clusterable {
	
	private Preference defaultPreference = new Preference();
	private List<Preference> conditionalPreferences = new ArrayList<Preference>();
	private JSONObject jsonObj = null;
	
	public UserPreferences() {
		
	}
	
	public UserPreferences(JSONObject json) {
		this.setJSONObject(json);
	}
	
	public UserPreferences( Preference defaultPreference, List<Preference> preferences) {
		this.setDefaultPreferences(defaultPreference);
		this.setConditionalPreferences(preferences);
		jsonObj = null;
	}

	public List<Preference> getConditionalPreferences() {
		return conditionalPreferences;
	}
	
	public Preference getDefaultPreference() {
		return defaultPreference;
	}

	public void setDefaultPreferences(Preference preferences) {
		defaultPreference = preferences;
		jsonObj = null;
	}
	
	public void setConditionalPreferences(List<Preference> preferences) {
		this.conditionalPreferences = preferences;
		jsonObj = null;
	}

	public JSONObject getJSONObject() {
		if(jsonObj == null) {
			toJSON();
		}
		return jsonObj;
	}

	public void setJSONObject(JSONObject json) {
			
		//set default preferences
		defaultPreference.setName("default");
		defaultPreference.setJSONObject(json.getJSONObject("default"));
		
		//set conditional preferences
		conditionalPreferences.clear();
		if(json.has("conditional")) {
			
			JSONArray conditional = json.getJSONArray("conditional");
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
			
			//add conditional preferences
			if(!conditionalPreferences.isEmpty()) {
				JSONArray conditional = new JSONArray();
				
				for (int i = 0; i < conditionalPreferences.size(); i++) 
					conditional.put(conditionalPreferences.get(i).getJSONObject());
				
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
