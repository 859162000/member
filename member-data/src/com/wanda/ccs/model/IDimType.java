package com.wanda.ccs.model;

/**
 * 维数据类型ID，以及维数据代码的定义
 */
public interface IDimType {
	/** 国家 
	public final static String DIMTYPE_COUNTRY = "101";
	public final static long LDIMTYPE_COUNTRY = 101;
	*/
	/** 国家 */
	public final static String DIMTYPE_COUNTRY = "148";
	public final static long LDIMTYPE_COUNTRY = 148;

	/** 语言 */
	public final static String DIMTYPE_LANGUAGE = "102";
	public final static long LDIMTYPE_LANGUAGE = 102;

	/** 假日类型 */
	public final static String DIMTYPE_HOLIDAY = "103";
	public final static long LDIMTYPE_HOLIDAY = 103;

	/** 区域 */
	public final static String DIMTYPE_AREA = "104";
	public final static long LDIMTYPE_AREA = 104;

	/** 城市级别 */
	public final static String DIMTYPE_CITYLEVEL = "105";
	public final static long LDIMTYPE_CITYLEVEL = 105;

	/** */
	public final static String DIMTYPE_SCENETYPE = "106";
	public final static long LDIMTYPE_SCENETYPE = 106;

	/** 影院类型 */
	public final static String DIMTYPE_CINEMATYPE = "107";
	public final static long LDIMTYPE_CINEMATYPE = 107;

	/** 影院级别 */
	public final static String DIMTYPE_CINEMALEVEL = "108";
	public final static long LDIMTYPE_CINEMALEVEL = 108;

	/** 影院属性 */
	public final static String DIMTYPE_CINEMAATTR = "109";
	public final static long LDIMTYPE_CINEMAATTR = 109;

	/** */
	public final static String DIMTYPE_CINEMA_FILM_STATUS = "110";
	public final static long LDIMTYPE_CINEMA_FILM_STATUS = 110;

	/** 影厅类型 */
	public final static String DIMTYPE_HALL_TYPE = "113";
	public final static long LDIMTYPE_HALL_TYPE = 113;

	/** 影厅放映制式 */
	public final static String DIMTYPE_PROJECT_TYPE = "115";
	public final static long LDIMTYPE_PROJECT_TYPE = 115;

	/** 排片信息状态 */
	public static final String DIMTYPE_SCHEDULE_INFO_STATUS = "117";
	public static final long LDIMTYPE_SCHEDULE_INFO_STATUS = 117;

	/** 排片信息版本 */
	public static final String DIMTYPE_SCHEDULE_INFO_VERSION = "118";
	public static final long LDIMTYPE_SCHEDULE_INFO_VERSION = 118;

	/** 职责 */
	public static final String DIMTYPE_DUTY = "120";
	public static final long LDIMTYPE_DUTY = 120;

	/** 性别 */
	public static final String DIMTYPE_GENDER = "121";
	public static final long LDIMTYPE_GENDER = 121;

	/** 分账方式 */
	public static final String DIMTYPE_ACCOUNT_TYPE = "122";
	public static final long LDIMTYPE_ACCOUNT_TYPE = 122;
	/**
	 * 买断式
	 */
	public static final String DIMTYPE_ACCOUNT_TYPE_BUYOUT = "2";

	/** 影片分账方式 */
	public static final String DIMTYPE_FILMACCOUNT_TYPE = "123";
	public static final long LDIMTYPE_FILMACCOUNT_TYPE = 123;

	/** 影片拷贝状态 */
	public static final String DIMTYPE_FILMCOPY_STATUS = "125";
	public static final long LDIMTYPE_FILMCOPY_STATUS = 125;

	/** 介质类型 */
	public static final String DIMTYPE_MEDIUM_TYPE = "127";
	public static final long LDIMTYPE_MEDIUM_TYPE = 127;

