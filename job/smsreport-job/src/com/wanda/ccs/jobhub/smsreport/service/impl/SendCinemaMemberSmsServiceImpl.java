package com.wanda.ccs.jobhub.smsreport.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.smsreport.CallBack;
import com.wanda.ccs.jobhub.smsreport.Contact;
import com.wanda.ccs.jobhub.smsreport.SmsSqlCont;
import com.wanda.ccs.jobhub.smsreport.Validate;
import com.wanda.ccs.jobhub.smsreport.dao.MemberCinemaSmsDao;
import com.wanda.ccs.jobhub.smsreport.dao.SmsCommonDao;
import com.wanda.ccs.jobhub.smsreport.service.SendCinemaMemberSmsService;
import com.wanda.ccs.jobhub.smsreport.vo.SMSData;
import com.wanda.ccs.jobhub.smsreport.vo.SendSms;
import com.wanda.ccs.jobhub.smsreport.vo.Target;

/**
 * 发送短信服务
 * 
 * @author zhurui
 * @date 2014年1月7日 下午4:20:35
 */
@Transactional
public class SendCinemaMemberSmsServiceImpl extends Validate implements SendCinemaMemberSmsService {
	
	@InstanceIn(path = "MemberCinemaSmsDao")
	public MemberCinemaSmsDao memberCinemaSmsDao;
	
	@InstanceIn(path = "SmsCommonDao")
	public SmsCommonDao smsCommonDao;
	
	/*public static void main(String[] args) {
		com.mchange.v2.c3p0.ComboPooledDataSource dataSource = null;
		try {
			dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
			dataSource.setUser("ccs_mbr_prod");
			dataSource.setPassword("ccs_mbr_prod");
			dataSource.setJdbcUrl("jdbc:oracle:thin:@10.199.201.105:1521:ccsstag");

			MemberCinemaSmsDao dao = new MemberCinemaSmsDao();
			dao.dataSource = dataSource;
			dao.dataSourceStatus= dataSource;
			
			CommonDao comDao = new CommonDao();
			comDao.dataSource = dataSource;
			
			SendCinemaMemberSmsServiceImpl service = new SendCinemaMemberSmsServiceImpl();
			service.commonDao = comDao;
			service.memberCinemaSmsDao = dao;
			
			service.cinemaSms("2013-08-18");
		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dataSource.close();
		}
	}*/
	
