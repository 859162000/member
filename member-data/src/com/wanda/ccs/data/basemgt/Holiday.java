package com.wanda.ccs.data.basemgt;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.aggrepoint.adk.ui.ValidateResult;
import com.icebean.core.adb.ADB;
import com.icebean.core.adb.AdbAdapter;
import com.xcesys.extras.core.util.DateUtil;

public class Holiday extends ADB {
	long lSeqId;
	String strName;
	Date dtStartDate;
	Timestamp dtEndDate;
	boolean bIsDelete;
	String strHolidayType;
	String searchYear;
	String strYear;
	
	public Holiday(){
		bIsDelete = false;
	}
	public void setSeqId(long seqId){
		lSeqId = seqId;
	}
	public long getSeqId(){
		return lSeqId;
	}
	public void setName(String name){
		strName = name;
	}
	public String getName(){
		return strName;
	}
	public void setStartDate(Date startDate){
		dtStartDate = startDate;
	}
	public Date getStartDate(){
		return dtStartDate;
	}
	public void setEndDate(Timestamp endDate){
		dtEndDate = endDate;
	}
	public Timestamp getEndDate(){
		return dtEndDate;
	}
	public void setIsDelete(int i){
		bIsDelete = i!=0;
	}
	public int getIsDelete(){
		return bIsDelete?1:0;
	}
	public String getYear() {
		if(dtStartDate==null)
			return null;
		return dtStartDate.toString().substring(0,4);
	}
	public void setYear(String strYear) {
		this.strYear = strYear;
	}
	
	public void setHolidayType(String holidayType){
		strHolidayType = holidayType;
	}
	public String getHolidayType(){
		return strHolidayType;
	}
	
	/** 查询条件  年度*/
	public void setSearchYear(String str){
		searchYear = str;
		if(str == null || str.equals("")){
			clearFlag("holidayYear");
		}else{
			setFlag("holidayYear");
		}
	}
	public String getSearchYear(){
		return searchYear;
	}
	
	/** 查询条件  类型*/
	public void setSearchHolidayType(String str){
		strHolidayType = str;
		if(str == null || str.equals("")){
			clearFlag("holidayType");
		}else{
			setFlag("holidayType");
		}
	}
	public String getSearchHolidayType(){
		return strHolidayType;
	}
	
	/** 获得年的集合*/
	public static Vector<String> getYears()
			throws Exception {
		Vector<String> vecYears = new Vector<String>();
		int year = Integer.valueOf(new SimpleDateFormat("yyyy").format(DateUtil.getCurrentDate()));
		for(int i=year-10;i<year+5;i++){
			vecYears.add(i+"");
		}
		return vecYears;
	}
	
	/** 检验开始日期*/
	public ValidateResult checkStartDate(AdbAdapter adapter, Vector<String> afgs){
		try{
			if(dtEndDate != null && dtStartDate != null){
				if(dtStartDate.after(dtEndDate)){
					return ValidateResult.FAILED;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ValidateResult.PASS;
	}
	/** 检验结束日期*/
	public ValidateResult checkEndDate(AdbAdapter adapter, Vector<String> afgs){
		try{
			if(dtEndDate != null && dtStartDate != null){
				if(dtStartDate.after(dtEndDate)){
					return ValidateResult.FAILED;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ValidateResult.PASS;
	}
	
}
