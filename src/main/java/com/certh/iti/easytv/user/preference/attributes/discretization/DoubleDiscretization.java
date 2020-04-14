package com.certh.iti.easytv.user.preference.attributes.discretization;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


public class DoubleDiscretization extends NumericDiscretization {
	
	public static class DoubleDiscrete extends Discrete {
		
		private static final String Double_XML_TYPE = "http://www.w3.org/2001/XMLSchema#double";
		private MathContext context = new MathContext(1, RoundingMode.DOWN);

		public DoubleDiscrete(BigDecimal firstValue) {
			super();
			
			this.range = new Double[] {firstValue.doubleValue()};
			this.type = Double_XML_TYPE;
			this.center = range[0];
		}
		
		public DoubleDiscrete(BigDecimal firstValue, BigDecimal lastValue, double step) {
			super();
			
			this.range = new Double[] {firstValue.doubleValue(), lastValue.doubleValue()};
			this.type = Double_XML_TYPE;
			this.center = new BigDecimal(String.valueOf(range[1]))
							.subtract(new BigDecimal(String.valueOf(range[0])))
							.divide(new BigDecimal(String.valueOf(step)))
							.add(new BigDecimal("1"))
							.divide(new BigDecimal(String.valueOf(2)), context)
							.multiply(new BigDecimal(String.valueOf(step)))
							.add(new BigDecimal(String.valueOf(range[0])))
							.doubleValue();
		}
		
		public DoubleDiscrete(Double[] range, double step) {
			super();
			
			this.range = range;
			this.type = Double_XML_TYPE;
			this.center = new BigDecimal(String.valueOf(range[1]))
							.subtract(new BigDecimal(String.valueOf(range[0])))
							.divide(new BigDecimal(String.valueOf(step)))
							.add(new BigDecimal("1"))
							.divide(new BigDecimal(String.valueOf(2)), context)
							.multiply(new BigDecimal(String.valueOf(step)))
							.add(new BigDecimal(String.valueOf(range[0])))
							.intValue();
		}
		
		public boolean inRange(Object literal) {
			
			if(!Double.class.isInstance(literal))
				throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into Double");
			
			double value = (double) literal;
			if(range.length == 2)
				return (value >= (double) range[0] && value <= (double) range[1]);
			else 
				return (value == (double) range[0]);
		}
		
		@Override
		public int compare(Object item) {
			if(!Double.class.isInstance(item))
				throw new IllegalArgumentException("Value of type " + item.getClass().getName() + " can't not be converted into Double");
			
			double value = (double) item;
			if(value > (double) range[range.length - 1]) 
				return 1;
			else if(value < (double) range[0]) 
				return -1;
			else 
				return 0;	
		}

		@Override
		public boolean checkType(Object item) {
			return Double.class.isInstance(item);
		}
	}
	
	public static class DoubleDiscretiationFactory implements NumericDiscreteFactory{

		@Override
		public Discrete createInstance(BigDecimal firstValue) {
			return new DoubleDiscrete(firstValue);
		}

		@Override
		public Discrete createInstance(BigDecimal firstValue, BigDecimal lastValue, double step) {
			return new DoubleDiscrete(firstValue, lastValue, step);
		}
		
	}
	
	public DoubleDiscretization(double[] range) {
		super(range, 1.0, new DoubleDiscretiationFactory());
	}
	
	public DoubleDiscretization(double[] range, double step) {
		super(range, step, new DoubleDiscretiationFactory());
	}
	
	public DoubleDiscretization(double[] range, double step, int binsNum) {
		super(range, step, binsNum, new DoubleDiscretiationFactory());
	}
	
	public DoubleDiscretization(double[] range, double step, Double[][] discretes) {
		super(range, step);
		
		this.bins = new Discrete[discretes.length];

		for(int i = 0; i < discretes.length; i++)
			this.bins[i] = new DoubleDiscrete(discretes[i], step);
	}

}
