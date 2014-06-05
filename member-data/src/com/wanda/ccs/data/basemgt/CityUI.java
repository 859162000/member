package com.wanda.ccs.data.basemgt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.aggrepoint.adk.ui.BasicSelectOption;
import com.aggrepoint.adk.ui.SelectOption;
import com.icebean.core.adb.AdbAdapter;
import com.icebean.core.adb.db.DbAdapter;
import com.wanda.ccs.model.IDimType;
import com.xcesys.extras.util.PinYinUtil;

public class CityUI {
	static SelectOption PROVINCE = null;
	static SelectOption AREA = null;
	static SelectOption CITYLEVEL = null;
	static Map<Integer, String> HT_PROVINCE = null;
	static Map<String, String> HT_AREA = null;
	static Hashtable<String, String> HT_CITYLEVEL = null;
	
	// 初始化省的列表
	static void initProvince(DbAdapter adapter) {
//		if (PROVINCE != null)
//			return;
		PROVINCE = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_PROVINCE = new LinkedHashMap<Integer, String>();
		PROVINCE.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
				"0","请选择所属省/直辖市", null, null, null));
		
		
		try {
			Vector<Province> v_dims = adapter.retrieveMulti(new Province(),"loadByCode","0");
			List<Province> l_dims = new ArrayList<Province>(v_dims);
			Collections.sort(l_dims, new Comparator<Province>(){
				@Override
				public int compare(Province o1, Province o2) {
					return PinYinUtil.toPinYin(o1.getName().substring(0, 1)).compareToIgnoreCase(
								PinYinUtil.toPinYin(o2.getName().substring(0, 1)));
				}
			});
			for (Province p : l_dims) {
				PROVINCE.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						"" + p.lSeqId, p.strName, null, null, null));
				HT_PROVINCE.put((int) p.lSeqId, p.strName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 初始化区域的列表
	static void initArea(DbAdapter adapter) {
//		if (AREA != null)
//			return;
		AREA = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_AREA = new LinkedHashMap<String, String>();
		AREA.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
				"000", "请选择区域", null, null, null));
		try {
			DimDef d = new DimDef();
			d.setTypeId(DimTypeDef.DIMTYPE_AREA);
			Vector<DimDef> v_dims = adapter.retrieveMulti(d, "loadByType", "0");
			List<DimDef> l_dims = new ArrayList<DimDef>(v_dims);
			Collections.sort(l_dims, new Comparator<DimDef>(){
				@Override
				public int compare(DimDef o1, DimDef o2) {
					return PinYinUtil.toPinYin(o1.getName().substring(0, 1)).compareToIgnoreCase(
								PinYinUtil.toPinYin(o2.getName().substring(0, 1)));
				}
			});
			for (DimDef def : l_dims) { //返回多个结果
				AREA.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						def.strCode, def.strName, null, null, null));
				HT_AREA.put(def.strCode, def.strName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 初始化城市级别的列表
	static  void initCityLevel(DbAdapter adapter) {
//		if (CITYLEVEL != null)
//			return;
		CITYLEVEL = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_CITYLEVEL = new Hashtable<String, String>();
		try {
			DimDef d = new DimDef();
			d.setTypeId(DimTypeDef.DIMTYPE_CITYLEVEL);
			for (DimDef def : adapter.retrieveMulti(d, "loadByType", null)) {
				CITYLEVEL.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						def.strCode, def.strName, null, null, null));
				HT_CITYLEVEL.put(def.strCode, def.strName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getDisplayProvinceId(City city, AdbAdapter adapter) {
		initProvince((DbAdapter) adapter);
		return HT_PROVINCE.get(city.provinceId);
	}

	public static SelectOption getProvinceIdList(City city, AdbAdapter adapter,
			boolean nameOnly) {
		initProvince((DbAdapter) adapter);
		return PROVINCE;
	}

	public static String getDisplayArea(City city, AdbAdapter adapter) {
		initArea((DbAdapter) adapter);
		return HT_AREA.get(city.area);
	}

	public static SelectOption getAreaList(City city, AdbAdapter adapter,
			boolean nameOnly) {
		initArea((DbAdapter) adapter);
		return AREA;
	}
	
	public static String getDisplayCityLevel(City city, AdbAdapter adapter) {
		initCityLevel((DbAdapter) adapter);
		return HT_CITYLEVEL.get(city.strCityLevel);
	}

	public static SelectOption getCityLevelList(City city, AdbAdapter adapter,
			boolean nameOnly) {
		initCityLevel((DbAdapter) adapter);
		return CITYLEVEL;
	}
	
	// 是否删除
	public static String getDisplayIsDelete(City city) {
		if (city.bIsDelete) {
			return "是";
		}
		return "否";

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

	public static SelectOption getIsDeleteList(City city, boolean nameOnly) {

		return ISDELETE;
	}
}