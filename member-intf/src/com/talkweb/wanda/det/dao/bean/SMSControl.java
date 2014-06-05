package com.talkweb.wanda.det.dao.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

import com.wanda.mrb.intf.SQLConstDef;
import com.wanda.mrb.intf.exception.BusinessException;
import com.wanda.mrb.intf.utils.ResultQuery;
import com.wanda.mrb.intf.utils.SqlHelp;
 
public class SMSControl{
	private static AbstractSMSControl asm = new AbstractSMSControl();
	//private static AbstractSMSControl.SendMMSThread smm = asm.new SendMMSThread();
	


	public void smssend(Connection conn,String mobileNo,String mt){
//		AbstractSMSControl asm= new AbstractSMSControl();
//		AbstractSMSControl.SendMMSThread smm = asm.new SendMMSThread();
		AbstractSMSControl.SendMMSThread smm=SmsThreadFactory.getSmsThreadInstance(asm);
		Sms sms = new Sms();
		int i = 0;
		asm.init();
		sms.setMobileNo(mobileNo);//
		sms.setMt(mt);
		Date date = new Date();
	
		sms.setSubmitTime(date);
//		sms.setSmsId(BasePK.getPK());
		String ip = "";
		int port = 0;
		
		try {
			//查询短信代理ip
			ResultQuery rsq=SqlHelp.query(conn, SQLConstDef.SELECT_MEMBER_MSG_IP);
			ResultSet rs=rsq.getResultSet();
			if(rs != null && rs.next()){
				ip = rs.getString("parameter_value"); 
			}else{
				throw new BusinessException("M130002", "短信代理IP错误！");
			}
			rsq.free();
			
			//查询短信代理port
			rsq=SqlHelp.query(conn, SQLConstDef.SELECT_MEMBER_MSG_PORT);
			rs=rsq.getResultSet();
			if(rs != null && rs.next()){
				port = rs.getInt("parameter_value"); 
			}else{
				throw new BusinessException("M130003", "短信代理端口错误！");
			}
			rsq.free();
		} catch (Exception e) {
		}
//		String ip ="10.0.8.55";
//		String ip ="10.199.201.103";
//		int port = 8038;
//		int port = 20003;
		
		smm.sendSms(sms,i,ip,port);		
	} 
	
	public static void main(String[] args) {
	 
//		AbstractSMSControl asm= new AbstractSMSControl();
//		AbstractSMSControl.SendMMSThread smm = asm.new SendMMSThread();
		
		AbstractSMSControl.SendMMSThread smm=SmsThreadFactory.getSmsThreadInstance(asm);
		AbstractSMSControl.SendMMSThread smm2=SmsThreadFactory.getSmsThreadInstance(asm);
		Sms sms = new Sms();
		int i = 0;
		asm.init();
		sms.setMobileNo("18601915759");//
		sms.setMt("wangshuai");
		Date date = new Date();
	
		sms.setSubmitTime(date);
		sms.setSmsId(BasePK.getPK());
		
//		String ip ="10.0.8.55";
		String ip ="10.199.201.103";
		int port = 8038;
//		int port = 20003;
		
		smm.sendSms(sms,i,ip,port);
		
		System.out.println("456");
		
		
	}
	
}
