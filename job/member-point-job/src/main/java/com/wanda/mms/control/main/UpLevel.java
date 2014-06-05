package com.wanda.mms.control.main;

import java.util.Calendar;

import com.solar.etl.SolarEtlExecutor;
import com.solar.etl.config.EtlBean;
import com.solar.etl.config.EtlConfig;

public class UpLevel {
	
	public static void main(String[] args) {
		if(args==null||args.length==0){
			//无参的按时间减一天来算等级
			Calendar cal=Calendar.getInstance();
			int x=-0;//or x=-3;
			cal.add(Calendar.DAY_OF_MONTH,x);
			String bzda=new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			EtlBean prmapping=EtlConfig.getInstance().getEtlBean("uplevel");//插入到临时表。 等待规则
	 		SolarEtlExecutor.runetlFixedThread(prmapping,new String[]{"-bizdate",bzda},10);
		}else if(args!=null&&args.length==2){
			//有参就按输入的时间来跑那天的等级
			if(args[0].equals("bizdate")&&args[1]!=null ){
			String foo=args[0];
			String bar=args[1];
			EtlBean prmapping=EtlConfig.getInstance().getEtlBean("uplevel");//插入到临时表。 等待规则
	 		SolarEtlExecutor.runetlFixedThread(prmapping,new String[]{"-bizdate",bar},10);
			}else if  (args[0].equals("islevel")&&args[1]!=null ){
				
				EtlBean prmapping=EtlConfig.getInstance().getEtlBean("upislevel");//插入到临时表。 等待规则
		 		SolarEtlExecutor.runetlFixedThread(prmapping,null,10);
			
			}else if (args[0].equals("memberid")&&args[1]!=null ){
				String foo=args[0];
				String memberid=args[1];
				EtlBean prmapping=EtlConfig.getInstance().getEtlBean("uplevelmemberid");//插入到临时表。 等待规则
		 		SolarEtlExecutor.runetl(prmapping,new String[]{"-member_id",memberid},1);
			}
			
			 //upislevel 
		}
	}

}
