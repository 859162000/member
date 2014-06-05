package com.wanda.mms.control.response;

import java.sql.Connection;

import com.solar.etl.SolarEtlExecutor;
import com.solar.etl.config.EtlBean;
import com.solar.etl.config.EtlConfig;
import com.solar.etl.db.ConnctionFactory;
import com.wanda.mms.dao.MyBatisDAO;

/**
 *	会员营销统计 调度
 * @author wangshuai
 * @date 2013-09-11	
 */

public class MarketingStatistics extends MyBatisDAO {
	
	static{
		EtlConfig.getInstance();
	}
	public static Connection mbr=ConnctionFactory.getConnection("db.mbr");
	
	

	public static void main(String[] args) {
		MarketingStatistics.execute();
	}

//	 10待统计  20统计结束  30统计中 40统计异常
	public static void execute() {
		step1();
//	 	step2();
	}
	
	public static void step1() {
		//1 找到 status =10的数据
		//2 处理成功状态改为status=30 ，如果失败 status =40
		EtlBean mapping=EtlConfig.getInstance().getEtlBean("tactresultup");//基础积分计算:影票计算 //改按影城计算。
	 	SolarEtlExecutor.runetlFixedThread(mapping,null,1);//影城内码 OK //关联和推荐打标签
	}
	
	public static void step2() {
		//1 找到 status=30的数据
		//2 处理 后状态更新为 20
		EtlBean mappingStatistics=EtlConfig.getInstance().getEtlBean("tactresultstatistics");//基础积分计算:影票计算 //改按影城计算。
	 	SolarEtlExecutor.runetlFixedThread(mappingStatistics,null,1);//统计关联 和推荐相应的总数
	 	
	}

}
