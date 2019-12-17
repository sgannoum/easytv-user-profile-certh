package com.certh.iti.easytv.util;

import java.util.Date;

import javax.lang.model.type.UnknownTypeException;

import org.apache.commons.math3.exception.OutOfRangeException;

public class Table {
	
	public enum Position {LEFT, CENTER, RIGTH} ;
	private int rowWidth = 0;
	private int rows = 0;
	
	private StringBuffer table = new StringBuffer();
	private CellFormat[] tableCellFormats;
	private String middleLine, upperLine; 
	
	/**
	 * A cell in the table
	 *
	 */
	public static class CellFormat {
		
		private Position position = Position.LEFT;
		private int cellWidth;
	
		public CellFormat(int width) {
			this.cellWidth = width;
		}
		
		public CellFormat(int width, Position position) {
			this.position = position;
			this.cellWidth = width;
		}
		
		public String formalize(Object obj) {	
			
			//Create one if no format
			String format = "%" + getConversion(obj);			
			String str = String.format(format, obj);
			
			if(position == Position.CENTER) {
				int width = (cellWidth/2);
				int emptySpace = width - str.length()/2;
				
				if(emptySpace > 0)
					return String.format("|%-"+emptySpace+"s%-"+(cellWidth - emptySpace)+"s", " ", str);
				else 
					return String.format("|%-"+cellWidth+"s",str);
			}
			else if(position == Position.LEFT) {
				return String.format("|%-"+cellWidth+"s", str);
			}
			else if(position == Position.RIGTH) {
				return String.format("|%"+cellWidth+"s", str);
			}
			
			return str;
		}
	}
	
	/**
	 * An internal class that has access to the table private data
	 * 
	 */
	public class Row {
		private int counts = 0;
		private CellFormat[] cellFormats;
		private StringBuffer row = new StringBuffer();
		
		protected Row(final CellFormat[] cellFormats) {
			this.cellFormats = cellFormats;			
		}
		
		protected Row(final CellFormat[] cellFormats, Position position) {
			this.cellFormats = new CellFormat[cellFormats.length];
			for(int i = 0; i < cellFormats.length; i++) {
				this.cellFormats[i] = new CellFormat(cellFormats[i].cellWidth, position);
			}
		}
		
		protected Row(final CellFormat[] cellFormats, int groups) {		
			this.cellFormats = new CellFormat[groups];
			int width = (rowWidth / groups) - 2;
			for(int i = 0; i < groups; i++) {
				this.cellFormats[i] = new CellFormat(width, cellFormats[i].position);
			}
		}
		
		protected Row(final CellFormat[] cellFormats, int groups, Position position) {			
			this.cellFormats = new CellFormat[groups];
			int width = (rowWidth / groups) - 2;
			for(int i = 0; i < groups; i++) {
				this.cellFormats[i] = new CellFormat(width, position);
			}
		}
		
		public void addCell(Object obj) {
			
			if(counts == cellFormats.length)
				throw new OutOfRangeException(cellFormats.length + 1, 0, cellFormats.length);

			CellFormat cellFormat = cellFormats[counts];
			row.append(cellFormat.formalize(obj));
			
			if(++counts == cellFormats.length) {
				row.append("|\n");
				row.append(middleLine);
			}
		}
		
		
		public void fill(Object obj) {
			if(counts == cellFormats.length) {

				for(int i = counts; i < cellFormats.length; i++) 
					row.append(cellFormats[i].formalize(obj));
				
				row.append("|\n");
				row.append(middleLine);
			}
		}
		
		public boolean isFull() {
			return (counts == 0);
		}
		
		public void reset() {
			row.setLength(0);
		}
		
	}
	
	
	public Table(CellFormat[] cellFormat) {
		this.tableCellFormats = cellFormat;
		
		rowWidth = cellFormat.length + 1;
		for(int i = 0; i < cellFormat.length; i++) {
			CellFormat f = cellFormat[i];
			rowWidth += f.cellWidth;
		}
		
		String emplyLine = String.format("%"+rowWidth+"s\n", " ");
		upperLine = emplyLine.replaceAll(" ", "+");
		middleLine = emplyLine.replaceAll(" ", "-");
		
		table.append(upperLine);
	}
	
	public Table(int columns, int columWidth) {
		rowWidth = (columWidth * columns) + (columns + 1);
		
		String emplyLine = String.format("%"+rowWidth+"s\n", " ");
		upperLine = emplyLine.replaceAll(" ", "+");
		middleLine = emplyLine.replaceAll(" ", "-");
		
		table.append(upperLine);
					
		tableCellFormats = new CellFormat[columns];
		for(int i = 0; i < columns; i++) {
			tableCellFormats[i] = new CellFormat(columWidth, Position.LEFT);
		}
		
	}
	
	public Table(Table other, int columns, int columWidth) {
		rowWidth = (columWidth * columns) + (columns + 1);
		
		String emplyLine = String.format("%"+rowWidth+"s\n", " ");
		upperLine = emplyLine.replaceAll(" ", "+");
		middleLine = emplyLine.replaceAll(" ", "-");
		
		table.append(other.table.toString());
		rows += other.rows;
	}
	
	public Row createRow() {
		return new Row(this.tableCellFormats, Position.LEFT);
	}
	
	public Row createRow(Position position) {	
		return new Row(this.tableCellFormats, position);
	}
	
	public Row createRow(int columns) {	
		return createRow(columns, Position.LEFT);
	}
	
	public Row createRow(int columns, Position position) {
		
		if(columns < 0 || columns > tableCellFormats.length)
			throw new OutOfRangeException(columns, 0, tableCellFormats.length);
		
		if(tableCellFormats.length % columns != 0 )
			throw new IllegalArgumentException("Can't merge " + tableCellFormats.length + " columns into: " + columns);
		
		return new Row(this.tableCellFormats, columns, position);
	}
	
	public void addRow(Object[] row) {
		
		Row newRow = new Row(tableCellFormats);	

		for(Object obj : row) 
			newRow.addCell(obj);
		
		addRow(newRow);
	}
	
	public void addRow(Object[] row, Position position) {
		
		Row newRow = new Row(this.tableCellFormats, position);	
		
		for(Object obj : row) 
			newRow.addCell(obj);
		
		addRow(newRow);
	}
	
	public void addRow(Row row) {
		table.append(row.row.toString());
		rows++;
	}
	
	public void addTable(Table other) {
		table.append(other.table.toString());
		rows += other.rows;
	}
	
	public void reset() {
		table.setLength(0);
		rows = 0;
		rowWidth = 0;
	}
	
	@Override
	public String toString() {
		return table.substring(0, table.length() - rowWidth - 1) + upperLine;
	}
	
	/**
	 * Find conversion type of the given object 
	 * 
	 * @param obj
	 * @return
	 */
	private static String getConversion(Object obj) {
		
		if(String.class.isInstance(obj)) return "s";
		else if(Double.class.isInstance(obj)) return ".1f";
		else if(Long.class.isInstance(obj)) return "d";
		else if(Integer.class.isInstance(obj)) return "d";
		else if(Date.class.isInstance(obj)) return "t";
		else if(Boolean.class.isInstance(obj)) return "b";
		
		throw new UnknownTypeException(null, obj);
	}

}
