package com.wanda.ccs.data.basemgt;

import java.util.Vector;

import com.icebean.core.adb.ADB;
import com.icebean.core.adb.db.DbAdapter;

/**
 * 维数据类型
 * 
 * @author Jim
 */
public class DimTypeDef extends ADB {
	/** 类型编号 */
	int iTypeId;
	/** 类型名称 */
	String strTypeName;
	/** 是否被删除 */
	boolean bIsDelete;

	public final static int DIMTYPE_COUNTRY = 101;
	public final static int DIMTYPE_LANGUAGE = 102;
	public final static int DIMTYPE_HOLIDAY = 103;
	public final static int DIMTYPE_AREA = 104;
	public final static int DIMTYPE_CITYLEVEL = 105;
	public final static int DIMTYPE_SCENETYPE = 106;
	public final static int DIMTYPE_CINEMATYPE = 107;
	public final static int DIMTYPE_CINEMALEVEL = 108;
	public final static int DIMTYPE_CINEMAATTR = 109;
	public static final int DIMTYPE_SCHEDULE_INFO_STATUS = 117;
	public static final int DIMTYPE_SCHEDULE_INFO_VERSION = 118;
	public static final int DIMTYPE_DUTY = 120;
	public static final int DIMTYPE_SEX = 121;
	/* 分账方式 */
	public static final int DIMTYPE_ACCOUNTTYPE = 122;
	/* 影片分账方式 */
	public static final int DIMTYPE_FILMACCOUNTTYPE = 123;
	/* 影片合同类型 */
	public static final int DIMTYPE_FILMCONTRACTTYPE= 124;
	/*影片类别*/
	public static final int DIMTYPE_FILM_CATETYPE = 130;
	/*影片类型*/
	public static final int DIMTYPE_FILMTYPE = 131;
	/*影片级别*/
	public static final int DIMTYPE_FILMLEVEL = 132;
	/*影片制式*/
	public static final int DIMTYPE_FILMSET = 133;
	/*影片放映制式*/
	public static final int DIMTYPE_SHOWSET = 134;
	/*音效类别*/
	public static final int DIMTYPE_AUDIOTYPE = 135;
	
	public DimTypeDef() {
		bIsDelete = false;
	}

	public int getTypeId() {
		return iTypeId;
	}

	public void setTypeId(int i) {
		iTypeId = i;
	}

	public String getTypeName() {
		return strTypeName;
	}

	public void setTypeName(String str) {
		strTypeName = str;
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

	static Vector<DimTypeDef> vecTypes;

	public static Vector<DimTypeDef> getTypes(DbAdapter adapter)
			throws Exception {
		if (vecTypes != null)
			return vecTypes;

		vecTypes = adapter.retrieveMulti(new DimTypeDef());
		return vecTypes;
	}
}
