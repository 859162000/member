package com.wanda.ccs.data.basemgt;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

import com.aggrepoint.adk.ui.ValidateResult;
import com.icebean.core.adb.ADB;
import com.icebean.core.adb.AdbAdapter;
import com.icebean.core.adb.db.DbAdapter;
import com.icebean.core.common.StringUtils;

/**
 * 维数据项
 * 
 * @author Jim
 */
public class DimDef extends ADB {
	long lSeqId;
	String strCode;
	String strName;
	int iTypeId;
	long lParent;
	Date dtValidStart;
	Date dtValidEnd;
	boolean bIsDelete;
	Timestamp create_Date;
	Timestamp update_Date;

	public DimDef() {
		bIsDelete = false;
	}

	public Timestamp getCreate_Date() {
		return create_Date;
	}

	public void setCreate_Date(Timestamp create_Date) {
		this.create_Date = create_Date;
	}

	public Timestamp getUpdate_Date() {
		return update_Date;
	}

	public void setUpdate_Date(Timestamp update_Date) {
		this.update_Date = update_Date;
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

	public String getSearchCode() {
		return StringUtils.toDbMatch(strCode, "/");
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
		return StringUtils.toDbMatch(strName, "/");
	}

	public void setSearchName(String str) {
		strName = str;

		if (str == null || str.equals(""))
			clearFlag("name");
		else
			setFlag("name");
	}

	public int getTypeId() {
		return iTypeId;
	}

	public void setTypeId(int i) {
		iTypeId = i;
	}

	//
	// //
	public int getSearchTypeId() {
		return iTypeId;
	}

	// 根据类型查询
	//
	public void setSearchTypeId(int typeId) {
		iTypeId = typeId;

	}

	public long getParent() {
		return lParent;
	}

	public void setParent(long l) {
		lParent = l;
	}

	public Date getValidStart() {
		return dtValidStart;
	}

	public void setValidStart(Date dt) {
		dtValidStart = dt;
	}

	public Date getValidEnd() {
		return dtValidEnd;
	}

	public void setValidEnd(Date dt) {
		dtValidEnd = dt;
	}

	public int getIsDelete() {
		return bIsDelete ? 1 : 0;
	}

	public void setIsDelete(int i) {
		bIsDelete = i != 0;
	}

	// 根据标记是否删除查询
	public int getSearchIsDelete() {
		return bIsDelete ? 1 : 0;
	}

	public void setSearchIsDelete(int i) {
		bIsDelete = i != 0;

	}

	public boolean isDelete() {
		return bIsDelete;
	}

	// 校验code是否重复
	public ValidateResult checkCode(AdbAdapter adapter, Vector<String> args) {
		try {
			if (strCode != null) {
				DimDef def = new DimDef();
				def.setTypeId(getTypeId());
				def.setCode(getCode());
				def = ((DbAdapter) adapter).retrieve(def, "checkCode");

				if (def == null)
					return ValidateResult.PASS;
				if (def.getSeqId() == lSeqId)
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

	// 校验查询name时，是否输入特殊字符
	// public ValidateResult checkSearchName(AdbAdapter adapter, Vector<String>
	// args) {
	// try {
	// if (strName != null) {
	// if (!strName.contains("%") || !strName.contains("_"))
	// return ValidateResult.PASS;
	// else
	// return ValidateResult.FAILED;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// return ValidateResult.FAILED;
	// }
	// return ValidateResult.PASS;
	//
	// }
	// 校验查询code时，是否输入特殊字符
	// public ValidateResult checkSearchCode(AdbAdapter adapter, Vector<String>
	// args) {
	// try {
	// if (strCode != null) {
	// if (!strCode.contains("%") || !strCode.contains("_"))
	// return ValidateResult.PASS;
	// else
	// return ValidateResult.FAILED;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// return ValidateResult.FAILED;
	// }
	// return ValidateResult.PASS;
	//
	// }
}
