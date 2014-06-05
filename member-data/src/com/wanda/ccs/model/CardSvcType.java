package com.wanda.ccs.model;

public interface CardSvcType {
	/** 预制*/
	public static final String CARD_SVC_TYPE_I = "预制";
	
	/** 发放*/
	public static final String CARD_SVC_TYPE_ISSUE = "发放";
	
	/** 充值*/
	public static final String CARD_SVC_TYPE_RECHANGE = "充值";
	
	/** 修改资料*/
	public static final String CARD_SVC_TYPE_EDITCUSTINFO = "修改资料";
	
	/** 修改密码*/
	public static final String CARD_SVC_TYPE_EDITPWD = "修改密码";
	
	/** 挂失*/
	public static final String CARD_SVC_TYPE_LOST = "挂失";
	
	/** 换卡*/
	public static final String CARD_SVC_TYPE_REPLACE = "换卡";
	
	/** 补卡*/
	public static final String CARD_SVC_TYPE_RENEW = "补卡";
	
	/** 卡升级*/
	public static final String CARD_SVC_TYPE_UPGRADE = "卡升级";
	
	/** 取消挂失*/
	public static final String CARD_SVC_TYPE_FOUND = "取消挂失";
	
	/** 退卡*/
	public static final String CARD_SVC_TYPE_RETURN = "退卡";
	
	/** 销卡*/
	public static final String CARD_SVC_TYPE_DISCARD = "销卡";
	
	/** 卡激活*/
	public static final String CARD_SVC_TYPE_ACTIVATION = "激活";
	
	/** 支付方式 现金*/
	public static final String CARD_SVC_PAY_METHOD_S = "S";
	
	/** 支付方式 信用卡*/
	public static final String CARD_SVC_PAY_METHOD_C = "C";
	
	/** 支付方式 支票*/
	public static final String CARD_SVC_PAY_METHOD_Q = "Q";
}
