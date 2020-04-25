package com.certh.iti.easytv.user.preference.attributes.discretization;

import java.math.BigDecimal;
import java.util.Map.Entry;

import java.util.TreeMap;
import java.util.Vector;

public abstract class NumericDiscretization extends Discretization {
	
	protected int exceededValues = 0;
	protected double valuesNum;
	protected int mostBinsSize = 1;
	
	public interface NumericDiscreteFactory {
		public abstract Discrete createInstance(BigDecimal discreteValue);
		public abstract Discrete createInstance(BigDecimal discreteMinValue, BigDecimal discreteMaxValue, double step);
	}
	
	protected NumericDiscretization(double[] range, double step){
		super(range, step);
	}
	
	public NumericDiscretization(double[] range, double step, NumericDiscreteFactory discreteFactory){
		super(range, step);
		
		valuesNum = ((range[1] - range[0]) / step) + 1;
		this.bins = new Discrete[(int) (valuesNum)];
		for(int i = 0; i < bins.length; i++) {
			
			//calculate the only bound of the discrete
			BigDecimal discreteValue = new BigDecimal(String.valueOf(i))
									.multiply(new BigDecimal(String.valueOf(step)))
									.add(new BigDecimal(String.valueOf(range[0])));
		
			bins[i] = discreteFactory.createInstance(discreteValue);
		}
	}
	
	public NumericDiscretization(double[] range, double step, int binsNum, NumericDiscreteFactory discreteFactory) {
		super(range, step);
		
		valuesNum = ((range[1] - range[0]) / step) + 1;
		exceededValues = (int) (valuesNum % binsNum);
		mostBinsSize = (int) ((valuesNum - exceededValues)  / binsNum);
		this.bins = new Discrete[binsNum];
	
		int currentBinSize = mostBinsSize + 1;
		double initialRange = 0;
		
		//second section with bins that has size of binSize 
		for(int i = 0; i < bins.length; i++) {
			
			if(i == exceededValues) {
				currentBinSize = mostBinsSize;
				initialRange = exceededValues * step ;
			}
			
			if(currentBinSize == 1) {
				//calculate the lower bound of the discrete
				BigDecimal discreteMinValue = getDiscreteLowerValue(i, currentBinSize, initialRange);
				bins[i] = discreteFactory.createInstance(discreteMinValue);
			} else {
				//calculate the lower and upper bounds of the discrete
				BigDecimal discreteMinValue = getDiscreteLowerValue(i, currentBinSize, initialRange);
				BigDecimal discreteMaxValue =  getDiscreteUpperValue(i, currentBinSize, initialRange);		
				bins[i] = discreteFactory.createInstance(discreteMinValue, discreteMaxValue, step);
			}
		}
	}
	
	public NumericDiscretization(double[] range, double step, int binsNum, TreeMap<? extends Number, Long> values, NumericDiscreteFactory discreteFactory) {
		super(range, step);
		
		valuesNum = ((range[1] - range[0]) / step) + 1;
		
		//binsNum not defined, set to all values range
		if(binsNum == -1) binsNum = (int) valuesNum;
		
		init(range, step, binsNum, values, discreteFactory);
	}
	
	public NumericDiscretization(double[] range, double step, TreeMap<? extends Number, Long> values, NumericDiscreteFactory discreteFactory) {
		super(range, step);
		
		valuesNum = ((range[1] - range[0]) / step) + 1;	
		init(range, step, (int) valuesNum, values, discreteFactory);
	}
	
