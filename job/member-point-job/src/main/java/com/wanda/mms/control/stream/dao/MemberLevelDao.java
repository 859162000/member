package com.wanda.mms.control.stream.dao;

import java.sql.Connection;
import java.util.List;

import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.MemberLevel;
import com.wanda.mms.control.stream.vo.MemberPoint;

/**
 *	会员级别DAO
 * @author wangshuai
 * @date 2013-05-28	
 */
public interface MemberLevelDao {
	
	/**
	 * SEQML
	 * @param  
	 */
	public long fandMemberLevelBySEQ(Connection conn);
	
	
	/**
	 * 查询会员级别
	 * @param  
	 */
	public MemberLevel fandMemberLevelByID(Connection conn ,long memberId);
	/**
	 * 更新会员级别
	 * @param  
	 */
	public int updateMemberLevelByID(Connection conn ,MemberLevel ml);
	
	/**
	 * 新增会员级别
	 * @param  
	 */
	public int addMemberLevelByID(Connection conn ,MemberLevel ml);
	
	/**
	 * 会员积分账户分页查
	 * @param  
	 */
	public List<MemberLevel> fandMemberLevelPage(Connection conn,Page page) ;
	/**
	 * 会员积分账户总条数
	 * @param  
	 */
	public long fandMemberLevelTotalNum(Connection conn);
	

}
