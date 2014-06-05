package com.wanda.mms.control.stream.dao;

import java.sql.Connection;

import com.wanda.mms.control.stream.vo.PointHistoryArchive;
/**
 *	会员积分历史归档DAO
 * @author wangshuai
 * @date 2013-05-26
 */
public interface PointHistoryArchiveDao {
	
	/**
	 * 添加会员积分历史归档
	 * @param  
	 */
	public int addPointHistoryArchive(Connection conn,PointHistoryArchive pointhistoryarchive);
	
	
	

}
