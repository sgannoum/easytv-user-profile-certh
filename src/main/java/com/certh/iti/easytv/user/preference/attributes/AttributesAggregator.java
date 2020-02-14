package com.certh.iti.easytv.user.preference.attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	
	private List<Map<String, Attribute>> attributesHandlers = new ArrayList<Map<String, Attribute>>();
	
	private int size = 0;
	private Vector<Bin>  bins = new Vector<Bin>();
	private Vector<Attribute> operands = new Vector<Attribute>();
	private Vector<String> uris = new Vector<String>();
	
	public AttributesAggregator() {}
	
	public void add(final Map<String, Attribute> attributesHandler) {
		this.attributesHandlers.add(attributesHandler);	
	}
	
	public boolean remove(Map<String, Attribute> attributesDimensions) {
		return this.attributesHandlers.remove(attributesDimensions);
	}
	
	public void reset(){
		attributesHandlers.clear();
		bins.clear();
		operands.clear();
		uris.clear();
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
		for(Map<String, Attribute> attributesHandler : attributesHandlers)
			for(Entry<String, Attribute> entry : attributesHandler.entrySet()) {
				String key = entry.getKey();
				Attribute attributHandler = entry.getValue();
													
				for(Bin bin : attributHandler.getBins()) { 
					Bin newBin = new Bin();
					newBin.center = bin.center;
					newBin.range = bin.range;
					newBin.counts = bin.counts;
					newBin.label =  String.format("%s - %s", key ,bin.label);
					newBin.type = bin.type;
					
					bins.add(newBin);
				}
			}
	
		return bins;
	}
		
	public int getBinNumber() {
		int size = 0;
		
		//get bin frequency counts
		for(Map<String, Attribute> attributesHandler : attributesHandlers)
			for(Attribute attr : attributesHandler.values()) 
				size += attr.getBinNumber();
		
		return size;
	}

}
