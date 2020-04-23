package com.certh.iti.easytv.user.preference.attributes;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.exceptions.UserProfileParsingException;
import com.certh.iti.easytv.user.preference.attributes.discretization.Discrete;

public class AttributesAggregatorTest {
	
	private AttributesAggregator aggregator1 = new AttributesAggregator();
	private AttributesAggregator aggregator2 = new AttributesAggregator();
	private AttributesAggregator aggregator3 = new AttributesAggregator();

	
	protected static Map<String, Attribute> attributes  =  new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
	{
		put("Integer", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, -1));
	    put("Double", new DoubleAttribute(new double[] {1.0, 2.0}, 0.5, -1));
	    put("Nominal", new NominalAttribute(new String[] {"1", "2", "3"}));
	    put("Ordinal", new OrdinalAttribute(new String[] {"15", "20", "23"}));					
	    put("Boolean", new SymmetricBinaryAttribute());

    }};
    
	protected static Map<String, Attribute> attributes1  =  new LinkedHashMap<String, Attribute>() {
		private static final long serialVersionUID = 1L;
	{
		put("Integer1", new IntegerAttribute(new double[] {0.0, 100.0}, 1.0, 25, -1));
	    put("Double1", new DoubleAttribute(new double[] {1.0, 2.0}, 0.5, -1));
	    put("Nominal1", new NominalAttribute(new String[] {"1", "2", "3"}));
	    put("Ordinal1", new OrdinalAttribute(new String[] {"15", "20", "23"}));					
	    put("Boolean1", new SymmetricBinaryAttribute());

    }};
    
    @BeforeClass
    public void beforeClass() {
    	Attribute attr;
    	
    	//load values
    	attr = attributes.get("Integer");
    	for(int i = 0; i < 101; i++) attr.handle(i);
    	attr = attributes.get("Double");
		for(double i = 1.0; i < 2.5; i+= 0.5) attr.handle(i);
    	attr = attributes.get("Nominal");
		attr.handle("1"); attr.handle("2"); attr.handle("3");
    	attr = attributes.get("Ordinal");
		attr.handle("15"); attr.handle("20"); attr.handle("23");

		
    	attr = attributes1.get("Integer1");
    	for(int i = 0; i < 101; i++) attr.handle(i);
    	attr = attributes1.get("Double1");
		for(double i = 1.0; i < 2.5; i+= 0.5) attr.handle(i);
    	attr = attributes1.get("Nominal1");
		attr.handle("1"); attr.handle("2"); attr.handle("3");
    	attr = attributes1.get("Ordinal1");
		attr.handle("15"); attr.handle("20"); attr.handle("23");

    	
    	aggregator1.add(attributes);
    	aggregator2.add(attributes1);
    	aggregator3.add(aggregator1);
    	aggregator3.add(aggregator2);

    }
	
	@Test
	public void test_size() {
		Assert.assertEquals(36, aggregator1.getBinNumber());
		Assert.assertEquals(36, aggregator2.getBinNumber());
		Assert.assertEquals(36 * 2, aggregator3.getBinNumber());
	}
	
	@Test
	public void test_iterator1() {
		
		Iterator<Discrete> iterator = aggregator1.discreteIterator();
		int counts = 0;
		for(; iterator.hasNext();  iterator.next(), counts++);
		
		Assert.assertEquals(aggregator1.getBinNumber(), counts);
	}
    
	@Test
	public void test_code1() {
		
		Assert.assertEquals(0, aggregator1.code("Integer", 0));
		Assert.assertEquals(0, aggregator1.code("Integer", 4));
		Assert.assertEquals(1, aggregator1.code("Integer", 5));
		Assert.assertEquals(1, aggregator1.code("Integer", 8));
		Assert.assertEquals(24, aggregator1.code("Integer", 100));
		
		Assert.assertEquals(25, aggregator1.code("Double", 1.0));
		Assert.assertEquals(26, aggregator1.code("Double", 1.5));
		Assert.assertEquals(27, aggregator1.code("Double", 2.0));

		Assert.assertEquals(28, aggregator1.code("Nominal", "1"));
		Assert.assertEquals(29, aggregator1.code("Nominal", "2"));
		Assert.assertEquals(30, aggregator1.code("Nominal", "3"));
		
		Assert.assertEquals(31, aggregator1.code("Ordinal", "15"));
		Assert.assertEquals(32, aggregator1.code("Ordinal", "20"));
		Assert.assertEquals(33, aggregator1.code("Ordinal", "23"));
		
		Assert.assertEquals(34, aggregator1.code("Boolean", false));
		Assert.assertEquals(35, aggregator1.code("Boolean", true));
	}
	
	@Test
	public void test_code12() throws UserProfileParsingException {
		
		 Map<String, Object> preferences = new LinkedHashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{	
		     	put("Integer", 5);
		     	put("Double", 1.5);
			    put("Nominal", "2");
			    put("Ordinal", "20");
			    put("Boolean", false);					

			}};
				
		int[] actual_itemset = aggregator1.code(preferences);
		int[] expected_itemset = new int[] {1, 26, 29, 32, 34};

		Assert.assertEquals(actual_itemset, expected_itemset);
	}
	
	@Test
	public void test_decode1() {
		
		//bins of the first dimension "Integer"
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer", attributes.get("Integer").getDiscretization().getBins()[0]), aggregator1.decode(0));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer", attributes.get("Integer").getDiscretization().getBins()[1]), aggregator1.decode(1));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer", attributes.get("Integer").getDiscretization().getBins()[2]), aggregator1.decode(2));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer", attributes.get("Integer").getDiscretization().getBins()[3]), aggregator1.decode(3));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer", attributes.get("Integer").getDiscretization().getBins()[24]), aggregator1.decode(24));
		
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Double", attributes.get("Double").getDiscretization().getBins()[0]), aggregator1.decode(25));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Double", attributes.get("Double").getDiscretization().getBins()[1]), aggregator1.decode(26));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Double", attributes.get("Double").getDiscretization().getBins()[2]), aggregator1.decode(27));
	
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Nominal", attributes.get("Nominal").getDiscretization().getBins()[0]), aggregator1.decode(28));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Nominal", attributes.get("Nominal").getDiscretization().getBins()[1]), aggregator1.decode(29));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Nominal", attributes.get("Nominal").getDiscretization().getBins()[2]), aggregator1.decode(30));
		
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Ordinal", attributes.get("Ordinal").getDiscretization().getBins()[0]), aggregator1.decode(31));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Ordinal", attributes.get("Ordinal").getDiscretization().getBins()[1]), aggregator1.decode(32));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Ordinal", attributes.get("Ordinal").getDiscretization().getBins()[2]), aggregator1.decode(33));

		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Boolean", attributes.get("Boolean").getDiscretization().getBins()[0]), aggregator1.decode(34));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Boolean", attributes.get("Boolean").getDiscretization().getBins()[1]), aggregator1.decode(35));
	}
	
	@Test
	public void test_decode12() {
		
		int[] items = new int[] {1, 26, 29, 32, 35};
		
		Map<String, Object> expected = new LinkedHashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{	
		     	put("Integer", attributes.get("Integer").getDiscretization().getBins()[1]);
		     	put("Double", attributes.get("Double").getDiscretization().getBins()[1]);
			    put("Nominal", attributes.get("Nominal").getDiscretization().getBins()[1]);
			    put("Ordinal", attributes.get("Ordinal").getDiscretization().getBins()[1]);
			    put("Boolean", attributes.get("Boolean").getDiscretization().getBins()[1]);					

			}};
			
		Map<String, Discrete> actual  = aggregator1.decode(items);
		Assert.assertEquals(actual, expected);
	}
	
	@Test
	public void test_decode13() {
		
		int[] items = new int[] {1, 29, 35};
		
		Map<String, Object> expected = new LinkedHashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{	
		     	put("Integer", attributes.get("Integer").getDiscretization().getBins()[1]);
			    put("Nominal", attributes.get("Nominal").getDiscretization().getBins()[1]);
			    put("Boolean", attributes.get("Boolean").getDiscretization().getBins()[1]);					

			}};
			
		Map<String, Discrete> actual  = aggregator1.decode(items);
		Assert.assertEquals(actual, expected);
	}
	
	@Test
	public void test_code2() {
		
		Assert.assertEquals(0, aggregator2.code("Integer1", 0));
		Assert.assertEquals(0, aggregator2.code("Integer1", 4));
		Assert.assertEquals(1, aggregator2.code("Integer1", 5));
		Assert.assertEquals(1, aggregator2.code("Integer1", 8));
		Assert.assertEquals(24, aggregator2.code("Integer1", 100));
		
		Assert.assertEquals(25, aggregator2.code("Double1", 1.0));
		Assert.assertEquals(26, aggregator2.code("Double1", 1.5));
		Assert.assertEquals(27, aggregator2.code("Double1", 2.0));

		Assert.assertEquals(28, aggregator2.code("Nominal1", "1"));
		Assert.assertEquals(29, aggregator2.code("Nominal1", "2"));
		Assert.assertEquals(30, aggregator2.code("Nominal1", "3"));
		
		Assert.assertEquals(31, aggregator2.code("Ordinal1", "15"));
		Assert.assertEquals(32, aggregator2.code("Ordinal1", "20"));
		Assert.assertEquals(33, aggregator2.code("Ordinal1", "23"));
		
		Assert.assertEquals(34, aggregator2.code("Boolean1", false));
		Assert.assertEquals(35, aggregator2.code("Boolean1", true));
	}
	
	@Test
	public void test_code3() {
		
		Assert.assertEquals(0, aggregator3.code("Integer", 0));
		Assert.assertEquals(0, aggregator3.code("Integer", 4));
		Assert.assertEquals(1, aggregator3.code("Integer", 5));
		Assert.assertEquals(1, aggregator3.code("Integer", 8));
		Assert.assertEquals(24, aggregator3.code("Integer", 100));
		
		Assert.assertEquals(25, aggregator3.code("Double", 1.0));
		Assert.assertEquals(26, aggregator3.code("Double", 1.5));
		Assert.assertEquals(27, aggregator3.code("Double", 2.0));

		Assert.assertEquals(28, aggregator3.code("Nominal", "1"));
		Assert.assertEquals(29, aggregator3.code("Nominal", "2"));
		Assert.assertEquals(30, aggregator3.code("Nominal", "3"));
		
		Assert.assertEquals(31, aggregator3.code("Ordinal", "15"));
		Assert.assertEquals(32, aggregator3.code("Ordinal", "20"));
		Assert.assertEquals(33, aggregator3.code("Ordinal", "23"));
		
		Assert.assertEquals(34, aggregator3.code("Boolean", false));
		Assert.assertEquals(35, aggregator3.code("Boolean", true));
		
		Assert.assertEquals(36 + 0, aggregator3.code("Integer1", 0));
		Assert.assertEquals(36 + 0, aggregator3.code("Integer1", 4));
		Assert.assertEquals(36 + 1, aggregator3.code("Integer1", 5));
		Assert.assertEquals(36 + 1, aggregator3.code("Integer1", 8));
		Assert.assertEquals(36 + 24, aggregator3.code("Integer1", 100));
		
		Assert.assertEquals(36 + 25, aggregator3.code("Double1", 1.0));
		Assert.assertEquals(36 + 26, aggregator3.code("Double1", 1.5));
		Assert.assertEquals(36 + 27, aggregator3.code("Double1", 2.0));

		Assert.assertEquals(36 + 28, aggregator3.code("Nominal1", "1"));
		Assert.assertEquals(36 + 29, aggregator3.code("Nominal1", "2"));
		Assert.assertEquals(36 + 30, aggregator3.code("Nominal1", "3"));
		
		Assert.assertEquals(36 + 31, aggregator3.code("Ordinal1", "15"));
		Assert.assertEquals(36 + 32, aggregator3.code("Ordinal1", "20"));
		Assert.assertEquals(36 + 33, aggregator3.code("Ordinal1", "23"));
		
		Assert.assertEquals(36 + 34, aggregator3.code("Boolean1", false));
		Assert.assertEquals(36 + 35, aggregator3.code("Boolean1", true));
	}
	
	@Test
	public void test_decode3() {
		
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer", attributes.get("Integer").getDiscretization().getBins()[0]), aggregator3.decode(0));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer", attributes.get("Integer").getDiscretization().getBins()[1]), aggregator3.decode(1));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer", attributes.get("Integer").getDiscretization().getBins()[2]), aggregator3.decode(2));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer", attributes.get("Integer").getDiscretization().getBins()[3]), aggregator3.decode(3));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer", attributes.get("Integer").getDiscretization().getBins()[24]), aggregator3.decode(24));
		
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Double", attributes.get("Double").getDiscretization().getBins()[0]), aggregator3.decode(25));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Double", attributes.get("Double").getDiscretization().getBins()[1]), aggregator3.decode(26));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Double", attributes.get("Double").getDiscretization().getBins()[2]), aggregator3.decode(27));
	
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Nominal", attributes.get("Nominal").getDiscretization().getBins()[0]), aggregator3.decode(28));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Nominal", attributes.get("Nominal").getDiscretization().getBins()[1]), aggregator3.decode(29));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Nominal", attributes.get("Nominal").getDiscretization().getBins()[2]), aggregator3.decode(30));
		
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Ordinal", attributes.get("Ordinal").getDiscretization().getBins()[0]), aggregator3.decode(31));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Ordinal", attributes.get("Ordinal").getDiscretization().getBins()[1]), aggregator3.decode(32));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Ordinal", attributes.get("Ordinal").getDiscretization().getBins()[2]), aggregator3.decode(33));

		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Boolean", attributes.get("Boolean").getDiscretization().getBins()[0]), aggregator3.decode(34));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Boolean", attributes.get("Boolean").getDiscretization().getBins()[1]), aggregator3.decode(35));
		
		
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer1", attributes1.get("Integer1").getDiscretization().getBins()[0]), aggregator3.decode(36 + 0));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer1", attributes1.get("Integer1").getDiscretization().getBins()[1]), aggregator3.decode(36 + 1));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer1", attributes1.get("Integer1").getDiscretization().getBins()[2]), aggregator3.decode(36 + 2));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer1", attributes1.get("Integer1").getDiscretization().getBins()[3]), aggregator3.decode(36 + 3));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Integer1", attributes1.get("Integer1").getDiscretization().getBins()[24]), aggregator3.decode(36 + 24));
		
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Double1", attributes1.get("Double1").getDiscretization().getBins()[0]), aggregator3.decode(36 + 25));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Double1", attributes1.get("Double1").getDiscretization().getBins()[1]), aggregator3.decode(36 + 26));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Double1", attributes1.get("Double1").getDiscretization().getBins()[2]), aggregator3.decode(36 + 27));
	
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Nominal1", attributes1.get("Nominal1").getDiscretization().getBins()[0]), aggregator3.decode(36 + 28));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Nominal1", attributes1.get("Nominal1").getDiscretization().getBins()[1]), aggregator3.decode(36 + 29));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Nominal1", attributes1.get("Nominal1").getDiscretization().getBins()[2]), aggregator3.decode(36 + 30));
		
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Ordinal1", attributes1.get("Ordinal1").getDiscretization().getBins()[0]), aggregator3.decode(36 + 31));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Ordinal1", attributes1.get("Ordinal1").getDiscretization().getBins()[1]), aggregator3.decode(36 + 32));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Ordinal1", attributes1.get("Ordinal1").getDiscretization().getBins()[2]), aggregator3.decode(36 + 33));

		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Boolean1", attributes1.get("Boolean1").getDiscretization().getBins()[0]), aggregator3.decode(36 + 34));
		Assert.assertEquals(new AttributesAggregator.Association<String, Object>("Boolean1", attributes1.get("Boolean1").getDiscretization().getBins()[1]), aggregator3.decode(36 + 35));

	}
	
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void test_code_wrong1() {
		aggregator1.code("wrong uri", 0);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void test_code_wrong2() {
		aggregator1.code("Integer", "wrong type");
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void test_decode_wrong1() {
		aggregator1.decode(36);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void test_decode_wrong2() {
		aggregator1.decode(-1);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void test_decode_wrong3() {
		int[] items = new int[] {60, 26, 29, 32, 35};
		
		aggregator1.decode(items);
	}

}
