package com.wanda.ccs.jobhub.member.utils;

import java.util.List;

/**
 * excel行解析器
 * 
 * @author admin
 * @date 2013年12月11日 上午9:35:56
 */
public interface RowHandler {
	/**
	 * 行处理
	 * 
	 * @param sheetIndex
	 * @param curRow
	 * @param rowList
	 * @return
	 */
	public void row(int sheetIndex, int curRow, List<String> rowList);
	
}
