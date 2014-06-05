package com.wanda.ccs.data.basemgt;

import java.util.Vector;

import com.aggrepoint.adk.ui.ValidateResult;
import com.icebean.core.adb.ADB;
import com.icebean.core.adb.AdbAdapter;
import com.icebean.core.adb.db.DbAdapter;
import com.icebean.core.common.StringUtils;

/**
 *  发行商设定
 * 
 * @author lujx
 */

public class Publisher extends ADB {
	long lSeqId;
	String strPublisherCode;
	String strPublisherName;
	String pinCode;     // 拼码
	String shortName;   // 简称
	String contact;     //联系人
	String contactPhone; //联系人电话 
	String contactMobile; //联系人手机
	String contactEmail; //联系人EMAIL
	String contactEmail1; //联系人EMAIL1
	String contactEmail2; //联系人EMAIL2
	String contactEmail3; //联系人EMAIL3
	String contactEmail4; //联系人EMAIL4
	String publisherPhone; //发行商电话 
	String publisherFax; //发行商传真 
	String publisherEmail; //发行商EMAIL
	public String getPublisherEmail() {
		return publisherEmail;
	}

	public void setPublisherEmail(String publisherEmail) {
		this.publisherEmail = publisherEmail;
	}
	String publisherAddress; //发行商地址 
	String bank;              // 开户银行
	String bankAccount;       // 银行帐号
	boolean bIsDelete;
	boolean bIsEmail;     //判断是否发送报表
	boolean bIsEmail1;
	boolean bIsEmail2;
	boolean bIsEmail3;
	boolean bIsEmail4;
	
	public long getSeqId() {
		return lSeqId;
	}

	public void setSeqId(long l) {
		this.lSeqId = l;
	}

	public String getPublisherCode() {
		return strPublisherCode;
	}

	public void setPublisherCode(String str) {
		this.strPublisherCode = str;
	}
	
	public String getSearchCode() {
		return StringUtils.toDbMatch(strPublisherCode, "/");
	}

	public void setSearchCode(String str) {
		strPublisherCode = str;

		if (str == null || str.equals(""))
			clearFlag("code");
		else
			setFlag("code");
	}
	
	public String getPublisherName() {
		return strPublisherName;
	}

	public void setPublisherName(String str) {
		this.strPublisherName = str;
	}
	
	public String getSearchName() {
		return StringUtils.toDbMatch(strPublisherName, "/");
	}

	public void setSearchName(String str) {
		strPublisherName = str;

		if (str == null || str.equals(""))
			clearFlag("name");
		else
			setFlag("name");
	}
	
	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String str) {
		this.pinCode = str;
	}
	
	public String getSearchPinCode() {
		return StringUtils.toDbMatch(pinCode, "/");
	}

	public void setSearchPinCode(String str) {
		pinCode = str;

		if (str == null || str.equals(""))
			clearFlag("pinCode");
		else
			setFlag("pinCode");
	}
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String str) {
		this.shortName = str;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String str) {
		this.contact = str;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String str) {
		this.contactPhone = str;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String str) {
		this.contactMobile = str;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String str) {
		this.contactEmail = str;
	}

	public String getContactEmail1() {
		return contactEmail1;
	}

	public void setContactEmail1(String str) {
		this.contactEmail1 = str;
	}

	public String getContactEmail2() {
		return contactEmail2;
	}

	public void setContactEmail2(String str) {
		this.contactEmail2 = str;
	}

	public String getContactEmail3() {
		return contactEmail3;
	}

	public void setContactEmail3(String str) {
		this.contactEmail3 = str;
	}

	public String getContactEmail4() {
		return contactEmail4;
	}

	public void setContactEmail4(String str) {
		this.contactEmail4 = str;
	}

	public String getPublisherPhone() {
		return publisherPhone;
	}

	public void setPublisherPhone(String str) {
		this.publisherPhone = str;
	}

	public String getPublisherFax() {
		return publisherFax;
	}

	public void setPublisherFax(String str) {
		this.publisherFax = str;
	}

	public String getPublisherAddress() {
		return publisherAddress;
	}

	public void setPublisherAddress(String str) {
		this.publisherAddress = str;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String str) {
		this.bank = str;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String str) {
		this.bankAccount = str;
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
	
	public int getIsEmail() {
		return bIsEmail ? 1 : 0;
	}

	public void setIsEmail(int i) {
		bIsEmail = i != 0;
	}

	public boolean isEmail() {
		return bIsEmail;
	}
	
	public int getIsEmail1() {
		return bIsEmail1 ? 1 : 0;
	}

	public void setIsEmail1(int i) {
		bIsEmail1 = i != 0;
	}

	public boolean isEmail1() {
		return bIsEmail1;
	}
	
	public int getIsEmail2() {
		return bIsEmail2 ? 1 : 0;
	}

	public void setIsEmail2(int i) {
		bIsEmail2 = i != 0;
	}

	public boolean isEmail2() {
		return bIsEmail2;
	}
	
	public int getIsEmail3() {
		return bIsEmail3 ? 1 : 0;
	}

	public void setIsEmail3(int i) {
		bIsEmail3 = i != 0;
	}

	public boolean isEmail3() {
		return bIsEmail3;
	}
	
	public int getIsEmail4() {
		return bIsEmail4 ? 1 : 0;
	}

	public void setIsEmail4(int i) {
		bIsEmail4 = i != 0;
	}

	public boolean isEmail4() {
		return bIsEmail4;
	}
	
	// 校验code是否重复
	public ValidateResult checkCode(AdbAdapter adapter, Vector<String> args) {
		try {
			if (strPublisherCode != null) {
				Publisher pub = new Publisher();
				pub.setPublisherCode(getPublisherCode());
				pub = ((DbAdapter)adapter).retrieve(pub, "checkCode");
				if (pub == null)
					return ValidateResult.PASS;
				if(pub.getSeqId() == lSeqId)
					return ValidateResult.PASS;
				return ValidateResult.FAILED;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ValidateResult.FAILED;
		}
		return ValidateResult.PASS;

	}
}
