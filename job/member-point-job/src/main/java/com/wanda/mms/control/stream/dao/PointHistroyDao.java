package com.wanda.mms.control.stream.dao;

import java.sql.Connection;
import java.util.List;

import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.MemberPoint;
import com.wanda.mms.control.stream.vo.PointHistroy;
/**
 *	会员积分历史DAO
 * @author wangshuai
 * @date 2013-05-20	
 */
public interface PointHistroyDao {
	
	/**
	 * 添加会员积分历史
	 * @param  
	 */
	public String addPointHistroy(Connection conn,PointHistroy pointhistroy);
	
	/**
	 * 添加会员积分历史
	 * @param  
	 */
	public String addPointHistroyNotAutoCommit(Connection conn,PointHistroy pointhistroy);
	
	
	/**
	 * 批量添加会员积分历史
	 * @param  
	 */
	public String addListPointHistroy(Connection conn,List<PointHistroy> pointhistroylist);
	
	/**
	 * 更新会员积分历史
	 * @param  
	 */
	public String updatePointHistroy(Connection conn,PointHistroy pointhistroy) ;
	/**
	 * SEQ 会员积分历史
	 * @param  
	 */
	public Long seqPointHistroy(Connection conn);
	
	/**
	 * 查询等级
	 * @param  
	 */
	public List<PointHistroy> fandPointHistroyByLevel(Connection conn,long memberId);
	public List<PointHistroy> fandPointHistroyByLevel(Connection conn,String date,long memberId);
	
	/**
	 * 查询积分有效期
	 * @param  
	 */
	public List<PointHistroy> fandPointHistroyByExchange_point_expire_time(Connection conn,String date,long memberId);
	
	/**
	 * 查询清零数据
	 * @param  
	 */
	public List<PointHistroy> fandPointHistroyByPoint_Trans_Type(Connection conn,String Point_Trans_Type,long memberId);
	/**
	 * 积分清零总条数
	 * @param  
	 */
	public long fandPointHistroyResetTotalNum(Connection conn,String date);
	/**
	 * 积分清零总条数
	 * @param  
	 */
	public long fandPointHistroyResetTotal(Connection conn,String date);
 
	/**
	 * 积分清零Point_Trans_Type总条数
	 * @param  
	 */
	public long fandPointHistroyByPoint_Trans_Type(Connection conn,String point_trans_type);
	/**
	 * 积分清零PointHistroy分页
	 * @param  
	 */
	public List<PointHistroy> fandPointHistroyByPage(Connection conn,String point_trans_type,Page page);
	
	/**
	 * 积分清零分页
	 * @param  
	 */
	
	public List<MemberPoint> fandPointHistroyResetPage(Connection conn,String date,Page page);
	
	
	/**
	 * 积分清零分页数据
	 * @param  
	 */
	
	public List<PointHistroy> fandPointHistroyPoint_Trans_Type(Connection conn,String point_trans_type,Page page);
	
	

	/**
	 * 积分归档查询
	 * @param  Archive
	 */
	
	public List<PointHistroy> fandPointHistroyResetArchive(Connection conn,String date,long member_id);
	
	
	/**
	 * 积分清零物理删除
	 * @param  
	 */
	public int delPointHistroyReset(Connection conn,long point_history_id);
	
	
}
