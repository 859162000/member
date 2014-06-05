package com.wanda.mms.control.stream;

import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import com.solar.etl.SolarEtlExecutor;
import com.solar.etl.config.EtlBean;
import com.solar.etl.config.EtlConfig;
import com.solar.etl.db.ConnctionFactory;
import com.solar.etl.log.LogUtils;
import com.wanda.mms.control.stream.dao.impl.TmpDaoImpl;
import com.wanda.mms.control.stream.util.DateUtil;

/**
 *	会员积分计算 调度
 * @author wangshuai
 * @date 2013-07-08	
 */

public class Basic {
	static Logger logger = Logger.getLogger(Basic.class.getName());
	static{
		EtlConfig.getInstance();
	}
	public static Connection mbr=ConnctionFactory.getConnection("db.mbr_uat");
	
	public static Connection mbrdw=ConnctionFactory.getConnection("db.mbr_dw_uat");
	
	
	/**
	 * 积分计算
	 * @param args 
	 */
	public static void main(String[] args) {
		//args[] is null 算前一个营业日积分及已经ｏｋ的影城积分
		//args[0] = bizdate/recaluc  按营业日计算/按营业日与影城重算 ,
		//args[0]=bizdate 时arg[1] 必须输入
		//args[0]=recaluc args[1],args[2]必须都有输入，如果输入其他字符，代表不重算，单独计算墨家影城积分
		//args[1] = 营业日
		//args[2] = 影城内码　　算前一个营业日指定影城积分
	
		// TODO Auto-generated method stub
		logger.info("Entering Basic()"); 
// 		Calendar cal=Calendar.getInstance();
//		int x=-5;//or x=-3;
//		cal.add(Calendar.DAY_OF_MONTH,x);
//		String bzda=new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
//		System.out.println("Bizdate:"+bzda);
//		
//		Basic ba = new Basic();
//		String cinema ="993";
//	    String 	da=bzda;
//		
//		   ba.againCalculate(cinema, da)	;	  
		//   ba.calculatePoint(cinema, da);
		    
		TmpDaoImpl tm = new TmpDaoImpl();
		
		String isOK="";
		
		
//		if(args==null||args.length==0){//args[] is null 算前一个营业日积分及已经ｏｋ的影城积分
//			Calendar cal=Calendar.getInstance();
//			int x=-1;//or x=-3;
//			cal.add(Calendar.DAY_OF_MONTH,x);
//			String bzda=new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
//			//System.out.println("Bizdate:"+bzda);
//			
//			Basic ba = new Basic();
//			//如果不重算那么便利看有多少个影城今天要进行积分计算一家家的跑。
//
//			List<String> strlist = tm.fandCinema_inner_code(Basic.mbr, bzda);
//			for (int i = 0; i < strlist.size(); i++) {
//				String cinema =strlist.get(i);
//				String oo= ba.calculatePoint(cinema, bzda);
//			}			
//			isOK="OK";
//			
//		}else 
		if(args[0].equals("recaluc")&&args[1]!=null&&args[2]!=null){
			String foo=args[0];
			String baz=args[1];
			String bar=args[2];
			
			Basic ba = new Basic();
			String cinema =bar;
		    String 	da=baz;
		    ba.againCalculate(cinema, da)	;	   
		    
		    ba.calculatePoint(cinema, da);
 		     
 			 
 		}else if(args[0].equals("bizdate")&&args[1]!=null&&args[2]!=null){//args[] is null 算前一个营业日积分及已经ｏｋ的影城积分
 			String foo=args[0];
			String baz=args[1];
			String bar=args[2];
			
			Basic ba = new Basic();
			String cinema =bar;
		    String 	da=baz;
		//    ba.againCalculate(cinema, da)	;	   
		    
		    ba.calculatePoint(cinema, da);
			Date od = new Date();
			String sd = DateUtil.getDateStrss(od);
			LogUtils.debug("今天的积分计算本影城以经跑完了当前时间是"+sd);
			
		}else{
 			LogUtils.debug("影城内码，营业日必须输入 exp:Basic recaluc 2013-07-23 993");
 		}
//		
//		 
//		 
			
		 
	
//		

	}
	
