package com.wanda.mms.control.stream.dao;

import java.sql.Connection;
import java.util.List;

import com.wanda.mms.control.stream.vo.DayTable;

public interface DayTableDao {
	
	public DayTable fandDayTableBynewMembers(Connection con ,String sql,String date1,String date2,String type);//新增会员数：14151(4361552) 
	
	public List<DayTable> fandDayTableBynewMembersList(Connection con ,String sql,String date1,String date2,String type);//新增会员数：14151(4361552) 

	public List<DayTable> fandDayInnerCodeList(Connection con);//新增会员数：14151(4361552) 

}
