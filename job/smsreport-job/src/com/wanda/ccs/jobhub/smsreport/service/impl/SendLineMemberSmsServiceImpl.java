package com.wanda.ccs.jobhub.smsreport.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.smsreport.CallBack;
import com.wanda.ccs.jobhub.smsreport.Contact;
import com.wanda.ccs.jobhub.smsreport.SmsSqlCont;
import com.wanda.ccs.jobhub.smsreport.Validate;
import com.wanda.ccs.jobhub.smsreport.dao.MemberLineSmsDao;
import com.wanda.ccs.jobhub.smsreport.dao.SmsCommonDao;
import com.wanda.ccs.jobhub.smsreport.service.SendLineMemberSmsService;
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
public class SendLineMemberSmsServiceImpl extends Validate implements SendLineMemberSmsService {
	
	@InstanceIn(path = "MemberLineSmsDao")
	public MemberLineSmsDao memberLineSmsDao;
	
	@InstanceIn(path = "SmsCommonDao")
	public SmsCommonDao smsCommonDao;
	
	@Transactional
	public void sendLineSms(String date) throws Exception {
		String[] time = date.split("-");
		int yyyyMMdd = Integer.parseInt(time[0]+time[1]+time[2]);
		//System.out.println("开始执行院线会员手机报");
		//boolean validFlag = Boolean.parseBoolean(PropertiesUtil.getValueByKey("valid"));
		
		try {
			/*if(Contact.VALID_FALG) {
				// 检测数据验证是否有错误发生
				buildLineVaildSend(date);
			}*/
			
			// 判断短信是否生成
			int count = smsCommonDao.querySmsCount(yyyyMMdd, false);
			System.out.println("短信数量"+count);
			if(count == 0) {
				SMSData sms = buildLineSMS(date);
				buildLineRelation(Long.parseLong(sms.getSeqid()));
			} else {	//会员短信已生成
				count = smsCommonDao.querySmsTargetCount(yyyyMMdd, false);
				if(count == 0) {
					Map<String, SMSData> smsMap = smsCommonDao.querySms(yyyyMMdd, false);
					buildLineRelation(Long.parseLong(smsMap.get(null).getSeqid()));
				}
				
				/*if(Contact.VALID_FALG && smsCommonDao.queryValidStatus(yyyyMMdd, false)) {	// 院线会员手机报数据校验是否有错误
					Cache.addValue(date, false);	//缓存数据可能丢失重新建立
					buildLineVaildSend(date);
					throw new Exception("院线数据校验出错，请及时修正并进行手动发送。");
				}*/
			}
			
			/*if(Contact.VALID_FALG) {
				System.out.println("开始校验院线数据");
				buildValid(date, yyyyMMdd);
			}*/
			/*System.out.println("开始发送短信");
			buildLineSend(yyyyMMdd);// 发送短信
			updateJobStatusForSuccess(yyyyMMdd);
			*/
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/*public static void main(String[] args) {
		com.mchange.v2.c3p0.ComboPooledDataSource dataSource = null;
		try {
			dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
			dataSource.setUser("ccs_mbr_prod");
			dataSource.setPassword("ccs_mbr_prod");
			dataSource.setJdbcUrl("jdbc:oracle:thin:@10.199.201.105:1521:ccsstag");
			
			MemberLineSmsDao dao = new MemberLineSmsDao();
			dao.dataSource = dataSource;
			dao.dataSourceStatus= dataSource;
			
			CommonDao comDao = new CommonDao();
			comDao.dataSource = dataSource;
			
			SendLineMemberSmsServiceImpl service = new SendLineMemberSmsServiceImpl();
			service.commonDao = comDao;
			service.memberLineSmsDao = dao;
			
			service.lineSMS("2013-08-18");
		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dataSource.close();
		}
	}*/
	 
	/*public void addNewStatus(int yyyyMMdd) {
		smsCommonDao.insertStatus(yyyyMMdd);
	}*/
	
	public void updateJobStatusForSuccess(int yyyyMMdd) {
		memberLineSmsDao.updateJobStatus(yyyyMMdd, 1);
	}
	
	public boolean checkStatus(int yyyyMMdd, String param) {
		return memberLineSmsDao.checkJobStatus(yyyyMMdd, param);
	}
	
	/**
	 * 构建院线短信
	 * 
	 * @param yyyyMMdd
	 * @return
	 */
	private SMSData buildLineSMS(String date) {
		
		String[] time = date.split("-");
		int yyyyMMdd = Integer.parseInt(time[0]+time[1]+time[2]);
		
		SMSData sms = memberLineSmsDao.queryLineSMS(date);
		Long seqId = smsCommonDao.querySmsId();
		sms.setSeqid(String.valueOf(seqId));
		smsCommonDao.insertSMSForSeqId(yyyyMMdd, Arrays.asList(sms));
		
		return sms;
	}
	
	/**
	 * 构建用户短信关系
	 * 
	 * @param smsId
	 * @throws Exception 
	 */
	private void buildLineRelation(Long smsId) throws Exception {
		Map<String, List<Target>> map = smsCommonDao.queryTargetList("1", false);
		System.out.println(map.size()+"查询到人数");
		if(map.size() == 0) {
			throw new Exception("查询到发送人员列表为空，请确认人员信息表中是否有数据");
		}
		memberLineSmsDao.insertTargetSmsRelationForLine(smsId, map.get(null));
	}
	
	/**
	 * 构建发送任务
	 * 
	 * @param yyyyMMdd
	 * @throws Exception
	 */
	@Transactional
	public void buildLineSend(int yyyyMMdd) throws Exception {
		SendSms sendSms = smsCommonDao.querySendTarget(SmsSqlCont.QUERY_WAIT_SEND_SMS_LINE_SQL, yyyyMMdd, new Object[] {1, 3});
		if(smsCommonDao.sendSMS(sendSms)) {
			throw new Exception("Send Line SMS Error!");
		}
		updateJobStatusForSuccess(yyyyMMdd);
	}
	
	/**
	 * 院线短信数据校验
	 * 
	 * @param date
	 * @param yyyyMMdd
	 * @throws Exception 
	 */
	public void buildLineValid(String date, int yyyyMMdd) throws Exception {
		// 准备校验数据
		SendSms sendSms = smsCommonDao.querySendTarget(SmsSqlCont.QUERY_WAIT_SEND_SMS_LINE_SQL, yyyyMMdd, new Object[] {1, 3});
		SMSData sms = memberLineSmsDao.queryLineSMSValidateData(date);
		
		Map<String, SMSData> mapSms = sendSms.getSendSmsList();
		Map<String, SMSData> valid = new HashMap<String, SMSData>();
		valid.put(null, sms);
		
		// 开始校验数据
		this.validate(date, mapSms, valid, false);	//校验院线短信数据
		
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
	public void buildLineVaildSend(final String date) throws Exception {
		this.sendValidSMS(date, false, new CallBack() {
			
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
						smsCommonDao.SetSMSInfo(Contact.FROM_SYS, target.deleteCharAt(target.length() - 1).toString(), Contact.VAILD_SMS.replace("{1}", date+"院线"));
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
		buildLineValid(date, yyyyMMdd);
		buildLineVaildSend(date);
	}

	@Override
	public Map<String, Integer> getStatus(int yyyyMMdd) {
		Map<String, Integer> map = smsCommonDao.queryStatus(yyyyMMdd);
		
		if(map.get("FLAG_SMS") != null && map.get("FLAG_SMS") == 1) {
			map.remove("FLAG_SMS");
		}
		if(map.get("FLAG_CINEMA_SMS") != null && map.get("FLAG_CINEMA_SMS") == 1) {
			map.remove("FLAG_CINEMA_SMS");
		}
		return map;
	}
	
}
