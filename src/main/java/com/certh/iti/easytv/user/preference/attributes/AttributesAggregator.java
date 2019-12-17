package com.certh.iti.easytv.user.preference.attributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import com.certh.iti.easytv.user.preference.attributes.Attribute.Bin;

/**
 * Aggregating functionalities on top of a set of attributes
 * 
 * @author salgan
 *
 */
public class AttributesAggregator {
	
	private List<LinkedHashMap<String, Attribute>> attributesHandlers = new ArrayList<LinkedHashMap<String, Attribute>>();
	
	private Vector<Bin>  bins = new Vector<Bin>();
	private Vector<Attribute> operands = new Vector<Attribute>();
	private Vector<String> uris = new Vector<String>();
	
	public AttributesAggregator() {}
	
	public void add(final LinkedHashMap<String, Attribute> attributesHandler) {
		this.attributesHandlers.add(attributesHandler);	
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
	
	public Vector<Bin> getBins() {
		//clear
		bins.clear();
		
		//get bin frequency counts
		for(LinkedHashMap<String, Attribute> attributesHandler : attributesHandlers)
			for(Entry<String, Attribute> entry : attributesHandler.entrySet()) {
				String key = entry.getKey();
				Attribute attributHandler = entry.getValue();
													
				for(Bin bin : attributHandler.getBins()) { 
					Bin newBin = new Bin();
					newBin.center = bin.center;
					newBin.range = bin.range;
					newBin.counts = bin.counts;
					newBin.label =  String.format("%s - %s", key ,bin.label);
					
					bins.add(newBin);
				}
			}
	
		return bins;
	}
		
	public int getBinNumber() {
		int size = 0;
		
		//get bin frequency counts
		for(LinkedHashMap<String, Attribute> attributesHandler : attributesHandlers)
			for(Entry<String, Attribute> entry : attributesHandler.entrySet()) 
				size += entry.getValue().getBinNumber();
		
		return size;
	}

}
