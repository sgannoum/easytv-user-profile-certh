package com.certh.iti.easytv.user;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

public class General implements Clusterable {
		
	private static final String[] GenderTypes = {"male", "female"};
	private int age;
	private int gender;
	private JSONObject jsonObj;
	
	public General(int age, String gender) {
		this.setAge(age);
		this.gender = indexOf(gender);
		this.jsonObj = null;
	}
	
	public General(JSONObject general) {
		this.setJSONObject(general);
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		if(age < 0 && age > 115)
			throw new IllegalStateException("Wrong number for age "+ age);
		
		this.age = age;
	}

	public int getGender() {
		return gender;
	}
	
	public static String getGender(int gender) {
		if(gender < 0 || gender > GenderTypes.length) 
			throw new IllegalStateException("Non existing gender type: " + gender);
		
		return GenderTypes[gender];
	}
	
	public void setGender(String gender) {
		this.gender = indexOf(gender); 
		
		if(this.gender == -1) 
			throw new IllegalStateException("Non existing gender type: " + gender);
	}
	
	public JSONObject getJSONObject() {
		return toJSON();
	}

	public void setJSONObject(JSONObject json) {
		this.setAge(json.getInt("age"));
		this.setGender(json.getString("gender"));
		this.jsonObj = json;
	}
	
	public double distanceTo(General other) { 
		return Math.pow((age - other.age), 2) + Math.pow((gender - other.gender), 2);
	}
	
	public double distanceTo(UserProfile other) {
		return distanceTo(other.getGeneral());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!General.class.isInstance(obj)) return false;
		
		General other = (General) obj;
		if(other.age != this.age) return false;
		if(other.gender != this.gender) return false;

		return true;
	}
	
	@Override
	public String toString() {
		return toJSON().toString();
	}
	
	/**
	 * @return JSON object representation
	 */
	public JSONObject toJSON() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			jsonObj.put("age", age);
			jsonObj.put("gender", GenderTypes[gender]);
		}  
		return jsonObj;
	}
	
	private int indexOf(String gender) {
		gender = gender.toLowerCase();
		if(gender.equals(GenderTypes[0]))
			return 0;
		else if(gender.equals(GenderTypes[1]))
			return 1;
		else 
			return -1;
	}

	public double[] getPoint() {
		return new double[] {age, gender};
	}

}
