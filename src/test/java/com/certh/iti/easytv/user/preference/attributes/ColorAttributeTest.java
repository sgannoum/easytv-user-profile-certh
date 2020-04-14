package com.certh.iti.easytv.user.preference.attributes;

import java.awt.Color;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class ColorAttributeTest {
	
	ColorAttribute attr1, attr2; 
	
	@BeforeClass
	public void beforClass() {
		attr1 = new ColorAttribute();
		attr2 = new ColorAttribute(new Integer[][] { new Integer[] {0x000000, 0x0000ff }, //blue 
													 new Integer[] {0x0001ff, 0x00ffff }, //green
													 new Integer[] {0x01ffff, 0xffffff }  //red
												   });		
	}
	
	@Test
	public void test_BinNumber() {
		Assert.assertEquals(100, attr1.getBinNumber());
		Assert.assertEquals(3, attr2.getBinNumber());
	}
	
	@Test
	public void test_code_attribute1() {
		Assert.assertEquals(0, attr1.code("#"+Integer.toHexString(Color.BLACK.getRGB() & 0x00ffffff )));
		Assert.assertEquals(0, attr1.code("#"+Integer.toHexString(167771)));
		Assert.assertEquals(0, attr1.code("#"+Integer.toHexString(167772)));

		Assert.assertEquals(1, attr1.code("#"+Integer.toHexString(167773)));
		Assert.assertEquals(1, attr1.code("#"+Integer.toHexString(167780)));
		Assert.assertEquals(1, attr1.code("#"+Integer.toHexString(335545)));
		
		Assert.assertEquals(99, attr1.code("#"+Integer.toHexString(16609444)));
		Assert.assertEquals(99, attr1.code("#"+Integer.toHexString(16609450)));
		Assert.assertEquals(99, attr1.code("#"+Integer.toHexString(16777215))); 
		
		Assert.assertEquals(99, attr1.code("#"+Integer.toHexString(Color.WHITE.getRGB() & 0x00ffffff )));
	}
	
	@Test
	public void test_decode_attribute1() {
		Assert.assertEquals("#"+Integer.toHexString(80000), attr1.decode(0));
		Assert.assertEquals("#"+Integer.toHexString(247773), attr1.decode(1));
		Assert.assertEquals("#"+Integer.toHexString(415546), attr1.decode(2));
	}
	
	@Test
	public void test_code_attribute2() {
		Assert.assertEquals(0, attr2.code("#"+Integer.toHexString(0x000000)));
		Assert.assertEquals(0, attr2.code("#"+Integer.toHexString(0x0000ff)));
		Assert.assertEquals(1, attr2.code("#"+Integer.toHexString(0x00ff00)));
		Assert.assertEquals(2, attr2.code("#"+Integer.toHexString(0xff0000)));
		Assert.assertEquals(2, attr2.code("#"+Integer.toHexString(0xffffff)));
		Assert.assertEquals(2, attr2.code("#"+Integer.toHexString(Color.pink.getRGB() & 0x00ffffff )));
	}
	
	@Test
	public void test_decode_attribute2() {
		Assert.assertEquals("#"+Integer.toHexString(100), attr2.decode(0));
		Assert.assertEquals("#"+Integer.toHexString(30511), attr2.decode(1));
		Assert.assertEquals("#"+Integer.toHexString(8131071), attr2.decode(2));
	}
	
}
