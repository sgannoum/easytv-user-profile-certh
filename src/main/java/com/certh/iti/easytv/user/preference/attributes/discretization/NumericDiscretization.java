package com.certh.iti.easytv.user.preference.attributes.discretization;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

public abstract class NumericDiscretization extends Discretization {
	
	protected int remaining = 0;
	protected double valueRange;
	protected int mostBinsSize = 1;
	
	public interface NumericDiscreteFactory {
		public abstract Discrete createInstance(BigDecimal firstValue);
		public abstract Discrete createInstance(BigDecimal firstValue, BigDecimal lastValue, double step);
	}
	
	protected NumericDiscretization(double[] range, double step){
		super(range, step);
	}
	
	public NumericDiscretization(double[] range, double step, NumericDiscreteFactory factory){
		super(range, step);
		
		valueRange = ((range[1] - range[0]) / step) + 1;
		this.bins = new Discrete[(int) (valueRange)];
		for(int i = 0; i < bins.length; i++) {
			
			//calculate the only bound of the discrete
			BigDecimal firstValue = new BigDecimal(String.valueOf(i))
									.multiply(new BigDecimal(String.valueOf(step)))
									.add(new BigDecimal(String.valueOf(range[0])));
		
			bins[i] = factory.createInstance(firstValue);
		}
	}
	
	public NumericDiscretization(double[] range, double step, int binsNum, NumericDiscreteFactory factory) {
		super(range, step);
		
		valueRange = ((range[1] - range[0]) / step) + 1;
		remaining = (int) (valueRange % binsNum);
		mostBinsSize = (int) ((valueRange - remaining)  / binsNum);
		this.bins = new Discrete[binsNum];
	
		int currentBinSize = mostBinsSize + 1;
		double initialRange = 0;
		
		//second section with bins that has size of binSize 
		for(int i = 0; i < bins.length; i++) {
			
			if(i == remaining) {
				currentBinSize = mostBinsSize;
				initialRange = remaining * step ;
			}
			
			if(currentBinSize == 1) {
				//calculate the lower bound of the discrete
				BigDecimal firstValue = getDiscreteLowerValue(i, currentBinSize, initialRange);
				bins[i] = factory.createInstance(firstValue);
			} else {
				//calculate the lower and upper bounds of the discrete
				BigDecimal firstValue = getDiscreteLowerValue(i, currentBinSize, initialRange);
				BigDecimal lastValue =  getDiscreteUpperValue(i, currentBinSize, initialRange);		
				bins[i] = factory.createInstance(firstValue, lastValue, step);
			}
		}
	}
	
	public NumericDiscretization(double[] range, double step, int binsNum, TreeMap<Double, Long> values, NumericDiscreteFactory factory) {
		super(range, step);
		
		valueRange = ((range[1] - range[0]) / step) + 1;
		if(binsNum == -1) binsNum = (int) valueRange;
		remaining = (int) (valueRange % binsNum);
		mostBinsSize = (int) ((valueRange - remaining)  / binsNum);
		int currentBinSize = mostBinsSize;
		if(remaining > 0 ) currentBinSize++;
		double initialRange = 0;
		
		int binIndex = 0;
		//calculate the lower and upper bounds of the first discrete
		BigDecimal firstValue = getDiscreteLowerValue(binIndex, currentBinSize, initialRange);
		BigDecimal lastValue =  getDiscreteUpperValue(binIndex, currentBinSize, initialRange);	
		
		Vector<Discrete> tmps = new Vector<Discrete>();
		Discrete currt = null;
		Iterator<Entry<Double, Long>> iterator = values.entrySet().iterator();
		Entry<Double, Long> entry = iterator.next();
		while(true) {
			
			if(entry.getKey() >= firstValue.doubleValue() && entry.getKey() <= lastValue.doubleValue()) {
				
				//first time, create discrete
				if(currt == null) { 
					if(firstValue.doubleValue() != lastValue.doubleValue())
						currt = factory.createInstance(firstValue, lastValue, step);
					else 
						currt = factory.createInstance(firstValue);
					tmps.add(currt);
				} 
				
				//add counts
				currt.counts += entry.getValue();
				
				//get next
				if(!iterator.hasNext()) break;
				entry = iterator.next();
				
			} else {
				//No match, inspect next possible discrete
				
				currt = null;
				binIndex++;
				
				//check which size the bin must take
				if(binIndex == remaining) {
					currentBinSize = mostBinsSize;
					initialRange = remaining * step ;
				}
				
				
				//calculate the lower and upper bounds of the discrete
				firstValue = getDiscreteLowerValue(binIndex, currentBinSize, initialRange);
				lastValue =  getDiscreteUpperValue(binIndex, currentBinSize, initialRange);
			}
		}
		
		//fill up the bins arrays
		this.bins = new Discrete[tmps.size()];
		for(int i = 0; i < tmps.size(); i++)
			this.bins[i] = tmps.get(i);
	}
	
	protected BigDecimal getDiscreteLowerValue(int binIndex, int currentBinSize, double initialRange) {
		return new BigDecimal(String.valueOf(binIndex))
				.multiply(new BigDecimal(String.valueOf(currentBinSize)))
				.multiply(new BigDecimal(String.valueOf(step)))
				.add(new BigDecimal(String.valueOf(initialRange)))
				.add(new BigDecimal(String.valueOf(range[0])));
	}
	
	protected BigDecimal getDiscreteUpperValue(int binIndex, int currentBinSize, double initialRange) {
		return new BigDecimal(String.valueOf(binIndex))
				.add(new BigDecimal(String.valueOf(1)))
				.multiply(new BigDecimal(String.valueOf(currentBinSize)))
				.multiply(new BigDecimal(String.valueOf(step)))
				.add(new BigDecimal(String.valueOf(initialRange)))
				.add(new BigDecimal(String.valueOf(range[0])))
				.subtract(new BigDecimal(String.valueOf(step)));
	}
	
	public int getRemaining() {
		return remaining;
	}
	
}