	protected void init(double[] range, double step, int binsNum, TreeMap<? extends Number, Long> values, NumericDiscreteFactory discreteFactory) {
		
		valuesNum = ((range[1] - range[0]) / step) + 1;
		exceededValues = (int) (valuesNum % binsNum);
		mostBinsSize = (int) ((valuesNum - exceededValues)  / binsNum);
		int binSize = mostBinsSize;
		double initialRange = 0;

		int lastBinId = -1;	
		Discrete currt = null;
		Vector<Discrete> discretesHolder = new Vector<Discrete>();
		BigDecimal discreteMinValue = null, discreteMaxValue = null;
		for(Entry<? extends Number, Long> entry : values.entrySet()) {
			
			//find binId
			BigDecimal value = new BigDecimal( String.valueOf(entry.getKey()));
			int binId = findBinId(value);
			
			//create new Discretization instance, only when new binId is changed
			if(lastBinId != binId || lastBinId == -1) {
				
				//check which size the bin must take
				if(binId >= exceededValues) {
					binSize = mostBinsSize;
					initialRange = exceededValues * step ;
				} else {
					binSize = mostBinsSize + 1;
					initialRange = 0;
				}
				
				//calculate discrete Min and Max values
				discreteMinValue = getDiscreteLowerValue(binId, binSize, initialRange);
				discreteMaxValue =  getDiscreteUpperValue(binId, binSize, initialRange);	
				
				if(discreteMinValue.compareTo(discreteMaxValue) != 0)
					currt = discreteFactory.createInstance(discreteMinValue, discreteMaxValue, step);
				else 
					currt = discreteFactory.createInstance(discreteMinValue);
				
				discretesHolder.add(currt);
				lastBinId = binId;
			}
			
			//check the value and discrete boundaries
			if(value.compareTo(discreteMinValue) >= 0 && 
					value.compareTo(discreteMaxValue) <= 0) {
				currt.counts += entry.getValue();
			} else
				throw new IllegalArgumentException("problem finding binId of number "+entry.getKey());
		}
		
		//fill up the bins arrays
		this.bins = new Discrete[discretesHolder.size()];
		for(int i = 0; i < discretesHolder.size(); i++)
			this.bins[i] = discretesHolder.get(i);
	}
	
	protected BigDecimal getDiscreteLowerValue(int binIndex, int binSize, double initialRange) {
		return new BigDecimal(String.valueOf(binIndex))
				.multiply(new BigDecimal(String.valueOf(binSize)))
				.multiply(new BigDecimal(String.valueOf(step)))
				.add(new BigDecimal(String.valueOf(initialRange)))
				.add(new BigDecimal(String.valueOf(range[0])));
	}
	
	protected BigDecimal getDiscreteUpperValue(int binIndex, int binSize, double initialRange) {
		return new BigDecimal(String.valueOf(binIndex))
				.add(new BigDecimal(String.valueOf(1)))
				.multiply(new BigDecimal(String.valueOf(binSize)))
				.multiply(new BigDecimal(String.valueOf(step)))
				.add(new BigDecimal(String.valueOf(initialRange)))
				.add(new BigDecimal(String.valueOf(range[0])))
				.subtract(new BigDecimal(String.valueOf(step)));
	}
	
	/**
	 * Get binId given a number
	 * @param value
	 * @return
	 */
	protected int findBinId(BigDecimal value) {
		
	    BigDecimal bdVal = value.remainder( new BigDecimal( String.valueOf(step))) ;
		if (bdVal.doubleValue() != 0)
			throw new IllegalArgumentException("The value " + value.toPlainString() + " is not compatible with step: " + step);
		
		//the value position in the sequence of value ranges
		int binId = 0;
		int position = value.subtract( new BigDecimal( String.valueOf(range[0])))
						.divide(new BigDecimal( String.valueOf(step)))
						.intValue();
		
		int firstValueRange = new BigDecimal( String.valueOf(mostBinsSize))
								.add( new BigDecimal( String.valueOf(1)))
								.multiply( new BigDecimal( String.valueOf(exceededValues)))
								.intValue();
				
		if(position < firstValueRange)
			binId = (int) Math.floor(position / (mostBinsSize + 1));
		else 
			binId = (int) Math.floor((position - firstValueRange) / mostBinsSize) + exceededValues;
		
		return binId;
	}
	
	public int getRemaining() {
		return exceededValues;
	}
	
}
