package com.certh.iti.easytv.user.preference.attributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * Aggregating functionalities on top of a set of attributes
 * 
 * @author salgan
 *
 */
public class AttributesAggregator {
	
	private List<LinkedHashMap<String, Attribute>> attributesHandlers = new ArrayList<LinkedHashMap<String, Attribute>>();
	
	private int size;
	private Vector<Integer> counts = new Vector<Integer>();
	private Vector<Object>  values = new Vector<Object>();
	private Vector<String> labels = new Vector<String>();
	private Vector<Attribute> operands = new Vector<Attribute>();
	private Vector<String> uris = new Vector<String>();
	
	public AttributesAggregator() {}
	
	
	public void add(final LinkedHashMap<String, Attribute> attributesHandler) {
		
		//add to list
		this.attributesHandlers.add(attributesHandler);				

		//get bin frequency counts
		for(Entry<String, Attribute> entry : attributesHandler.entrySet()) {
			String key = entry.getKey();
			Attribute attributHandler = entry.getValue();
			
			//add operands
			operands.add(attributHandler);
			
			//add uris
			uris.add(key);
			
			//Add discretazation data
			int[] binCounter = attributHandler.getBinsCounter();
			Object[] binsValues = attributHandler.getBinsValues();
			String[] binsLabels = attributHandler.getBinsLabel();
			
			size += attributHandler.getBinNumber();

			for(int j = 0; 
					j < attributHandler.getBinNumber();
					counts.add(binCounter[j]),
					values.add(binsValues[j]),
					labels.add(key + " - " +binsLabels[j]),
					j++) 
				;
			
		}
	}
	
	public boolean remove(LinkedHashMap<String, Attribute> attributesDimensions) {
		return this.attributesHandlers.remove(attributesDimensions);
	}
	
	public Vector<Attribute> getOperands() {		
		return 	operands;
	}
	
	public Vector<String> getUris(){
		return 	uris;
	}
	
	public Vector<Integer> getBinsCounts() {
	
		//clear
		counts.clear();
		
		//get bin frequency counts
		for(LinkedHashMap<String, Attribute> attributesHandler : attributesHandlers)
			for(Entry<String, Attribute> entry : attributesHandler.entrySet()) {
				Attribute attributHandler = entry.getValue();
				
				int[] binCounter = attributHandler.getBinsCounter();
					
				for(int j = 0; 
						j < attributHandler.getBinNumber(); 
						counts.add(binCounter[j++])) 
					;
				
			}
		
		return counts;
	}
	
	public Vector<Object> getBinsValues() {
		return values;
	}
	
	public Vector<String> getBinsLables() {
		return labels;
	}
		
	public int getSize() {
		return size;
	}

}
