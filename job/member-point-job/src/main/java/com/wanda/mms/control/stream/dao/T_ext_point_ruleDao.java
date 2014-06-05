package com.wanda.mms.control.stream.dao;

import java.sql.Connection;
import java.util.List;

import com.wanda.mms.control.stream.vo.T_ext_point_rule;

/**
 *	特殊积分规则DAO
 * @author wangshuai
 * @date 2013-05-29	
 */
public interface T_ext_point_ruleDao {
	/**
	 * 特殊积分规则查询
	 * @param 
	 * @param  
	 */
	public List<T_ext_point_rule> fandT_ext_point_ruleByDate(Connection conn,String statusa,String statusb,String end_dtime);
	
	

}
