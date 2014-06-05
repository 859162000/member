package com.talkweb.wanda.det.sms;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.wanda.mms.control.stream.dao.LogModuleDao;
import com.wanda.mms.control.stream.dao.MsgToSendDao;
import com.wanda.mms.control.stream.dao.SendTaskDao;
import com.wanda.mms.control.stream.dao.impl.LogModuleDaoImpl;
import com.wanda.mms.control.stream.dao.impl.MsgToSendDaoImpl;
import com.wanda.mms.control.stream.dao.impl.SendTaskDaoImpl;
import com.wanda.mms.control.stream.util.DateUtil;
import com.wanda.mms.control.stream.vo.LogModule;
import com.wanda.mms.control.stream.vo.MsgToSend;
import com.wanda.mms.control.stream.vo.Send;
import com.wanda.mms.control.stream.vo.SendTask;
 
 

// 消息发送器
public class MbrSMSReport {
	static Logger logger = Logger.getLogger(MbrSMSReport.class.getName());

 
 
 
 
	public void mbrDayReportCinema( String datetimes){
		LogModuleDao logdao = new LogModuleDaoImpl();
		LogModule lm = new LogModule();
		logger.info("Entering msgSendtaskMonthReport(" + datetimes + ").");
 		String sendtype = "MsgSender.msgSendtaskMonthReport()";
 	//	Long ldate = Long.valueOf(datetimes);
 		MsgToSend mt = null;
 		 
 		int retrytimes = 3;// 得到最大次数改成 发送状态 1成功，0不成功
		List<SendTask> stlist =  null;// 得到需要发送的任务
		//缺查询任务
		MsgToSendDao mtdao = new MsgToSendDaoImpl();
		
		String[] sp =datetimes.split("-");
		String da = sp[0]+sp[1]+sp[2];
		System.out.println(da);
		long dataymd = Long.valueOf(da); 
		String type ="3";
	    List<MsgToSend> mtlist = 	mtdao.findMsgToSendByType(dataymd, type);
	    SendTaskDao stdao = new SendTaskDaoImpl();
	    MsgToSend msg = new MsgToSend();
	    for (int i = 0; i < mtlist.size(); i++) {
	    	 MsgToSend	mts =mtlist.get(i);
	    	 stlist =stdao.fandMsgCode(mts.getMsgId());	 
	  	   	List<Send> sendlist = listTargetToString(stlist);// 把任务列表中的数据转换为每百个电话号码一个String串的数据
	  		
	  		if(stlist.size()!=0){
	  			System.out.println("AAA:"+mts.getContent());
	  		sendSMS(stlist, sendlist, retrytimes, mts.getContent(), sendtype);// 调用发送短信
	  		
	  		Date dd = new Date();

			lm.setDataYMD(Long.valueOf(DateUtil.getDateStr(dd)));
			lm.setModuleID(4);
			lm.setStartTime(DateUtil.getDateStrss(dd));
			lm.setEndTime(DateUtil.getDateStrss(dd));
			lm.setStatus(0);// if判断是否成功
			lm.setDescription("发送会员短信日报"+datetimes+"需要发送消息模块调用 信息发送内容ID为 " + mts.getMsgId());

			logdao.addLogModuleDao(lm);
	  		}
			type="4"; 
			mts.setMsmflag(type);
			mtdao.upMsgToSend(mts);
	}
	   
	    
	   
	    
	  	 
			 

			

		 

	}
	//给院线发的方法
	public void mbrDayReport( String datetimes) {
		LogModuleDao logdao = new LogModuleDaoImpl();
		LogModule lm = new LogModule();
		logger.info("Entering msgSendtaskMonthReport(" + datetimes + ").");
 		String sendtype = "MsgSender.msgSendtaskMonthReport()";
 	//	Long ldate = Long.valueOf(datetimes);
 		MsgToSend mt = null;
 		 
 		int retrytimes = 3;// 得到最大次数改成 发送状态 1成功，0不成功
		List<SendTask> stlist =  null;// 得到需要发送的任务
		//缺查询任务
		MsgToSendDao mtdao = new MsgToSendDaoImpl();
		
		String[] sp =datetimes.split("-");
		String da = sp[0]+sp[1]+sp[2];
		System.out.println(da);
		long dataymd = Long.valueOf(da); 
		String type ="1";
	    List<MsgToSend> mtlist = 	mtdao.findMsgToSendByType(dataymd, type);
	    MsgToSend msg = new MsgToSend();
	    for (int i = 0; i < mtlist.size(); i++) {
	    	 MsgToSend	mts =mtlist.get(i);
	    	 msg.setMsgId(mts.getMsgId());
	    	 msg.setSendTypeId(mts.getSendTypeId());
	    	 msg.setDataYMD(mts.getDataYMD());
	    	 msg.setContent(mts.getContent());
	}
	   
	    
	    SendTaskDao stdao = new SendTaskDaoImpl();
	    
	    stlist =stdao.fandMsgCode(msg.getMsgId());
	    
			 
		List<Send> sendlist = listTargetToString(stlist);// 把任务列表中的数据转换为每百个电话号码一个String串的数据
		System.out.println("AAA:"+msg.getContent());
		sendSMS(stlist, sendlist, retrytimes, msg.getContent(), sendtype);// 调用发送短信
		type="2"; 
		msg.setMsmflag(type);
		mtdao.upMsgToSend(msg);
			Date dd = new Date();

			lm.setDataYMD(Long.valueOf(DateUtil.getDateStr(dd)));
			lm.setModuleID(4);
			lm.setStartTime(DateUtil.getDateStrss(dd));
			lm.setEndTime(DateUtil.getDateStrss(dd));
			lm.setStatus(0);// if判断是否成功
			lm.setDescription("发送会员短信日报"+datetimes+"需要发送消息模块调用 信息发送内容ID为 " + msg.getMsgId());

			logdao.addLogModuleDao(lm);

		 

	}

