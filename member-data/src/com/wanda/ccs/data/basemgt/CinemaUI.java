package com.wanda.ccs.data.basemgt;

import java.util.Hashtable;

import com.aggrepoint.adk.ui.BasicSelectOption;
import com.aggrepoint.adk.ui.SelectOption;
import com.icebean.core.adb.AdbAdapter;
import com.icebean.core.adb.db.DbAdapter;

public class CinemaUI {
	//区域
	static SelectOption AREA = null;
	static Hashtable<String, String> HT_AREA = null;
	//影院属性
	static SelectOption CINEMAATTR = null;
	static Hashtable<String, String> HT_CINEMAATTR = null;
	
	//影院级别
	static SelectOption CINEMALEVEL = null;
	static Hashtable<String, String> HT_CINEMALEVEL = null;
	
	//影院类别
	static SelectOption CINEMATYPE = null;
	static Hashtable<String, String> HT_CINEMATYPE = null;
	//省
	static SelectOption PROVINCE = null;
	static Hashtable<String, String> HT_PROVINCE = null;
	
	//市
	static SelectOption CITY = null;
	static Hashtable<String, String> HT_CITY = null;


	// 初始化区域的列表
	static void initArea(DbAdapter adapter) {
		if (AREA != null)
			return;
		AREA = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_AREA = new Hashtable<String, String>();
		try {
			DimDef dimDef = new DimDef();
			dimDef.setTypeId(DimTypeDef.DIMTYPE_AREA);
			for (DimDef def : adapter.retrieveMulti(dimDef, "loadByType", null)) { //返回多个结果
				AREA.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						def.strName, def.strName, null, null, null));
				HT_AREA.put(def.strName, def.strName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getDisplayArea(Cinema cinema, AdbAdapter adapter) {
		initArea((DbAdapter) adapter);
		return HT_AREA.get(cinema.getArea());
	}

	public static SelectOption getAreaList(Cinema cinema, AdbAdapter adapter,
			boolean nameOnly) {
		initArea((DbAdapter) adapter);
		return AREA;
	}
	
	// 初始化影院属性列表
	static void initCinemaAttr(DbAdapter adapter) {
		if (CINEMAATTR != null)
			return;
		CINEMAATTR = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_CINEMAATTR = new Hashtable<String, String>();
		try {
			DimDef dimDef = new DimDef();
			dimDef.setTypeId(DimTypeDef.DIMTYPE_CINEMAATTR);
			for (DimDef def : adapter.retrieveMulti(dimDef, "loadByType", null)) { //返回多个结果
				CINEMAATTR.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						def.strName, def.strName, null, null, null));
				HT_CINEMAATTR.put(def.strName, def.strName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getDisplayCinemaAttr(Cinema cinema, AdbAdapter adapter) {
		initCinemaAttr((DbAdapter) adapter);
		return HT_CINEMAATTR.get(cinema.getCinema_Attr());
	}

	public static SelectOption getCinemaAttrList(Cinema cinema, AdbAdapter adapter,
			boolean nameOnly) {
		initCinemaAttr((DbAdapter) adapter);
		return CINEMAATTR;
	}
	
	// 初始化影院级别列表
	static void initCinemaLevel(DbAdapter adapter) {
		if (CINEMALEVEL != null)
			return;
		CINEMALEVEL = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_CINEMALEVEL = new Hashtable<String, String>();
		try {
			DimDef dimDef = new DimDef();
			dimDef.setTypeId(DimTypeDef.DIMTYPE_CINEMALEVEL);
			for (DimDef def : adapter.retrieveMulti(dimDef, "loadByType", null)) { //返回多个结果
				CINEMALEVEL.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						def.strName, def.strName, null, null, null));
				HT_CINEMALEVEL.put(def.strName, def.strName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getDisplayCinemaLevel(Cinema cinema, AdbAdapter adapter) {
		initCinemaLevel((DbAdapter) adapter);
		return HT_CINEMALEVEL.get(cinema.getCinema_Level());
	}

	public static SelectOption getCinemaLevelList(Cinema cinema, AdbAdapter adapter,
			boolean nameOnly) {
		initCinemaLevel((DbAdapter) adapter);
		return CINEMALEVEL;
	}
	
	
	// 初始化影院类别的列表
	static void initCinemaType(DbAdapter adapter) {
		if (CINEMATYPE != null)
			return;
		CINEMATYPE = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_CINEMATYPE = new Hashtable<String, String>();
		try {
			DimDef dimDef = new DimDef();
			dimDef.setTypeId(DimTypeDef.DIMTYPE_CINEMATYPE);
			for (DimDef def : adapter.retrieveMulti(dimDef, "loadByType", null)) { //返回多个结果
				CINEMATYPE.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						def.strName, def.strName, null, null, null));
				HT_CINEMATYPE.put(def.strName, def.strName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getDisplayCinemaType(Cinema cinema, AdbAdapter adapter) {
		initCinemaType((DbAdapter) adapter);
		return HT_CINEMATYPE.get(cinema.getCinema_Type());
	}

	public static SelectOption getCinemaTypeList(Cinema cinema, AdbAdapter adapter,
			boolean nameOnly) {
		initCinemaType((DbAdapter) adapter);
		return CINEMATYPE;
	}
	
	

	// 初始化省的列表
	static void initProvince(DbAdapter adapter) {
		if (PROVINCE != null)
			return;
		PROVINCE = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_PROVINCE = new Hashtable<String, String>();
		try {
			for (Province p : adapter.retrieveMulti(new Province())) {
				PROVINCE.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						p.strName, p.strName, null, null, null));
				HT_PROVINCE.put(p.strName, p.strName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getDisplayProvince(Cinema cinema, AdbAdapter adapter) {
		initProvince((DbAdapter) adapter);
		return HT_PROVINCE.get(cinema.getProvince());
	}

	public static SelectOption getProvinceList(Cinema cinema, AdbAdapter adapter,
			boolean nameOnly) {
		initProvince((DbAdapter) adapter);
		return PROVINCE;
	}
	
	// 初始化市的列表
	static void initCity(DbAdapter adapter) {
		if (CITY != null)
			return;
		CITY = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_CITY = new Hashtable<String, String>();
		try {
			for (City city : adapter.retrieveMulti(new City())) {
				CITY.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						city.strName, city.strName, null, null, null));
				HT_CITY.put(city.strName, city.strName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getDisplayCity(Cinema cinema, AdbAdapter adapter) {
		initCity((DbAdapter) adapter);
		return HT_CITY.get(cinema.getCity());
	}

	public static SelectOption getCityList(Cinema cinema, AdbAdapter adapter,
			boolean nameOnly) {
		initCity((DbAdapter) adapter);
		return CITY;
	}

	// 初始化标记，是否已删除
	static SelectOption ISDELETE = new BasicSelectOption(
			SelectOption.TYPE_LABEL, "status", "", null, null, null);
	static {
		ISDELETE.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION, "" + 1,
				"是", null, null, null));
		ISDELETE.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION, "" + 0,
				"否", null, null, null));
	}

	public static SelectOption getIsDeleteList(Cinema cinema, boolean nameOnly) {

		return ISDELETE;
	}
	
	// 是否开业
	public static String getDisplayIsOpen(Cinema cinema) {
		if (cinema.getIsOpen()==1) {
			return "是";
		}
		return "否";

	}

	// 初始化标记，是否已开业
	static SelectOption ISOPEN = new BasicSelectOption(
			SelectOption.TYPE_LABEL, "status", "", null, null, null);
	static {
		ISOPEN.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION, "" + 1,
				"是", null, null, null));
		ISOPEN.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION, "" + 0,
				"否", null, null, null));
	}

	public static SelectOption getIsOpenList(Cinema cinema, boolean nameOnly) {

		return ISOPEN;
	}
}