	@Transactional
	public void sendCinemaSms(String date) throws Exception {
		String[] time = date.split("-");
		int yyyyMMdd = Integer.parseInt(time[0]+time[1]+time[2]);
		//System.out.println("开始发送影城短信");
		try {
			/*if(Contact.VALID_FALG) {
				// 检测数据验证是否有错误发生
				buildCinemaVaildSend(date);
			}*/
			
			// 判断短信是否生成
			int count = smsCommonDao.querySmsCount(yyyyMMdd, true);
			if(count == 0) {
				buildCinemaSMS(date);
				buildCinemaRelation(yyyyMMdd);
			} else {	//会员短信已生成
				count = smsCommonDao.querySmsTargetCount(yyyyMMdd, true);
				if(count == 0) {
					buildCinemaRelation(yyyyMMdd);
				}
				
				/*if(Contact.VALID_FALG && smsCommonDao.queryValidStatus(yyyyMMdd, true)) {	// 院线会员手机报数据校验是否有错误
					Cache.addValue(date, true);	//缓存数据可能丢失重新建立
					buildCinemaVaildSend(date);
					throw new Exception("院线数据校验出错，请及时修正并进行手动发送。");
				}*/
			}
			
			/*if(Contact.VALID_FALG) {
				System.out.println("开始校验院线数据");
				buildCinemaValid(date, yyyyMMdd);
			}
			
			System.out.println("影城手机报统计完成，开始发送");
			buildCinemaSend(yyyyMMdd);// 发送短信
			updateJobStatusForSuccess(yyyyMMdd);*/
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 构建院线短信
	 * 
	 * @param yyyyMMdd
	 * @return
	 */
	private void buildCinemaSMS(String date) {
		String[] time = date.split("-");
		int yyyyMMdd = Integer.parseInt(time[0]+time[1]+time[2]);
		
		Map<String, SMSData> cienmaSms = memberCinemaSmsDao.queryCinemaSMS(date);
		List<SMSData> list = new ArrayList<SMSData>();
		for(Map.Entry<String, SMSData> entry : cienmaSms.entrySet()) {
			list.add(entry.getValue());
		}
		smsCommonDao.insertSMS(yyyyMMdd, list);
	}
	
	public boolean checkStatus(int yyyyMMdd, String param) {
		return memberCinemaSmsDao.checkJobStatus(yyyyMMdd, param);
	}
	
	/**
	 * 构建用户短信关系
	 * 
	 * @param smsId
	 * @throws Exception 
	 */
	private void buildCinemaRelation(int yyyyMMdd) throws Exception {
		Map<String, SMSData> cienmaSms = smsCommonDao.querySms(yyyyMMdd, true);
		Map<String, List<Target>> targetMap = smsCommonDao.queryTargetList("1", true);
		if(targetMap.size() == 0) {
			throw new Exception("查询到发送人员列表为空，请确认人员信息表中是否有数据");
		}
		memberCinemaSmsDao.insertTargetSmsRelationForCinema(cienmaSms, targetMap);
	}
	
	/**
	 * 构建发送任务
	 * 
	 * @param yyyyMMdd
	 * @throws Exception
	 */
	@Transactional
	public void buildCinemaSend(int yyyyMMdd) throws Exception {
		SendSms sendSms = smsCommonDao.querySendTarget(SmsSqlCont.QUERY_WAIT_SEND_SMS_CINEMA_SQL, yyyyMMdd, new Object[] {1, 3});
		if(smsCommonDao.sendSMS(sendSms)) {	// 调用短信接口出错
			throw new Exception("Send Cinema SMS Error!");
		}
		updateJobStatusForSuccess(yyyyMMdd);
	}
	
	/*public void addNewStatus(int yyyyMMdd) {
		smsCommonDao.insertStatus(yyyyMMdd);
	}*/
	
	public void updateJobStatusForSuccess(int yyyyMMdd) {
		memberCinemaSmsDao.updateJobStatus(yyyyMMdd, 1);
	}
	
	/**
	 * 院线短信数据校验
	 * 
	 * @param date
	 * @param yyyyMMdd
	 * @throws Exception 
	 */
	public void buildCinemaValid(String date, int yyyyMMdd) throws Exception {
		// 准备校验数据
		SendSms sendSms = smsCommonDao.querySendTarget(SmsSqlCont.QUERY_WAIT_SEND_SMS_CINEMA_SQL, yyyyMMdd, new Object[] {1, 3});
		Map<String, SMSData> valid = memberCinemaSmsDao.queryCinemaSMSValidateData(date);
		Map<String, SMSData> mapSms = sendSms.getSendSmsList();
		
		// 开始校验数据
		this.validate(date, mapSms, valid, true);	//校验院线短信数据
		
		// 更新校验情况
		List<SMSData> list = new ArrayList<SMSData>();
		for(Map.Entry<String, SMSData> entry : mapSms.entrySet()) {
			list.add(entry.getValue());
		}
		smsCommonDao.updateSMSValidStatus(list);
	}
	
	/**
	 * 院线发送校验短信
	 * 
	 * @param date
	 * @throws Exception
	 */
	public void buildCinemaVaildSend(final String date) throws Exception {
		sendValidSMS(date, true, new CallBack() {
			
			@Override
			public void execute() throws Exception {
				try {
					Map<String, List<Target>> map = smsCommonDao.queryTargetList("2", false);
					List<Target> list = map.get(null);
					if(list != null && list.size() > 0) {
						StringBuilder target = new StringBuilder();
						for(int i=0,len=list.size();i<len;i++) {
							target.append(list.get(i).getMobile()).append(",");
						}
						smsCommonDao.SetSMSInfo(Contact.FROM_SYS, target.deleteCharAt(target.length() - 1).toString(), Contact.VAILD_SMS.replace("{1}", date+"影城"));
					} else {
						throw new Exception("未找到会员手机报数据校验失败维护人员");
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		});
	}

	@Override
	public void buildValid(String date, int yyyyMMdd) throws Exception {
		buildCinemaValid(date, yyyyMMdd);
		buildCinemaVaildSend(date);
	}
	
}
