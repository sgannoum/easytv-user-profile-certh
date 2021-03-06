package com.certh.iti.easytv.user.preference.attributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.exception.OutOfRangeException;

import java.util.Vector;

import com.certh.iti.easytv.user.preference.attributes.discretization.Discrete;
import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;

/**
 * Aggregating functionalities on top of a set of attributes
 * 
 * @author salgan
 *
 */
public class AttributesAggregator {
	
	private class DiscretizationWrapper extends Discretization implements Comparable<DiscretizationWrapper>{
		public Discretization discrete;
		public int base = 0;
		
		public DiscretizationWrapper(Discretization discrete, int base) {
			this.discrete = discrete;
			this.base = base;
		}

		@Override
		public int code(Object literal) {
			return discrete.code(literal) + base;
		}

		@Override
		public Object decode(int item) {
			return discrete.decode(item - base);
		}
		
		@Override
		public int getBinNumber() {
			return discrete.getBinNumber();
		}
		
		@Override
		public int getDiscreteSize(int index) {
			return discrete.getDiscreteSize(index);
		}
		
		@Override
		public Discrete[] getBins() {
			return discrete.getBins();
		}
		
		@Override
		public double[] getRange() {
			return discrete.getRange();
		}
		
		@Override
		public boolean inRange(Object literal, int binId) {
			if(binId < base + discrete.getBinNumber()) 
				return discrete.inRange(literal, binId - base);
			else 
				return false;
		}
		
		@Override
		public int getBinId(Object value) {
			return discrete.getBinId(value);
		}
		
		@Override
		public String getBinsHistogram() {
			return discrete.getBinsHistogram();
		}
		
		@Override
		public void handle(Object value) {

		}

		@Override
		public int compareTo(DiscretizationWrapper o) {
			return base - o.base;
		}
		
		public Discrete getDiscrete(int item) {
			return discrete.getBins()[item - base];
		}
	}
	
	public static class Association<K, V> {
		private K uri;
		private V value;
		
		public Association(K uri, V value) {
			this.uri = uri;
			this.value = value;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == this) return true;
			if(!(obj instanceof Association)) return false;
			
			Association<?, ?> tmp = (Association<?, ?>) obj;
			
			return uri.equals(tmp.uri) && value.equals(tmp.value);
		}
		
		private void setUri(K uri) {
			this.uri = uri;
		}
		
		private void setValue(V value) {
			this.value = value;
		}
		
		public K getUri() {
			return this.uri;
		}
		
