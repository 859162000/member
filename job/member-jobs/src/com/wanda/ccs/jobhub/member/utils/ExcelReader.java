package com.wanda.ccs.jobhub.member.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 使用xml方式对excel 2007进行解析
 * 
 * @author zhurui
 * @date 2013年12月9日 上午9:39:56
 */
public class ExcelReader extends DefaultHandler {
	
	private SharedStringsTable sst = null;
	/* 当前sheet页 */
	private int sheetIndex = 0;
	/* 当前行 */
	private int curRow = 1;
	/* 当前v标签中的内容 */
	private String content;
	/* 每一行数据 */
	private List<String> rowlist = new ArrayList<String>();
	/* 自定义处理器 */
	private RowHandler handler = null;
	/* 输入流 */
	private InputStream in = null;
	/* 对于字符串需要索引 */
	private boolean nextIsString = false;
	
	public ExcelReader(InputStream in, RowHandler handler) throws IOException {
		this.handler = handler;
		this.in = in;
	}
	
	/**
	 * 读取第一个工作簿的入口方法
	 * 
	 * @param path
	 */
	public void readOneSheet() throws Exception {
		InputStream sheet = null;
		try {
			OPCPackage pkg = OPCPackage.open(in);
			XSSFReader r = new XSSFReader(pkg);
			SharedStringsTable sst = r.getSharedStringsTable();

			XMLReader parser = fetchSheetParser(sst);
			
			sheet = r.getSheet("rId1");	//获取当前sheet页
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
		} finally {
			if (sheet != null)
				sheet.close();
		}
	}
	
	/**
	 * 该方法自动被调用，每读一行调用一次，在方法中写自己的业务逻辑即可
	 * 
	 * @param sheetIndex
	 *            工作簿序号
	 * @param curRow
	 *            处理到第几行
	 * @param rowList
	 *            当前数据行的数据集合
	 * @throws IOException 
	 */
	public void optRow(int sheetIndex, int curRow, List<String> rowList) {
		handler.row(sheetIndex, curRow, rowList);
	}

	public XMLReader fetchSheetParser(SharedStringsTable sst)
			throws SAXException {
		XMLReader parser = XMLReaderFactory
				.createXMLReader("org.apache.xerces.parsers.SAXParser");
		this.sst = sst;
		parser.setContentHandler(this);
		return parser;
	}

	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		
		if (name.equals("c")) {
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true  
            String cellType = attributes.getValue("t");  
            if (cellType != null && cellType.equals("s")) {  
            	nextIsString = true;
            } else {  
            	nextIsString = false;
            }  
        } else if("v".equals(name)) {	//读到值开始标签时，将值设置为初始化
			// 置空
			content = "";
		}
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		 if (nextIsString) {  
            try {  
                int idx = Integer.parseInt(content);  
                content = new XSSFRichTextString(sst.getEntryAt(idx)).toString();  
            } catch (Exception e) {}
        }
		
		// v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
		// 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
		if ("v".equals(name)) {
			String value = content.trim();
			if(value != null && !"".equals(value)) {
				// 去除科学计数法和小数点
				try {
					value = new BigDecimal(value).stripTrailingZeros().toPlainString();
					if(value.indexOf(".") > -1) {
						value = value.substring(0, value.indexOf("."));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				rowlist.add(value);
			}
		} else if ("row".equals(name)) {
			// 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
			optRow(sheetIndex, curRow, rowlist);
			rowlist.clear();
			curRow++;
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// 得到单元格内容的值
		content += new String(ch, start, length);
	}

	public static void main(String[] args) throws Exception {
		
//		ExcelUtil util = new ExcelUtil("D:\\sqlload\\test.xlsx", -1);
//		util.readOneSheet();
		
//		StringBuilder buffer = new StringBuilder("12345\n");
//		System.out.println(buffer.delete(buffer.length() - LINE.length(), buffer.length()));
		//System.out.println(System.getProperty("line.separator").length());
		
//		Long startTime = System.currentTimeMillis();
//		
//		final String fileId = "104";
//		final String createBy = "zr";
//		
//		CopyOfExcelReader util = new CopyOfExcelReader("C:\\ttttttttttttt\\104", "test.xlsx", fileId+".txt", new RowHandler() {
//			@Override
//			public String row(int sheetIndex, int curRow, List<String> rowList) {
//				return rowList.get(0)+ROW_SEPARATOR+fileId+ROW_SEPARATOR+createBy+LINE;
//			}
//		});
//		util.readOneSheet();
//		util.flush();
//		
//		System.out.println("生成文件需要时间(秒)" + new Double((System.nanoTime() - startTime) * 1.E-009D).longValue());
		
		
		
//		ExcelUtil util = new ExcelUtil();
//		util.readOneSheet("D:\\sqlload\\test2007.xlsx");
		
//		String line = System.getProperty("line.separator");
//		System.out.print("==="+line);
//		System.out.print("===");
		
		// 去除科学计数法和小数点
//		String value = "18301021500";
//		value = new BigDecimal(value).stripTrailingZeros().toPlainString();
//		if(value.indexOf(".") > -1) {
//			value = value.substring(0, value.indexOf("."));
//		}
//		System.out.println(value);
//		String writeFile = "D:\\file\\file.txt";
//		System.out.println(writeFile.lastIndexOf(File.separator));
//		System.out.println(writeFile.substring(writeFile.lastIndexOf(File.separator)+1));
	}

}