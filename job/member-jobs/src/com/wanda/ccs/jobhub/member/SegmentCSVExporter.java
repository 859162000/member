package com.wanda.ccs.jobhub.member;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.wanda.ccs.sqlasm.DataType;
import com.wanda.ccs.sqlasm.expression.DateUtils;

public class SegmentCSVExporter {
	
	public final static int BUFFER_SIZE = 2 * 1024;
	public final static String CHARSET = "GB2312";
	public final static String PATH = "/csv/";
	public final static String SUFFIX = ".csv";
	
	private int rowIndex = 0;
	private File csvFile = null;
	private BufferedWriter out = null;
	private LinkedHashMap<String, ColumnDef> exportColumnDefs = null;
	
	public SegmentCSVExporter(String fileName, LinkedHashMap<String, ColumnDef> exportColumnDefs) throws IOException {
		this.exportColumnDefs = exportColumnDefs;
		
		File directory = new File("..");//设定为当前文件夹
		File path = new File(directory.getAbsolutePath() + PATH);
		if(!path.exists()) {
			path.mkdirs();
		}
		csvFile = new File(path, fileName + SUFFIX);
		if(csvFile.exists()) {
			csvFile.delete();
		}
		
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), CHARSET), BUFFER_SIZE);
        Collection<ColumnDef> defs = this.exportColumnDefs.values();
        Iterator<ColumnDef> iterator = defs.iterator();
        StringBuilder sb = new StringBuilder();
        
        while(iterator.hasNext()) {
        	sb.append(iterator.next().getLabel());
        	if(iterator.hasNext()) {
        		sb.append(",");
        	}
        }
        out.write(sb.toString());
        out.newLine();
	}
	
	public void appendRows(List<Map<String, Object>> rowsData) throws IOException {
		StringBuilder sb = new StringBuilder();
		if(rowsData.size() > 0) {
			Iterator<ColumnDef> iterator = null;
			for(Map<String, Object> rowData : rowsData) {
				rowIndex++;
				iterator = exportColumnDefs.values().iterator();
				
				ColumnDef def = null; 
	        	Object rowValue = null;
		        while(iterator.hasNext()) {
		        	def = iterator.next(); 
		        	rowValue = rowData.get(def.getName());
		        	if(rowValue != null) {
		                String value = def.convertValue(rowValue);
		                sb.append(value);
	                }
		        	if(iterator.hasNext()) {
		        		sb.append(",");
		        	} else {
		        		sb.append("\n");
		        	}
		        	
		        	def = null; 
		        	rowValue = null;
		        }
		        iterator = null;
			}
			
			out.write(sb.toString());
		}
	}
	
	public void close() {
		try {
			if(out != null) out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(csvFile.exists()) csvFile.delete();
		}
	}
	
	public void write(OutputStream output) throws IOException {
		if(out != null) {
			out.close();
			out = null;
		}
		
		FileInputStream input = null;
		try {
			int len = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			input = new FileInputStream(csvFile);
			while((len = input.read(buffer)) != -1) {
				output.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(input != null) input.close();
		}
	}
	
	public int exportedRowCount() {
		return rowIndex;
	}
	
	public static class ColumnDef {
		private String name;
		private String label;
		private DataType dataType;
		
		public ColumnDef(DataType dataType) {
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
		public DataType getDataType() {
			return dataType;
		}
		public void setDataType(DataType dataType) {
			this.dataType = dataType;
		}
		
		public String convertValue(Object value) {
	        String ret = null;
	        switch(this.dataType) {
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
	}
	
}
