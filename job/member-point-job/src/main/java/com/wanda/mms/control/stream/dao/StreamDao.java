package com.wanda.mms.control.stream.dao;

import java.sql.Connection;
import java.util.List;

import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.Stream;


/**
 *  
 * 流水DAO
 * @author wangshuai 
 * @date 2013-05-20
 */
public interface StreamDao {
	
	  
	
	/**
	 * 添加流水
	 * @param  
	 */
	public String addStreamDao(Connection conn,Stream stream);
	
	
	/**
	 * 批量添加流水
	 * @param  
	 */
	public String addListStreamDao(Connection conn,List<Stream> streamlist);
	
	}
