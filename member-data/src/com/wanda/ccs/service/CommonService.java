package com.wanda.ccs.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.aggrepoint.adk.ui.BasicSelectOption;
import com.aggrepoint.adk.ui.SelectOption;
import com.icebean.core.adb.AdbAdapter;
import com.icebean.core.adb.db.DbAdapter;
import com.wanda.ccs.data.basemgt.DimDef;

/**
 * 
 * 公共数据读取服务.
 * 
 * @author Danne
 * 
 */
public class CommonService {

	/**
	 * 根据维数据类型获取一个维数据Map.
	 * 
	 * @param adapter
	 *            数据库适配器
	 * @param typeId
	 *            维数据类型ID
	 * @return
	 */
	public static final Map<String, String> getDimDefByTypeId(
			DbAdapter adapter, int typeId) {
		try {
			DimDef dimDef = new DimDef();
			dimDef.setTypeId(typeId);
			dimDef.setIsDelete(0);
			dimDef.setSearchTypeId(typeId);
			Vector<DimDef> list = adapter.retrieveMulti(dimDef, "loadByType",
					null);
			if (list != null) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				for (DimDef d : list) {
					map.put(d.getCode(), d.getName());
				}
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String, String>();
	}
	
	/**
	 * 根据维数据类型获取一个维数据SelectOption.
	 * @param adapter
	 * @param typeId
	 * @return
	 * @author Benjamin
	 */
	public static SelectOption getSelectOptionByTypeId(AdbAdapter adapter,int typeId){
		
		SelectOption s = new BasicSelectOption(SelectOption.TYPE_LABEL,
				"status", "", null, null, null);
		Map<String, String> map = getDimDefByTypeId((DbAdapter) adapter, typeId);
		for (String code : map.keySet()) {
			s.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
					code, map.get(code), null, null, null));
		}
		return s;
	}
	
}
