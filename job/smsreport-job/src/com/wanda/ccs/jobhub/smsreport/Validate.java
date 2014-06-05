package com.wanda.ccs.jobhub.smsreport;

import java.util.Map;

import com.wanda.ccs.jobhub.smsreport.vo.SMSData;
import com.wanda.ccs.jobhub.smsreport.NotSendException;

public abstract class Validate {
	
	/**
	 * 数据校验
	 * 
	 * @param date
	 * @param map
	 * @param valid
	 * @param isCienma
	 */
	public void validate(String date, Map<String, SMSData> map, Map<String, SMSData> valid, boolean isCienma) {
		boolean flag = true;
		
		for(Map.Entry<String, SMSData> entry : map.entrySet()) {
			System.out.println("开始校验inner_code为"+entry.getKey()+"的数据");
			SMSData sms = valid.get(entry.getKey());
			String old = entry.getValue().validData(sms);
			if(old.length() > 0) {
				sms.setValid("2");	//验证失败
				sms.setFailDesc(old);
				flag = false;
			} else {
				sms.setValid("1");
			}
		}
		
		if(flag == false) { // 校验失败
			Cache.addValue(date, false);
		} else {			// 如果存在，移除校验失败
			Cache.remove(date, false);
		}
	}

	/**
	 * 
	 * 
	 * @param date
	 * @param callBack
	 * @throws Exception
	 */
	public void sendValidSMS(String date, boolean isCinema, CallBack callBack) throws Exception {
		try {
			if(Cache.getValue(date, isCinema) != null) {
				if(Integer.parseInt(Cache.getValue(date, isCinema)) == 0 || Integer.parseInt(Cache.getValue(date, isCinema)) == 2) {
					callBack.execute();
					Cache.setSuccessValue(date, isCinema);	//通知短信发送成功
				} else {
					throw new NotSendException("会员手机报数据有误，警告短信已发出请维护人员及时修正。");
				}
			}
		} catch (NotSendException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			Cache.setFailValue(date, isCinema);		//通知短信发送失败
			e.printStackTrace();
			throw e;
		}
	}
	
}
