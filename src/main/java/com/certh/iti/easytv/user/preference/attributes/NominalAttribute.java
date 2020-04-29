package com.certh.iti.easytv.user.preference.attributes;

import java.util.LinkedHashMap;
import java.util.Random;

import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;
import com.certh.iti.easytv.user.preference.attributes.discretization.StringDiscretization;

public class NominalAttribute extends Attribute implements INominal {

	protected int state;
	protected long n = 0;
	protected String[] states = null;
	protected String[][] discretes = null;
	protected LinkedHashMap<String, Long> frequencyHistogram = new LinkedHashMap<String, Long>();
	protected StringConverter converter = new StringConverter() {

		@Override
		public boolean isInstance(Object obj) {
			return String.class.isInstance(obj);
		}

		@Override
		public String valueOf(Object obj) {
			return String.valueOf(obj);
		}
		
	};
	
	public interface StringConverter {
		public boolean isInstance(Object obj);
		public String valueOf(Object obj);
	}
	
	public static class NominalBuilder{
				
		NominalAttribute instance;
		
		public NominalBuilder() {
			instance = new NominalAttribute();
		}
		
		public NominalBuilder setRange(double[] range) {
			instance.range = range;
			return this;
		}
		
		public NominalBuilder setMissingValue(double missingValue) {
			instance.missingValue = missingValue;
			return this;
		}
		
		protected NominalBuilder(NominalAttribute instance) {
			this.instance = instance;
		}
		
		public NominalBuilder setState(String[] states) {
			instance.states = states;
			return this;
		}
		
		public NominalBuilder setDiscretes(String[][] discretes) {
			instance.discretes = discretes;
			return this;
		}
		
		public NominalBuilder setConverter(StringConverter converter) {
			instance.converter = converter;
			return this;
		}
		
		public Attribute build() {
			if(instance.states == null) 
				throw new IllegalArgumentException("No states defined");
			
			if(instance.range == null) 
				instance.range = new double[] { 0.0, instance.states.length - 1 };
			
			
			return instance;
		}
	}
	

	protected NominalAttribute() {
		super();
	}
	
	public NominalAttribute(String[] states) {
		super(new double[] { 0.0, states.length - 1 });
		this.states = states;
	}
	
	protected NominalAttribute(String[] states, Discretization distrectization) {
		super(new double[] { 0.0, states.length - 1 });
		this.states = states;
		this.discretization = distrectization;
	}

	public NominalAttribute(double[] range, String[] states) {
		super(range);
		this.states = states;
	}

	public NominalAttribute(double operandMissingValue, String[] states) {
		super(new double[] { 0.0, states.length - 1 }, operandMissingValue);
		this.states = states;
	}

	public NominalAttribute(double[] range, double operandMissingValue, String[] states) {
		super(range, operandMissingValue);
		this.states = states;
	}
	
	public NominalAttribute(double[] range, double operandMissingValue, String[] states, String[][] discretes) {
		super(range, operandMissingValue);
		this.states = states;
		this.discretes = discretes;
	}

	public final String[] getStates() {
		return states;
	}
	
	@Override
	public Object getRandomValue(Random rand) {
		int state = rand.nextInt((int) this.range[1]);
		return this.states[state];	
	}
	
	@Override
	public String getXMLDataTypeURI() {
		return "http://www.w3.org/2001/XMLSchema#string";
	}
	
	@Override
	public double getPoints(Object literal) {
		if (literal == null) {
			return missingValue;
		}

		String convertedValue = converter.valueOf(literal);

		int state = orderOf(convertedValue);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + literal);

		return state;
	}

	@Override
	public Object handle(Object value) {
		
		if(!converter.isInstance(value))
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " can't not be converted into String");
		
		String convertedValue = converter.valueOf(value);
		
		int state = orderOf(convertedValue);
		if (state == -1)
			throw new IllegalStateException("Unknown state " + convertedValue);
		
		// Increase histogram counts
		String key = convertedValue;
		Long tmp = (tmp = frequencyHistogram.get(key)) == null ? 1L : (tmp + 1L);
		frequencyHistogram.put(key, tmp);
		
		// increase counts
		if(discretization != null)
			discretization.handle(convertedValue);
		
		n++;

		return value;

	}

	protected int orderOf(String state) {
		for (int i = 0; i < states.length; i++)
			if (state.equalsIgnoreCase(states[i]))
				return i;

		return -1;
	}

	protected String stateOf(int index) {
		if (index < 0 || index >= states.length)
			return null;

		return states[index];
	}
	
	@Override
	public Discretization getDiscretization() {
		if(discretization == null) {
			if(frequencyHistogram.isEmpty()) return null;
			else if(discretes == null)
				return new StringDiscretization(range, frequencyHistogram);
			else
				return new StringDiscretization(range, discretes, frequencyHistogram);
		}
		else 
			return discretization;
	}
	
	
	public static NominalBuilder Builder() {
		return new NominalBuilder();
	}

}
