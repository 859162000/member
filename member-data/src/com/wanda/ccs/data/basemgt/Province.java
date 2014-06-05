package com.wanda.ccs.data.basemgt;

import com.icebean.core.adb.ADB;

/**
 * 省/直辖市类型
 * 
 * @author lujx
 */
public class Province extends ADB {
	/** 自动编码 */
	long lSeqId;
	/** 省编号 */
	String strCode;
	/** 省名称 */
	String strName;
	/** 区域 */
	String strArea;
	/** 是否被删除 */
	boolean bIsDelete;

	public Province() {
		bIsDelete = false;
	}

	public long getSeqId() {
		return lSeqId;
	}

	public void setSeqId(long l) {
		lSeqId = l;
	}

	public String getCode() {
		return strCode;
	}

	public void setCode(String str) {
		strCode = str;
	}

	public String getName() {
		return strName;
	}

	public void setName(String str) {
		strName = str;
	}

	public String getArea() {
		return strArea;
	}

	public void setArea(String str) {
		strArea = str;
	}
	
	public int getIsDelete() {
		return bIsDelete ? 1 : 0;
	}

	public void setIsDelete(int i) {
		bIsDelete = i != 0;
	}

	public boolean isDelete() {
		return bIsDelete;
	}
	
}
