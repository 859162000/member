package com.wanda.ccs.model;

/**
 * 会员维数据类型ID，以及维数据代码的定义
 */
public interface IMemberDimType extends IDimType{
	/** 营销活动状态*/
	public static final String DIMTYPE_CAMPAIGN_STATUS = "1002";
	public static final long LDIMTYPE_CAMPAIGN_STATUS = 1002;
	
	/** 活动统计渠道*/
	public static final String DIMTYPE_CAMPAIGN_CHANNEL = "1003";
	public static final long LDIMTYPE_CAMPAIGN_CHANNEL = 1003;
	
	/** 活动类型*/
	public static final String DIMTYPE_CAMPAIGN_TYPE = "1004";
	public static final long LDIMTYPE_CAMPAIGN_TYPE = 1004;
	
	/** 波次营销类型*/
	public static final String DIMTYPE_ACTIVITY_PROMOTION_TYPE = "1005";
	public static final long LDIMTYPE_ACTIVITY_PROMOTION_TYPE = 1005;
	
	/**券发放指定类型*/
	public static final String DIMTYPE_VOUCHER_RULE_TYPE = "1007";
	public static final long LDIMTYPE_VOUCHER_RULE_TYPE = 1007;
	
	/**券发放次序*/
	public static final String DIMTYPE_VOUCHER_RULE_ORDER = "1008";
	public static final long LDIMTYPE_VOUCHER_RULE_ORDER = 1008;
	
	/** 波次优先级*/
	public static final String DIMTYPE_ACTIVITY_PRIORITY = "1006";
	public static final long LDIMTYPE_ACTIVITY_PRIORITY = 1006;
	
	/** 响应统计方式*/
	public static final String DIMTYPE_RES_CONFIG_TYPE = "1010";
	public static final long LDIMTYPE_RES_CONFIG_TYPE = 1010;
	
	/** 话术变量*/
	public static final String DIMTYPE_OFFER_VARIABLE = "1011";
	public static final long LDIMTYPE_OFFER_VARIABLE = 1011;
	
	/** 波次目标类型*/
	public static final String DIMTYPE_TARGET_TYPE = "1012";
	public static final long LDIMTYPE_TARGET_TYPE = 1012;
	
	/** 通知话术 接触渠道*/
	public static final String DIMTYPE_OFFER_CHANNEL = "1016";
	public static final long LDIMTYPE_OFFER_CHANNEL = 1016;
	
	/** 活动结算方式 院线*/
	public static final String CAMPAINGN_SETTLEMENT_TYPE_GROUP = "10";
	/** 活动结算方式 影城*/
	public static final String CAMPAINGN_SETTLEMENT_TYPE_CINEMA = "20";
	
	
	/** 营销活动状态 计划*/
	public static final String CAMPAINGN_STATUS_PLAN = "10";
	/** 营销活动状态 发布*/
	public static final String CAMPAINGN_STATUS_PUBLISH = "20";
	/** 营销活动状态 执行*/
	public static final String CAMPAINGN_STATUS_EXECUTE = "30";
	/** 营销活动状态 结束*/
	public static final String CAMPAINGN_STATUS_FINISH = "40";
	/** 营销活动状态 停用*/
	public static final String CAMPAINGN_STATUS_INACTIVE = "50";
	
	/** 波次目标状态  计划*/
	public static final String ACTIVITY_ACT_TARGET_PLAN = "10";
	/** 波次目标状态  冻结*/
	public static final String ACTIVITY_ACT_TARGET_LOCKED = "20";
	
	/** 波次响应统计结果状态  执行中*/
	public static final String ACTIVITY_ACT_RESULT_EXEXUTE = "10";
	/** 波次响应统计结果状态  执行结束*/
	public static final String ACTIVITY_ACT_RESULT_FINISH = "20";
	/** 波次响应统计结果状态  统计中*/
	public static final String ACTIVITY_ACT_RESULT_EXECUTE_COUNT = "30";
	/** 波次响应统计结果状态  统计异常*/
	public static final String ACTIVITY_ACT_RESULT_EXCEPTION = "40";
	
	/** 波次优先级 低*/
	public static final String ACTIVITY_PRIORITY_LOW = "10";
	/** 波次优先级 中*/
	public static final String ACTIVITY_PRIORITY_MIDDLE = "20";
	/** 波次优先级 高*/
	public static final String ACTIVITY_PRIORITY_HIGH = "30";
	
	/** 活动统计渠道 线上*/
	public static final String CAMPAINGN_CHANNEL_ONLINE = "10";
	/** 活动统计渠道 线下*/
	public static final String CAMPAINGN_CHANNEL_OFFLINE = "20";
	
	/** 活动类型 会员关怀*/
	public static final String CAMPAIGN_TYPE_MEMBER = "10";
	/** 活动类型 影片关怀*/
	public static final String CAMPAIGN_TYPE_FILM = "20";
	/** 活动类型 卖品关怀*/
	public static final String CAMPAIGN_TYPE_CONCESSION = "30";
	
	/** 波次营销类型 主动营销*/
	public static final String ACTIVITY_PROMOTION_TYPE_OFFER = "10";
	/** 波次营销类型 事件营销*/
	public static final String ACTIVITY_PROMOTION_TYPE_EVENT = "20";
	
	/** 波次目标类型 客群*/
	public static final String TARGET_TYPE_SEGMENT = "10";
	/** 波次目标类型  从文件导入*/
	public static final String TARGET_TYPE_EXCEL = "20";
	
	/**券发放类型 文件指定*/
	public static final String VOUCHER_TYPE_FILE = "10";
	/**券发放类型 规则指定*/
	public static final String VOUCHER_TYPE_RULE = "20";
	
	/**券发放次序 随机*/
	public static final String VOUCHER_TYPE_RANDOM = "10";
	
	/**券发放次序 顺序*/
	public static final String VOUCHER_TYPE_ORDER = "20";
	

	/** 活动OFFER 积分活动*/
	public static final String ACT_OFFER_TYPE_INTEGRATE = "10";
	/** 活动OFFER 券发放*/
	public static final String ACT_OFFER_TYPE_VOUCHER = "20";
	
	/** 响应统计方式 积分活动*/
	public static final String RES_CONFIG_TYPE_INTEGRATE = "10";
	/** 响应统计方式 券发放*/
	public static final String RES_CONFIG_TYPE_VOUCHER = "20";
	/** 响应统计方式 其他*/
	public static final String RES_CONFIG_TYPE_OTHER = "30";
	
	/** 话术通知 接触渠道 短信*/
	public static final String OFFER_CHANNEL_SHORT_MESSAGE = "10";
	/** 话术通知 接触渠道 彩信*/
	public static final String OFFER_CHANNEL_MULTIMEDIA_MESSAGE = "20";
	/** 话术通知 接触渠道 EDM*/
	public static final String OFFER_CHANNEL_EDM = "30";
	/** 话术通知 接触渠道 网站*/
	public static final String OFFER_CHANNEL_WEB = "40";
	/** 话术通知 接触渠道 其他*/
	public static final String OFFER_CHANNEL_OTHER = "50";
	/** 证件类型*/
	public final static String DIMTYPE_IDCARDTYPE = "200";
	public final static long LIMTYPE_IDCARDTYPE = 200;
	
	/** 会员注册类型*/
	public final static String DIMTYPE_MEMBER_REGISTTYPE = "201";
	public final static long LIMTYPE_MEMBER_REGISTTYPE = 201;
	
	/** 会员注册类型*/
	public final static String DIMTYPE_MEMBER_FACCONTACT = "211";
	public final static long LIMTYPE_MEMBER_FACCONTACT = 211;
}
