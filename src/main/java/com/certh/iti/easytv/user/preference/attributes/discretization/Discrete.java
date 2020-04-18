package com.certh.iti.easytv.user.preference.attributes.discretization;

public abstract class Discrete {
	
	protected Object[] range;
	protected Object center;
	protected String type;
	protected int counts = 0;
	
	public Discrete() {
	}
	
	public Discrete(Object[] range, Object center, String type) {
		this.range = range;
		this.center = center;
		this.type = type;
	}
			
	public void setRange(Object[] range) {
		this.range = range;
	}
	
	public void setCenter(Object center) {
		this.center = center;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Object[] getRange(){
		return range;
	}
	
	public Object getCenter(){
		return center;
	}
	
	public String getLabel(){
		return range.length == 1 ? String.valueOf(range[0]) : String.valueOf(range[0]) + ", " + String.valueOf(range[1]);
	}
	
	public String getXMLDataTypeURI(){
		return type;
	}
	
	public int increase(){
		return ++counts;
	}
	
	public int getCounts(){
		return counts;
	}
	
	public int getSize() {
		return 1;
	}
	
	public abstract boolean inRange(Object literal);
	
	public abstract int compare(Object item);
	
	protected abstract boolean checkType(Object item);
}
