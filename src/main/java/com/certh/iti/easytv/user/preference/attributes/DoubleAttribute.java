package com.certh.iti.easytv.user.preference.attributes;

import java.math.BigDecimal;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.DummyLocalizable;

import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;
import com.certh.iti.easytv.user.preference.attributes.discretization.DoubleDiscretization;
import com.certh.iti.easytv.util.Table;
import com.certh.iti.easytv.util.Table.Position;

public class DoubleAttribute extends NumericAttribute {
	
	private int binsNum;
	private Double[][] discretes = null;
	protected TreeMap<Double, Long> frequencyHistogram = new TreeMap<Double, Long>();
	protected DoubleConverter converter = new DoubleConverter() {

		@Override
		public boolean isInstance(Object obj) {
			return Number.class.isInstance(obj);
		}

		@Override
		public Double valueOf(Object obj) {
			Number num = Number.class.cast(obj);
			return num.doubleValue();
		}
	};
	
	public interface DoubleConverter {
		public boolean isInstance(Object obj);
		public Double valueOf(Object obj);
	}
	
	public static class DoubleBuilder{
				
		DoubleAttribute instance;
		
		public DoubleBuilder() {
			instance = new DoubleAttribute();
		}
		
		public DoubleBuilder setRange(double[] range) {
			instance.range = range;
			return this;
		}
		
		public DoubleBuilder setMissingValue(double missingValue) {
			instance.missingValue = missingValue;
			return this;
		}
		
		public DoubleBuilder setStep(double step) {
			instance.step = step;
			return this;
		}
		
		public DoubleBuilder setDiscretization(Discretization distrectization) {
			instance.discretization = distrectization;
			return this;
		}
		
		public DoubleBuilder setDiscretes(Double[][] discretes) {
			instance.discretes = discretes;
			return this;
		}
		
		public DoubleBuilder setConverter(DoubleConverter converter) {
			instance.converter = converter;
			return this;
		}
		
		public Attribute build() {
			return instance;
		}
	}
	
	
	protected DoubleAttribute() {
		super();
	}
	
	public DoubleAttribute(double[] range) {
		super(range);
		this.binsNum = -1;
	}

	public DoubleAttribute(double[] range, double operandMissingValue) {
		super(range, operandMissingValue);	
		this.binsNum = -1;
	}
	
	public DoubleAttribute(double[] range, double step, double operandMissingValue) {
		super(range, step, operandMissingValue);
		this.binsNum = -1;
	}
	
	public DoubleAttribute(double[] range, double step, int binsNum, double operandMissingValue) {
		super(range, step, operandMissingValue);		
		this.binsNum = binsNum;
	}
	
	public DoubleAttribute(double[] range, double step, double operandMissingValue, Discretization discretization) {
		super(range, step, operandMissingValue);
		this.discretization = discretization;
	}
	
	public DoubleAttribute(double[] range, double step, Double[][] discretes) {
		super(range, step);
		this.discretes = discretes;
	}

	@Override
	public double getPoints(Object literal) {
		if (literal == null) {
			return missingValue;
		}

		return converter.valueOf(literal);
	}
	
	@Override
	public String getXMLDataTypeURI() {
		return "http://www.w3.org/2001/XMLSchema#double";
	}

	@Override
	public Object handle(Object value) {

		if(!converter.isInstance(value)) 
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " can't not be converted into Double");
			
		
		double numericValue = converter.valueOf(value);
		
		if(numericValue < range[0] || numericValue > range[1])
			throw new OutOfRangeException(numericValue, range[0], range[1]);
		
		
	    BigDecimal x = new BigDecimal( String.valueOf(numericValue) );
	    BigDecimal bdVal = x.remainder( new BigDecimal( String.valueOf(step) ) ) ;
		if(bdVal.doubleValue() != 0.0) 
			throw new OutOfRangeException(new DummyLocalizable(value+" Non compatible with step: " + step), numericValue, range[0], range[1]);
		
		
		// Increase histogram counts
		Double key = new Double(numericValue);
		Long tmp = (tmp = frequencyHistogram.get(key)) == null ? 1L : (tmp + 1L);
		frequencyHistogram.put(key, tmp);
		
		//Increment the number of occurrences 
		if(discretization != null)
			discretization.handle(numericValue);
		
		// Set Min Max value
		setMinMaxValue(numericValue);

		sum += numericValue;

		n++;
		
		return numericValue;
	}
	
	@Override
	public double getStandardDeviation() {
		if(n == 0)
			return 0.0;
		
		if(standard_deviation == -1.0) {
			
			double var = 0.0;
			double mean = sum/n;
			
			for(Entry<Double, Long> entry : frequencyHistogram.entrySet()) 
				var += entry.getValue() * Math.pow(entry.getKey() - mean, 2);
			
			standard_deviation = Math.sqrt(var/n);
		}
		
		return standard_deviation; 
	}
	
	@Override
	protected String getValueshistogram() {
		
		//Histogram table
		Table histTable = new Table(2, 11);
		Table.Row headerRow = histTable.createRow(1, Position.CENTER);		
		headerRow.addCell("Values histogram");
		histTable.addRow(headerRow);
		histTable.addRow(new Object[] {" Value", " Frequency"}, Position.CENTER);			
		for(Entry<Double, Long> entry : frequencyHistogram.entrySet()) 
			histTable.addRow(new Object[] {entry.getKey().doubleValue() , entry.getValue().intValue()});
		
		return histTable.toString() +" \r\n";
	}
	
	@Override
	public Discretization getDiscretization() {
		if(discretization == null) {
			if(frequencyHistogram.isEmpty()) return null;
			else if(binsNum != -1)
				return new DoubleDiscretization(range, step, binsNum, frequencyHistogram);
			else if(discretes != null)
				return new DoubleDiscretization(range, step, discretes, frequencyHistogram);
			else 
				return new DoubleDiscretization(range, step, frequencyHistogram);
		}
		else 
			return discretization;
	}
	
	public static DoubleBuilder Builder() {
		return new DoubleBuilder();
	}

}