	public void againCalculate(String cinema,String date){
		//先看当天本影城的会员定级，非定级，可对换积分 是多少，把他们的状态改成不可用除可对换   OK
		//把所有影票表中is_ticket 是0的定级与非定级，总积分初始化为0。
		//-----1 映射中 没有逻辑处理 可以忽略
		
		// --1 李宁 注释 1 按影城 按日期 将 T_POINT_HISTORY 对应记录设置为删除 和 重新计算
		EtlBean uphmapping=EtlConfig.getInstance().getEtlBean("uphistory");// 
		SolarEtlExecutor.runetl(uphmapping,new String[]{"-bizdate",date,"-cinema",cinema},1); 
		//-----1 映射中 没有逻辑处理 可以忽略
		
		//插入一条积分是 减去今天会员的积分表中的所有定级，非定级，可对换，并且状态是删除的。    
		// --2 李宁 积分历史冲正
		EtlBean upczhmapping=EtlConfig.getInstance().getEtlBean("upczhistory");// 
		SolarEtlExecutor.runetl(upczhmapping,new String[]{"-bizdate",date,"-cinema",cinema},1); 
		
		//把今天的票明细，卖品明细，票订单，卖品订单状态初始化。
		//upgoodsdetail.xml
		//upgoodsorder.xml
		//upticketdetail.xml
		//upticketorder.xml
		//--李宁 3 初始 T_GOODS_TRANS_DETAIL
		EtlBean upgdmapping=EtlConfig.getInstance().getEtlBean("upgoodsdetail");//状态初始化。卖品明细
		SolarEtlExecutor.runetl(upgdmapping,new String[]{"-bizdate",date,"-cinema",cinema},1);//影城内码
		//--李宁 4 初始 T_GOODS_TRANS_ORDER
		EtlBean upgomapping=EtlConfig.getInstance().getEtlBean("upgoodsorder");//状态初始化。卖品订单
		SolarEtlExecutor.runetl(upgomapping,new String[]{"-bizdate",date,"-cinema",cinema},1);//影城内码
		//--李宁 5 初始 T_GOODS_TRANS_DETAIL
		EtlBean uptdmapping=EtlConfig.getInstance().getEtlBean("upticketdetail");//状态初始化。票房明细
		SolarEtlExecutor.runetl(uptdmapping,new String[]{"-bizdate",date,"-cinema",cinema},1);//影城内码
		//--李宁 6 初始 T_TICKET_TRANS_DETAIL
		EtlBean uptomapping=EtlConfig.getInstance().getEtlBean("upticketorder");//状态初始化。票房订单
		SolarEtlExecutor.runetl(uptomapping,new String[]{"-bizdate",date,"-cinema",cinema},1);//影城内码
		
	}
	/**
	 * add by lining 2013-11-23
	 * @param cinema
	 * @param date
	 */
	public void prepareActivePointTransData(String cinema,String date){
		EtlBean prmapping=EtlConfig.getInstance().getEtlBean("textpointrule");//插入到临时表。 等待规则
 		SolarEtlExecutor.runetlFixedThread(prmapping,new String[]{"-bizdate",date,"-cinema",cinema},1);
	}
	public String calculatePoint(String cinema,String date){
		String OK="ON";

		//特殊积分临时表清理
		//查看 状态 得到影城OK的把影城内码传过来，开始算积分。还要看一下Handle 内的SQL 是否要加限定
	//	EtlBean mapping=EtlConfig.getInstance().getEtlBean("basicpoint");//基础积分计算:影票计算 //改按影城计算。
	//	SolarEtlExecutor.runetlFixedThread(mapping,new String[]{"-bizdate",date,"-cinema",cinema},80);//影城内码 OK
 		EtlBean mapping=EtlConfig.getInstance().getEtlBean("basicpoint");//基础积分计算:影票计算 //改按影城计算。
		SolarEtlExecutor.runetlFixedThread(mapping,new String[]{"-bizdate",date,"-cinema",cinema},10);//影城内码 OK
		
		//基础积分计算:卖品计算 先写出来等表到了在试
		EtlBean gsmapping=EtlConfig.getInstance().getEtlBean("goodsbasicpoint");//等表测试
		SolarEtlExecutor.runetlFixedThread(gsmapping,new String[]{"-bizdate",date,"-cinema",cinema},10);//影城内码
		//查询特殊积分规则，插入到临时表。 等待规则
//		EtlBean prmapping=EtlConfig.getInstance().getEtlBean("textpointrule");//插入到临时表。 等待规则
//		SolarEtlExecutor.runetl(prmapping,new String[]{"-bizdate",date,"-cinema",cinema},1);

		EtlBean apmapping=EtlConfig.getInstance().getEtlBean("activitypoint");//特殊积分计算：影票计算
		SolarEtlExecutor.runetlFixedThread(apmapping,new String[]{"-bizdate",date,"-cinema",cinema},10);//影城内码
		
		//特殊积分计算：卖品计算
		EtlBean gapmapping=EtlConfig.getInstance().getEtlBean("goodsactivitypoint");//特殊积分计算：卖品计算
		SolarEtlExecutor.runetlFixedThread(gapmapping,new String[]{"-bizdate",date,"-cinema",cinema},10);//影城内码
		
		EtlBean zgapmapping=EtlConfig.getInstance().getEtlBean("totalactivityhistory");////更新影票总积分
		SolarEtlExecutor.runetlFixedThread(zgapmapping,new String[]{"-bizdate",date,"-cinema",cinema},10);//影城内码
		
		
		EtlBean zjgapmapping=EtlConfig.getInstance().getEtlBean("totalgoodsactivityhistory");////更新卖品总积分
		SolarEtlExecutor.runetlFixedThread(zjgapmapping,new String[]{"-bizdate",date,"-cinema",cinema},10);//影城内码
		
		
		EtlBean pmapping=EtlConfig.getInstance().getEtlBean("point");//影票积分计算完成
		SolarEtlExecutor.runetlFixedThread(pmapping,new String[]{"-bizdate",date,"-cinema",cinema},10);//影城内码
		//卖品计算完成
		EtlBean gpmapping=EtlConfig.getInstance().getEtlBean("goodspoint");//卖品积分计算完成
		SolarEtlExecutor.runetlFixedThread(gpmapping,new String[]{"-bizdate",date,"-cinema",cinema},10);//影城内码
		// 现在开始插入历史 并更新 本表与会员积分表 还有订单表
		EtlBean hmapping=EtlConfig.getInstance().getEtlBean("history");//插入积分历史,基础积分计算:影票计算
		SolarEtlExecutor.runetlFixedThread(hmapping,new String[]{"-bizdate",date,"-cinema",cinema},1);//影城内码
		//插入积分历史,基础积分计算:卖品计算
		EtlBean ghmapping=EtlConfig.getInstance().getEtlBean("goodshistory");//插入积分历史,基础积分计算:卖品计算
		SolarEtlExecutor.runetlFixedThread(ghmapping,new String[]{"-bizdate",date,"-cinema",cinema},1);//影城内码
		
		//插入积分历史,特殊积分计算：影票计算
 		EtlBean ahmapping=EtlConfig.getInstance().getEtlBean("activityhistory");//插入积分历史,特殊积分计算：影票计算
 		SolarEtlExecutor.runetlFixedThread(ahmapping,new String[]{"-bizdate",date,"-cinema",cinema},1);//影城内码
		
		//插入积分历史,特殊积分计算：卖品计算
		EtlBean gahmapping=EtlConfig.getInstance().getEtlBean("goodsactivityhistory");//插入积分历史,特殊积分计算：卖品计算
		SolarEtlExecutor.runetlFixedThread(gahmapping,new String[]{"-bizdate",date,"-cinema",cinema},1);//影城内码
		
		 //把所有影票表中is_ticket 是0的定级与非定级，总积分初始化为0。
	 	EtlBean upmapping=EtlConfig.getInstance().getEtlBean("upactivityhistory");// 
	 	SolarEtlExecutor.runetlFixedThread(upmapping,new String[]{"-bizdate",date,"-cinema",cinema},10);//影城内码
		
		return OK="OK";
	}

}
