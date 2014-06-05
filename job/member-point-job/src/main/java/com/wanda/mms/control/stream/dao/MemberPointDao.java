package com.wanda.mms.control.stream.dao;

import java.sql.Connection;
import java.util.List;

import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.MemberPoint;
/**
 *	会员积分DAO
 * @author wangshuai
 * @date 2013-05-28	
 */
public interface MemberPointDao {
	
	/**
	 * 查询会员积分账户
	 * @param  
	 */
	public MemberPoint fandMemberPointByID(Connection conn ,long memberId);
	/**
	 * 更新会员积分账户
	 * @param  
	 */
	public int updateMemberPointByID(Connection conn ,MemberPoint mb);
	
	public int upMemberPointBylevel(Connection conn,long MemberId) ;
	
	/**
	 * 更新会员积分账户
	 * @param  
	 */
	public int addMemberPointByID(Connection conn ,MemberPoint mb);
	/**
	 * 会员积分账户分页查
	 * @param  
	 */
	public List<MemberPoint> fandMemberPointPage(Connection conn,Page page) ;
	/**
	 * 会员积分账户总条数
	 * @param  
	 */
	public long fandMemberPointTotalNum(Connection conn);
	
	
	/**
	 * 会员积分账户总条数
	 * @param  
	 */
	public long fandMemberPointTotalNumDay(Connection conn );
	
	public long fandMemberPointTotalNumDay(Connection conn,String date);
	/**
	 * 会员积分账户分页查
	 * @param  
	 */
	public List<MemberPoint> fandMemberPointDayPage(Connection conn,Page page,String date) ;
	public List<MemberPoint> fandMemberPointDayPage(Connection conn, Page page);
	/**
	 * 返回seq
	 * @param conn
	 * @return
	 */
	public Long seqMemberPoint(Connection conn);
}
