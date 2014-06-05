package com.wanda.mms.control.stream;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.solar.etl.SolarEtlExecutor;
import com.solar.etl.config.EtlBean;
import com.solar.etl.config.EtlConfig;
import com.solar.etl.db.ConnctionFactory;
import com.wanda.mms.control.stream.dao.impl.TmpDaoImpl;

/**
 *	会员最新消费调度
 * @author wangshuai
 * @date 2013-07-08	
 */

public class Result {
	static Logger logger = Logger.getLogger(Result.class.getName());
	static{
		EtlConfig.getInstance();
	}
	public static Connection mbr=ConnctionFactory.getConnection("db.mbr_uat");
	/**
	 * 积分计算
	 * @param args 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info("Entering Basic()");
 

		EtlBean mapping=EtlConfig.getInstance().getEtlBean("basicresult");// 会员最近一次观影计算
		SolarEtlExecutor.runetl(mapping,null,1);
		
		EtlBean gsmapping=EtlConfig.getInstance().getEtlBean("goodsresult");// 会员最近一次卖品消费计算
		SolarEtlExecutor.runetl(gsmapping,null,1);
		// 
	//	EtlBean prmapping=EtlConfig.getInstance().getEtlBean("pointrule");// 会员最近订单计录
	//	SolarEtlExecutor.runetl(prmapping,null,1);
	 
	}

}
