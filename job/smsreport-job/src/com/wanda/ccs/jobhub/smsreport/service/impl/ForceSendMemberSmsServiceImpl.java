package com.wanda.ccs.jobhub.smsreport.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.smsreport.SmsSqlCont;
import com.wanda.ccs.jobhub.smsreport.dao.SmsCommonDao;
import com.wanda.ccs.jobhub.smsreport.dao.MemberCinemaSmsDao;
import com.wanda.ccs.jobhub.smsreport.dao.MemberLineSmsDao;
import com.wanda.ccs.jobhub.smsreport.service.ForceSendMemberSmsService;
import com.wanda.ccs.jobhub.smsreport.service.SendCinemaMemberSmsService;
import com.wanda.ccs.jobhub.smsreport.service.SendLineMemberSmsService;
import com.wanda.ccs.jobhub.smsreport.vo.SMSData;
import com.wanda.ccs.jobhub.smsreport.vo.SendSms;
import com.wanda.ccs.jobhub.smsreport.vo.Target;

import freemarker.template.TemplateException;

/**
 * 强迫发送短信服务
 * 
 * @author zhurui
 * @date 2014年1月7日 下午4:20:35
 */
@Transactional
public class ForceSendMemberSmsServiceImpl implements ForceSendMemberSmsService {
	
	public static final Logger logger = Logger.getLogger(ForceSendMemberSmsServiceImpl.class);
	
	@InstanceIn(path = "MemberLineSmsDao")
	private MemberLineSmsDao memberLineSmsDao;
	
	@InstanceIn(path = "MemberCinemaSmsDao")
	private MemberCinemaSmsDao memberCinemaSmsDao;
	
	@InstanceIn(path = "SmsCommonDao")
	private SmsCommonDao smsCommonDao;
	
	@InstanceIn(path = "SendCinemaMemberSmsService")
	SendCinemaMemberSmsService sendCinemaMemberSmsService;
	
	@InstanceIn(path = "SendLineMemberSmsService")
	SendLineMemberSmsService sendLineMemberSmsService;
	
	/*public static void main(String[] args) {
		com.mchange.v2.c3p0.ComboPooledDataSource dataSource = null;
		try {
			dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
			dataSource.setUser("ccs_mbr_prod");
			dataSource.setPassword("ccs_mbr_prod");
			dataSource.setJdbcUrl("jdbc:oracle:thin:@10.199.201.105:1521:ccsstag");

			MemberCinemaSmsDao cinemaDao = new MemberCinemaSmsDao();
			cinemaDao.dataSource = dataSource;
			cinemaDao.dataSourceStatus= dataSource;
			
			smsCommonDao comDao = new smsCommonDao();
			comDao.dataSource = dataSource;
			
			MemberLineSmsDao lineDao = new MemberLineSmsDao();
			lineDao.dataSource = dataSource;
			lineDao.dataSourceStatus= dataSource;
			
			ForceSendMemberSmsServiceImpl service = new ForceSendMemberSmsServiceImpl();
			service.smsCommonDao = comDao;
			service.memberCinemaSmsDao = cinemaDao;
			service.memberLineSmsDao = lineDao;
			
			service.forceSend("2013-08-17", "cinema", 2);
		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dataSource.close();
		}
	}*/
	
