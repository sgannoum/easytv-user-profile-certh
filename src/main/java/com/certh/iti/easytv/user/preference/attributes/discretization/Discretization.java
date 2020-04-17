package com.certh.iti.easytv.user.preference.attributes.discretization;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import com.certh.iti.easytv.util.Table;
import com.certh.iti.easytv.util.Table.Position;

public abstract class Discretization {
	
	protected static final int MAX_BINS_NUMS = 10000;

	protected double[] range;
	protected double step = 1.0;
	protected Discrete[] bins;
	
	protected Discretization(){
	}
	
	public Discretization(double[] range){
		this.range = range;
	}
	
	public Discretization(double[] range, double step){
		this.range = range;
		this.step = step;
	}
	
	public int getBinNumber() {
		return bins.length;
	}
	
	public Discrete[] getBins() {
		return bins;
	}
	
	public double[] getRange() {
		return range;
	}
	
	public void handle(Object value) {
		int bindId = getBinId(value);
		
		if(bindId == -1)
			throw new IllegalArgumentException("Value " + value + " is out of bin range ["+range[0]+","+range[1]+"]");

		bins[bindId].increase();
	}
	
	
	public void handle(Map<Object, Long> frequencyHistogram) {
		for(Entry<Object, Long> entry : frequencyHistogram.entrySet()) {
			this.handle(entry.getKey());
		}
	}
	
	/**
	 * Get an integer representation of the given value
	 * 
	 * @return
	 */
	public int code(Object literal) {
		int binId = getBinId(literal);
		
		if(binId == -1)
			throw new IllegalArgumentException("Value " + literal + " is out of bin range ["+range[0]+","+range[1]+"]");

		//check that the given value belongs to the bin range
		if(!bins[binId].inRange(literal)) 
			throw new IllegalArgumentException("Value " + literal + " is not in bin range ["+bins[binId].getLabel()+"]");

		return binId;
	}
	
	/**
	 * Check whether the given object is in the given bin range
	 * 
	 * @param literal
	 * @param binId
	 * @return
	 */
	public boolean inRange(Object literal, int binId) {
		if(binId < 0 || binId >= bins.length)
			throw new IllegalArgumentException("Out of Range bin id: " + binId+" ["+0+","+bins.length+"]");
		
		return bins[binId].inRange(literal);
	}
	
	/**
	 * Return the size of a specific bin
	 * @param index
	 * @return
	 */
	public int getDiscreteSize(int index) {
		if(index < 0 || index >= bins.length)
			throw new IllegalArgumentException("Out of Range bin id: " + index+" ["+0+","+bins.length+"]");
		
		Object[] binRange = bins[index].range;
		if(binRange.length == 1)
			return 1;
		else
			return new BigDecimal(String.valueOf(binRange[1]))
						.subtract(new BigDecimal(String.valueOf(binRange[0])))
						.divide(new BigDecimal(String.valueOf(step)))
						.add(new BigDecimal("1"))
						.intValue();
	}
	
	/**
	 * Get the binId that value belongs to 
	 * 
	 * @param value
	 * @return
	 */
	public int getBinId(Object value) {
		
		//check type
		if(!bins[0].checkType(value))
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " is not compatible with "+ this.getClass().getName());
		
		//check compatibility with step
	    BigDecimal x = new BigDecimal( String.valueOf(value) );
	    BigDecimal bdVal = x.remainder( new BigDecimal( String.valueOf(step) ) ) ;
		if (bdVal.doubleValue() != 0)
			throw new IllegalArgumentException("The value " + value + " is not compatible with step: " + step);
		
		int begin = 0, last = bins.length - 1;
		while(begin <= last) {
			int mid = ((begin + last) / 2);
			int res = bins[mid].compare(value);
			
			if(res > 0) 
				begin = mid + 1;
			else if(res < 0) 
				last = mid - 1;
			else 
				return mid;		
		}

		//specify the itemId
		return -1;
	}
	
	/**
	 * Get the corresponding dimension value
	 * 
	 * @return
	 */
	public Object decode(int binId) {		
		if (binId >= bins.length || binId < 0)
			throw new IllegalArgumentException("Out of range bin id: " + binId);
						
		return bins[binId].center;
	}
	
	/**
	 * Print in the form of table the bins histogram
	 * 
	 * @return
	 */
	public String getBinsHistogram() {
		
		//find table columns width
		int maxColumnWdith = 6;
		for(int i = 0 ; i < bins.length; i++) {
			String label = bins[i].getLabel();
			if(maxColumnWdith < label.length())
				maxColumnWdith = label.length();
		}
		

		Table table = new Table(bins.length + 1, maxColumnWdith);
		Table.Row headerRow = table.createRow(1, Position.CENTER);
		Table.Row binIdRow = table.createRow();
		Table.Row binRangeRow = table.createRow();
		Table.Row binCenterRow = table.createRow();
		Table.Row binCountsRow = table.createRow();
		
		headerRow.addCell("Bins histogram");
		binIdRow.addCell("Id");
		binRangeRow.addCell("Range");
		binCenterRow.addCell("Center");
		binCountsRow.addCell("Counts");
		
		for(int i = 0 ; i < bins.length; i++) {
			
			binIdRow.addCell(i);
			binRangeRow.addCell(bins[i].getLabel());
			binCenterRow.addCell(bins[i].getCenter());
			binCountsRow.addCell(bins[i].getCounts());
		}
		
		table.addRow(headerRow);
		table.addRow(binIdRow);
		table.addRow(binRangeRow);
		table.addRow(binCenterRow);
		table.addRow(binCountsRow);
		
		return table.toString() +"\r\n";
	}

}
