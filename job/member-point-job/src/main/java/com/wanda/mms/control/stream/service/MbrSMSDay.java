package com.wanda.mms.control.stream.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.wanda.mms.control.stream.dao.DayTableDao;
import com.wanda.mms.control.stream.dao.MsgToSendDao;
import com.wanda.mms.control.stream.dao.SendTaskDao;
import com.wanda.mms.control.stream.dao.impl.DayTableDaoImpl;
import com.wanda.mms.control.stream.dao.impl.MsgToSendDaoImpl;
import com.wanda.mms.control.stream.dao.impl.SendTaskDaoImpl;
import com.wanda.mms.control.stream.dao.impl.TargetDaoImpl;
import com.wanda.mms.control.stream.db.STAGDBConnection;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.CinemaString;
import com.wanda.mms.control.stream.vo.DayTable;
import com.wanda.mms.control.stream.vo.MsgToSend;
import com.wanda.mms.control.stream.vo.SendTask;
import com.wanda.mms.control.stream.vo.Target;

public class MbrSMSDay {
	
	public String daySaveContent(Connection con ,String date){
		
		String ss = "";
		//先把短信内容得到
		DayTableService bt = new DayTableService();
		DayTable dt =	bt.fandByDay(con,date);
		String content = bt.dayTabletoString(dt, date);//得到短信内容
		
		String[] sp =date.split("-");
		String da = sp[0]+sp[1]+sp[2];
		System.out.println(da);
		
		//成到信息发送内容表的序列
		MsgToSend mts = new MsgToSend();
		
		MsgToSendDao mtsdao = new MsgToSendDaoImpl();
		
		long seq = mtsdao.findMsgSEQ();
		System.out.println("SEQ:"+seq);
		long dataymd = Long.valueOf(da); 
		mts.setSendTypeId(1);
		mts.setMsgId(seq);
		mts.setContent(content);
		mts.setDataYMD(dataymd);
		String msmflag ="1";
		mts.setMsmflag(msmflag);
		
		//把短信内容存到 信息发送表中
		mtsdao.addMsgToSend(mts);
		//查到所要发送人员的手机号
		Date datetime = new Date();
		String time = DateUtil.getDateStrss(datetime);
		TargetDaoImpl tdm= new TargetDaoImpl();
		List<Target> tarlist = tdm.findTargetByIsSystemUser();
		List<SendTask> stlist = new ArrayList<SendTask>();
		for (int i = 0; i < tarlist.size(); i++) {
			//接收人ID
			Target ta = tarlist.get(i);
			SendTask st = new SendTask();
			st.setTargetId(ta.getTargetId());
			st.setMsgId(seq);
			st.setSendTime(time);
			st.setStatus(0);
			st.setRetryTimes(0);
			st.setTargetMobile(ta.getMobile());
			st.setMsgType(1);
			st.setPriority(3);
			stlist.add(st);
		}
		
//		pst.setLong(1, st.setTargetId());
//		pst.setLong(2, st.setMsgId());				
//	 
//		//to_date('" + st.setSendTime() + "','yyyy-mm-dd hh24:mi:ss'))";
//		// System.out.println("插入任务对列方法调用 : 接收人是"+st.getTargetId()+"  信息发送内容ID=" +st.getMsgId());
//		pst.setString(3,st.setSendTime() );
//		pst.setLong(4, st.setStatus());
//		pst.setLong(5, st.setRetryTimes());
//		pst.setString(6, st.setTargetMobile());
//		pst.setLong(7, st.setMsgType());
//		pst.setLong(8, st.setPriority());
		
		//把要发送的人员的手机号与短信内容序列号存到任务表。
		   SendTaskDao send = new SendTaskDaoImpl();
		   send.addSendTask(stlist);
		return ss;
	}
	public String daySave(Connection con ,String date){
		
		String ss = "";
		//先把短信内容得到
		DayTableDao ddao = new DayTableDaoImpl();
		//fandDayInnerCodeList
		STAGDBConnection db=STAGDBConnection.getInstance();	
		List<DayTable> dtlist =ddao.fandDayInnerCodeList(db.getConnection());
		 
		DayTableService bt = new DayTableService();
		List<DayTable> dtlist1 =	bt.fandByDayCinema(con,date,dtlist);
		 
			
		
		List<CinemaString> cslist = bt.dayTabletoCinemaString(dtlist1, date);//得到短信内容
		for (int i = 0; i < cslist.size(); i++) {
			CinemaString cstr= cslist.get(i);
		
		String[] sp =date.split("-");
		String da = sp[0]+sp[1]+sp[2];
		System.out.println(da);
		
		//成到信息发送内容表的序列
		MsgToSend mts = new MsgToSend();
		
		MsgToSendDao mtsdao = new MsgToSendDaoImpl();
		
		long seq = mtsdao.findMsgSEQ();
		System.out.println("SEQ:"+seq);
		long dataymd = Long.valueOf(da); 
		mts.setSendTypeId(1);
		mts.setMsgId(seq);
		mts.setContent(cstr.getContent());
		mts.setDataYMD(dataymd);
		mts.setCinema_inner_code(cstr.getCinema_inner_code());
		mts.setMsmflag("3");
		
		//把短信内容存到 信息发送表中
		mtsdao.addMsgToSend(mts);
		//查到所要发送人员的手机号
		Date datetime = new Date();
		String time = DateUtil.getDateStrss(datetime);
		TargetDaoImpl tdm= new TargetDaoImpl();
		List<Target> tarlist = tdm.findTargetByIsSystem(cstr.getCinema_inner_code());
		List<SendTask> stlist = new ArrayList<SendTask>();
		for (int j = 0; j < tarlist.size(); j++) {
			//接收人ID
			Target ta = tarlist.get(j);
			SendTask st = new SendTask();
			st.setTargetId(ta.getTargetId());
			st.setMsgId(seq);
			st.setSendTime(time);
			st.setStatus(0);
			st.setRetryTimes(0);
			st.setTargetMobile(ta.getMobile());
			st.setMsgType(1);
			st.setPriority(3);
			stlist.add(st);
		}
		
//		pst.setLong(1, st.setTargetId());
//		pst.setLong(2, st.setMsgId());				
//	 
//		//to_date('" + st.setSendTime() + "','yyyy-mm-dd hh24:mi:ss'))";
//		// System.out.println("插入任务对列方法调用 : 接收人是"+st.getTargetId()+"  信息发送内容ID=" +st.getMsgId());
//		pst.setString(3,st.setSendTime() );
//		pst.setLong(4, st.setStatus());
//		pst.setLong(5, st.setRetryTimes());
//		pst.setString(6, st.setTargetMobile());
//		pst.setLong(7, st.setMsgType());
//		pst.setLong(8, st.setPriority());
		
		//把要发送的人员的手机号与短信内容序列号存到任务表。
		   SendTaskDao send = new SendTaskDaoImpl();
		   if(stlist.size()!=0){
		   send.addSendTask(stlist);
		   }
		}
		return ss;
	}
	public static void main(String[] args) {
		Calendar cal=Calendar.getInstance();
		int x=-28;//or x=-3;
		cal.add(Calendar.DAY_OF_MONTH,x);
		String bzda=new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		System.out.println("Bizdate:"+bzda);
		STAGDBConnection db=STAGDBConnection.getInstance();	
		
		MbrSMSDay mbr = new MbrSMSDay();
		
		mbr.daySaveContent(db.getConnection(), bzda);
	}

}
