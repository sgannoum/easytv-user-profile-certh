package com.certh.iti.easytv.user.preference.attributes;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class SymmetricBinaryAttributeTest {
	
	SymmetricBinaryAttribute attr1; 
	
	@BeforeClass
	public void beforTest() {
		attr1 = new SymmetricBinaryAttribute();
		
		System.out.println("\n\nBefore Class");
		System.out.println(attr1.toString());
	}
	
	@AfterClass
	public void afterClass() {
		
		System.out.println("\n\nAfter Class");
		System.out.println(attr1.toString());
	}

	@Test
	public void test_code_attribute1() {
		Assert.assertEquals(0, attr1.code(false) - attr1.getAttributeCodeBase());
		Assert.assertEquals(1, attr1.code(true) - attr1.getAttributeCodeBase());
	}
	
	@Test
	public void test_decode_attribute1() {
		Assert.assertEquals(false, attr1.decode(attr1.getAttributeCodeBase() + 0));
		Assert.assertEquals(true, attr1.decode(attr1.getAttributeCodeBase() + 1));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_code_wrongDataType() {
		attr1.code(0);
	}

	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID1() {
		attr1.decode(attr1.getAttributeCodeBase() + attr1.getBinNumber());
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_decode_WrongBinID2() {
		attr1.decode(-1);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test_handle_outOfRange() {
		attr1.handle(0);
	}
	
	
		
}