	/** 拷贝类型 */
	public static final String DIMTYPE_COPY_TYPE = "126";
	public static final long LDIMTYPE_COPY_TYPE = 126;
	public static final String DIMTYPE_COPY_TYPE_MAIDUAN = "1";
	public static final String DIMTYPE_COPY_TYPE_NORMAL = "2";

	/** 拷贝来源 */
	public static final String DIMTYPE_COPY_FROM = "128";
	public static final long LDIMTYPE_COPY_FROM = 128;

	/** 影片类别 */
	public static final String DIMTYPE_FILM_CATETYPE = "130";
	public static final long LDIMTYPE_FILM_CATETYPE = 130;

	/** 影片类型 */
	public static final String DIMTYPE_FILM_TYPE = "131";
	public static final long LDIMTYPE_FILM_TYPE = 131;

	/** 影片级别 */
	public static final String DIMTYPE_FILM_LEVEL = "132";
	public static final long LDIMTYPE_FILM_LEVEL = 132;

	/** 影片制式 */
	public static final String DIMTYPE_FILM_SET = "133";
	public static final long LDIMTYPE_FILM_SET = 133;

	/** 影片放映制式 */
	public static final String DIMTYPE_FILM_SHOW_SET = "134";
	public static final long LDIMTYPE_FILM_SHOW_SET = 134;

	/** 音效类别 */
	public static final String DIMTYPE_AUDIO_TYPE = "135";
	public static final long LDIMTYPE_AUDIO_TYPE = 135;

	/** 删除标记 */
	public static final String DIMTYPE_ISDELETE = "136";
	public static final long LDIMTYPE_ISDELETE = 136;

	/** 排片指导状态 */
	public static final String DIMTYPE_SCHEDULE_STATUS = "137";
	public static final long LDIMTYPE_SCHEDULE_STATUS = 137;

	/** 开业状态 */
	public static final String DIMTYPE_ISOPEN = "139";
	public static final long LDIMTYPE_ISOPEN = 139;

	/** 提交状态 */
	public static final String DIMTYPE_SUBMIT = "140";
	public static final long LDIMTYPE_SUBMIT = 140;

	/** 影片伤等 */
	public static final String DIMTYPE_FILM_COPY_DAMAGE = "141";
	public static final long LDIMTYPE_FILM_COPY_DAMAGE = 141;

	/** 影片是否在库 */
	public static final String DIMTYPE_FILM_INSTOCK = "142";
	public static final long LDIMTYPE_FILM_INSTOCK = 142;

	/** 区域排片状态 */
	public static final String DIMTYPE_SCHEDULE_PLAN_STATUS = "143";
	public static final long LDIMTYPE_SCHEDULE_PLAN_STATUS = 143;
	
	/** 影厅名称 */
	public static final String DIMTYPE_HALL_NAME = "145";
	public static final long LDIMTYPE_HALL_NAME = 145;

	/** 卡状态 */
	public static final String DIMTYPE_CARD_STATUS = "146";
	public static final long LDIMTYPE_CARD_STATUS = 146;

	/** 卡类型状态 */
	public static final String DIMTYPE_CARD_TYPE_STATUS = "147";
	public static final long LDIMTYPE_CARD_TYPE_STATUS = 147;

	/**
	 * 场区类型
	 */
	public static final String DIMTYPE_HALL_BLOCK_TYPE = "150";
	public static final long LDIMTYPE_HALL_BLOCK_TYPE = 150;
	/**
	 * 影片发行方式
	 */
	public static final long LDIMTYPE_FILM_RELEASE_TYPE = 151;
	public static final String DIMTYPE_FILM_RELEASE_TYPE = "151";
	
	/**
	 * 场区类型-普通区
	 */
	public static final String DIMTYPE_HALL_BLOCK_TYPE_NORMAL = "01";

	/** 影片开放地点 */
	public static final String DIMTYPE_CHANNEL_TYPE = "153";
	public static final long LDIMTYPE_CHANNEL_TYPE = 153;

