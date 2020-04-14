package com.certh.iti.easytv.user.preference.attributes.discretization;

public class NoDiscretization extends Discretization{

	public NoDiscretization() {
		super();
		bins = new Discrete[0];
	}
	
	@Override
	public void handle(Object value) {
		
	}

	@Override
	public int code(Object literal) {
		return -1;
	}
	
	@Override
	public boolean inRange(Object literal, int binId) {
		return false;
	}
	
	@Override
	public int getDiscreteSize(int index) {
		return -1;
	}

	@Override
	public int getBinId(Object value) {
		return -1;
	}
	
	@Override
	public Object decode(int binId) {
		return binId;
	}
	
	@Override
	public String getBinsHistogram() {
		return null;
	}

}
