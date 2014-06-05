package com.wanda.ccs.jobhub.smsreport.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信数据
 * 
 * @author zhurui
 * @date 2014年1月7日 下午3:38:25
 */
public class SMSData implements Serializable {

	private static final long serialVersionUID = 1L;
	/* ID */
	private String seqid;
	/* 短信日期 */
	private String dataymd;
	/* 创建时间 */
	private Timestamp createTime;
	/* 影城内码 */
	private String cinemaInnerCode;
	/* 影城名称 */
	private String cinemaInnerName;

	/* 新增会员数 */
	private String newMember;
	/* 线下新增会员数 */
	private String notNetMember;
	/* 线上新增会员数 */
	private String netMember;

	/* 会员总数 */
	private String newMemberSum;
	/* 线下新增会员总数 */
	private String notNetMemberSum;
	/* 线上新增会员总数 */
	private String netMemberSum;

	/* 会员昨日线下票房 */
	private String notNetTicketSum;
	/* 会员昨日线上票房 */
	private String netTicketSum;

	/* 线下会员卖品 */
	private String notNetGoodsSum;

	/* 可兑换积分兑换 */
	private String exchangePoint;
	/* 可兑换积分新增 */
	private String exchangePointNew;
	/* 可兑换积分余额 */
	private String exchangePointBalance;
	/* 线下月会员票房 */
	private String notNetTicketMonthSum;
	/* 线上月会员票房 */
	private String netTicketMonthSum;
	/* 会员本年线下票房 */
	private String notNetTicketYearSum;
	/* 会员本年线上票房 */
	private String netTicketYearSum;
	/* 校验状态 */
	private String valid;
	/* 校验描述 */
	private String failDesc;

	public SMSData() {
	}

	public SMSData(String cinemaInnerCode, String cinemaInnerName) {
		this.cinemaInnerCode = cinemaInnerCode;
		this.cinemaInnerName = cinemaInnerName;
	}

	public SMSData(String dataymd, String cinemaInnerCode, String cinemaInnerName, 
			String newMember, String notNetMember, String netMember,
			String newMemberSum, String notNetMemberSum, String netMemberSum,
			String notNetTicketSum, String netTicketSum, String notNetGoodsSum,
			String exchangePoint, String exchangePointNew,
			String exchagePointBalance, String notNetTicketMonthSum,
			String netTicketMonthSum, String notNetTicketYearSum,
			String netTicketYearSum) {
		this.dataymd = dataymd;
		this.cinemaInnerCode = cinemaInnerCode;
		this.cinemaInnerName = cinemaInnerName;
		this.newMember = newMember;
		this.notNetMember = notNetMember;
		this.netMember = netMember;
		this.newMemberSum = newMemberSum;
		this.notNetMemberSum = notNetMemberSum;
		this.netMemberSum = netMemberSum;
		this.notNetTicketSum = notNetTicketSum;
		this.netTicketSum = netTicketSum;
		this.notNetGoodsSum = notNetGoodsSum;
		this.exchangePoint = exchangePoint;
		this.exchangePointNew = exchangePointNew;
		this.exchangePointBalance = exchagePointBalance;
		this.notNetTicketMonthSum = notNetTicketMonthSum;
		this.netTicketMonthSum = netTicketMonthSum;
		this.notNetTicketYearSum = notNetTicketYearSum;
		this.netTicketYearSum = netTicketYearSum;
	}

	public String getSeqid() {
		return seqid;
	}

	public void setSeqid(String seqid) {
		this.seqid = seqid;
	}

	public String getDataymd() {
		return dataymd;
	}