	/** 营销活动状态 */
	public static final String DIMTYPE_COMPAIGN_STATUS = "156";
	public static final long LDIMTYPE_COMPAIGN_STATUS = 156;

	/** 制卡申请单状态 */
	public static final String DIMTYPE_CARD_ORDER_STATUS = "157";
	public static final long LDIMTYPE_CARD_ORDER_STATUS = 157;

	/** 一般申请状态 */
	public static final String DIMTYPE_REQUEST_STATUS = "158";
	public static final long LDIMTYPE_REQUEST_STATUS = 158;

	/** 票类性质 */
	public static final String DIMTYPE_TICKET_TYPE_TYPE = "159";
	public static final long LDIMTYPE_TICKET_TYPE_TYPE = 159;

	/** 城市类别 */
	public static final String DIMTYPE_CITY_TYPE = "160";
	public static final long LDIMTYPE_CITY_TYPE = 160;
	public static final String DIMTYPE_CITY_TYPE_A = "A";
	public static final String DIMTYPE_CITY_TYPE_B = "B";
	public static final String DIMTYPE_CITY_TYPE_C = "C";
	/**
	 * 其他城市类别
	 */
	public static final String DIMTYPE_CITY_TYPE_O = "O";

	/** 销售单审核状态 */
	public static final String DIMTYPE_SALES_ORDER_APP_STATUS = "161";
	public static final long LDIMTYPE_SALES_ORDER_APP_STATUS = 161;

	/** 销售单状态 */
	public static final String DIMTYPE_SALES_ORDER_STATUS = "162";
	public static final long LDIMTYPE_SALES_ORDER_STATUS = 162;

	/** 券的使用方式 */
	public static final String DIMTYPE_SALES_ORDER_USE_TYPE = "163";
	public static final long LDIMTYPE_SALES_ORDER_USE_TYPE = 163;

	/** 券类型状态 */
	public static final String DIMTYPE_VOUCHER_TYPE_STATUS = "164";
	public static final long LDIMTYPE_VOUCHER_TYPE_STATUS = 164;

	/** 销售渠道 */
	public static final String DIMTYPE_OPEN_CHANNEL = "166";
	public static final long LDIMTYPE_OPEN_CHANNEL = 166;

	/** 券的状态 */
	public static final String DIMTYPE_VOUCHER_STATUS = "167";
	public static final long LDIMTYPE_VOUCHER_STATUS = 167;
	
	/** 排片指导影厅范围	 */
	public static final String DIMTYPE_SCHEDULE_GUIDE_HALLRANGE = "168";
	public static final long LDIMTYPE_SCHEDULE_GUIDE_HALLRANGE = 168;
	
	/** 票类分类*/
	public static final String DIMTYPE_TICKET_CATEGORY = "169";
	public static final long LDIMTYPE_TICKET_CATEGORY = 169;
	
	/** 卡服务的支付方式 */
	public static final String DIMTYPE_CARD_PAY_METHOD = "170";
	public static final long LDIMTYPE_CARD_PAY_METHOD = 170;
	
	/** 票类类型 */
	public static final String DIMTYPE_TICKET_SCATEGORY = "171";
	public static final long LDIMTYPE_TICKET_SCATEGORY = 171;

	/** 销售单申请结算记录支付方式 */
	public static final String DIMTYPE_SALEORDER_SETT_PAY_METHOD = "173";
	public static final long LDIMTYPE_SALEORDER_SETT_PAY_METHOD = 173;
	
	
	// /////////////////////////////////////////////
	//
	// SCENETYPE
	//
	// /////////////////////////////////////////////
	/** 连场 */
	public static final String DIMTYPE_SCENETYPE_JOINT = "4";

	// /////////////////////////////////////////////
	//
	// CAMPAIGN_STATUS
	//
	// /////////////////////////////////////////////

