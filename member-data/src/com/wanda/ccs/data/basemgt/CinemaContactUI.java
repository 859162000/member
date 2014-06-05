package com.wanda.ccs.data.basemgt;

import java.util.Hashtable;

import com.aggrepoint.adk.ui.BasicSelectOption;
import com.aggrepoint.adk.ui.SelectOption;
import com.icebean.core.adb.AdbAdapter;
import com.icebean.core.adb.db.DbAdapter;
/**
 * 影院联系人
 * @author:陈薪民
 *@date 2011-10-13
 */
public class CinemaContactUI {
	//职责
	static SelectOption DUTY = null;
	static Hashtable<String, String> HT_DUTY = null;
	
	//影院
	static SelectOption CINEMA = null;
	static Hashtable<Long, String> HT_CINEMA = null;


	// 初始化职责的列表
	static void initDuty(DbAdapter adapter) {
		if (DUTY != null)
			return;
		DUTY = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_DUTY = new Hashtable<String, String>();
		try {
			DimDef dimDef = new DimDef();
			dimDef.setTypeId(DimTypeDef.DIMTYPE_DUTY);
			for (DimDef def : adapter.retrieveMulti(dimDef, "loadByType", null)) { //返回多个结果
				DUTY.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						def.strName, def.strName, null, null, null));
				HT_DUTY.put(def.strName, def.strName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getDisplayDuty(CinemaContact cinemaContact, AdbAdapter adapter) {
		initDuty((DbAdapter) adapter);
		return HT_DUTY.get(cinemaContact.getDuty());
	}

	public static SelectOption getDutyList(CinemaContact CinemaContact, AdbAdapter adapter,
			boolean nameOnly) {
		initDuty((DbAdapter) adapter);
		return DUTY;
	}
	
	// 初始化影院的列表
	static void initCinema(DbAdapter adapter) {
		if (CINEMA != null)
			return;
		CINEMA = new BasicSelectOption(SelectOption.TYPE_LABEL, "status", "",
				null, null, null);
		HT_CINEMA = new Hashtable<Long, String>();
		try {
			for (Cinema cinema : adapter.retrieveMulti(new Cinema())) {
				CINEMA.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION,
						Long.toString(cinema.getSeqId()), cinema.getName(), null, null, null));
				HT_CINEMA.put(cinema.getSeqId(), cinema.getName());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getDisplayCinemaId(Cinema cinema, AdbAdapter adapter) {
		initCinema((DbAdapter) adapter);
		return HT_CINEMA.get(cinema.getName());
	}

	public static SelectOption getCinemaIdList(CinemaContact cinema, AdbAdapter adapter,
			boolean nameOnly) {
		initCinema((DbAdapter) adapter);
		return CINEMA;
	}

	// 是否删除
	public static String getDisplayIsDelete(CinemaContact cinemaContact) {
		if (cinemaContact.getIsDelete()==1) {
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
	public static SelectOption getIsDeleteList(CinemaContact cinemaContact, boolean nameOnly) {

		return ISDELETE;
	}
	
	// 性别
	public static String getDisplaySex(CinemaContact cinemaContact) {
		if (cinemaContact.getSex()==1) {
			return "男";
		}
		return "女";

	}

	// 性别
	static SelectOption SEX = new BasicSelectOption(
			SelectOption.TYPE_LABEL, "status", "", null, null, null);
	static {
		SEX.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION, "" + 1,
				"男", null, null, null));
		SEX.addSub(new BasicSelectOption(SelectOption.TYPE_OPTION, "" + 0,
				"女", null, null, null));
	}

	public static SelectOption getSexList(CinemaContact cinemaContact, boolean nameOnly) {

		return SEX;
	}
}
