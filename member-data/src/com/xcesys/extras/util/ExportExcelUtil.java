package com.xcesys.extras.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.basemgt.service.TDimTypeDefService;
import com.wanda.ccs.model.IDimType;
import com.wanda.ccs.model.TRoundFilm;
import com.wanda.ccs.model.TSchedulePlanB;
import com.wanda.ccs.model.TSchedulePlanH;
import com.wanda.ccs.model.TVoucherOrder;
import com.wanda.ccs.model.TVoucherOrderSettLog;
import com.wanda.ccs.service.SpringCommonService;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.webapp.model.ExcelCommonBean;

/**
 * 利用开源组件POI3.8动态导出EXCEL文档
 * 
 * @author Benjamin
 * 
 * @param <T>
 *            应用泛型,代表一个符合javabean风格的类
 */
public class ExportExcelUtil<T extends ExcelCommonBean> {
	
	private static final int A = 0;
	private static final int B = 1;
	private static final int C = 2;
	private static final int D = 3;
	private static final int E = 4;
	private static final int F = 5;
	private static final int G = 6;
	private static final int H = 7;
	private static final int I = 8;
	private static final int K = 10;
	private static final int L = 11;
	private static final int M = 12;
	private static final int N = 13;
	private static final int P = 15;
	private static final int Q = 16;
	private static final int R = 17;
	private static final int S = 18;
	private static final int U = 20;
	private static final int V = 21;
	private static final int W = 22;
	private static final int X = 23;
	private static final int Y = 24;
	private static final int Z = 25;
	private static final int AA = 26;
	private static final int AB = 27;
	private static final int AC = 28;
	private static final int AD = 29;
	private static final int AE = 30;
	private static final int AF = 31;
	
	//影厅号映射其影厅中排片数据开始行和开始列
	private static Map<String,Integer[]> rowColMap = new HashMap<String,Integer[]>();
	private static Map<Integer,String> weekMap = new HashMap<Integer,String>();
	
	static{
		rowColMap.put("1", new Integer[]{14,A});
		rowColMap.put("2", new Integer[]{14,F});
		rowColMap.put("3", new Integer[]{14,K});
		rowColMap.put("4", new Integer[]{14,P});
		rowColMap.put("5", new Integer[]{14,U});
		rowColMap.put("6", new Integer[]{33,A});
		rowColMap.put("7", new Integer[]{33,F});
		rowColMap.put("8", new Integer[]{33,K});
		rowColMap.put("9", new Integer[]{33,P});
		rowColMap.put("10", new Integer[]{33,U});
		rowColMap.put("11", new Integer[]{71,A});
		rowColMap.put("12", new Integer[]{72,F});
		rowColMap.put("13", new Integer[]{73,K});
		rowColMap.put("14", new Integer[]{74,P});
		rowColMap.put("15", new Integer[]{75,U});
		rowColMap.put("0", new Integer[]{7,AB});//右方所有场次按影厅号、开始时间排序显示
		
		weekMap.put(1, "周一");
		weekMap.put(2, "周二");
		weekMap.put(3, "周三");
		weekMap.put(4, "周四");
		weekMap.put(5, "周五");
		weekMap.put(6, "周六");
		weekMap.put(7, "周日");
	}

