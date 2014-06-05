package com.wanda.ccs.jobhub.member;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * excel2003操作类
 * 
 * @author zhurui
 * @date 2013年12月2日 下午5:14:01 
 *
 */
public class ExcelUtil {
	
	/* excel工作空间 */
	private HSSFWorkbook wb = null;
	/* 当前操作sheet页 */
	private HSSFSheet sheet = null;
	/* 当前操作sheet页数 */
	private int sheetNum = -1; 

	private ExcelUtil() {}
	
	public static ExcelUtil getInstance() {
		return new ExcelUtil();
	}

	/**
	 * 读取excel文件
	 * 
	 * @param @param file
	 * @param @throws IOException    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void open(File file) throws IOException {
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
			wb = new HSSFWorkbook(new POIFSFileSystem(input));
		} finally {
			if (input != null) input.close();
		}
	}

	/**
	 * 读取输入流
	 * 
	 * @param @param input
	 * @param @throws IOException    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void openIn(InputStream input) throws IOException {
		try {
			wb = new HSSFWorkbook(new POIFSFileSystem(input));
		} finally {
			if (input != null) input.close();
		}
	}

	/**
	 * 返回sheet表数目
	 * 
	 * @return int
	 */
	public int getSheetCount() {
		if (wb == null) {
			return 0;
		} else {
			return wb.getNumberOfSheets();
		}
	}

	/**
	 * 改变sheet工作环境
	 * 
	 * @param @param index 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void changeSheet(int index) {
		if (this.sheetNum != index) {
			this.sheet = wb.getSheetAt(index);
			this.sheetNum = index;
		}
	}

	/**
	 * sheetNum下的记录行数
	 * 
	 * @return int
	 */
	public int getRowCount() {
		if (sheet == null) {
			return 0;
		}
		return sheet.getLastRowNum();
	}

	/**
	 * 读取指定sheetNum的rowCount
	 * 
	 * @param sheetNum
	 * @return int
	 */
	public int getRowCount(int sheetNum) {
		if (sheetNum != this.sheetNum) {
			sheet = wb.getSheetAt(sheetNum);
		}
		return sheet.getLastRowNum();
	}

	/**
	 * 得到指定行的内容
	 * 
	 * @param lineNum
	 * @return String[]
	 */
	public String[] readExcelLine(int lineNum) {
		String[] strExcelLine = null;
		HSSFRow row = sheet.getRow(lineNum);
		if(row != null && row.getLastCellNum() > 0) {
			int cellCount = row.getLastCellNum();
			strExcelLine = new String[cellCount];
			for (int i = 0; i < cellCount; i++) {
				strExcelLine[i] = readStringExcelCell(row, i);
			}
		}

		return strExcelLine;
	}

	/**
	 * 读取指定列的内容
	 * 
	 * @param cellNum
	 * @return String
	 */
	public String readStringExcelCell(HSSFRow row, int cellNum) {
		String strExcelCell = "";
		
		try {
			switch (row.getCell(cellNum).getCellType()) {
//			case HSSFCell.CELL_TYPE_FORMULA:
//				strExcelCell = "FORMULA";
//				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				// 去除科学计数法和小数点
				strExcelCell = new BigDecimal(row.getCell(cellNum)
						.getNumericCellValue()).stripTrailingZeros()
						.toPlainString();
				/*if (strExcelCell.indexOf(".") > -1) { // 如果有出现0.0
					strExcelCell = strExcelCell.substring(0, strExcelCell.indexOf("."));
				}*/
				break;
			case HSSFCell.CELL_TYPE_STRING:
				strExcelCell = row.getCell(cellNum).getStringCellValue().trim();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				strExcelCell = "";
				break;
			default:
				strExcelCell = "";
				break;
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return strExcelCell;
	}
	
	public static void main(String[] args) throws IOException {
		 ExcelUtil util = ExcelUtil.getInstance();
		 util.open(new File("D:\\sqlload\\new.xls"));
		 //util.changeSheet(0);
		 int rowCount = util.getRowCount(0);
		 for(int row=1;row<=rowCount;row++) {
			 String[] str = util.readExcelLine(row);
			 System.out.println("row:"+row);
			 for(int i=0;i<str.length;i++) {
				 System.out.print(str[i]+"-");
			 }
			 System.out.println();
		 }
	}

}
