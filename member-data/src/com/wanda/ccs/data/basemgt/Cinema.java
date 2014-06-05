package com.wanda.ccs.data.basemgt;

import java.util.Date;
import java.util.Vector;

import com.aggrepoint.adk.ui.ValidateResult;
import com.icebean.core.adb.ADB;
import com.icebean.core.adb.AdbAdapter;
import com.icebean.core.adb.db.DbAdapter;

/**
 * 影院
 * 
 * @author Danne Leung
 */
public class Cinema extends ADB {
	private long seqId;
	private String code;
	private String name;
	private String short_Name;
	private String pin_Code;
	private String inner_Code;
	private int isOpen;
	private Date open_Date;
	private String contract_Begin;
	private String contract_End;
	private int hall_Count;
	private int Td_Hall_Count;
	private int digital_Hall_Count;
	private int imax_Hall_Count;
	private int seat_Count;
	private String address;
	private String post_Code;
	private String cinema_Level;
	private String cinema_Attr;
	private String supp_Type;
	private String mkt_Type;
	private String province;
	private String city;
	private String area;
	private String cinema_Type;

	public Cinema() {
	}

	public long getSeqId() {
		return seqId;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSearchName() {
		return name;
	}

	public void setSearchName(String str) {
		name = str;

		if (str == null || str.equals(""))
			clearFlag("name");
		else
			setFlag("name");
	}
	
	public String getShort_Name() {
		return short_Name;
	}

	public void setShort_Name(String short_Name) {
		this.short_Name = short_Name;
	}


	public String getPin_Code() {
		return pin_Code;
	}

	public void setPin_Code(String pin_Code) {
		this.pin_Code = pin_Code;
	}

	public String getInner_Code() {
		return inner_Code;
	}

	public void setInner_Code(String inner_Code) {
		this.inner_Code = inner_Code;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public Date getOpen_Date() {
		return open_Date;
	}

	public void setOpen_Date(Date open_Date) {
		this.open_Date = open_Date;
	}

	public String getContract_Begin() {
		return contract_Begin;
	}

	public void setContract_Begin(String contract_Begin) {
		this.contract_Begin = contract_Begin;
	}

	public String getContract_End() {
		return contract_End;
	}

	public void setContract_End(String contract_End) {
		this.contract_End = contract_End;
	}

	public int getHall_Count() {
		return hall_Count;
	}

	public void setHall_Count(int hall_Count) {
		this.hall_Count = hall_Count;
	}

	public int getTd_Hall_Count() {
		return Td_Hall_Count;
	}

	public void setTd_Hall_Count(int td_Hall_Count) {
		Td_Hall_Count = td_Hall_Count;
	}

	public int getDigital_Hall_Count() {
		return digital_Hall_Count;
	}

	public void setDigital_Count(int digital_Hall_Count) {
		this.digital_Hall_Count = digital_Hall_Count;
	}

	public int getImax_Hall_Count() {
		return imax_Hall_Count;
	}

	public void setImax_Hall_Count(int imax_Hall_Count) {
		this.imax_Hall_Count = imax_Hall_Count;
	}

	public int getSeat_Count() {
		return seat_Count;
	}

	public void setSeat_Count(int seat_Count) {
		this.seat_Count = seat_Count;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPost_Code() {
		return post_Code;
	}

	public void setPost_Code(String post_Code) {
		this.post_Code = post_Code;
	}

	public String getCinema_Level() {
		return cinema_Level;
	}

	public void setCinema_Level(String cinema_Level) {
		this.cinema_Level = cinema_Level;
	}

	public String getCinema_Attr() {
		return cinema_Attr;
	}

	public void setCinema_Attr(String cinema_Attr) {
		this.cinema_Attr = cinema_Attr;
	}

	public String getSupp_Type() {
		return supp_Type;
	}

	public void setSupp_Type(String supp_Type) {
		this.supp_Type = supp_Type;
	}

	public String getMkt_Type() {
		return mkt_Type;
	}

	public void setMkt_Type(String mkt_Type) {
		this.mkt_Type = mkt_Type;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getSearchArea() {
		return area;
	}

	public void setSearchArea(String str) {
		area = str;

		if (str == null || str.equals(""))
			clearFlag("area");
		else
			setFlag("area");
	}

	public String getCinema_Type() {
		return cinema_Type;
	}

	public void setCinema_Type(String cinema_Type) {
		this.cinema_Type = cinema_Type;
	}

	// 校验code是否重复
	public ValidateResult checkCode(AdbAdapter adapter, Vector<String> args) {
		try {
			if (code != null) {
				 Cinema cinema = new Cinema();
				 cinema.setSeqId(getSeqId());
				 cinema.setCode(getCode());
				 cinema = ((DbAdapter) adapter).retrieve(cinema, "checkCode");
				if (cinema == null)
					return ValidateResult.PASS;
				if(cinema.getSeqId() == getSeqId())
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
