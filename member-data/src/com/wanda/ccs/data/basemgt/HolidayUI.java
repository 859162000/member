package com.wanda.ccs.data.basemgt;

import java.util.Hashtable;

import com.aggrepoint.adk.ui.BasicSelectOption;
import com.aggrepoint.adk.ui.SelectOption;
import com.icebean.core.adb.AdbAdapter;
import com.icebean.core.adb.db.DbAdapter;

public class HolidayUI {
	/** 定义    年度    下拉列表的option*/
	static SelectOption HOLIDAYYEAR = null;
	
	/** 定义   类型    下拉列表的option */
	static SelectOption HOLIDAYTYPE = null;
	
	/** 定义存放下拉列表的数据的集合*/
	
	/** 类型*/
	static Hashtable<String, String> HT_HOLIDAYTYPES = null;

	/** 初始化维数据类型*/
	/** 年度*/
	static void initHolidayYear(DbAdapter adapter) {
		
		HOLIDAYYEAR = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HOLIDAYYEAR.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
				null, "请选择" , null, null,
				null));
		try {
			for (String year : Holiday.getYears()) {
				HOLIDAYYEAR.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						year, year , null, null,
						null));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 类型*/
	static void initHolidayType(DbAdapter adapter) {
		HOLIDAYTYPE = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_HOLIDAYTYPES = new Hashtable<String, String>();
		HOLIDAYTYPE.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
				null, "请选择", null, null,
				null));
		try {
			DimDef def = new DimDef();
			def.setTypeId(DimTypeDef.DIMTYPE_HOLIDAY);
			def.setIsDelete(0);
			for (DimDef d : adapter.retrieveMulti(def,"loadNodeleteByTypeId",null)) {
				HOLIDAYTYPE.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						d.strCode, d.strName, null, null,
						null));
				HT_HOLIDAYTYPES.put(d.strCode, d.strName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/** 年度列表*/
	public static SelectOption getYearList(Holiday def, AdbAdapter adapter,
			boolean nameOnly) {
		initHolidayYear((DbAdapter) adapter);
		return HOLIDAYYEAR;
	}
	
	/** 查询 类型 列表*/
	public static SelectOption getHolidayTypeList(Holiday def, AdbAdapter adapter,
			boolean nameOnly) {
		initHolidayType((DbAdapter) adapter);
		return HOLIDAYTYPE;
	}
	public static String getDisplayHolidayType(Holiday def, AdbAdapter adapter) {
		initHolidayType((DbAdapter) adapter);
		return HT_HOLIDAYTYPES.get(def.strHolidayType);
	}
}
