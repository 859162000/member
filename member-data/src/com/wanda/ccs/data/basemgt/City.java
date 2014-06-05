package com.wanda.ccs.data.basemgt;

import java.util.Vector;

import com.aggrepoint.adk.ui.ValidateResult;
import com.icebean.core.adb.ADB;
import com.icebean.core.adb.AdbAdapter;
import com.icebean.core.adb.db.DbAdapter;
import com.icebean.core.common.StringUtils;

/**
 * 城市
 * 
 * @author lujx
 */

public class City extends ADB {
	long lSeqId;
	int provinceId;
	String area;
	String strCode;
	String strName;
	String strCityLevel;
	boolean bIsDelete;
	
	public City() {
	}

	public long getSeqId() {
		return lSeqId;
	}

	public void setSeqId(long l) {
		lSeqId = l;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int i) {
		provinceId = i;
	}

	public int getSearchProvince() {
		return provinceId;
	}

	public void setSearchProvince(int i) {
		provinceId = i;

		if (String.valueOf(i) == null || String.valueOf(i).equals("") || String.valueOf(i).equals("0"))
			clearFlag("province");
		else
			setFlag("province");
	}

	public String getArea() {
		return area;
	}

	public void setArea(String str) {
		area = str;
	}

	public String getSearchArea() {
		return area;
	}

	public void setSearchArea(String str) {
		area = str;

		if (str == null || str.equals("") || str.equals("000"))
			clearFlag("areaCode");
		else
			setFlag("areaCode");
	}

	public String getCode() {
		return strCode;
	}

	public void setCode(String str) {
		strCode = str;
	}

	public String getSearchCode() {
		return StringUtils.toDbMatch(strCode);
	}

	public void setSearchCode(String str) {
		strCode = str;

		if (str == null || str.equals(""))
			clearFlag("code");
		else
			setFlag("code");
	}

	public String getName() {
		return strName;
	}

	public void setName(String str) {
		strName = str;
	}

	public String getSearchName() {
		return StringUtils.toDbMatch(strName);
	}

	public void setSearchName(String str) {
		strName = str;

		if (str == null || str.equals(""))
			clearFlag("name");
		else
			setFlag("name");
	}

	public String getCityLevel() {
		return strCityLevel;
	}

	public void setCityLevel(String str) {
		strCityLevel = str;
	}

	public String getSearchCityLevel() {
		return strCityLevel;
	}

	public void setSearchCityLevel(String str) {
		strCityLevel = str;

		if (str == null || str.equals(""))
			clearFlag("cityLevel");
		else
			setFlag("cityLevel");
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
	
	// 校验code是否重复
	public ValidateResult checkCode(AdbAdapter adapter, Vector<String> args) {
		try {
			if (strCode != null) {
				City city = new City();
				city.setCode(getCode());
				city = ((DbAdapter) adapter).retrieve(city, "checkCode");
				if (city == null)
					return ValidateResult.PASS;
				if (city.getSeqId() == lSeqId)
					return ValidateResult.PASS;
				return ValidateResult.FAILED;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ValidateResult.FAILED;
		}
		return ValidateResult.PASS;

	}
	
	// 校验code长度是否过长
	public ValidateResult checkCodeLenth(AdbAdapter adapter, Vector<String> args) {
		try {
			if (strCode != null) {
				byte[] bDef = strCode.getBytes();
				if (bDef.length <= 20 && bDef.length > 0)
					return ValidateResult.PASS;
				else
					return ValidateResult.FAILED;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return ValidateResult.FAILED;
		}
		return ValidateResult.PASS;

	}

	// 校验name长度是否过长
	public ValidateResult checkNameLenth(AdbAdapter adapter, Vector<String> args) {
		try {
			if (strName != null) {
				byte[] bDef = strName.getBytes();
				if (bDef.length <= 20 && bDef.length > 0)
					return ValidateResult.PASS;
				else
					return ValidateResult.FAILED;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return ValidateResult.FAILED;
		}
		return ValidateResult.PASS;

	}
	
	// 校验是否选择上级省/城市
	public ValidateResult checkProvince(AdbAdapter adapter, Vector<String> args) {
		try {
			if (provinceId != 0)
				return ValidateResult.PASS;
			else
				return ValidateResult.FAILED;
		} catch (Exception e) {
			e.printStackTrace();
			return ValidateResult.FAILED;
		}
	}
	
	// 校验是否选择所属区域
	public ValidateResult checkArea(AdbAdapter adapter, Vector<String> args) {
		try {
			if (area != null) {
				if (!area.equals("000"))
					return ValidateResult.PASS;
				else
					return ValidateResult.FAILED;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return ValidateResult.FAILED;
		}
		return ValidateResult.PASS;

	}
}
