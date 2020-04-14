package com.certh.iti.easytv.user.preference.attributes.discretization;


public class BooleanDiscretization extends Discretization {
	
	private static final String BOOLEAN_XML_TYPE = "http://www.w3.org/2001/XMLSchema#boolean";

	public class BooleanDiscrete extends Discrete {
		
		public BooleanDiscrete(boolean item) {
			super(new Boolean[] {item}, item, BOOLEAN_XML_TYPE);
		}

		@Override
		public boolean inRange(Object literal) {
			if(!Boolean.class.isInstance(literal))
				throw new IllegalArgumentException("Value of type " + literal.getClass().getName() + " can't not be converted into boolean");
			
			return range[0] == literal;
		}

		@Override
		public int compare(Object item) {
			if(!Boolean.class.isInstance(item))
				throw new IllegalArgumentException("Value of type " + item.getClass().getName() + " can't not be converted into boolean");
			
			if(range[0] == item)
				return 0;
			else 
				return -1;
		}

		@Override
		public boolean checkType(Object item) {
			return Boolean.class.isInstance(item);
		}
		
	}

	public BooleanDiscretization() {
		super(new double[] {0.0, 1.0});
		this.bins = new Discrete[] {new BooleanDiscrete(false), new BooleanDiscrete(true)};
	}
	
	@Override
	public int getBinId(Object value) {
		if(!bins[0].checkType(value))
			throw new IllegalArgumentException("Value of type " + value.getClass().getName() + " is not compatible with "+ this.getClass().getName());

		boolean bolValue = (boolean) value;
		if(bolValue)
			return 1;
		else
			return 0;
	}

}