	/**
	 * 这是一个通用的方法,利用了JAVA的反射机制,可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合，集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型以String,Date,byte[](图片数据)
	 * @param resp
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattem
	 *            如果有时间数据，设定输出格式。默认为"yyyy-MM-dd";
	 * @param beInsertedRow
	 *            要插入的行
	 * @param headerBelow
	 *            标题行与内容行之间的数据
	 */
	public void exportExcel(String title, Collection<String> headers,
			Collection<T> dataset, IModuleResponse resp, String pattem,
			Map<Integer, List<String>> beInsertedRow,int colSplit, int rowSplit,
			List<String>... headerBelow) {
		try {
			// 声明一个工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(title);
			sheet.setColumnWidth(0, 5066);
			sheet.setColumnWidth(13, 12 * 256);
			// sheet.setDefaultColumnWidth(10);
			// 生成一个样式(用于单元格)
			HSSFCellStyle style = workbook.createCellStyle();
			// style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			// style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			// style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			// style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			// style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			// style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setWrapText(false);
			// 生成一个字体(用于单元格)
			HSSFFont font = workbook.createFont();
			// font.setColor(HSSFColor.VIOLET.index);
			// font.setFontHeightInPoints((short)12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// 把字体应用到当前的样式
			style.setFont(font);
			
			
			// 生成一个样式(用于单元格)
			HSSFCellStyle style2 = workbook.createCellStyle();
			style2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style2.setWrapText(false);
			style2.setFont(font);

			// 声明一个画图的顶级管理器
			HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
			// 定义注释的大小和位置,详见文档
			// HSSFComment comment = patriarch.createComment(new
			// HSSFClientAnchor(0,0,0,0,(short)4,2,(short)6,5));
			// 设置注释内容
			// comment.setString(new HSSFRichTextString("可以在POI中添加注释!"));
			// 设置注释作者,当鼠标移动单单元格上是可以在状态栏中看到该内容。
			// comment.setAuthor("");

			// 产生表格标题行
			//sheet.addMergedRegion(new CellRangeAddress(0,1, 0, 7));
//			HSSFCell titleCell = sheet.createRow(0).createCell(0);
//			titleCell.setCellStyle(style);
//			titleCell.setCellValue(new HSSFRichTextString(title));
			sheet.createRow(0);
			sheet.createRow(1);
			HSSFRow rowHeader = sheet.createRow(2);
			String[] headers_str = new String[headers.size()];
			String[] array = headers.toArray(headers_str);
			for (int i = 0; i < array.length; i++) {
				HSSFCell cell = rowHeader.createCell(i);
				cell.setCellStyle(style);
				HSSFRichTextString text = new HSSFRichTextString(array[i]);
				cell.setCellValue(text);
			}

			// 显示副标题行
			int subheadRowIndex = 3;
			if (headerBelow != null && headerBelow.length > 0) {
				for (List<String> subheadRow : headerBelow) {
					HSSFRow rowSubhead = sheet.createRow(subheadRowIndex);
					sheet.addMergedRegion(new CellRangeAddress(subheadRowIndex,
							subheadRowIndex, 0, 7));

					for (int i = 0; i < subheadRow.size(); i++) {
						HSSFCell subheadCell = rowSubhead.createCell(i);
						subheadCell.setCellStyle(style2);
						subheadCell.setCellValue(new HSSFRichTextString(
								subheadRow.get(i)));
					}

					subheadRowIndex++;
				}
			}

			// 判断是否有要在中间插入的行
			if (beInsertedRow != null) {
				HSSFCellStyle beInstRowCellStyle = workbook.createCellStyle();
				beInstRowCellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
				beInstRowCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				beInstRowCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				beInstRowCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				
				HSSFFont beInstRowCellFont = workbook.createFont();
				beInstRowCellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				beInstRowCellStyle.setFont(beInstRowCellFont);
				
				for (Integer rIndex : beInsertedRow.keySet()) {
					sheet.addMergedRegion(new CellRangeAddress(rIndex,
							rIndex, 0, headers.size()));
					HSSFRow r = sheet.createRow(rIndex);
					List<String> list = beInsertedRow.get(rIndex);
					for (int i = 0; i < list.size(); i++) {
						HSSFCell c = r.createCell(0);
						c.setCellStyle(beInstRowCellStyle);
						c.setCellValue(new HSSFRichTextString(list.get(i)));
					}
				}
			}

			// 产生表格内容行
			Iterator<T> it = dataset.iterator();
			int index = subheadRowIndex;
			while (it.hasNext()) {
				int idx = index++;
				if (beInsertedRow != null && beInsertedRow.containsKey(index)) {
					continue;
				}
				HSSFRow row = sheet.createRow(idx);
				T t = (T) it.next();
				Field[] fields = t.getClass().getDeclaredFields();
				int columnSeq = 0;
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					String fieldName = field.getName();
					String getMethodName = "get"
							+ fieldName.substring(0, 1).toUpperCase()
							+ fieldName.substring(1);
					Class<? extends ExcelCommonBean> tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName,
							new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					if (value == null)
						continue;
					if (value instanceof Collection) {
						@SuppressWarnings("unchecked")
						Collection<String> collection = (Collection<String>) value;
						for (String cValue : collection) {
							int cs = columnSeq++;
							HSSFCell cell = row.createCell(cs);
							cell.setCellStyle(style);
							cell.setCellValue(cValue);
							sheet.setColumnWidth(cs, 25 * 256);
						}
					} else {
						HSSFCell cell = row.createCell(columnSeq++);
						cell.setCellStyle(style);
						String textValue = null;
						if (value instanceof Boolean) {
							boolean booleanValue = (Boolean) value;
							textValue = "是";
							if (!booleanValue) {
								textValue = "否";
							}
							cell.setCellValue(textValue);
						} else if (value instanceof Date) {
							Date date = (Date) value;
							SimpleDateFormat sdf = new SimpleDateFormat(pattem);
							textValue = sdf.format(date);
							cell.setCellValue(textValue);
						} else if (value instanceof byte[]) {
							// 有图片时,设置行高为60px;
							row.setHeightInPoints(60);
							// 设置有图片所在列宽度为80px,注意这里单位的一个换算
							sheet.setColumnWidth(i, (short) (35.7 * 80));
							byte[] bsValue = (byte[]) value;
							HSSFClientAnchor anchor = new HSSFClientAnchor(0,
									0, 1023, 55, (short) 6, index, (short) 6,
									index);
							anchor.setAnchorType(2);
							patriarch.createPicture(anchor, workbook
									.addPicture(bsValue,
											HSSFWorkbook.PICTURE_TYPE_JPEG));
						} else {
							// 其它数据类型都当做字符串简单处理
							if ("null".equalsIgnoreCase("" + value)) {
								value = "";
							}
							textValue = "" + value;
						}
						// 如果不是图片数据,就利用正则表达式判断textValue是否全部由数字组成
						if (textValue != null) {
//							Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
//							Matcher matcher = p.matcher(textValue);
//							if (matcher.matches()) {
								// 是数字当作double处理
//								cell.setCellValue(Double.parseDouble(textValue));
//							} else {
								HSSFRichTextString richString = new HSSFRichTextString(
										textValue);
								// HSSFFont font3 = workbook.createFont();
								// font3.setColor(HSSFColor.BLUE.index);
								// richString.applyFont(font3);
								cell.setCellValue(richString);
//							}
						}
					}
				}
			}
			//冻结区域
			sheet.createFreezePane(colSplit,rowSplit);

			HttpServletResponse httprep = (HttpServletResponse) resp
					.getResponseObject();
			httprep.setContentType("application/vnd.ms-excel;charset=utf-8");
			httprep.addHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(title + ".xls", "UTF-8"));
			workbook.write(httprep.getOutputStream());
			httprep.getOutputStream().flush();
			httprep.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 导出区域排片Excel数据
	 * 
	 * @param plan 指定区域与日期的排片数据
	 * @param tmpl 数据模板，它的路径在src/tmpl/plan.xlsm
	 */
	public static void exptPlanExcl(TSchedulePlanH plan,InputStream tmpl, OutputStream os) throws Exception{
		String ymd = DateUtil.formatDate(plan.getYmd());
	    Workbook wb = new HSSFWorkbook(tmpl);
	    Sheet sheet = wb.getSheetAt(0);
	    wb.setSheetName(0, ymd);
	    
	    /* Date */
	    Row row = sheet.getRow(1);
	    Cell date1 = row.getCell(C);
	    date1.setCellValue(ymd);
	    Cell date2 = row.getCell(H);
	    date2.setCellValue(ymd);
	    Cell week = row.getCell(L);
	    Calendar t = Calendar.getInstance();
		t.setTime(DateUtil.getCurrentDate());
	    week.setCellValue(weekMap.get(t.get(Calendar.DAY_OF_WEEK)-1));
	    
	    /* cinema */
	    Row rCinema = sheet.getRow(3);
	    rCinema.getCell(C).setCellValue(plan.gettCinema().getShortName());
	    rCinema.getCell(H).setCellValue(plan.getTotal());
	    
	    //排序
	    Collections.sort(plan.getPlanList());
	    
	    Map<String,List<TSchedulePlanB>> hallMap = new LinkedHashMap<String,List<TSchedulePlanB>>();
	    String hallName = null;
	    for (TSchedulePlanB b : plan.getPlanList()) {
			// 影厅改变
	    	if (hallName == null || !hallName.equals(b.getHallName())) {
	    		hallName = b.getHallName();
	    		hallMap.put(hallName, new ArrayList<TSchedulePlanB>());
	    	}
	    	hallMap.get(hallName).add(b);
		}
	    
	    for (String hallCode : hallMap.keySet()) {
	    	Integer[] rowCol = rowColMap.get(hallCode);
	    	fillInHallData(hallMap.get(hallCode),sheet,rowCol[0],rowCol[1]);
		}
	    
	    //右方垂直显示所有场次
	    Integer[] rowCol = rowColMap.get("0");
	    fillInBasketData(plan.getPlanList(),sheet,rowCol[0],rowCol[1]);
	    wb.write(os);
	}
	private static void fillInBasketData(List<TSchedulePlanB> list,Sheet sheet,int startRow,int startCol){
		Map<String, String> languagesMap = SpringCommonService.getDimDefByTypeId(IDimType.LDIMTYPE_LANGUAGE);
		String timePtn = "HH:mm";
		Date lastEndtime = null;
		int rn = 0;
		for (TSchedulePlanB b : list) {
			
			Row rRound = sheet.getRow(startRow+(rn++));
			Cell cStatime = rRound.getCell(startCol);
			Cell cEndtime = rRound.getCell(startCol+1);
			Cell cFilName = rRound.getCell(startCol+2);
			Cell cHallCod = rRound.getCell(startCol+3);
			String name4Schedule = "";
			StringBuilder sb = new StringBuilder();
			if(b.gettFilm()!=null){
				// 非连场
				name4Schedule = b.gettFilm().getName4Schedule();
				sb.append(name4Schedule.substring(0,name4Schedule.length()-1)).append("/").append(languagesMap.get(b.getLanguage()).charAt(0)).append(")");
			}else{
				// 连场
				for (TRoundFilm rf : b.getRoundFilms()) {
					name4Schedule = rf.getFilm().getName4Schedule();
					sb.append(name4Schedule.substring(0,name4Schedule.length()-1)).append("/").append(languagesMap.get(rf.getLanguage()).charAt(0)).append(")");
					sb.append(" » ");
				}
				if(sb.length() > 0)sb.replace(sb.length()-3, sb.length(), "");
			}
			cFilName.setCellValue(sb.toString());
			cStatime.setCellValue(DateUtil.formatDate(b.getStartTime(), timePtn));
			cEndtime.setCellValue(DateUtil.formatDate(b.getEndTime(), timePtn));
			cHallCod.setCellValue(b.getHallName());
			if(rn > 1){
				Cell cSpctime = sheet.getRow(startRow+rn-2).getCell(startCol+4);
				int minute = (int) ((b.getStartTime().getTime() - lastEndtime.getTime()) / (1000 * 60));
				Date begin = DateUtil.getDayBegin(DateUtil.getCurrentDate());
				cSpctime.setCellValue(DateUtil.formatDate(DateUtil.addMinutes(begin, minute),timePtn));
			}
			lastEndtime = b.getEndTime();
		}
	}
	private static void fillInHallData(List<TSchedulePlanB> list,Sheet sheet,int startRow,int startCol){
		Map<String, String> hallPrj = SpringContextUtil.getBean(TDimTypeDefService.class).getDimDefsByTypeId(IDimType.LDIMTYPE_PROJECT_TYPE);
		Map<String, String> languagesMap = SpringCommonService.getDimDefByTypeId(IDimType.LDIMTYPE_LANGUAGE);
		String timePtn = "HH:mm";
		Date lastEndtime = null;
		int rn = 0;
		for (TSchedulePlanB b : list) {
			
			// 影厅座位数
			Row rSeat = sheet.getRow(startRow-7);
			rSeat.getCell(startCol+2).setCellValue(b.gettHall().getSeatCount()+b.gettHall().getDisabledSeatCount());
			
			// 影厅类型
			Row rTpy = sheet.getRow(startRow-6);
			String[] types = b.gettHall().getProjectTypes();
			String pt = "";
			for (String typ : types) {
				pt += hallPrj.get(typ) + "+";
			}
			if(pt.length() > 0)
				rTpy.getCell(startCol+1).setCellValue(pt.substring(0, pt.length()-1));
			
			Row rRound = sheet.getRow(startRow+(rn++));
			Cell cFilName = rRound.getCell(startCol);
			Cell cStatime = rRound.getCell(startCol+1);
			Cell cEndtime = rRound.getCell(startCol+2);
			
			String name4Schedule = "";
			StringBuilder sb = new StringBuilder();
			if(b.gettFilm()!=null){
				// 非连场
				name4Schedule = b.gettFilm().getName4Schedule();
				sb.append(name4Schedule.substring(0,name4Schedule.length()-1)).append("/").append(languagesMap.get(b.getLanguage()).charAt(0)).append(")");
			}else{
				// 连场
				for (TRoundFilm rf : b.getRoundFilms()) {
					name4Schedule = rf.getFilm().getName4Schedule();
					sb.append(name4Schedule.substring(0,name4Schedule.length()-1)).append("/").append(languagesMap.get(rf.getLanguage()).charAt(0)).append(")");
					sb.append(" » ");
				}
				if(sb.length() > 0)sb.replace(sb.length()-3, sb.length(), "");
			}
			cFilName.setCellValue(sb.toString());
			cStatime.setCellValue(DateUtil.formatDate(b.getStartTime(), timePtn));
			cEndtime.setCellValue(DateUtil.formatDate(b.getEndTime(), timePtn));
			if(rn > 1){
				Cell cSpctime = sheet.getRow(startRow+rn-2).getCell(startCol+3);
				int minute = (int) ((b.getStartTime().getTime() - lastEndtime.getTime()) / (1000 * 60));
				Date begin = DateUtil.getDayBegin(DateUtil.getCurrentDate());
				cSpctime.setCellValue(DateUtil.formatDate(DateUtil.addMinutes(begin, minute),timePtn));
			}
			lastEndtime = b.getEndTime();
		}
	}
	
