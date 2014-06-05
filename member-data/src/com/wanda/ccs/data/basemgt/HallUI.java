package com.wanda.ccs.data.basemgt;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import com.aggrepoint.adk.ui.BasicSelectOption;
import com.aggrepoint.adk.ui.SelectOption;
import com.icebean.core.adb.AdbAdapter;
import com.icebean.core.adb.db.DbAdapter;
import com.wanda.ccs.service.CommonService;

/**
 * 影厅UI
 * @author Benjamin
 * @date 2011-10-13
 */
public class HallUI {

	static SelectOption opts = null;
	static Hashtable<Long, String> ht = null;

	static void init(DbAdapter adapter) {
		if (opts != null)
			return;

		opts = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		ht = new Hashtable<Long, String>();

		try {
			Vector<Cinema> vecTypes = adapter.retrieveMulti(new Cinema());
//			Vector<Cinema> vecTypes = Hall.cinemas;
			for (Cinema c : vecTypes) {
				opts.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						Long.toString(c.getSeqId()), c.getName(), null, null,
						null));
				ht.put(c.getSeqId(), c.getName());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 影院名称
	public static String getDisplayCinemaId(Hall hall, AdbAdapter adapter) {
		init((DbAdapter) adapter);
		return ht.get(hall.getCinemaId());
	}

	public static SelectOption getCinemaIdList(Hall hall, AdbAdapter adapter,
			boolean nameOnly) {
		init((DbAdapter) adapter);
		return opts;
	}

	// 设备归属
	public static String getDisplayBelongsTo(Hall hall, AdbAdapter adapter) {
		Map<String, String> map = CommonService.getDimDefByTypeId(
				(DbAdapter) adapter, 116);
		if (map == null || map.size() <= 0)
			return null;
		return map.get(hall.getBelongsTo());
	}

	public static SelectOption getBelongsToList(Hall hall, AdbAdapter adapter,
			boolean nameOnly) throws Exception {
		DimTypeDef d = new DimTypeDef();
		// d.setTypeName(BELONGTO);
		d.setTypeId(116);
		SelectOption belongsTo = new BasicSelectOption(SelectOption.TYPE_LABEL,
				"status", "", null, null, null);
		DimTypeDef d2 = adapter.retrieve(d);
		Map<String, String> map = CommonService.getDimDefByTypeId(
				(DbAdapter) adapter, d2.getTypeId());
		for (String code : map.keySet()) {
			belongsTo.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
					code, map.get(code), null, null, null));
		}

		return belongsTo;
	}

	// 影厅类型
	public static String getDisplayHallType(Hall hall, AdbAdapter adapter) {
		Map<String, String> map = CommonService.getDimDefByTypeId(
				(DbAdapter) adapter, 113);
		if (map == null || map.size() <= 0)
			return null;
		return map.get(String.valueOf(hall.getHallType()));
	}

	public static SelectOption getHallTypeList(Hall hall, AdbAdapter adapter,
			boolean nameOnly) {
		return CommonService.getSelectOptionByTypeId(adapter, 113);
	}

	// 音响类型
	public static String getDisplayAudioType(Hall hall, AdbAdapter adapter) {
		Map<String, String> map = CommonService.getDimDefByTypeId(
				(DbAdapter) adapter, 114);
		if (map == null || map.size() <= 0)
			return null;
		return map.get(String.valueOf(hall.getAudioType()));
	}

	public static SelectOption getAudioTypeList(Hall hall, AdbAdapter adapter,
			boolean nameOnly) {
		return CommonService.getSelectOptionByTypeId(adapter, 114);
	}

	// 放映制式
	public static String getDisplayProjectType(Hall hall, AdbAdapter adapter) {
		Map<String, String> map = CommonService.getDimDefByTypeId(
				(DbAdapter) adapter, 115);
		if (map == null || map.size() <= 0)
			return null;
		return map.get(String.valueOf(hall.getProjectType()));
	}

	public static SelectOption getProjectTypeList(Hall hall,
			AdbAdapter adapter, boolean nameOnly) {
		return CommonService.getSelectOptionByTypeId(adapter, 115);
	}

	// 胶片
	public static String getDisplayIsDigital(Hall hall, AdbAdapter adapter) {
		return ysMap.get(hall.getIsDigital());
	}

	public static SelectOption getIsDigitalList(Hall hall, AdbAdapter adapter,
			boolean nameOnly) {
		return YES_NO;
	}

	// RealD
	public static String getDisplayIsRealD(Hall hall, AdbAdapter adapter) {
		return ysMap.get(hall.getIsRealD());
	}

	public static SelectOption getIsRealDList(Hall hall, AdbAdapter adapter,
			boolean nameOnly) {
		return YES_NO;
	}

	// 3D
	public static String getDisplayIs3D(Hall hall, AdbAdapter adapter) {
		return ysMap.get(hall.getIs3D());
	}

	public static SelectOption getIs3DList(Hall hall, AdbAdapter adapter,
			boolean nameOnly) {
		return YES_NO;
	}

	// IMAX
	public static String getDisplayIsIMAX(Hall hall, AdbAdapter adapter) {
		return ysMap.get(hall.getIsIMAX());
	}

	public static SelectOption getIsIMAXList(Hall hall, AdbAdapter adapter,
			boolean nameOnly) {
		return YES_NO;
	}

	static Map<Integer,String> ysMap = new HashMap<Integer,String>();
	static SelectOption YES_NO = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "", null, null, null);
	static {
		YES_NO.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION, "" + 1,
				"是", null, null, null));
		YES_NO.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION, "" + 0,
				"否", null, null, null));
		ysMap.put(1, "是");
		ysMap.put(0, "否");
		
	}

}
