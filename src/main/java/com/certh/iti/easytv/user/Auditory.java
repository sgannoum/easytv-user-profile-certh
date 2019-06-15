package com.certh.iti.easytv.user;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

public class Auditory implements Clusterable{
	
    private int quarterK;
    private int halfK;
    private int oneK;
    private int twoK;
    private int fourK;
    private int eightK;
    private JSONObject jsonObj;
    
    public Auditory(int quarterK, int halfK, int oneK, int twoK, int fourK, int eightK) {
        this.setQuarterK(quarterK);
        this.setHalfK(halfK);
        this.setOneK(oneK);
        this.setTwoK(twoK);
        this.setFourK(fourK);
        this.setEightK(eightK);
        this.jsonObj = null;
    }
    
    public Auditory(JSONObject json) {
        this.setJSONObject(json);
    }

	public int getQuarterK() {
		return quarterK;
	}

	public void setQuarterK(int quarterK) {
		//TO-DO Check value
		this.quarterK = quarterK;
	}

	public int getHalfK() {
		return halfK;
	}

	public void setHalfK(int halfK) {
		//TO-DO Check value
		this.halfK = halfK;
	}

	public int getOneK() {
		return oneK;
	}

	public void setOneK(int oneK) {
		//TO-DO Check value
		this.oneK = oneK;
	}

	public int getTwoK() {
		return twoK;
	}

	public void setTwoK(int twoK) {
		//TO-DO Check value
		this.twoK = twoK;
	}

	public int getFourK() {
		return fourK;
	}

	public void setFourK(int fourK) {
		//TO-DO Check value
		this.fourK = fourK;
	}

	public int getEightK() {
		return eightK;
	}

	public void setEightK(int eightK) {
		//TO-DO Check value
		this.eightK = eightK;
	}
	
	public JSONObject getJSONObject() {
		return toJSON();
	}

	public void setJSONObject(JSONObject json) {
        this.setQuarterK(json.getInt("quarterK"));
        this.setHalfK(json.getInt("halfK"));
        this.setOneK(json.getInt("oneK"));
        this.setTwoK(json.getInt("twoK"));
        this.setFourK(json.getInt("fourK"));
        this.setEightK(json.getInt("eightK"));
		jsonObj = json;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!Auditory.class.isInstance(obj)) return false;
		
		Auditory other = (Auditory) obj;
		if(other.eightK != this.eightK) return false;
		if(other.fourK != this.fourK) return false;
		if(other.halfK != this.halfK) return false;
		if(other.oneK != this.oneK) return false;
		if(other.quarterK != this.quarterK) return false;
		if(other.twoK != this.twoK) return false;

		return true;
	}
	
	@Override
	public String toString() {
		if(jsonObj == null) {
			jsonObj = toJSON();
		}
		return jsonObj.toString();
	}
	
	public JSONObject toJSON() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			jsonObj.put("quarterK", quarterK);
			jsonObj.put("halfK", halfK);
			jsonObj.put("oneK", oneK);
			jsonObj.put("twoK", twoK);
			jsonObj.put("fourK", fourK);
			jsonObj.put("eightK", eightK);
	
		}
		return jsonObj;
	}
	
	public double distanceTo(Auditory other) {
		return  Math.sqrt(quarterK - other.quarterK) + 
				Math.sqrt(halfK - other.halfK) +
				Math.sqrt(oneK - other.oneK) + 
				Math.sqrt(twoK - other.twoK) + 
				Math.sqrt(fourK - other.fourK) + 
				Math.sqrt(eightK - other.eightK);
	}
	
	public double distanceTo(UserProfile other) {
		return distanceTo(other.getAuditoryCapabilities());
	}

	public double[] getPoint() {		
		return new double[] {quarterK, halfK, oneK, twoK, fourK, eightK};
	}
	

}