	/** 营销活动状态 有效 */
	public static final String DIMTYPE_CAMPAIGN_STATUS_E = "E";

	/** 营销活动状态 失效 */
	public static final String DIMTYPE_CAMPAIGN_STATUS_D = "D";

	/** 营销活动状态 删除 */
	public static final String DIMTYPE_CAMPAIGN_STATUS_X = "X";

	// /////////////////////////////////////////////
	//
	// DIMTYPE_FILMCOPY_STATUS
	//
	// /////////////////////////////////////////////

	/** 未发送时的状态 */
	public static final String DIMTYPE_FILMCOPY_STATUS_NOTSEND = "01";

	/** 已发送时的状态 */
	public static final String DIMTYPE_FILMCOPY_STATUS_SEND = "02";

	/** 已接受时的状态 */
	public static final String DIMTYPE_FILMCOPY_STATUS_IS = "03";

	public static final String FILMCOPY_FROMTO_PUBLISHER = "1";
	public static final String FILMCOPY_FROMTO_CINEMA = "2";

	// /////////////////////////////////////////////
	//
	// DIMTYPE_SCHEDULE_STATUS
	//
	// /////////////////////////////////////////////

	/** 区域排片状态 已审核 */
	public static final String DIMTYPE_SCHEDULE_STATUS_APPROVED = "3";
	/** 区域排片状态 已下发 */
	public static final String DIMTYPE_SCHEDULE_STATUS_SEND = "5";
	/** 区域排片状态 已退回 */
	public static final String DIMTYPE_SCHEDULE_STATUS_UNTREAD = "4";
	/** 区域排片状态 未审核 */
	public static final String DIMTYPE_SCHEDULE_STATUS_NOTAPPROVED = "2";
	/** 区域排片状态 未提交 */
	public static final String DIMTYPE_SCHEDULE_STATUS_NOTSUBMIT = "1";
	/** 区域排片状态 已锁定 */
	public static final String DIMTYPE_SCHEDULE_STATUS_LOCKED = "6";
	/** 区域排片状态 已取消 */
	public static final String DIMTYPE_SCHEDULE_STATUS_CANCEL = "0";

	// /////////////////////////////////////////////
	//
	// DIMTYPE_ROUND_TYPE
	//
	// /////////////////////////////////////////////
	/** 普通场 */
	public static final String DIMTYPE_SCENETYPE_NORMAL = "1";

	// /////////////////////////////////////////////
	//
	// DIMTYPE_PROJECT_TYPE
	//
	// /////////////////////////////////////////////

	/**
	 * 放映制式 2k
	 */
	public static final String DIMTYPE_PROJECT_TYPE_2k = "01";
	/**
	 * 放映制式 4k
	 */
	public static final String DIMTYPE_PROJECT_TYPE_4k = "02";
	/**
	 * 放映制式 IMAX 胶片
	 */
	public static final String DIMTYPE_PROJECT_TYPE_IMAX_JIAOPIAN = "03";
	/**
	 * 放映制式 IMAX 数字
	 */
	public static final String DIMTYPE_PROJECT_TYPE_IMAX_DIGIT = "04";
	/**
	 * 放映制式 IMAX 胶片3D
	 */
	public static final String DIMTYPE_PROJECT_TYPE_IMAX_JIAOPIAN_3D = "05";
	/**
	 * 放映制式 IMAX 数字3D
	 */
	public static final String DIMTYPE_PROJECT_TYPE_IMAX_DIGIT_3D = "06";
	/**
	 * 放映制式 35mm 胶片
	 */
	public static final String DIMTYPE_PROJECT_TYPE_IMAX_35mm = "07";
	/**
	 * 放映制式 普通3D
	 */
	public static final String DIMTYPE_PROJECT_TYPE_3D = "08";
	/**
	 * 放映制式 RealD
	 */
	public static final String DIMTYPE_PROJECT_TYPE_REALD = "09";
	/**
	 * 放映制式 双机3D
	 */
	public static final String DIMTYPE_PROJECT_TYPE_SJ_3D = "10";

