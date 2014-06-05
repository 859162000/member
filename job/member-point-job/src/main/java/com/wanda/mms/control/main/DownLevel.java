package com.wanda.mms.control.main;

import java.util.Date;


import com.wanda.mms.control.stream.util.DateUtil;

public class DownLevel {
	
	public static void main(String[] args) {
		
		//如果无参 执行降级任务 判断当前时间不是一月
		
		//如果不是一月份把当年01月到12月低的积分查出来判断是否满足降级条件
		DownLevel down = new DownLevel();//创建降级调度
		Date da = new Date();//创建时间
		String datetime = DateUtil.getDateStrss(da);//时间可能是 时分秒
		String date =DateUtil.getDateStr(da);
		String yy = (String) date.subSequence(0, 4);
		String mm = date.substring(4, 6);
		int intyy = Integer.valueOf(yy);
		 intyy = intyy-1;
		String yyyy = String.valueOf(intyy);
		String te = "-12-31 23:59:59";
		String im = "-01-01 00:00:00";
		String timeend =yyyy+te; 
		String time =yyyy+im; 
		
		String yydateend=yy+te;
		String yydate=yy+im;
		//System.out.println(time+"downlevels"+date);
		if(mm.equals("01")){//调度时间如果过了有可能会把明年的用户全都降一级，那可就悲剧了。所以加个判断
			System.out.println(time);
			System.out.println(timeend);//根据去年的时间执行降级方法
			
		}else{
			System.out.println(yydate);
			System.out.println(yydateend);//查询今年的时间执行降级方法
		}
		
		
		
		
	}

}