	/**
	 * 把任务列表中的电话号分成每百条一个的RTX串中并添加到List中返回
	 * 
	 * @param s
	 * @return
	 */
	public List<Send> listTargettoRtx(List<SendTask> s) {

		StringBuffer sst = new StringBuffer();

		List<Send> targetlist = new ArrayList<Send>();
		int j = s.size() - 1;
		int k = 99;
		for (int i = 0; i < s.size(); i++) {
			if (i == k || i == j) {

				SendTask st = s.get(i);

				sst.append(st.getTargetRTX());

				k += 100;
				String sTarget = sst.toString();
				Send sd = new Send();
				sd.setTarget(sTarget);
				targetlist.add(sd);
				int sb_length = sst.length();// 取得字符串的长度
				sst.delete(0, sb_length); // 删除字符串从0~sb_length-1处的内容
											// (这个方法就是用来清除StringBuffer中的内容的)

			} else {
				SendTask st = s.get(i);
				// 号码用逗号分割
				sst.append(st.getTargetRTX()).append(",");

			}
		}
		return targetlist;

	}

	/**
	 * 把任务列表中的电话号分成每百条一个String串中并添加到List中返回
	 * 
	 * @param s
	 * @return
	 */
	public List<Send> listTargetToString(List<SendTask> s) {
		StringBuffer sst = new StringBuffer();

		List<Send> targetlist = new ArrayList<Send>();
		int j = s.size() - 1;
		int k = 99;
		logger.debug(s.size() + "=s.size()");
		for (int i = 0; i < s.size(); i++) {
			if (i == k || i == j) {
				logger.debug(i + "i" + k + "=k" + j + "=j");
				SendTask st = s.get(i);

				sst.append(st.getTargetMobile());

				k += 100;
				String sTarget = sst.toString();
				Send sd = new Send();
				sd.setTarget(sTarget);
				targetlist.add(sd);
				int sb_length = sst.length();// 取得字符串的长度
				sst.delete(0, sb_length); // 删除字符串从0~sb_length-1处的内容
											// (这个方法就是用来清除StringBuffer中的内容的)
				//
				// 还有另外一种方式
				// my_StringBuffer.setLength(0); //设置StringBuffer变量的长度为0
			} else {

				SendTask st = s.get(i);
				// 号码用逗号分割
				sst.append(st.getTargetMobile()).append(",");

			}

		}

		return targetlist;
	}

 

 
 

 

	 
	 