	/**
	 * 根据模板导出结算单信息
	 * @param order
	 * 销售单
	 * @param orderSettLogs
	 * 结算单
	 * @param tmpl
	 * 模板
	 * @param os
	 * 输出流
	 * @throws IOException
	 */
	public static void initOrderSettLogExcel(TVoucherOrder order,List<TVoucherOrderSettLog> orderSettLogs,InputStream tmpl, OutputStream os,String password) throws IOException{
//		InputStream tmpl = TestExcleTmpl.class.getResourceAsStream("/tmpl/order.xls");
		HSSFWorkbook wb = new HSSFWorkbook(tmpl);
//		wb.unwriteProtectWorkbook();
	    Sheet sheet = wb.getSheetAt(0);
	    sheet.protectSheet(password);//设置工作区保护
	    wb.setSheetName(0, order.gettVoucherType().getTypeName()+"结算信息");
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    HSSFCellStyle cellStyle = wb.createCellStyle();
	    cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
	    cellStyle.setLocked(true);
	    HSSFCellStyle cellStyleindate = wb.createCellStyle();
	    cellStyleindate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
	    cellStyleindate.setLocked(true);
	    
	    HSSFCellStyle cellStyleOher = wb.createCellStyle();
	    cellStyleOher.setLocked(true);//加锁，只读
	    HSSFCellStyle cellStyleOherdatain = wb.createCellStyle();
	    cellStyleOherdatain.setLocked(true);//加锁，只读
	    cellStyleOherdatain.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    cellStyleOherdatain.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    cellStyleOherdatain.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    cellStyleOherdatain.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    
//	    /* Data */
	    Row row2 = sheet.getRow(B);
	    
	    Cell orderNum = row2.createCell(C);
	    orderNum.setCellValue(order.getOrderNum());
	    orderNum.setCellStyle(cellStyleOher);
	    Cell saleDate = row2.createCell(E);
	    saleDate.setCellValue(order.getSaleDate());
	    saleDate.setCellStyle(cellStyle);
	    Cell expiryDate = row2.createCell(G);
	    expiryDate.setCellValue(order.getExpiryDate());
	    expiryDate.setCellStyle(cellStyle);
	    
	    Row row3 = sheet.getRow(C);
	    Cell salesName = row3.createCell(C);
	    salesName.setCellValue(order.getSalesName());
	    salesName.setCellStyle(cellStyleOher);
	    Cell settlementType = row3.createCell(E);
	    // 结算方式 P预付款、A后结算
	    if("P".equals(order.getSettlementType())){
	    	settlementType.setCellValue("预付款");
	    	settlementType.setCellStyle(cellStyleOher);
	    }else{
	    	settlementType.setCellValue("后结算");
	    	settlementType.setCellStyle(cellStyleOher);
	    }
	    
	    
	    Row row4 = sheet.getRow(D);
	    Cell unitValue = row4.createCell(C);
	    unitValue.setCellValue(order.getUnitValue());
	    unitValue.setCellStyle(cellStyleOher);
	    Cell numTotal = row4.createCell(E);
	    numTotal.setCellValue(order.getNumTotal());
	    numTotal.setCellStyle(cellStyleOher);
	    Cell totalValue = row4.createCell(G);
	    totalValue.setCellValue(order.getTotalValue());
	    totalValue.setCellStyle(cellStyleOher);
	    Float countOfAmount = 0f;
	    for(int i=0;i<orderSettLogs.size();i++){
	    	Row row = sheet.getRow(i+5);
	    	Cell rowNum = row.getCell(B);
	    	rowNum.setCellValue(i+1);
	    	rowNum.setCellStyle(cellStyleOherdatain);
	    	Cell settlementDate = row.getCell(C);
	    	settlementDate.setCellValue(orderSettLogs.get(i).getSettlementDate());
	    	cellStyleindate.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    	cellStyleindate.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    	cellStyleindate.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    	cellStyleindate.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    	settlementDate.setCellStyle(cellStyleindate);
	    	
	    	Cell amount = row.getCell(D);
	    	amount.setCellValue(orderSettLogs.get(i).getAmount());
	    	amount.setCellStyle(cellStyleOherdatain);
	    	Cell payType = row.getCell(E);
	    	 //支付方式：C 信用卡 ，Q支票，S现金
	    	if("S".equals(orderSettLogs.get(i).getPayType())){
	    		payType.setCellValue("现金");
	    		payType.setCellStyle(cellStyleOherdatain);
	    	}else if("Q".equals(orderSettLogs.get(i).getPayType())){
	    		payType.setCellValue("支票");
	    		payType.setCellStyle(cellStyleOherdatain);
	    	}else{
	    		payType.setCellValue("信用卡");
	    		payType.setCellStyle(cellStyleOherdatain);
	    	}
	    	
	    	
	    	Cell orderSetNum = row.getCell(F);
	    	orderSetNum.setCellValue(orderSettLogs.get(i).getOrderSetNum());
	    	orderSetNum.setCellStyle(cellStyleOherdatain);
	    	
	    	Cell receiptNum = row.getCell(G);
	    	if(orderSettLogs.get(i).getReceiptNum() == null){
	    		receiptNum.setCellValue("");
	    	}else{
	    		receiptNum.setCellValue(orderSettLogs.get(i).getReceiptNum().toString());
	    	}
	    	receiptNum.setCellStyle(cellStyleOherdatain);
	    	countOfAmount = countOfAmount + orderSettLogs.get(i).getAmount();
	    }
	    Row rowcount = sheet.getRow(23);
	    Cell count = rowcount.getCell(D);
	    count.setCellValue(countOfAmount);
	    count.setCellStyle(cellStyleOherdatain);
//	    File excleFile = new File("c://tmpl//order.xls");
//	    FileOutputStream out=new FileOutputStream(excleFile,true); 
//	    wb.write(out);
	    wb.write(os);
	}
}
