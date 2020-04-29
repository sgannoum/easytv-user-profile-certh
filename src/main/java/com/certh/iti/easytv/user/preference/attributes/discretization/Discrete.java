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
	
	/**
	 * Check that the given value is in the discrete range
	 * @param literal
	 * @return
	 */
	public abstract boolean inRange(Object literal);
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	public abstract int compare(Object item);
	
	/**
	 * Allow subclasses to do proper conversion if needed
	 * @param obj
	 * @return
	 */
	public Object convert(Object obj) {
		return obj;
	}
	
	/**
	 * Check that the item given type is an expected one
	 * @param item
	 * @return
	 */
	protected abstract boolean checkType(Object item);
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(!(obj instanceof Discrete)) return false;
		
		Discrete tmp = (Discrete) obj;
		
		if(range.length != tmp.range.length) return false;
		
		for(int i = 0; i < range.length; i++)
			if(!range[i].equals(tmp.range[i])) return false;
		
		return center.equals(tmp.center) && 
				 type.equals(tmp.type); 
		
	}
}
