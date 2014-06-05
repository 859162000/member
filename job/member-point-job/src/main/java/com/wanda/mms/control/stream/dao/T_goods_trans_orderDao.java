package com.wanda.mms.control.stream.dao;

import java.sql.Connection;
import java.util.List;

import com.wanda.mms.control.stream.util.Page;
import com.wanda.mms.control.stream.vo.T_goods_trans_order;

/**
 *	会员卖品交易DAO
 * @author wangshuai
 * @date 2013-05-28	
 */
public interface T_goods_trans_orderDao {
	
	/**
	 * 会员卖品交易分页查
	 * @param  
	 */
	public List<T_goods_trans_order> fandT_goods_trans_orderPage(Connection conn,int point,Page page) ;
	
	/**
	 * 会员卖品交易更新
	 * @param  
	 */
	public String updateT_goods_trans_order(Connection conn,T_goods_trans_order tg);
	/**
	 * 会员卖品交易总行数
	 * @param  
	 */
	public long countT_goods_trans_order(Connection conn,String IS_POINT);
	
}
