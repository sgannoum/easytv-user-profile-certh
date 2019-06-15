package com.certh.iti.easytv.user;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.json.JSONObject;

public class Visual implements Clusterable{
	
	private static final String[] ColorBlindessStrTypes = { "normal", "deuteranomaly" ,"deuteranopia","protanomaly","protanopia","tritanomaly","tritanopia"};

	private int visualAcuity;
	private int contrastSensitivity;
	private int colorBlindness;
	private JSONObject jsonObj;
	
	public Visual(int visualAcuity, int contrastSensitivity, String colorBlindness) {
		this.setVisualAcuity(visualAcuity);
		this.setContrastSensitivity(contrastSensitivity);
		this.setColorBlindness(colorBlindness);
		this.jsonObj = null;
	}
	
	public Visual(JSONObject json) {
		this.setJSONObject(json);
	}
	
	public double distanceTo(Visual other) {
		return  Math.sqrt(visualAcuity - other.visualAcuity) + 
				Math.sqrt(contrastSensitivity - other.contrastSensitivity) + 
				Math.sqrt(colorBlindness - other.colorBlindness);
	}
	
	public double distanceTo(UserProfile other) {
		return distanceTo(other.getVisualCapabilities());
	}
	
	public int getVisualAcuity() {
		return visualAcuity;
	}
	public void setVisualAcuity(int visualAcuity) {
		if(visualAcuity < 1 && visualAcuity > 20)
			throw new IllegalStateException("Wrong value for visual_acuity "+visualAcuity);
		
		this.visualAcuity = visualAcuity;
	}
	public int getContrastSensitivity() {
		return contrastSensitivity;
	}
	public void setContrastSensitivity(int contrastSensitivity) {
		if(contrastSensitivity < 1 && contrastSensitivity > 20)
			throw new IllegalStateException("Wrong value for visual_acuity "+contrastSensitivity);
		
		this.contrastSensitivity = contrastSensitivity;
	}
	
	public void setColorBlindness(String colorBlindness) {
		this.colorBlindness = indexOf(colorBlindness);
		if(this.colorBlindness == -1)
			throw new IllegalStateException("Unknown Color blindess condition " + colorBlindness);
	}
	
	public static String getColorBlindness(int colorBlindness) {
		if(colorBlindness < 0 || colorBlindness >= ColorBlindessStrTypes.length) 
			throw new IllegalStateException("Non existing Color blindess type: " + colorBlindness);
		
		return ColorBlindessStrTypes[colorBlindness];
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(!Visual.class.isInstance(obj)) return false;
		
		Visual other = (Visual) obj;
		if(other.visualAcuity != this.visualAcuity) return false;
		if(other.contrastSensitivity != this.contrastSensitivity) return false;
		if(other.colorBlindness != this.colorBlindness) return false;

		return true;
	}
	
	@Override
	public String toString() {
		return jsonObj.toString();
	}

	public JSONObject getJSONObject() {
		return toJSON();
	}

	public void setJSONObject(JSONObject json) {
		this.setVisualAcuity(json.getInt("visual_acuity"));
		this.setContrastSensitivity(json.getInt("contrast_sensitivity"));
		this.setColorBlindness(json.getString("color_blindness"));
		jsonObj = json;
	}
	
	public JSONObject toJSON() {
		if(jsonObj == null) {
			jsonObj = new JSONObject();
			jsonObj.put("visual_acuity", visualAcuity);
			jsonObj.put("contrast_sensitivity", contrastSensitivity);
			jsonObj.put("color_blindness", ColorBlindessStrTypes[colorBlindness]);
		}
		return jsonObj;
	}
	
	private int indexOf(String colorBlindness) {
		for(int i = 0; i < ColorBlindessStrTypes.length; i++) 
			if(colorBlindness.equalsIgnoreCase(ColorBlindessStrTypes[i])) 
				return i;
			
		return -1;
	}

	public double[] getPoint() {
		return new double[] {visualAcuity, contrastSensitivity, colorBlindness};
	}
}
