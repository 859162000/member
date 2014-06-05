package com.wanda.ccs.jobhub.member;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.*;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.*;

import com.wanda.ccs.sqlasm.Clause;
import com.wanda.ccs.sqlasm.CriteriaParser;
import com.wanda.ccs.sqlasm.DataType;
import com.wanda.ccs.sqlasm.expression.DateUtils;

public class SegmentExcelExporter {
	
	private LinkedHashMap<String, ColumnDef> exportColumnDefs;
	
	private SXSSFWorkbook xlswb;
	
	private Sheet sh;
	
	private int rowIndex;
	
	public SegmentExcelExporter(int fetchSize, LinkedHashMap<String, ColumnDef> exportColumnDefs) {
		
		this.rowIndex = 0;
		this.exportColumnDefs = exportColumnDefs;
		
		this.xlswb = new SXSSFWorkbook(fetchSize); // keep fetch size rows in memory, exceeding rows will be flushed to disk
        this.sh = xlswb.createSheet();
        
        //Create first header row, set column label and column width
        Row headerRow = sh.createRow(rowIndex++);
        int headerColumnIdx = 0;
        Collection<ColumnDef> defs = this.exportColumnDefs.values();
        for(ColumnDef def : defs) {
        	this.sh.setColumnWidth(headerColumnIdx, def.getWidth());
        	Cell c = headerRow.createCell(headerColumnIdx);
        	c.setCellValue(def.getLabel());
        	headerColumnIdx ++;
        }
	}
	
	public void appendRows(List<Map<String, Object>> rowsData) {
		if(rowsData.size() > 0) {
			for(Map<String, Object> rowData : rowsData) {
	            Row row = sh.createRow(rowIndex++);
	            
	            int columnIdx = 0;
		        for(ColumnDef columnDef : exportColumnDefs.values()) {
	                Cell cell = row.createCell(columnIdx ++ );
	                Object rowValue = rowData.get(columnDef.getName());
	                if(rowValue != null) {
		                String value = convertValue(rowValue, columnDef.getDataType());
		                cell.setCellValue(value);
	                }
	            }
			}
		}
	}
	
	public void write(OutputStream out) throws IOException {
		xlswb.write(out);
	}

	public int getRowIndex() {
		return rowIndex;
	}
	
	public int exportedRowCount() {
		return rowIndex - 1;
	}

	/**
	 * Release the occupied resources.
	 */
	public void close() {
		xlswb.dispose();
	}

	private String convertValue(Object value, DataType type) {
        String ret = null;
        switch(type) {
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case SHORT:
            case STRING:
            	ret = value.toString();
            	break;
            case DATE:
            	ret = DateUtils.dateToMediumStr((java.util.Date)value);
                break;
            case DATE_TIME:
            	ret = DateUtils.dateTimeToMediumStr((java.util.Date)value);
                break;
            case BOOLEAN:
                ret = ((Boolean)value) ? "是" : "否";
                break;
            default:
                ret = null;
                assert false; //it will never go here
                break;
        }
        
        return ret;
	}
	
	
	public static class ColumnDef {
		
		private String name;
		
		private String label;
		
		private int width; //use units of 1/256th of a character(In POI Sheet.setColumnWidth(int))
		
		private DataType dataType;
		
		public ColumnDef(int width, DataType dataType) {
			this.width = width;
			this.dataType = dataType;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public DataType getDataType() {
			return dataType;
		}
		public void setDataType(DataType dataType) {
			this.dataType = dataType;
		}
	}
}
