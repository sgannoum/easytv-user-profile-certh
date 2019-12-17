package com.certh.iti.easytv.util;

import org.testng.annotations.Test;

import com.certh.iti.easytv.util.Table;
import com.certh.iti.easytv.util.Table.CellFormat;
import com.certh.iti.easytv.util.Table.Position;

public class TableTest {
	
	@Test
	public void test() {
		
		Table table = new Table(2, 10);
		Table.Row headerRow = table.createRow();
		Table.Row bodyRow = table.createRow();
		
		headerRow.addCell("header1");
		headerRow.addCell("header2");
		bodyRow.addCell("column1");
		bodyRow.addCell("column2");
		
		table.addRow(headerRow);
		table.addRow(bodyRow);
		
		System.out.println(table.toString());

		
	}
	
	@Test
	public void test_merge() {
		
		Table table = new Table(2, 10);
		Table.Row headerRow = table.createRow(1);
		Table.Row bodyRow = table.createRow();
		
		headerRow.addCell("header1");
		bodyRow.addCell("column1");
		bodyRow.addCell("column2");
		
		table.addRow(headerRow);
		table.addRow(bodyRow);
		
		System.out.println(table.toString());
	}
	
	@Test
	public void test_center() {
		
		Table table = new Table(2, 10);
		Table.Row headerRow = table.createRow(1, Position.CENTER);
		Table.Row headerRow1 = table.createRow(1, Position.LEFT);
		Table.Row headerRow2 = table.createRow(1, Position.RIGTH);
		Table.Row bodyRow = table.createRow(Position.CENTER);
		Table.Row bodyRow1 = table.createRow(Position.LEFT);
		Table.Row bodyRow2 = table.createRow(Position.RIGTH);
		
		headerRow.addCell("header1");
		headerRow1.addCell("header1");
		headerRow2.addCell("header1");
		bodyRow.addCell("column1"); bodyRow.addCell("column2");
		bodyRow1.addCell("column1"); bodyRow1.addCell("column2");
		bodyRow2.addCell("column1"); bodyRow2.addCell("column2");
		
		table.addRow(headerRow);
		table.addRow(headerRow1);
		table.addRow(headerRow2);
		table.addRow(bodyRow);
		table.addRow(bodyRow1);
		table.addRow(bodyRow2);
		
		System.out.println(table.toString());
	}
	
	@Test
	public void test_dynamic() {
		
		CellFormat[] CellFormat = new CellFormat[] {new CellFormat(10),
													new CellFormat(20),
													new CellFormat(30)};
		
		Table table = new Table(CellFormat);
		Table.Row headerRow = table.createRow(1, Position.CENTER);
		Table.Row headerRow1 = table.createRow(1, Position.LEFT);
		Table.Row headerRow2 = table.createRow(1, Position.RIGTH);
		Table.Row bodyRow = table.createRow(Position.CENTER);
		Table.Row bodyRow1 = table.createRow(Position.LEFT);
		Table.Row bodyRow2 = table.createRow(Position.RIGTH);
		
		headerRow.addCell("header1");
		headerRow1.addCell("header1");
		headerRow2.addCell("header1");
		bodyRow.addCell("column1"); bodyRow.addCell("column2"); bodyRow.addCell("column3");
		bodyRow1.addCell("column1"); bodyRow1.addCell("column2"); bodyRow1.addCell("column3");
		bodyRow2.addCell("column1"); bodyRow2.addCell("column2"); bodyRow2.addCell("column3");
		
		table.addRow(headerRow);
		table.addRow(headerRow1);
		table.addRow(headerRow2);
		table.addRow(bodyRow);
		table.addRow(bodyRow1);
		table.addRow(bodyRow2);
		
		System.out.println(table.toString());
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void test_dynamic1() {
		
		CellFormat[] CellFormat = new CellFormat[] {new CellFormat(10),
													new CellFormat(20),
													new CellFormat(30)};
		
		Table table = new Table(CellFormat);
		Table.Row headerRow = table.createRow(2, Position.CENTER); //not allowd as three cells can 't be merged into groups of 2
	}

}
