package com.wanda.mms.control.stream.dao;

import java.sql.Connection;
import java.util.List;

import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.T_ticket_trans_order;

/**
 *	会员影票交易DAO
 * @author wangshuai
 * @date 2013-05-28	
 */
public interface T_ticket_trans_orderDao {
	
	/**
	 * 会员影票交易分页查
	 * @param  
	 */
	public List<T_ticket_trans_order> fandT_ticket_trans_orderPage(Connection conn,int point,Page page) ;
	
	/**
	 * 会员影票交易更新
	 * @param  
	 */
	public String updateT_ticket_trans_order(Connection conn,T_ticket_trans_order tt);
	/**
	 * 会员影票交易总行数
	 * @param  
	 */
	public long countT_ticket_trans_order(Connection conn,String IS_POINT);
	
}