	public void setDataymd(String dataymd) {
		this.dataymd = dataymd;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getCinemaInnerCode() {
		return cinemaInnerCode;
	}

	public void setCinemaInnerCode(String cinemaInnerCode) {
		this.cinemaInnerCode = cinemaInnerCode;
	}


	public String getNewMember() {
		return newMember;
	}

	public void setNewMember(String newMember) {
		this.newMember = newMember;
	}

	public String getNotNetMember() {
		return notNetMember;
	}

	public void setNotNetMember(String notNetMember) {
		this.notNetMember = notNetMember;
	}

	public String getNetMember() {
		return netMember;
	}

	public void setNetMember(String netMember) {
		this.netMember = netMember;
	}

	public String getNewMemberSum() {
		return newMemberSum;
	}

	public void setNewMemberSum(String newMemberSum) {
		this.newMemberSum = newMemberSum;
	}

	public String getNotNetMemberSum() {
		return notNetMemberSum;
	}

	public void setNotNetMemberSum(String notNetMemberSum) {
		this.notNetMemberSum = notNetMemberSum;
	}

	public String getNetMemberSum() {
		return netMemberSum;
	}

	public void setNetMemberSum(String netMemberSum) {
		this.netMemberSum = netMemberSum;
	}

	public String getNotNetTicketSum() {
		return notNetTicketSum;
	}

	public void setNotNetTicketSum(String notNetTicketSum) {
		this.notNetTicketSum = notNetTicketSum;
	}

	public String getNetTicketSum() {
		return netTicketSum;
	}

	public void setNetTicketSum(String netTicketSum) {
		this.netTicketSum = netTicketSum;
	}

	public String getNotNetGoodsSum() {
		return notNetGoodsSum;
	}

	public void setNotNetGoodsSum(String notNetGoodsSum) {
		this.notNetGoodsSum = notNetGoodsSum;
	}

	public String getExchangePoint() {
		return exchangePoint;
	}

	public void setExchangePoint(String exchangePoint) {
		this.exchangePoint = exchangePoint;
	}

	public String getExchangePointNew() {
		return exchangePointNew;
	}

	public void setExchangePointNew(String exchangePointNew) {
		this.exchangePointNew = exchangePointNew;
	}

	public String getExchangePointBalance() {
		return exchangePointBalance;
	}

	public void setExchangePointBalance(String exchangePointBalance) {
		this.exchangePointBalance = exchangePointBalance;
	}

	public String getNotNetTicketMonthSum() {
		return notNetTicketMonthSum;
	}

	public void setNotNetTicketMonthSum(String notNetTicketMonthSum) {
		this.notNetTicketMonthSum = notNetTicketMonthSum;
	}

	public String getNetTicketMonthSum() {
		return netTicketMonthSum;
	}

	public void setNetTicketMonthSum(String netTicketMonthSum) {
		this.netTicketMonthSum = netTicketMonthSum;
	}

	public String getNotNetTicketYearSum() {
		return notNetTicketYearSum;
	}

	public void setNotNetTicketYearSum(String notNetTicketYearSum) {
		this.notNetTicketYearSum = notNetTicketYearSum;
	}

	public String getNetTicketYearSum() {
		return netTicketYearSum;
	}

	public void setNetTicketYearSum(String netTicketYearSum) {
		this.netTicketYearSum = netTicketYearSum;
	}

	public String getCinemaInnerName() {
		return cinemaInnerName;
	}

	public void setCinemaInnerName(String cinemaInnerName) {
		this.cinemaInnerName = cinemaInnerName;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getFailDesc() {
		return failDesc;
	}

	public void setFailDesc(String failDesc) {
		this.failDesc = failDesc;
	}

	public Map<String, Object> getRootMap() {
		Map<String, Object> root = new HashMap<String, Object>();
		if (getCinemaInnerCode() == null) {
			root.put("type", "line");
		} else {
			root.put("type", "cinema");
		}
		root.put("yyyyMMdd", getDataymd());
		root.put("innerName", getCinemaInnerName());
		root.put("NewMember", getValue(getNewMember()));
		root.put("NewMemberSum", getValue(getNewMemberSum()));
		root.put("NetMember", getValue(getNetMember()));
		root.put("NetMemberSum", getValue(getNetMemberSum()));
		root.put("NotNetMember", getValue(getNotNetMember()));
		root.put("NotNetMemberSum", getValue(getNotNetMemberSum()));
		root.put("NetTicketSum", getValue(getNetTicketSum()));
		root.put("NetTicketMonthSum", getValue(getNetTicketMonthSum()));
		root.put("NetTicketYearSum", getValue(getNetTicketYearSum()));
		root.put("NotNetTicketSum", getValue(getNotNetTicketSum()));
		root.put("NotNetTicketMonthSum", getValue(getNotNetTicketMonthSum()));
		root.put("NotNetTicketYearSum", getValue(getNotNetTicketYearSum()));
		root.put("NotNetGoodsSum", getValue(getNotNetGoodsSum()));
		root.put("ExchangePointnew", getValue(getExchangePointNew()));
		root.put("ExchangePoint", getValue(getExchangePoint()));
		root.put("ExchangePointBalance", getValue(getExchangePointBalance()));

		return root;
	}

	private String getValue(String value) {
		String result = new BigDecimal(value).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		result = numberFormat.format(Double.parseDouble(result));
		
		return result;
	}
	
	public String validData(SMSData sms) {
		StringBuilder log = new StringBuilder();
		
		if(Long.parseLong(this.getNewMember()) != Long.parseLong(sms.getNewMember())) {
			log.append("NewMember&");
		}
		
		if(Long.parseLong(this.getNewMemberSum()) != Long.parseLong(sms.getNewMemberSum())) {
			log.append("NewMemberSum&");
		}
		
		if(Long.parseLong(this.getNetMember()) != Long.parseLong(sms.getNetMember())) {
			log.append("NetMember&");
		}
		
		if(Long.parseLong(this.getNetMemberSum()) != Long.parseLong(sms.getNetMemberSum())) {
			log.append("NetMemberSum&");
		}
		
		if(Long.parseLong(this.getNotNetMember()) != Long.parseLong(sms.getNotNetMember())) {
			log.append("NotNetMember&");
		}
		
		if(Long.parseLong(this.getNotNetMemberSum()) != Long.parseLong(sms.getNotNetMemberSum())) {
			log.append("NotNetMemberSum&");
		}
		
		if(Long.parseLong(this.getNetTicketSum()) != Long.parseLong(sms.getNetTicketSum())) {
			log.append("NetTicketSum&");
		}
		
		if(Long.parseLong(this.getNetTicketMonthSum()) != Long.parseLong(sms.getNetTicketMonthSum())) {
			log.append("NetTicketMonthSum&");
		}
		
		if(Long.parseLong(this.getNetTicketYearSum()) != Long.parseLong(sms.getNetTicketYearSum())) {
			log.append("NetTicketYearSum&");
		}
		
		if(Long.parseLong(this.getNotNetTicketSum()) != Long.parseLong(sms.getNotNetTicketSum())) {
			log.append("NotNetTicketSum&");
		}
		
		if(Long.parseLong(this.getNotNetTicketMonthSum()) != Long.parseLong(sms.getNotNetTicketMonthSum())) {
			log.append("NotNetTicketMonthSum&");
		}
		
		if(Long.parseLong(this.getNotNetTicketYearSum()) != Long.parseLong(sms.getNotNetTicketYearSum())) {
			log.append("NotNetTicketYearSum&");
		}
		
		if(Long.parseLong(this.getNotNetGoodsSum()) != Long.parseLong(sms.getNotNetGoodsSum())) {
			log.append("NotNetGoodsSum&");
		}
		
		if(Long.parseLong(this.getExchangePointNew()) != Long.parseLong(sms.getExchangePointNew())) {
			log.append("ExchangePointNew&");
		}
		
		if(Long.parseLong(this.getExchangePoint()) != Long.parseLong(sms.getExchangePoint())) {
			log.append("ExchangePoint&");
		}
		
		if(Long.parseLong(this.getExchangePointBalance()) != Long.parseLong(sms.getExchangePointBalance())) {
			log.append("ExchangePointBalance&");
		}
		
		return log.length() > 0 ? log.deleteCharAt(log.length() - 1).toString() : log.toString() ;
		
	}
	
}
