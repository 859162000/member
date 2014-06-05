package com.wanda.mms.control.main;

import java.util.Calendar;

import com.talkweb.wanda.det.sms.MbrSMSReport;
import com.wanda.mms.control.stream.db.STAGDBConnection;
import com.wanda.mms.control.stream.service.MbrSMSDay;

public class TestSms {
	
	public static void main(String[] args) {
		
		if(args==null||args.length==0){	
		Calendar cal=Calendar.getInstance();
		int x=-4;//or x=-3;
		cal.add(Calendar.DAY_OF_MONTH,x);
		String bzda=new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		System.out.println("Bizdate:"+bzda);
		CinemaSMSDayMain sdm = new CinemaSMSDayMain();
	//	int flag1 = sdm.fandstatus(bzda);
			//if (flag1==1) {
			//Cinema
			STAGDBConnection db=STAGDBConnection.getInstance();	
			
			MbrSMSDay mbr = new MbrSMSDay();
			
 			mbr.daySave(db.getConnection(), bzda);
			
			MbrSMSReport report = new MbrSMSReport();
			report.mbrDayReportCinema(bzda);
			 
			//改变状态表中的状态短信发送完毕
		//	}
		} 
		
		//日志与状态。
	}
}
