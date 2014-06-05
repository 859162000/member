package com.wanda.ccs.data.basemgt;

import java.util.Hashtable;

import com.aggrepoint.adk.ui.BasicSelectOption;
import com.aggrepoint.adk.ui.SelectOption;
import com.icebean.core.adb.AdbAdapter;
import com.icebean.core.adb.db.DbAdapter;

public class DimDefUI {
	// 定义类型下拉列表的option
	static SelectOption DIMTYPE = null;
	// 定义存放下拉列表的数据的集合
	static Hashtable<Integer, String> HT_DIMTYPES = null;

	// 初始化维数据类型
	static void initDimType(DbAdapter adapter) {
		if (DIMTYPE != null)
			return;

		DIMTYPE = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_DIMTYPES = new Hashtable<Integer, String>();

		try {
			for (DimTypeDef d : DimTypeDef.getTypes(adapter)) {
				DIMTYPE.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						Integer.toString(d.iTypeId), d.strTypeName, null, null,
						null));
				HT_DIMTYPES.put(d.iTypeId, d.strTypeName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 类型
	public static String getDisplayTypeId(DimDef def, AdbAdapter adapter) {
		initDimType((DbAdapter) adapter);
		return HT_DIMTYPES.get(def.getTypeId());
	}

	public static SelectOption getTypeIdList(DimDef def, AdbAdapter adapter,
			boolean nameOnly) {
		initDimType((DbAdapter) adapter);
		return DIMTYPE;
	}

	// 是否删除
	public static String getDisplayIsDelete(DimDef def) {
		if (def.bIsDelete) {
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

	public static SelectOption getIsDeleteList(DimDef def, boolean nameOnly) {

		return ISDELETE;
	}
}
