package com.wanda.mms.control.stream;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.solar.etl.SolarEtlExecutor;
import com.solar.etl.config.EtlBean;
import com.solar.etl.config.EtlConfig;
import com.solar.etl.db.ConnctionFactory;



public class MemberBasic {
	static Logger logger = Logger.getLogger(MemberBasic.class.getName());
	static{
		EtlConfig.getInstance();
	}
	public static Connection mbr=ConnctionFactory.getConnection("db.mbr_uat");
	/**
	 * 基础积分计算
	 * @param args 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info("Entering Basic()");
//		BasicPoint bp = new BasicPoint();
//		BasicPointGoods bpg = new BasicPointGoods();
//		//创建连接
//		SENDDBConnection db=SENDDBConnection.getInstance();	
//	    Connection conn = null;
//		conn=db.getConnection();
//		
//		bp.basicpointcount(conn);//计算影票
//		
//		bpg.basicpointcountGoods(conn);//计算卖品
//	
//		//System.out.println("aa");
//		logger.info("Exiting Basic()");
//		System.exit(0);

		EtlBean mapping=EtlConfig.getInstance().getEtlBean("basicpoint");//基础积分计算
		SolarEtlExecutor.runetl(mapping,null,1);

		
		
		
		
	}

}
