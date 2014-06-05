package com.wanda.mms.control.stream.dao;

import java.sql.Connection;
import java.util.List;

import com.wanda.mms.control.stream.vo.LevelHistory;
import com.wanda.mms.control.stream.vo.MemberLevel;
/**
 *	会员级别历史DAO
 * @author wangshuai
 * @date 2013-05-28	
 */
public interface LevelHistoryDao {
	
	/**
	 * SEQ 会员级别历史
	 * @param  conn
	 */
	public Long seqLevelHistroy(Connection conn);
	
	/**
	 * 添加会员级别历史
	 * @param  conn
	 * @param  levelhistory
	 */
	public String addLevelHistory(Connection conn,LevelHistory levelhistory,MemberLevel ml);
	
	/**
	 * 添加会员级别初始化
	 * @param  conn
	 * @param  levelhistory
	 */
	public String addLevelHistoryByInitialization(Connection conn,LevelHistory levelhistory);

	/**
	 * 批量添加会员级别历史
	 * @param  conn
	 * @param  levelhistorylist
	 */
	public String addListLevelHistory(Connection conn,List<LevelHistory> levelhistorylist);

	/**
	 * 更新会员积分历史
	 * @param  
	 */
	public String updateLevelHistory(Connection conn,LevelHistory levelhistory) ;
	
	
	
}
