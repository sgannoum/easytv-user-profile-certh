package com.certh.iti.easytv.user.preference.attributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.certh.iti.easytv.user.preference.attributes.discretization.BooleanDiscretization;
import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;

public abstract class BinaryAttribute extends Attribute {
	
	protected Map<Boolean, Long> frequencyHistogram = new HashMap<Boolean, Long>();
	
	
	public static class BirnaryBuilder {
		BinaryAttribute instance;
		
		protected BirnaryBuilder(BinaryAttribute instance) {
			this.instance = instance;
		}
		
		public BirnaryBuilder setDiscreatization(BooleanDiscretization discretization) {
			instance.discretization = discretization;
			return this;
		}
		
		public BirnaryBuilder enableDiscretization(boolean enable) {
			instance.enableDiscretization = enable;
			return this;
		}
		
		public BirnaryBuilder enableFrequencyHistogram(boolean enable) {
			instance.enableFrequencyHistogram = enable;
			return this;
		}
		
		public Attribute build() {
			return instance;
		}
	}
	
	
	public BinaryAttribute(double[] range) {
		super(range);
		discretization = null;
	}
	
	public BinaryAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);
		discretization = null;
	}
	
	public BinaryAttribute(double[] range, double operandMissingValue, BooleanDiscretization discretization) {
		super(range, operandMissingValue);
		this.discretization = discretization;
	}
	
	@Override
	public Object getRandomValue(Random rand) {		
		return  rand.nextBoolean();
	}
	
	@Override
	public String getXMLDataTypeURI() {
		return "http://www.w3.org/2001/XMLSchema#boolean";
	}
	
	@Override
	public Object handle(Object value) {
		
		if(!Boolean.class.isInstance(value))
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " can't not be converted into Boolean");
		

		Boolean literal = (Boolean) value;
		
		// Increase histogram counts
		if(enableFrequencyHistogram) {
			Long tmp = (tmp = frequencyHistogram.get(literal)) == null ? 1L : (tmp + 1L);
			frequencyHistogram.put(literal, tmp);
			
			if(tmp > mostFrequentCount) {
				mostFrequentCount = tmp;
				mostFrequentValue = value;
			}
		}
			
		//increase bin counter
		if(discretization != null)
			discretization.handle(literal);
		
		return value;
	}
	
	@Override
	public Discretization getDiscretization() {
		if(discretization == null) {
			if(!frequencyHistogram.isEmpty()) 
				return new BooleanDiscretization(frequencyHistogram);
			else 
				return null;
		}
		else 
			return discretization;
	}

}