	public String SetMMSInfo(String fromSys, String Ttrget, String msContent, String title) {
		String s = "";
		com.wanda.ccs2.wcf.ReceiveServerStub stub;
		try {
			stub = new com.wanda.ccs2.wcf.ReceiveServerStub();

			com.wanda.ccs2.wcf.service.SetMMSInfo setMMSInfo56 = new com.wanda.ccs2.wcf.service.SetMMSInfo();
			setMMSInfo56.setMsTitle(title);
			setMMSInfo56.setFromSys(fromSys);
			setMMSInfo56.setTarget(Ttrget);
			setMMSInfo56.setMsContent(msContent);

			com.wanda.ccs2.wcf.service.SetMMSInfoResponse r = stub.setMMSInfo(setMMSInfo56);
			s = r.getSetMMSInfoResult();
			logger.info("MMS,Target=" + Ttrget+"|content="+msContent);

		} catch (AxisFault e) {
			e.printStackTrace();
		}// the default implementation should point to the right endpoint
		catch (RemoteException e) {
			e.printStackTrace();
		}

		return s;
	}

	public String SetSMSInfo(String fromSys, String Ttrget, String msContent) {
		String s = "";
		try {
			com.wanda.ccs2.wcf.ReceiveServerStub stub = new com.wanda.ccs2.wcf.ReceiveServerStub();// the
																									// default
																									// implementation
																									// should
																									// point
																									// to
																									// the
																									// right
																									// endpoint

			com.wanda.ccs2.wcf.service.SetSMSInfo setSMSInfo58 = new com.wanda.ccs2.wcf.service.SetSMSInfo();
			setSMSInfo58.setFromSys(fromSys);
			setSMSInfo58.setTarget(Ttrget);
			setSMSInfo58.setMsContent(msContent);
			com.wanda.ccs2.wcf.service.SetSMSInfoResponse r = stub.setSMSInfo(setSMSInfo58);
			logger.info("SMS,Target=" + Ttrget + "|content=" + msContent);
			s = r.getSetSMSInfoResult();
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// the default implementation should point to the right endpoint

		return s;
	}

	public String SetRTXInfo(String fromSys, String target, String msContent) {
		String s = "";
		try {
			com.wanda.ccs2.wcf.ReceiveServerStub stub = new com.wanda.ccs2.wcf.ReceiveServerStub();// the
																									// default
																									// implementation
																									// should
																									// point
																									// to
																									// the
																									// right
																									// endpoint

			com.wanda.ccs2.wcf.service.SetRTXInfo setRTXInfo50 = new com.wanda.ccs2.wcf.service.SetRTXInfo();
			setRTXInfo50.setFromSys(fromSys);
			setRTXInfo50.setTarget(target);// TRX号
			setRTXInfo50.setMsContent(msContent);// 内容

			com.wanda.ccs2.wcf.service.SetRTXInfoResponse r = stub.setRTXInfo(setRTXInfo50);
			logger.info("SMS,Target=" + target + "|content=" + msContent);
			s = r.getSetRTXInfoResult();
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return s;
	}

	/***
	 * 发送彩信方法
	 * 
	 * @param stlist
	 * @param sendlist
	 * @param retrytimes
	 * @param content
	 */
	public void sendMMS(List<SendTask> stlist, List<Send> sendlist,
			int retrytimes, String content, String sendtype, String title) { }

	/***
	 * 发送RTX方法
	 * 
	 * @param stlist
	 * @param sendlist
	 * @param retrytimes
	 * @param content
	 */
	public void sendRTX(List<SendTask> stlist, List<Send> sendlist,
			int retrytimes, String content, String sendtype) { }

	/***
	 * 发送短信方法
	 * 
	 * @param stlist
	 * @param sendlist
	 * @param retrytimes
	 * @param content
	 */
	public void sendSMS(List<SendTask> stlist, List<Send> sendlist,
			int retrytimes, String content, String sendtype) {
		LogModuleDao logdao = new LogModuleDaoImpl();
		LogModule lm = new LogModule();
		int q = 0;
		String ss = "";
		for (int i = 0; i < sendlist.size(); i++) {

			Send ssend = new Send();
			ssend = sendlist.get(i);
			String fromSys, Ttrget, msContent;
			fromSys = "YXZBXT001";// 不知道从那得到先写死
			Ttrget = ssend.getTarget();
			msContent = content;
			logger.info("Target:"+Ttrget);
			ss = SetSMSInfo(fromSys, Ttrget, msContent);// 调用短信功能
			logger.info("SendResult="+ss);
			System.out.println("SendResult="+ss);
			if ("OK".equals(ss)) {
				List<SendTask> stlists = new ArrayList<SendTask>();
				for (int j = 0; j < stlist.size(); j++) {
					SendTask tsk = stlist.get(j);
					q = 1;
					tsk.setRetryTimes(q);
					tsk.setStatus(1);
					int k = 1;
					tsk.setStatus(k);// 发送成功
					stlists.add(tsk);
				}
				Date dd = new Date();
				
				lm.setDataYMD(Long.valueOf(DateUtil.getDateStr(dd)));
				lm.setModuleID(4);
				lm.setStartTime(DateUtil.getDateStrss(dd));
				lm.setEndTime(DateUtil.getDateStrss(dd));
				lm.setStatus(1);// if判断是否成功
				lm.setDescription("发送成功 " + sendtype + "短信返回内容是" + ss);

				logdao.addLogModuleDao(lm);


	

			} else {

				for (int j = 1; j < retrytimes; j++) {
					q++;
					if (ss.equals("OK")) {
						// logger.info("第" + q + "次发送成功");
						List<SendTask> stlists = new ArrayList<SendTask>();
						for (int k = 0; k < stlist.size(); k++) {
							SendTask tsk = stlist.get(k);
							tsk.setRetryTimes(q);

							tsk.setStatus(1);// 发送成功
							stlists.add(tsk);
						}
						
						updateSendTask(stlists);// 更新重复次数
						Date dd = new Date();
						lm.setDataYMD(Long.valueOf(DateUtil.getDateStr(dd)));
						lm.setModuleID(4);
						lm.setStartTime(DateUtil.getDateStrss(dd));
						lm.setEndTime(DateUtil.getDateStrss(dd));
						lm.setStatus(2);// if判断是否成功
						lm.setDescription("发送成功 " + sendtype + "短信返回内容是" + ss);

						logdao.addLogModuleDao(lm);
						break;// 如果成功结束本次循环
					} else {
						// 先修改在执行发送方法
						List<SendTask> stlists = new ArrayList<SendTask>();
						for (int k = 0; k < stlist.size(); k++) {
							SendTask tsk = stlist.get(k);
							tsk.setRetryTimes(q);
							tsk.setStatus(3);// 3-失败重试
							stlists.add(tsk);
						}
						updateSendTask(stlists);// 更新重复次数

						ss = SetSMSInfo(fromSys, Ttrget, msContent);//
						// 调用短信功能

						Date dd = new Date();

						lm.setDataYMD(Long.valueOf(DateUtil.getDateStr(dd)));
						lm.setModuleID(4);
						lm.setStartTime(DateUtil.getDateStrss(dd));
						lm.setEndTime(DateUtil.getDateStrss(dd));
						lm.setStatus(3);// if判断是否成功
						lm.setDescription("本次短信发送失败 " + sendtype + "短信功能返回" + ss);

						logdao.addLogModuleDao(lm);
						 
					}
				}
			}
		}

		 
	}

	public List<SendTask> sendTaskLead(List<SendTask> stlist) {
		List<SendTask> stk = new ArrayList<SendTask>();

		for (int i = 0; i < stlist.size(); i++) {
			SendTask st = stlist.get(i);
			if (st.getPriority() == 5) {
				stk.add(st);
			}
		}

		return stk;
	}

	public void updateSendTask(List<SendTask> sendtasklist) {
		SendTaskDao std = new SendTaskDaoImpl();
		//List<SendTask> list = new ArrayList<SendTask>();
		Date date = new Date();
		String ssdate = DateUtil.getDateStrss(date);
		String stding = std.updateListSendTask(sendtasklist, ssdate);
		logger.debug("updateSendTask:"+stding);

	}

}
