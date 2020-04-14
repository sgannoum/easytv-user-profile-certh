package com.certh.iti.easytv.user.preference.attributes.discretization;

import java.math.BigDecimal;

public abstract class NumericDiscretization extends Discretization {
	
	private int remaining = 0;
	
	public interface NumericDiscreteFactory {
		public abstract Discrete createInstance(BigDecimal firstValue);
		public abstract Discrete createInstance(BigDecimal firstValue, BigDecimal lastValue, double step);
	}
	
	protected NumericDiscretization(double[] range, double step){
		super(range, step);
	}
	
	public NumericDiscretization(double[] range, double step, NumericDiscreteFactory factory){
		super(range, step);
		
		double valueRange = ((range[1] - range[0]) / step) + 1;
		int binsNum = (int) (valueRange);
		
		this.bins = new Discrete[binsNum];
	
		//second section with bins that has size of binSize 
		for(int i = 0; i < bins.length; i++) {
			
			//the bin middle value
			BigDecimal firstValue = new BigDecimal(String.valueOf(i))
									.multiply(new BigDecimal(String.valueOf(step)))
									.add(new BigDecimal(String.valueOf(range[0])));
		
			bins[i] = factory.createInstance(firstValue);
		}
	}
	
	public NumericDiscretization(double[] range, double step, int binsNum, NumericDiscreteFactory factory) {
		super(range, step);
		
		double valueRange = ((range[1] - range[0]) / step) + 1;
		remaining = (int) (valueRange % binsNum);
		int binSize = (int) ((valueRange - remaining)  / binsNum);
		
		this.bins = new Discrete[binsNum];
	
		int size = binSize + 1;
		double initialRange = 0;
		
		//second section with bins that has size of binSize 
		for(int i = 0; i < bins.length; i++) {
			
			if(i == remaining) {
				size = binSize;
				initialRange = remaining * step ;
			}
			
			//the bin middle value
			BigDecimal firstValue = new BigDecimal(String.valueOf(i))
									.multiply(new BigDecimal(String.valueOf(size)))
									.multiply(new BigDecimal(String.valueOf(step)))
									.add(new BigDecimal(String.valueOf(initialRange)))
									.add(new BigDecimal(String.valueOf(range[0])));
			
			if(size == 1) {
				bins[i] = factory.createInstance(firstValue);

			} else {
				BigDecimal lastValue =  new BigDecimal(String.valueOf(i))
										.add(new BigDecimal(String.valueOf(1)))
										.multiply(new BigDecimal(String.valueOf(size)))
										.multiply(new BigDecimal(String.valueOf(step)))
										.add(new BigDecimal(String.valueOf(initialRange)))
										.add(new BigDecimal(String.valueOf(range[0])))
										.subtract(new BigDecimal(String.valueOf(step)));	
				
				bins[i] = factory.createInstance(firstValue, lastValue, step);
			}
		}
	}
	
	public int getRemaining() {
		return remaining;
	}
	


}
