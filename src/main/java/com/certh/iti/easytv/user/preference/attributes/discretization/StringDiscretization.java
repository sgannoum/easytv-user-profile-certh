package com.certh.iti.easytv.user.preference.attributes.discretization;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class StringDiscretization extends Discretization {
		
	public class StringDiscrete extends Discrete {
		
		private static final String STRING_XML_TYPE = "http://www.w3.org/2001/XMLSchema#string";
		private MathContext context = new MathContext(1, RoundingMode.DOWN);
	
		public StringDiscrete(String[] states) {
			super();
			
			this.range = states;
			this.type = STRING_XML_TYPE;
			this.center = states[
			                new BigDecimal(String.valueOf(states.length - 1))
							.subtract(new BigDecimal(String.valueOf(0)))
							.divide(new BigDecimal(String.valueOf(step)))
							.add(new BigDecimal("1"))
							.divide(new BigDecimal(String.valueOf(2)), context)
							.multiply(new BigDecimal(String.valueOf(step)))
							.add(new BigDecimal(String.valueOf(0)))
							.intValue()];
		}

		@Override
		public boolean inRange(Object literal) {
			if(!String.class.isInstance(literal))
				throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into String");
			
			return compare(literal) == 0;
		}

		@Override
		public int compare(Object item) {
			if(!String.class.isInstance(item))
				throw new IllegalArgumentException("Value of type " + item.getClass().getName() + " can't not be converted into String");
			
			String value = (String) item;
			for(Object state : this.range) {
				if (value.compareToIgnoreCase((String) state) == 0)
					return 0;
			}
			return -1;
		}

		@Override
		public boolean checkType(Object item) {
			return String.class.isInstance(item);
		}
	}

	public StringDiscretization(String[] states) {
		super(new double[] { 0.0, states.length - 1 });
		
		//initialize bins
		this.bins = new Discrete[states.length];
		for(int i = 0; i < states.length; i++) 
			this.bins[i] = new StringDiscrete(new String[] {states[i]});
	}
	
	public StringDiscretization(String[][] bins) {
		super();
		
		//find out range values
		int max = 0;
		for(int i = 0; i < bins.length; i++)
			max += bins[i].length;
		
		//set up range
		this.range = new double[] { 0.0, max - 1 };
		
		//initialize bins
		this.bins = new Discrete[bins.length];
		for(int i = 0; i < bins.length; i++) 
			this.bins[i] = new StringDiscrete(bins[i]);
	}
	
	/**
	 * Runs over all bins to find the bin id
	 */
	@Override
	public int getBinId(Object value) {
		if(!bins[0].checkType(value))
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " is not compatible with "+ this.getClass().getName());

		for(int i = 0; i < bins.length; i++) 
			if(bins[i].compare(value) == 0)
				return i;
		
		return -1;
	}


}
