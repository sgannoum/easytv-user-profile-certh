package com.certh.iti.easytv.user.preference.attributes;

import java.awt.Color;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.certh.iti.easytv.user.preference.attributes.discretization.Discretization;

import junit.framework.Assert;

public class ColorAttributeTest {
	
	ColorAttribute attr1, attr2, attr3; 
	Discretization dist_attr1, dist_attr2, dist_attr3;
	
	@BeforeClass
	public void beforClass() {
		attr3 = new ColorAttribute(); 
		attr1 = new ColorAttribute(100);
		attr2 = new ColorAttribute(new Integer[][] { new Integer[] {0x000000, 0x0000ff }, //blue 
													 new Integer[] {0x0001ff, 0x00ffff }, //green
													 new Integer[] {0x01ffff, 0xffffff }  //red
												   });
		//load values from all bins
		int binSize = (int) (Math.pow(2, 24) / 100);
		for(int i = 0; i < 101; i++)
			attr1.handle(String.format("#%06x", 0x000000 + (binSize * i)));
		
		//load values for other bins
		attr2.handle("#000000");
		attr2.handle("#ff1000");
		attr2.handle("#0001ff");
		attr2.handle("#0000ff");
		attr2.handle("#ffffff");
		
		//load values 
		attr3.handle("#000000");
		attr3.handle("#ff1000");
		attr3.handle("#0001ff");
		attr3.handle("#0000ff");
		attr3.handle("#ffffff");
		
		//get discretization
		dist_attr1 = attr1.getDiscretization();
		dist_attr2 = attr2.getDiscretization();
		dist_attr3 = attr3.getDiscretization();

	}
	
	@Test
	public void test_BinNumber() {
		Assert.assertEquals(100, dist_attr1.getBinNumber());
		Assert.assertEquals(3, dist_attr2.getBinNumber());
	}
	
	@Test
	public void test_code_attribute1() {
		Assert.assertEquals(0, dist_attr1.code("#"+Integer.toHexString(Color.BLACK.getRGB() & 0x00ffffff )));
		Assert.assertEquals(0, dist_attr1.code("#"+Integer.toHexString(0x28f5b)));
		Assert.assertEquals(0, dist_attr1.code("#"+Integer.toHexString(0x28f5c)));

		Assert.assertEquals(1, dist_attr1.code("#"+Integer.toHexString(0x28f5d)));
		Assert.assertEquals(1, dist_attr1.code("#"+Integer.toHexString(0x28f64)));
		Assert.assertEquals(1, dist_attr1.code("#"+Integer.toHexString(0x51eb9)));
		
		Assert.assertEquals(99, dist_attr1.code("#"+Integer.toHexString(0xfd70a4)));
		Assert.assertEquals(99, dist_attr1.code("#"+Integer.toHexString(0xfd70aa)));
		Assert.assertEquals(99, dist_attr1.code("#"+Integer.toHexString(0xffffff))); 
		
		Assert.assertEquals(99, dist_attr1.code("#"+Integer.toHexString(Color.WHITE.getRGB() & 0x00ffffff )));
	}
	
	@Test
	public void test_decode_attribute1() {
		Assert.assertEquals("#013880", dist_attr1.decode(0));
		Assert.assertEquals("#03c7dd", dist_attr1.decode(1));
		Assert.assertEquals("#06573a", dist_attr1.decode(2));
	}
	
	@Test
	public void test_code_attribute2() {
		Assert.assertEquals(0, dist_attr2.code("#"+Integer.toHexString(0x000000)));
		Assert.assertEquals(0, dist_attr2.code("#"+Integer.toHexString(0x0000ff)));
		Assert.assertEquals(1, dist_attr2.code("#"+Integer.toHexString(0x00ff00)));
		Assert.assertEquals(2, dist_attr2.code("#"+Integer.toHexString(0xff0000)));
		Assert.assertEquals(2, dist_attr2.code("#"+Integer.toHexString(0xffffff)));
		Assert.assertEquals(2, dist_attr2.code("#"+Integer.toHexString(Color.pink.getRGB() & 0x00ffffff )));
	}
	
	@Test
	public void test_decode_attribute2() {
		Assert.assertEquals("#000064", dist_attr2.decode(0));
		Assert.assertEquals("#00772f", dist_attr2.decode(1));
		Assert.assertEquals("#7c11ff", dist_attr2.decode(2));
	}
	
	@Test
	public void test_code_attribute3() {
		Assert.assertEquals(0, dist_attr3.code("#000000"));
		Assert.assertEquals(1, dist_attr3.code("#0000ff"));
		Assert.assertEquals(2, dist_attr3.code("#0001ff"));
		Assert.assertEquals(3, dist_attr3.code("#ff1000"));
		Assert.assertEquals(4, dist_attr3.code("#ffffff"));
	}
	
	@Test
	public void test_decode_attribute3() {
		Assert.assertEquals("#000000", dist_attr3.decode(0));
		Assert.assertEquals("#0000ff", dist_attr3.decode(1));
		Assert.assertEquals("#0001ff", dist_attr3.decode(2));
		Assert.assertEquals("#ff1000", dist_attr3.decode(3));
		Assert.assertEquals("#ffffff", dist_attr3.decode(4));
	}
}
