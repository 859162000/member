package com.wanda.mms.control.stream.dao;

import java.sql.Connection;

import com.wanda.mms.control.stream.vo.T_Dimdef;

 
/**
 *	维表DAO
 * @author wangshuai
 * @date 2013-05-28	
 */
public interface T_DimdefDao {
	
	public T_Dimdef fandT_DimdefByID(Connection conn ,long T_Dimdefid,String name);

	
}
