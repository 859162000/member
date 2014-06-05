package com.wanda.ccs.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CardTypeDefRule {
	private String expireRule = "Y";  //有效期规则：Y永久有效    D指定特定日期  T指定月份
	private String extendRule = "N";  //充值延期规则：N不能延期 D指定特定日期 O从失效日期开始X个月  R从充值日期开始X个月
	private String activateRule = "N";//激活延期规则：N不能延期 D指定特定日期 O从失效日期开始X个月  A从激活日期开始X个月
	
	private Date expireDate;
	private Date extendDate;
	private Date activateDate;
	
	private Long expireTime;
	private Long extendTimeR;//从充值日期开始延期
	private Long activateTimeA;//从激活日期开始延期
	private Long extendTimeO;//从失效日期开始延期
	private Long activateTimeO;//从失效日期开始延期
	public CardTypeDefRule(){
		
	}
	public CardTypeDefRule(String expire, String extend, String activate){
		if(expire != null && !expire.equals("")){
			this.expireRule = expire.substring(0, 1);
			if(this.expireRule.equals("Y")){
				this.extendRule = "N";
				this.activateRule = "N";
			}else{
				if(this.expireRule.equals("D")){
//					this.expireDate
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						String stringdate = expire.substring(2)+" 23:59:59";
						this.expireDate = sdf.parse(stringdate);
					} catch (ParseException e) {
					}
				}else if(this.expireRule.equals("T")){
					this.expireTime = Long.valueOf(expire.substring(2));
				}
				if(extend != null && !extend.equals("")){
					this.extendRule = extend.substring(0, 1);
					if(this.extendRule.equals("N")){
						
					}else if(this.extendRule.equals("D")){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							String stringdate = extend.substring(2)+" 23:59:59";
							this.extendDate = sdf.parse(stringdate);
						} catch (ParseException e) {
						}
					}else if(this.extendRule.equals("R")){
						this.extendTimeR = Long.valueOf(extend.substring(2));
					}else if(this.extendRule.equals("O")){
						this.extendTimeO = Long.valueOf(extend.substring(2));
					}
				}else{
					this.extendRule = "N";
				}
				if(activate != null && !activate.equals("")){
					this.activateRule = activate.substring(0, 1);
					if(this.activateRule.equals("N")){
						
					}else if(this.activateRule.equals("D")){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							String stringdate = activate.substring(2)+" 23:59:59";
							this.activateDate = sdf.parse(stringdate);
						} catch (ParseException e) {
						}
					}else if(this.activateRule.equals("A")){
						this.activateTimeA = Long.valueOf(activate.substring(2));
					}else if(this.activateRule.equals("O")){
						this.activateTimeO = Long.valueOf(activate.substring(2));
					}
				}else {
					this.activateRule = "N";
				}
			}
		}else{
			this.expireRule = "Y";
		}
	}
	/** 获取有效期的方式*/
	public String getExpireRule(){
		return this.expireRule;
	}
	public void setExpireRule(String expireRule) {
		this.expireRule = expireRule;
	}
	
	/** 获取充值延期方式*/
	public String getExtendRule(){
		return this.extendRule;
	}
	public void setExtendRule(String extendRule) {
		this.extendRule = extendRule;
	}
	
	/** 获取激活延期方式*/
	public String getActivateRule(){
		return this.activateRule;
	}
	public void setActivateRule(String activateRule) {
		this.activateRule = activateRule;
	}
	
	/** 获取有效期  到期日期方式*/
	public Date getExpireDate(){
		return this.expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	/** 获取充值延期  指定日期方式*/
	public Date getExtendDate(){
		return this.extendDate;
	}
	public void setExtendDate(Date extendDate) {
		this.extendDate = extendDate;
	}
	
	/** 获取激活延期  指定日期方式*/
	public Date getActivateDate(){
		return this.activateDate;
	}
	public void setActivateDate(Date activateDate) {
		this.activateDate = activateDate;
	}
	
	/** 获取有效期  X个月*/
	public Long getExpireTime(){
		return this.expireTime;
	}
	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}
	
	/** 获取充值延期 从充值日期延期 X个月*/
	public Long getExtendTimeR(){
		return this.extendTimeR;
	}
	public void setExtendTimeR(Long extendTimeR) {
		this.extendTimeR = extendTimeR;
	}
	/** 获取充值延期  从失效日期延期X个月*/
	public Long getExtendTimeO() {
		return extendTimeO;
	}
	public void setExtendTimeO(Long extendTimeO) {
		this.extendTimeO = extendTimeO;
	}
	/** 获取激活延期 从激活日期延期 X个月*/
	public Long getActivateTimeA(){
		return this.activateTimeA;
	}
	public void setActivateTimeA(Long activateTimeA) {
		this.activateTimeA = activateTimeA;
	}
	
	/** 获取激活延期 从失效日期延期X个月*/
	public Long getActivateTimeO() {
		return activateTimeO;
	}
	public void setActivateTimeO(Long activateTimeO) {
		this.activateTimeO = activateTimeO;
	}
	/** 获取有效期规则和有效期的和*/
	public String getDefExpireRule(){
		String defExpireRule = this.expireRule + "|";
		if(this.expireRule.equals("Y")){
			
		}else if(this.expireRule.equals("D")){
			if(expireDate !=null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				defExpireRule += sdf.format(this.expireDate);
			}
			
		}else if(this.expireRule.equals("T")){
			if(expireTime!=null)
				defExpireRule += this.expireTime.toString();
		}
		return defExpireRule;
	}
	/** 获取充值延期规则和充值延期的和*/
	public String getDefExtendRule(){
		if(this.expireRule.equals("Y")){
			return null;
		}
		String defExtendRule = this.extendRule + "|";
		if(this.extendRule.equals("N")){
			
		}else if(this.extendRule.equals("D")){
			if(extendDate != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				defExtendRule += sdf.format(this.extendDate);
			}
		}else if(this.extendRule.equals("R")){
			if(extendTimeR != null)
				defExtendRule += this.extendTimeR.toString();
		}else if(this.extendRule.equals("O")){
			if(extendTimeO != null)
				defExtendRule += this.extendTimeO.toString();
		}
		return defExtendRule;
	}
	/** 获取激活延期规则和激活延期的和*/
	public String getDefActivateRule(){
		if(this.expireRule.equals("Y")){
			return null;
		}
		String defActivateRule = this.activateRule + "|";
		if(this.activateRule.equals("N")){
			
		}else if(this.activateRule.equals("D")){
			if(activateDate != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				defActivateRule += sdf.format(this.activateDate);
			}
		}else if(this.activateRule.equals("A")){
			if(activateTimeA != null)
				defActivateRule += this.activateTimeA.toString();
		}else if(this.activateRule.equals("O")){
			if(activateTimeO != null)
				defActivateRule += this.activateTimeO.toString();
		}
		return defActivateRule;
	}
	
	//获取激活延期所到的日期
	public Date getActivateExpiryDate(Date expiryDate) throws ParseException{
		if (this.getActivateRule().equals("D")) {
			 return this.getActivateDate();
		} else if (this.getActivateRule().equals("A")) {
			Calendar date = Calendar.getInstance();
			int year = date.get(Calendar.YEAR);
			int month = date.get(Calendar.MONTH) + 1;
			int day = date.get(Calendar.DATE);
			int addYear = (int) (this.getActivateTimeA() / 12);
			year += addYear;
			int m = (int) (this.getActivateTimeA() - addYear * 12);
			if (month + m > 12) {
				year += 1;
				month = (int) (month + m - 12);
			} else {
				month = (int) (month + m);
			}
			String strExpiryDate = year + "-" + month + "-" + day
					+ " 23:59:59";
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(strExpiryDate);
		} else if (this.getActivateRule().equals("O")) {
			Calendar date = Calendar.getInstance();
			date.setTime(expiryDate);
			int year = date.get(Calendar.YEAR);
			int month = date.get(Calendar.MONTH) + 1;
			int day = date.get(Calendar.DATE);
			int addYear = (int) (this.getActivateTimeO() / 12);
			year += addYear;
			int m = (int) (this.getActivateTimeO() - addYear * 12);
			if (month + m > 12) {
				year += 1;
				month = (int) (month + m - 12);
			} else {
				month = (int) (month + m);
			}
			String strExpiryDate = year + "-" + month + "-" + day
					+ " 23:59:59";
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(strExpiryDate);
		}
		return null;
	}
	
	//获取充值延期所到的日期
	public Date getExtendExpiryDate(Date expiryDate) throws ParseException{
		if (this.getExtendRule().equals("D")) {
			return this.getExtendDate();
		} else if (this.getExtendRule().equals("R")) {
			Calendar date = Calendar.getInstance();
			int year = date.get(Calendar.YEAR);
			int month = date.get(Calendar.MONTH) + 1;
			int day = date.get(Calendar.DATE);
			int addYear = (int) (this.getExtendTimeR() / 12);
			year += addYear;
			int m = (int) (this.getExtendTimeR() - addYear * 12);
			if (month + m > 12) {
				year += 1;
				month = (int) (month + m - 12);
			} else {
				month = (int) (month + m);
			}
			String strExpiryDate = year + "-" + month + "-" + day
					+ " 23:59:59";
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(strExpiryDate);
		} else if (this.getExtendRule().equals("O")) {
			Calendar date = Calendar.getInstance();
			date.setTime(expiryDate);
			int year = date.get(Calendar.YEAR);
			int month = date.get(Calendar.MONTH) + 1;
			int day = date.get(Calendar.DATE);
			int addYear = (int) (this.getExtendTimeO() / 12);
			year += addYear;
			int m = (int) (this.getExtendTimeO() - addYear * 12);
			if (month + m > 12) {
				year += 1;
				month = (int) (month + m - 12);
			} else {
				month = (int) (month + m);
			}
			String strExpiryDate = year + "-" + month + "-" + day
					+ " 23:59:59";
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(strExpiryDate);
		}
		return null;
	}
}
