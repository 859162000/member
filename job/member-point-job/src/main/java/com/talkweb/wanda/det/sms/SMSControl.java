package com.talkweb.wanda.det.sms;

import java.util.Date;

import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.Sms;






 
public class SMSControl{
	
	public void smssend(String mobileNo,String mo){
		AbstractSMSControl asm= new AbstractSMSControl();
		AbstractSMSControl.SendMMSThread smm = asm.new SendMMSThread();
		Sms sms = new Sms();
		int i = 0;
		asm.init();
		sms.setMobileNo(mobileNo);// 
		sms.setMo(mo);
		Date date = new Date();
		  
		sms.setMoTime(date);
		sms.setMtTime(date);
//		String ip ="10.0.8.55";
		String ip ="10.199.201.103";
		int port = 20003;
		
		smm.sendSms(sms,i,ip,port);
		
		
	} 
	
	public static void main(String[] args) {
	 
		AbstractSMSControl asm= new AbstractSMSControl();
		AbstractSMSControl.SendMMSThread smm = asm.new SendMMSThread();
		Sms sms = new Sms();
		int i = 0;
		asm.init();
		sms.setMobileNo("18801276787");//
		sms.setMo("wangshuai");
		Date date = new Date();
		String time = DateUtil.getDateStrss(date);
		sms.setMoTime(date);
		sms.setMtTime(date);
//		String ip ="10.0.8.55";
		String ip ="10.199.201.103";
		int port = 8038;

		
		smm.sendSms(sms,i,ip,port);
		
		System.out.println("456");
		
		
	}
	
}