	/**
	 * 手动发送短信
	 * 
	 * @param date
	 * @param type				line 院线		cinema 影城
	 * @param sendType			1 重新统计发送 	2只是发送不统计 
	 * @param valid				true校验数据	false不校验数据
	 * @throws Exception 
	 * @throws SystemException
	 */
	@Transactional
	public void forceSend(String date, String type, int sendType, boolean valid) throws Exception {
		String[] time = date.split("-");
		int yyyyMMdd = Integer.parseInt(time[0]+time[1]+time[2]);
		
		try {
			if(1 == sendType) {	// 重新统计数据后重新发送短信
				updateSms(type, date, yyyyMMdd, valid);
			} else if(2 == sendType) {	// 不统计重新发送短信
				sendSms(type, yyyyMMdd);
			} else {
				throw new Exception("手动操作类型错误");
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void updateSms(String type, String date, int yyyyMMdd, boolean valid) throws Exception {
		if("line".equals(type)) {
			/*if(!sendLineMemberSmsService.checkStatus(yyyyMMdd, "")) {
				throw new Exception("统计院线手机报数据失败，尚未满足条件");
			}*/
			
			int count = smsCommonDao.querySmsCount(yyyyMMdd, false);
			if(count > 0) {
				SMSData newSms = memberLineSmsDao.queryLineSMS(date);	//重新统计会员手机报
				
				Map<String, SMSData> smsMap = smsCommonDao.querySms(yyyyMMdd, false);
				Iterator<Entry<String, SMSData>> set = smsMap.entrySet().iterator();
				if(set.hasNext()) {
					SMSData oldSms = set.next().getValue();
					newSms.setSeqid(oldSms.getSeqid());
					newSms.setCreateTime(new Timestamp(System.currentTimeMillis()));
				}
				smsCommonDao.updateSMS(yyyyMMdd, Arrays.asList(newSms));
				if(valid) {
					// 校验数据
					sendLineMemberSmsService.buildValid(date, yyyyMMdd);
				}
			} else {
				sendLineMemberSmsService.sendLineSms(date);
			}
			sendSms(type, yyyyMMdd);
		} else if("cinema".equals(type)) {
			/*if(!sendCinemaMemberSmsService.checkStatus(yyyyMMdd, "")) {
				throw new Exception("统计影城手机报数据失败，尚未满足条件");
			}*/
			
			int count = smsCommonDao.querySmsCount(yyyyMMdd, true);
			if(count > 0) {
				// 重新统计会员手机报
				List<SMSData> updateList = new ArrayList<SMSData>();
				Map<String, SMSData> newMap = memberCinemaSmsDao.queryCinemaSMS(date);
				Map<String, SMSData> oldMap = smsCommonDao.querySms(yyyyMMdd, true);
				
				// 更新旧的数据
				SMSData sms = null;
				for(Map.Entry<String, SMSData> entry : newMap.entrySet()) {
					sms = newMap.get(entry.getKey());
					entry.getValue().setSeqid(oldMap.get(entry.getKey()).getSeqid());
					entry.getValue().setCreateTime(new Timestamp(System.currentTimeMillis()));
					updateList.add(entry.getValue());	//添加到更新列表中
					sms = null;
				}
				
				for(int i=0,len=updateList.size();i<len;i++) {
					newMap.remove(updateList.get(i).getCinemaInnerCode());	//移除要更新的影城
				}
				
				if(newMap.size() > 0) {	// 统计出来了新的影城数据
					List<SMSData> insertList = new ArrayList<SMSData>();
					for(Map.Entry<String, SMSData> entry : newMap.entrySet()) {
						sms = newMap.get(entry.getKey());
						sms.setSeqid(String.valueOf(smsCommonDao.querySmsId()));
						insertList.add(sms);
					}
					smsCommonDao.insertSMSForSeqId(yyyyMMdd, insertList);	// 添加新的影城统计数据
					// 添加人员关系映射
					Map<String, List<Target>> targetMap = smsCommonDao.queryTargetList("1", true);
					// 添加新影城统计数据与人员关系映射
					memberCinemaSmsDao.insertTargetSmsRelationForCinema(newMap, targetMap);
				}
				smsCommonDao.updateSMS(yyyyMMdd, updateList);
				
				if(valid) {
					sendCinemaMemberSmsService.buildValid(date, yyyyMMdd);
				}
			} else {
				sendCinemaMemberSmsService.sendCinemaSms(date);
			}
			sendSms(type, yyyyMMdd);
		}
	}
	
	public void sendSms(String type, int yyyyMMdd) throws Exception, TemplateException, IOException {
		if("line".equals(type)) {
			int count = smsCommonDao.querySmsTargetCount(yyyyMMdd, false);
			if(count == 0) {
				throw new Exception("发送院线会员手机报失败，请查看T_IM_SENDTASK表中待发送短信是否生成。");
			}
			SendSms sendSms = smsCommonDao.querySendTarget(SmsSqlCont.QUERY_WAIT_SEND_SMS_LINE_SQL, yyyyMMdd, null);
			if(smsCommonDao.sendSMS(sendSms)) {
				throw new Exception("Send Line SMS Error!");
			}
			//memberLineSmsDao.updateJobStatus(yyyyMMdd, 1);
		} else if("cinema".equals(type)) {
			int count = smsCommonDao.querySmsTargetCount(yyyyMMdd, true);
			if(count == 0) {
				throw new Exception("发送影城会员手机报失败，请查看T_IM_SENDTASK表中待发送短信是否生成。");
			}
			SendSms sendSms = smsCommonDao.querySendTarget(SmsSqlCont.QUERY_WAIT_SEND_SMS_CINEMA_SQL, yyyyMMdd, null);
			if(smsCommonDao.sendSMS(sendSms)) {
				throw new Exception("Send Cinema SMS Error!");
			}
			//memberCinemaSmsDao.updateJobStatus(yyyyMMdd, 1);
		}
	}
	
}
