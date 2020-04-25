package com.certh.iti.easytv.user.preference.attributes.discretization;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map.Entry;

import java.util.TreeMap;


public class IntegerDiscretization extends NumericDiscretization {
		
	public static class IntegerDiscrete extends Discrete {
		
		private static final String INT_XML_TYPE = "http://www.w3.org/2001/XMLSchema#integer";
		private MathContext context = new MathContext(1, RoundingMode.DOWN);

		public IntegerDiscrete(BigDecimal firstValue) {
			super();
			
			this.range = new Integer[] {firstValue.intValue()};
			this.type = INT_XML_TYPE;
			this.center = range[0];
		}
		
		public IntegerDiscrete(BigDecimal firstValue, BigDecimal lastValue, double step) {
			super();
			
			this.range = new Integer[] {firstValue.intValue(), lastValue.intValue()};
			this.type = INT_XML_TYPE;
			this.center = new BigDecimal(String.valueOf(range[1]))
							.subtract(new BigDecimal(String.valueOf(range[0])))
							.divide(new BigDecimal(String.valueOf(step)))
							.add(new BigDecimal("1"))
							.divide(new BigDecimal(String.valueOf(2)), context)
							.multiply(new BigDecimal(String.valueOf(step)))
							.add(new BigDecimal(String.valueOf(range[0])))
							.intValue();
		}
		
		public IntegerDiscrete(Integer[] range, double step) {
			super();
			
			this.range = range;
			this.type = INT_XML_TYPE;
			this.center = new BigDecimal(String.valueOf(range[1]))
							.subtract(new BigDecimal(String.valueOf(range[0])))
							.divide(new BigDecimal(String.valueOf(step)))
							.add(new BigDecimal("1"))
							.divide(new BigDecimal(String.valueOf(2)), context)
							.multiply(new BigDecimal(String.valueOf(step)))
							.add(new BigDecimal(String.valueOf(range[0])))
							.intValue();
		}
		
		@Override
		public boolean inRange(Object literal) {
			
			if(!Integer.class.isInstance(literal))
				throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into integer");
			
			int value = (int) literal;
			if(range.length == 2)
				return (value >= (int) range[0] && value <= (int) range[1]);
			else 
				return (value == (int) range[0]);
		}

		@Override
		public int compare(Object item) {
			if(!Integer.class.isInstance(item))
				throw new IllegalArgumentException("Value of type " + item.getClass().getName() + " can't not be converted into integer");
			
			int value = (int) item;
			if(value > (int) range[range.length - 1]) 
				return 1;
			else if(value < (int) range[0]) 
				return -1;
			else 
				return 0;	
		}

		@Override
		public boolean checkType(Object item) {
			return Integer.class.isInstance(item);
		}
	}
	
	public static class IntegerDiscretiationFactory implements NumericDiscreteFactory{

		@Override
		public Discrete createInstance(BigDecimal firstValue) {
			return new IntegerDiscrete(firstValue);
		}

		@Override
		public Discrete createInstance(BigDecimal firstValue, BigDecimal lastValue, double step) {
			return new IntegerDiscrete(firstValue, lastValue, step);
		}
	}
	
	
	public IntegerDiscretization(double[] range) {
		super(range, 1.0, new IntegerDiscretiationFactory());
	}
	
	public IntegerDiscretization(double[] range, double step) {
		super(range, step, new IntegerDiscretiationFactory());
	}

	public IntegerDiscretization(double[] range, double step, int binsNum) {
		super(range, step, binsNum, new IntegerDiscretiationFactory());
	}
	
	public IntegerDiscretization(double[] range, double step, Integer[][] discretes) {
		super(range, step);
		
		this.bins = new Discrete[discretes.length];
		for(int i = 0; i < discretes.length; i++)
			this.bins[i] = new IntegerDiscrete(discretes[i], step);
	}
	
	public IntegerDiscretization(double[] range, double step, TreeMap<Integer, Long> values) {
		super(range, step, values, new IntegerDiscretiationFactory());
	}
	
	public IntegerDiscretization(double[] range, double step, int binsNum, TreeMap<Integer, Long> values) {
		super(range, step, binsNum, values, new IntegerDiscretiationFactory());
	}
	
	public IntegerDiscretization(double[] range, double step, Integer[][] discretes, TreeMap<Integer, Long> values) {
		super(range, step);
		
		int[] counts = new int[discretes.length];
		int binIndex = 0, nonZeroCounters = 0;
		boolean counted = false;
		Iterator<Entry<Integer, Long>> iterator = values.entrySet().iterator();
		Entry<Integer, Long> entry = iterator.next();
		while(true) {
			if(entry.getKey() >= discretes[binIndex][0] && entry.getKey() <= discretes[binIndex][1]) {
				counts[binIndex] += entry.getValue();
				if(!counted) { nonZeroCounters++; counted = true; }
				if(!iterator.hasNext()) break;
				entry = iterator.next();
			} else {
				counted = false;
				binIndex++;
			}
		} 
		
		this.bins = new Discrete[nonZeroCounters];
		binIndex = 0;
		for(int i = 0; i < discretes.length; i++) {
			if(counts[i] != 0) {
				IntegerDiscrete tmp = new IntegerDiscrete(discretes[i], step);
				tmp.counts = counts[i];
				this.bins[binIndex++] = tmp;
			}
		}
	}

}