		public V getValue() {
			return this.value;
		}
	}
	
	
	/**
	 * Iterator over the dimensions discrete 
	 * @author salim
	 *
	 */
	public class DiscreteIterator implements Iterator<Discrete> {

		private Iterator<Entry<String, DiscretizationWrapper>> iterator;
		private int index = 0;
		private Discrete[] currt;
		
		public DiscreteIterator() {
			iterator = discretizationHandlers.entrySet().iterator();
			Entry<String, DiscretizationWrapper> entry = iterator.next();
			currt = entry.getValue().getBins();
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext() || index < currt.length;
		}

		@Override
		public Discrete next() {
			if(index == currt.length) {
				index = 0;
				Entry<String, DiscretizationWrapper> entry = iterator.next();
				currt = entry.getValue().getBins();
			}
			return currt[index++];
		}
	}
	
	/**
	 * Iterator over the dimensions discrete 
	 * @author salim
	 *
	 */
	public class BinsIterator implements Iterator<Association<String, Discrete>> {

		private Iterator<Entry<String, DiscretizationWrapper>> iterator;
		private int index = 0;
		private Discrete[] currt;
		private Association<String, Discrete> association;
		
		public BinsIterator() {
			iterator = discretizationHandlers.entrySet().iterator();
			Entry<String, DiscretizationWrapper> entry = iterator.next();
			currt = entry.getValue().getBins();
			association = new Association<String, Discrete> (entry.getKey(), currt[index++]);
		}
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext() || index < currt.length;
		}

		@Override
		public Association<String, Discrete> next() {
			if(index == currt.length) {
				index = 0;
				Entry<String, DiscretizationWrapper> entry = iterator.next();
				currt = entry.getValue().getBins();
				association.setUri(entry.getKey());
			}
			association.setValue(currt[index++]);
			return association;
		}
	}
	
	private Map<String, DiscretizationWrapper> discretizationHandlers = new LinkedHashMap<String, DiscretizationWrapper>();
	private Map<String, Attribute> attributesHandlers = new LinkedHashMap<String, Attribute>();

	
	private int lastBase = 0;
	private Map<String, Integer> groupsBase = new HashMap<String, Integer>(); 
	
	public AttributesAggregator() {
	}
	
	public void add(final Map<String, Attribute> attributesHandler, String label) {
		
		//associate group label with their base number
		if(!groupsBase.containsKey(label))
			groupsBase.put(label, lastBase);
		
		//add elements
		for(Entry<String, Attribute> entry : attributesHandler.entrySet()) {
			attributesHandlers.put(entry.getKey(), entry.getValue());
			Discretization discretization = entry.getValue().getDiscretization();
			
			if(discretization == null) continue;
			
			DiscretizationWrapper wrapper = new DiscretizationWrapper(discretization, lastBase);
			discretizationHandlers.put(entry.getKey(), wrapper);
			lastBase += discretization.getBinNumber();
		}
	}
	
	public void add(AttributesAggregator aggregator) {		
		
		//add group
		for(Entry<String, Integer> entry : aggregator.groupsBase.entrySet())
			if(!groupsBase.containsKey(entry.getKey()))
				groupsBase.put(entry.getKey(), entry.getValue() + lastBase);
		
		for(Entry<String, DiscretizationWrapper> entry : aggregator.discretizationHandlers.entrySet()) {
			DiscretizationWrapper discretization = new DiscretizationWrapper(entry.getValue().discrete, lastBase);
			discretizationHandlers.put(entry.getKey(), discretization);
			lastBase += entry.getValue().discrete.getBinNumber();
		}
		
		for(Entry<String, Attribute> entry : aggregator.attributesHandlers.entrySet()) 
			attributesHandlers.put(entry.getKey(), entry.getValue());
		
	}
	
	public Map<String, Attribute> getAttributes() {
		return Collections.unmodifiableMap(attributesHandlers);
	}
	
	/**
	 * Iterator over the discrete of the aggregator
	 * @return
	 */
	public Iterator<Discrete> discreteIterator() {
		return new DiscreteIterator();
	}
	
	/**
	 * Iterator over the discrete of the aggregator
	 * @return
	 */
	public Iterator<Association<String, Discrete>> iterator() {
		return new BinsIterator();
	}
	
	public int getBase(String uri) {
		return discretizationHandlers.get(uri).base;
	}
	
	public int getGroupBase(String lable) {
		return groupsBase.get(lable);
	}
		
	public int getBinNumber() {
		return lastBase;
	}
	
	/**
	 * 
	 * @param uri
	 * @param value
	 * @return
	 */
	public int code(String uri, Object value) {
		
		//find out the attribute
		DiscretizationWrapper attr = discretizationHandlers.get(uri);
		
		if(attr == null)
			throw new IllegalArgumentException("Unknown uri "+ uri);

		return attr.code(value);
	}
	
	/**
	 * 
	 * @param associations
	 * @return
	 */
	public int[] code(Map<String, Object> associations) {
		int index = 0;
		int[] itemSet = new int[associations.size()];
		
		for(Entry<String, Object> entry : associations.entrySet()) 
			if(discretizationHandlers.containsKey(entry.getKey()))
				itemSet[index++] = code(entry.getKey(), entry.getValue());
		
		return Arrays.copyOfRange(itemSet, 0, index);
	}
	
	/**
	 * 
	 * @param associations
	 * @return
	 */
	public int[] code(Vector<Map<String, Object>> associations) {
		
		int size = 0;
		//Calculate the itemset size
		for(Map<String, Object> map : associations) size += map.size();
		
		int index = 0;
		int[] itemSet = new int[size];
		for(Map<String, Object> map : associations)
			for(Entry<String, Object> entry : map.entrySet()) 
				if(discretizationHandlers.containsKey(entry.getKey()))	
					itemSet[index++] = code(entry.getKey(), entry.getValue());
			
		return Arrays.copyOfRange(itemSet, 0, index);
	}
	
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	public Association<String, Discrete> decode(int item) {
		
		if(item < 0 || item > lastBase - 1)
			throw new OutOfRangeException(item, 0, lastBase - 1);
		
		int base = 0;
		for(Iterator<Entry<String, DiscretizationWrapper>> iter = discretizationHandlers.entrySet().iterator(); iter.hasNext(); ){
			Entry<String, DiscretizationWrapper> currt = iter.next();
			int binSize = currt.getValue().getBinNumber();
			
			//it is in the bin range value
			if(item < base + binSize)
				return new Association<String, Discrete>(currt.getKey(), currt.getValue().getDiscrete(item));

			base += binSize;
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param items
	 * @return
	 */
	public Map<String, Discrete> decode(int[] items) {
		Map<String, Discrete> associations = new HashMap<String, Discrete>();
		
		//sort out items
		Arrays.sort(items);
		
		int base = 0, index = 0, item = items[index++];
		for(Iterator<Entry<String, DiscretizationWrapper>> iter = discretizationHandlers.entrySet().iterator(); iter.hasNext(); ){
			Entry<String, DiscretizationWrapper> currt = iter.next();
			int binSize = currt.getValue().getBinNumber();
			
			//it is in the bin range value
			if(item < base + binSize) {
				associations.put(currt.getKey(), currt.getValue().getDiscrete(item));
				if(index == items.length) return associations;
				item = items[index++];
			} 
			base += binSize;
		}
		
		//some items are excluded, throw an exception
		throw new OutOfRangeException(item, 0, lastBase - 1);
	}

}