	// /////////////////////////////////////////////
	//
	// DIMTYPE_HALL_TYPE
	//
	// /////////////////////////////////////////////
	/**
	 * 影厅类型 普通
	 */
	public static final String DIMTYPE_HALL_TYPE_NORMAL = "N";
	/**
	 * 影厅类型 VIP
	 */
	public static final String DIMTYPE_HALL_TYPE_VIP = "Y";
	/**
	 * 影厅类型 3D
	 */
	public static final String DIMTYPE_HALL_TYPE_3D = "M";
	/**
	 * 影厅类型 IMAX
	 */
	public static final String DIMTYPE_HALL_TYPE_IMAX = "I";

	// /////////////////////////////////////////////
	//
	// DIMTYPE_CARD_TYPE_STATUS
	//
	// /////////////////////////////////////////////

	/** 卡类型状态 可用 */
	public static final String DIMTYPE_CARD_TYPE_STATUS_E = "E";

	/** 卡类型状态 不可用 */
	public static final String DIMTYPE_CARD_TYPE_STATUS_D = "D";

	/** 卡类型状态 删除 */
	public static final String DIMTYPE_CARD_TYPE_STATUS_X = "X";

	// /////////////////////////////////////////////
	//
	// DIMTYPE_CARD_TYPE_STATUS
	//
	// /////////////////////////////////////////////

	/** 券类型状态 可用 */
	public static final String DIMTYPE_VOUCHER_TYPE_STATUS_E = "E";

	/** 券类型状态 不可用 */
	public static final String DIMTYPE_VOUCHER_TYPE_STATUS_D = "D";

	/** 券类型状态 删除 */
	public static final String DIMTYPE_VOUCHER_TYPE_STATUS_X = "X";

	// /////////////////////////////////////////////
	//
	// DIMTYPE_CARD_ORDER_STATUS
	//
	// /////////////////////////////////////////////
	/** 待区域审核 */
	public static final String DIMTYPE_CARD_ORDER_STATUS_A = "A";

	/** 待院线审核 */
	public static final String DIMTYPE_CARD_ORDER_STATUS_G = "G";

	/** 审核通过 */
	public static final String DIMTYPE_CARD_ORDER_STATUS_P = "P";

	/** 审核不通过，发回返修 */
	public static final String DIMTYPE_CARD_ORDER_STATUS_E = "E";

	/** 审核不通过，结束审核 */
	public static final String DIMTYPE_CARD_ORDER_STATUS_F = "F";

	/** 取消审核 */
	public static final String DIMTYPE_CARD_ORDER_STATUS_X = "X";

	// /////////////////////////////////////////////
	//
	// DIMTYPE_REQUEST_STATUS
	//
	// /////////////////////////////////////////////
	/** 待审核 */
	public static final String DIMTYPE_REQUEST_STATUS_A = "A";
	
	/** 待院线审核 */
	public static final String DIMTYPE_REQUEST_STATUS_G = "G";
	
	/** 审核通过 */
	public static final String DIMTYPE_REQUEST_STATUS_P = "P";

	/** 审核不通过，发回返修 */
	public static final String DIMTYPE_REQUEST_STATUS_E = "E";

	/** 审核不通过，结束审核 */
	public static final String DIMTYPE_REQUEST_STATUS_F = "F";

	/** 取消审核 */
	public static final String DIMTYPE_REQUEST_STATUS_X = "X";

	/**
	 * 英文
	 */
	public static final String DIMDEF_LANGUAGE_EN = "EN";
	/**
	 * 中文
	 */
	public static final String DIMDEF_LANGUAGE_CN = "CN";
	
	/** 券分类 */
	public final static String DIMTYPE_ORDERSORT = "189";
	public final static long LDIMTYPE_ORDERSORT = 189;
	

}
