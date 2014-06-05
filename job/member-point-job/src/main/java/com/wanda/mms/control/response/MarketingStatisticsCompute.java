package com.wanda.mms.control.response;

import java.sql.Connection;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.solar.etl.SolarEtlExecutor;
import com.solar.etl.config.EtlBean;
import com.solar.etl.config.EtlConfig;
import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.wanda.mms.dao.MyBatisDAO;
import com.wanda.mms.data.TActResult;
import com.wanda.mms.data.TActResultMapper;
import com.wanda.mms.service.MbrApplicationContext;

/**
 * 会员营销统计 调度
 * @author wangshuai
 * @date 2013-09-11	
 */

public class MarketingStatisticsCompute extends MyBatisDAO {
	static ApplicationContext context = null;
	static MyBatisDAO myBatisDAO = null;
	static{
//		EtlConfig.getInstance();
		context = MbrApplicationContext.getInstance();
		 myBatisDAO = context.getBean("myBatisDAO",MyBatisDAO.class);
//		mbr = myBatisDAO.getConnection();
	}
//	public static Connection mbr=ConnctionFactory.getConnection("db.mbr");
	
	public static Connection mbr= null;

	public static void main(String[] args) {
		MarketingStatisticsCompute.execute();
	}

//	 10待统计  20统计结束  30统计中 40统计异常
	public static void execute() {
		TActResultMapper mapper = myBatisDAO.sqlSession.getMapper(TActResultMapper.class);
		MarketingEffectEvaluation marketing = context.getBean("marketingEffectEvaluation", MarketingEffectEvaluation.class);
		List<TActResult> list = mapper.selectByStatus("10");
		if(list!=null){
			for(TActResult res:list){
				Gson gson = new Gson();
				System.out.println(gson.toJson(res));
				marketing.handle(getFiledSet(res));				
				System.out.println("resulet="+gson.toJson(marketing.handleResult));
			}
		}
//		step1();
//	 	step2();
	}
	
	private static FieldSet getFiledSet(TActResult re) {
		FieldSet fieldSet = new FieldSet();
		Field field = new Field();
		field.destName="ACT_RESULT_ID";
		field.destValue=String.valueOf(re.getActResultId());
		fieldSet.put(field);
		
		field = new Field();
		field.destName="CMN_ACTIVITY_ID";
		field.destValue=String.valueOf(re.getCmnActivityId());
		fieldSet.put(field );
		//6 6 10 2 1
		field = new Field();
		field.destName="RES_SEGMENT_ID";
		field.destValue=String.valueOf(re.getResSegmentId());
		fieldSet.put(field );
		return fieldSet;	
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
