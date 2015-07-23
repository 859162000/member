package com.wanda.ccs.member.segment;

public class ConstDef {  
	/*接口编码*/
	//卡接口
	public final static String CONST_INTFCODE_C_INFOQUERY = "C1001";
	public final static String CONST_INTFCODE_C_TICKETHISTORY = "C1002";
	public final static String CONST_INTFCODE_C_GOODSHISTORY = "C1003";
	public final static String CONST_INTFCODE_C_RECHARGEHISTORY = "C1004";
	public final static String CONST_INTFCODE_C_AVAILCINEMALIST = "C1005";
	public final static String CONST_INTFCODE_C_CINEMAAVAIL = "C1006";
	public final static String CONST_INTFCODE_C_TICKETTYPEQUERY = "C1007";
	//影片上映资讯查询
	public final static String CONST_INTFCODE_F_GETSCHEDULES = "C1011";
	public final static String CONST_INTFCODE_C_CONSUME = "C2001";
	//public final static String CONST_INTFCODE_C_SELLGOODS = "C2002";
	public final static String CONST_INTFCODE_C_DRAWBACK = "C2003";
	public final static String CONST_INTFCODE_C_CHANGEPWD = "C2004";
	public final static String CONST_INTFCODE_C_PARTIALDRAWBACK = "C2005";
	public final static String CONST_INTFCODE_C_RECHARGE = "C2006";
	public final static String CONST_INTFCODE_C_CINEMACARDTYPE ="C1008";
	public final static String CONST_INTFCODE_C_CARDTYPEQUERY ="C1009";
	public final static String CONST_INTFCODE_C_BUYEDTICKETQUERY ="C1010";
	public final static String CONST_INTFCODE_C_POSRECHARGE = "C2007";
	public final static String CONST_INTFCODE_C_ISSUECARD = "C2008";
	public final static String CONST_INTFCODE_C_CHANGEPHONENUMBER = "C2009";
	public final static String CONST_INTFCODE_C_CHECKPWD = "C2010";
	/**
	 * 影厅服务费
	 */
	public final static String CONST_INTFCODE_C_SERFEE = "C2011";
	/** 卡激活*/
	public final static String CONST_INTFCODE_C_ACTIVATION = "C2012";
	/**
	 * 网站发售虚拟卡
	 */
	public final static String CONST_INTFCODE_C_ISSUEVIRTUALCARD = "C2013";
	/**
	 * 一体机消费
	 */
	public final static String CONST_INTFCODE_C_CONSUMEUNI="2015";
	/**
	 * 发送验证码
	 */
	public final static String CONST_INTFCODE_C_SENDCODE="2016";
	//券接口
	public final static String CONST_INTFCODE_V_INFOQUERY = "C3001";
	public final static String CONST_INTFCODE_V_RULEQUERY = "C3002";
	public final static String CONST_INTFCODE_V_EXCHANGE = "C3003";
	public final static String CONST_INTFCODE_V_LOCK = "C3004";
	public final static String CONST_INTFCODE_V_UNLOCK = "C3005";
	public final static String CONST_INTFCODE_V_TICKETTYPEQUERY = "C3006";
	public final static String CONST_INTFCODE_V_WEBRULEQUERY = "C3007";
	public final static String CONST_INTFCODE_V_RETURN = "C3008";
	public final static String CONST_INTFCODE_V_INQUIRY = "C3009";
	public final static String CONST_INTFCODE_V_CINEMAVOUCHERTYPE = "C3010";
	/* 卖品兑换券编码  */
	public final static String CONST_INTFCODE_V_CONRULEQUERY = "C3011";
	/**
	 * 券启用 网站
	 */
	public final static String CONST_INTFCODE_V_ENABLED = "C3012";
	/**
	 * 刮刮券 涂层码校验
	 */
	public final static String CONST_INTFCODE_V_SCRATCH = "C3013";
	/**
	 * 单张券作废
	 */
	public final static String CONST_INTFCODE_V_CANCEL = "C3015";
	/**
	 * 单张券补码
	 */
	public final static String CONST_INTFCODE_V_REPAIR = "C3016";
	//UI集成接口
	public final static String CONST_INTFCODE_PREPARESESSION = "C4001";
    
	//轮班结算	
	public final static String CONST_INTFCODE_MEMBERSHIFT = "C4002";
	public final static String CONST_INTFCODE_MEMBERLIST = "C4003";
	public final static String CONST_INTFCODE_VOUCHERCOLLECTION = "C4004";
	public final static String CONST_INTFCODE_RECHARGEDETAIL = "C4005";
	public final static String CONST_INTFCODE_VOUCHERCOLLECTIONNEW = "C4006";
	
	//POS提供接口
	public final static String CONST_INTFCODE_POS_LOCKROUND = "C9001";
	public final static String CONST_INTFCODE_POS_UNLOCKROUND = "C9002";
	
	/*接口处理返回码*/
	public final static String  CONST_RESPCODE_SUCCESS 				= "0";//成功返回码
	public final static String  CONST_RESPCODE_CARD_NOT_EXIST       = "C010001"; //卡号或序列号不正确,卡不存在
	public final static String  CONST_RESPCODE_PASSWORD_INVALID     = "C010002"; //卡密码不正确
	public final static String  CONST_RESPCODE_LACK_OF_PARAM        = "C010003"; //缺少参数
	public final static String  CONST_RESPCODE_INVALID_DATE			= "C010004"; //日期格式非YYYY-MM-DD
	public final static String  CONST_RESPCODE_PARAM_EMPTY			= "C010005"; //参数值为空
	public final static String  CONST_RESPCODE_PARAM_TYPE_ERROR		= "C010006"; //参数值类型错误
	public final static String  CONST_RESPCODE_CARD_ABNORMAL		= "C010007"; //卡状态非正常
	/*** 此卡已禁用*/
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_D		= "C01000701"; 
	/** * 此卡已 换卡不可用 */
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_R		= "C01000702"; 
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_N		= "C01000703"; //此卡已补卡不可用
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_U		= "C01000704"; //此卡已升级不可用
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_L		= "C01000705"; //此卡已丢失不可用
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_X		= "C01000706"; //此卡已销卡
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_E		= "C01000707"; //此卡预制卡失败
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_T		= "C01000708"; //此卡已退卡不可用 
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_W		= "C01000709"; //此卡待激活不可用 
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_F		= "C01000710"; //此卡已冻结不可用 
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_I		= "C01000711"; //此卡已预制未发卡
	public final static String  CONST_RESPCODE_CARD_ABNORMAL_W_U	= "C01000712"; //请持卡到影城进行激活或升级
	public final static String  CONST_RESPCODE_ORDERID_INVALID		= "C010008"; //订单号不存在
	public final static String  CONST_RESPCODE_ORDERID_DRAWBACKED	= "C010009"; //该订单已经退款
	public final static String  CONST_RESPCODE_ORDERID_DUPLICATE    = "C010010"; //订单号重复
	public final static String  CONST_RESPCODE_BALANCE_NOT_ENOUGH   = "C010011"; //卡余额不足
	public final static String  CONST_RESPCODE_INVALID_PERFORM_TICKETTYPE   = "C010012"; //当前卡、场次、票类不匹配
	public final static String  CONST_RESPCODE_INVALID_CINEMA_CODE  = "C010013"; //影院编码无效
	public final static String  CONST_RESPCODE_PARAM_LENGTH_ERROR   = "C010014"; //参数长度错误
	public final static String  CONST_RESPCODE_INVALID_PARTIALDRAWBACK = "C010015";//退款总额超出订单总金额
	public final static String  CONST_RESPCODE_TICKETNUM_LIMIT_EXCEED = "C010016"; //购票总额超出总数	
	public final static String  CONST_RESPCODE_CINEMA_NOT_EXIST = "C010017"; //影院不存在
	public final static String  CONST_RESPCODE_CINEMA_CODE_EMPTY = "C10018"; //影院编码为空
	public final static String  CONST_RESPCODE_CARD_TYPE_NOT_EXIST = "C010019"; //卡类型不存在
	public final static String  CONST_RESPCODE_TICKET_TOTAL_NUM_LIMIT_EXCEED = "C010020"; //已购总票数超出总数
	public final static String  CONST_RESPCODE_TICKET_FILM_NUM_LIMIT_EXCEED = "C010021"; //每个影片购票数超出总数
	public final static String  CONST_RESPCODE_TICKET_FILM_DAY_LIMIT_EXCEED = "C010022"; //片日票数超出总数
	public final static String  CONST_RESPCODE_TICKET_NUM_LIMIT_EXCEED = "C010023"; //每日购票数超出总数
	public final static String  CONST_RESPCODE_GOODS_AMT_LIMIT_EXCEED = "C010024"; //每日购货额超出总数
	public final static String  CONST_RESPCODE_PASSWORD_NOT_ALLROW_RULE = "C010025"; //密码规则过于简单
	public final static String  CONST_RESPCODE_CARD_TYPE_ERROR 		= "C010026"; //此类型不能充值
	public final static String  CONST_RESPCODE_RECHARGE_CHANNEL_ERROR 		= "C010027"; //充值渠道错误
	public final static String  CONST_RESPCODE_FILM_INFO_EMPTY = "C010028"; //没有找到相关的排片信息
	public final static String  CONST_RESPCODE_CARD_EXPIRYDATE = "C010029";//卡过期
	public final static String  CONST_RESPCODE_TCARD_GOODS = "C010030";//记次卡不能购买卖品
	public final static String  CONST_RESPCODE_DATE_EMPTY     = "C010031";//时间为空
	public final static String  CONST_RESPCODE_CARD_PHONE		= "C010032"; //手机号非法
	public final static String  CONST_RESPCODE_CARD_DATA_ERRE		= "C010033"; //校验非法
	public final static String  CONST_RESPCODE_CARD_INITPASSWORD_ERRE		= "C010034"; //刮刮卡初始密码错误
	public final static String  CONST_RESPCODE_CARD_NOT_VIRTUAL		= "C010035"; //非虚拟卡
	public final static String  CONST_RESPCODE_CARD_ORDER_STATUS		= "C010036"; //制卡申请单状态未审批通过
	public final static String  CONST_RESPCODE_CARD_PAY_FEE		= "C010037"; //付款金额小于卡工本费
	public final static String  CONST_RESPCODE_CARD_NOTIN_CINEMA		= "C010038"; //卡类型在此影城不可用
	public final static String  CONST_RESPCODE_CARD_NOTIN_CHECKCODE		= "C010039"; //卡类型在此影城不可用
	public final static String  CONST_RESPCODE_INVALID_VOUCHERNO = "C020001"; //券编码不合法
	public final static String  CONST_RESPCODE_VOUCHER_NOT_FIT   = "C020002"; //券不可用于场次
	public final static String  CONST_RESPCODE_VOUCHER_DUP_LOCK  = "C020003"; //券重复锁定
	public final static String  CONST_RESPCODE_INVALID_LOCKNO    = "C020004"; //券锁定编号不一致
	public final static String  CONST_RESPCODE_VOUCHER_ABNORMAL  = "C020005"; //券状态为不可用-非启用
	public final static String  CONST_RESPCODE_VOUCHER_EXPIRED   = "C020006"; //券已到期
	public final static String  CONST_RESPCODE_VOUCHER_ORDERNULL = "C020007";//订单不存在
	/**没有可用券号码*/
	public final static String  CONST_RESPCODE_LOCK_VOUCHER = "E0001";
	/**场次消费渠道验证失败*/
	public final static String  CONST_RESPCODE_LOCK_ROUND = "E0002";
	/**场次票类验证失败*/
	public final static String  CONST_RESPCODE_LOCK_TICKET = "E0003";
	/**销售单状态异常*/
	public final static String  CONST_RESPCODE_LOCK_STATUS = "E0004";
	/**券状态不可兑换*/
	public final static String  CONST_RESPCODE_UN_CHANGE = "E0005";
	/**券状态不可兑换已经被锁住*/
	public final static String  CONST_RESPCODE_VOUCHER_LOCK = "E0006";
	/**代金券单座满一定金额才允许使用*/
	public final static String  CONST_RESPCODE_VOUCHER_PRICE = "E0007";
	/**超出每座限定张数*/
	public final static String  CONST_RESPCODE_VOUCHER_AMOUNT = "E0008";
	/**卖品只允许代金券*/
	public final static String  CONST_RESPCODE_VOUCHER_GOODS = "E0009";
	/**券不支持购买卖品*/
	public final static String  CONST_RESPCODE_VOUCHER_UNGOODS = "E0010";
	/**未查询到该券*/
	public final static String  CONST_RESPCODE_UNVOUCHER = "E0011";
	/**订单券张数超过限制*/
	public final static String  CONST_RESPCODE_MOUNTOUT = "E0012";
	/**该实体券不能用于线上消费*/
	public final static String  CONST_RESPCODE_LOCK_UNNET = "EC0001";
	
	
	public final static String  CONST_RESPCODE_OTHER_EXCEPTION      = "PT90000"; //其他未知系统exception
	
	public final static String	CONST_RESPCODE_GET_DBCONNECTION_FAILED	 = "PT91001";	/* 取数据库连接失败	*/
	public final static String	CONST_RESPCODE_CLOSE_DBCONNECTION_FAILED = "PT91002";	/* 关闭数据库连接失败	*/
	public final static String 	CONST_RESPCODE_ACCESSLOG_CREATE_FAILED = "PT91003";		/*接口访问日志创建错误 */
	public final static String 	CONST_RESPCODE_TIME_OUT_VALUE = "PT91004";		/*业务访问响应超时*/
	
	public final static String	CONST_RESPCODE_CLIENTIP_IN_BLACKLIST 	  = "PT92001";	/* 客户端IP在黑名单   */
	public final static String	CONST_RESPCODE_CLIENTTYPE_NOTIN_WHITELIST = "PT92002";	/* 客户端类型不在白名单     */
	public final static String	CONST_RESPCODE_CLIENT_CONNUSER_NOTVALID   = "PT92003";	/* 客户端用户名或密码错误    */
	
	public final static String  CONST_RESPCODE_POS_LOCKROUND_FAIL   = "POS0001"; //场次锁定失败
	public final static String  CONST_RESPCODE_POS_UNLOCKROUND_FAIL = "POS0002"; //场次解锁失败
	
	//卡接口业务参数标签名
	public final static String CONST_BIZPARAM_CARDNUMBER = "CARD_NUMBER"; 		//卡号
	public final static String CONST_BIZPARAM_SERIALNUMBER = "SERIAL_NUMBER"; 	//卡序列号
	public final static String CONST_BIZPARAM_PASSWORD = "PASSWORD"; 			//卡密码
	public final static String CONST_BIZPARAM_STARTDATE = "START_DATE"; 		//开始日期 YYYY-MM-DD
	public final static String CONST_BIZPARAM_ENDDATE = "END_DATE"; 			//结束日期 YYYY-MM-DD
	public final static String CONST_BIZPARAM_STARTTIME = "START_TIME"; 		//开始日期 YYYY-MM-DD HH:MM:SS
	public final static String CONST_BIZPARAM_ENDTIME = "END_TIME"; 			//结束日期 YYYY-MM-DD HH:MM:SS
	public final static String CONST_BIZPARAM_CINEMACODE = "CINEMA";			//影院编码
	public final static String CONST_BIZPARAM_CINEMA_ID = "CINEMA_ID";
	public final static String CONST_BIZPARAM_TYPE_CODE = "TYPE_CODE";			//卡类型编码
	public final static String CONST_BIZPARAM_CHANNEL = "CHANNEL";				//售票渠道
	public final static String CONST_BIZPARAM_PERFORMID = "PERFORM_ID";			//场次编号，即影片标识代号
	public final static String CONST_BIZPARAM_CINEMA_CODE = "CINEMA_CODE";//影城CODE
	public final static String CONST_BIZPARAM_PAY_TYPE = "PAY_TYPE";//影城CINEMA_NAME
	public final static String CONST_BIZPARAM_PAY_NUMBER = "PAY_NUMBER";//影城CINEMA_NAME
	public final static String CONST_BIZPARAM_ISSUE_FEE = "ISSUE_FEE";//售卡费用
	public final static String CONST_BIZPARAM_CUST_NAME = "CUST_NAME";//持卡人名
	public final static String CONST_BIZPARAM_MOBILE_NO = "MOBILE_NO";//手机号
	public final static String CONST_BIZPARAM_ORDERID = "ORDER_ID"; 			//订单号
	public final static String CONST_BIZPARAM_CCSORDERID = "CCS_ORDER_NO"; 		//总部系统卡预制申请订单号
	public final static String CONST_BIZPARAM_TICKETTYPEID = "TICKET_TYPE_ID"; 	//票类编码
	public final static String CONST_BIZPARAM_DEDUCTNUM = "DEDUCT_NUM";			//扣减数额
	public final static String CONST_BIZPARAM_TICKETNUM = "TICKET_NUM";			//购票张数
	public final static String CONST_BIZPARAM_CINEMANAME = "CINEMA_NAME";		//影院名称
	public final static String CONST_BIZPARAM_BALCHANGE = "BAL_CHANGE";  		//扣减金额
	public final static String CONST_BIZPARAM_GOODSINFO = "GOODS_INFO";			//卖品信息
	public final static String CONST_BIZPARAM_GOODSID = "GOODS_ID";				//卖品编号
	public final static String CONST_BIZPARAM_GOODSNAME = "GOODS_NAME";			//卖品名称
	public final static String CONST_BIZPARAM_GOODSNUM = "GOODS_NUM";			//卖品数量
	public final static String CONST_BIZPARAM_ORDERTYPE = "ORDER_TYPE";			//订单类型:T G A
	public final static String CONST_BIZPARAM_NEWPWD = "NEW_PWD";				//新密码
	public final static String CONST_BIZPARAM_AMOUNT = "AMOUNT";				//部分退款金额
	
	//卡消费接口    一个订单支持多个场次
	public final static String CONST_BIZPARAM_FILM_INFO = "FILM_INFO";			//卡消费场次购票信息
	public final static String CONST_BIZPARAM_TYPE_NAME = "TYPE_NAME";			//票类名称
	public final static String CONST_BIZPARAM_SUB_AMOUNT = "SUB_AMOUNT";		//扣减金额
	public final static String CONST_BIZPARAM_FILM_NAME = "FILM_NAME";			//影片名称
	

	
	public final static String CONST_BIZPARAM_VOUCHER_NUMBER = "VOUCHER_NUMBER"; //券编号
	public final static String CONST_BIZPARAM_VOUCHER_LOCKNO = "LOCK_NO";        //券锁定编号
	public final static String CONST_BIZPARAM_VOUCHER_INFO = "VOUCHER_INFO";	 //券信息
	public final static String CONST_BIZPARAM_HALL_TYPE    = "HALL_TYPE";		 //影厅类型
	public final static String CONST_BIZPARAM_HALL_ID    = "HALL_ID";		 //影厅编号
	public final static String CONST_BIZPARAM_PROJECT_TYPE = "PROJECT_TYPE";	 //放映制式
	public final static String CONST_BIZPARAM_SCENE_TYPE   = "SCENE_TYPE";		 //场次类型
	public final static String CONST_BIZPARAM_FILM_CODE    = "FILM_CODE";		 //影片编码（8位国家编码）
	public final static String CONST_BIZPARAM_SHOW_TIME	   = "SHOW_TIME";		 //放映时间
	public final static String CONST_BIZPARAM_SHOW_DATE    = "SHOW_DATE";        //放映日期
	public final static String CONST_BIZPARAM_STANDARD_PRICE = "STANDARD_PRICE"; //标准标价
	public final static String CONST_BIZPARAM_LOWEST_PRICE = "LOWEST_PRICE";	 //最低票价
	public final static String CONST_BIZPARAM_LOGIN_ID	   = "LOGIN_ID";		 //
	
	
	
	//会话准备业务参数标签
	public final static String CONST_BIZPARAM_SESSIONID = "SESSION_ID"; //会话编号
	public final static String CONST_BIZPARAM_USERID = "USER_ID";		//用户代码
	public final static String CONST_BIZPARAM_USERACL = "USER_ACL";		//用户权限编号列表
	
	//枚举类型常量
	public final static String CONST_TYPE_ORDER_TICKET = "T"; 		//订单类型：T-票
	public final static String CONST_TYPE_ORDER_GOODS  = "G"; 		//订单类型：G-卖品
	public final static String CONST_TYPE_ORDER_TICKET_GOODS  = "A";//订单类型：票和卖品
	
	public final static String CONST_TYPE_VOUCHER_USETYPE_M = "M";  //券使用方式，M代表代金，T代表兑换, D代表折扣
	public final static String CONST_TYPE_VOUCHER_USETYPE_T = "T";  //券使用方式，M代表代金，T代表兑换, D代表折扣
	public final static String CONST_TYPE_VOUCHER_USETYPE_D = "D";  //券使用方式，M代表代金，T代表兑换, D代表折扣
	
//	000000,111111,222222,333333,444444,555555,666666,777777,888888,999999,123456
	public final static String CHANGE_PASSWORD_NOT_ALLOW_RULE=  "670b14728ad9902aecba32e22fa4f6bd," +
																"96e79218965eb72c92a549dd5a330112," +
																"e3ceb5881a0a1fdaad01296d7554868d," +
																"1a100d2c0dab19c4430e7d73762b3423," +
																"73882ab1fa529d7273da0db6b49cc4f3," +
																"5b1b68a9abf4d2cd155c81a9225fd158," +
																"f379eaf3c831b04de153469d1bec345e," +
																"f63f4fbc9f8c85d409f2f59f2b9e12d5," +
																"21218cca77804d2ba1922c33e0151105," +
																"52c69e3a57331081823331c4e69d3f2e," +
																"e10adc3949ba59abbe56e057f20f883e";
	
	/** WCF URL */
	public static final String NAMED_SMS_URL = "smsUrl=";
	/** 来源系统参数，由统一信息平台注册系统信息时生成 */
	public static final String NAMED_SMS_FROMSYS = "&FromSys=";
	/** 手机号参数名称 */
	public static final String NAMED_SMS_TARGET = "&Target=";
	/** 短信内容参数名称*/
	public static final String NAMED_SMS_MSCONTENT = "&MSContent=";
	/** 短信内容参数名称*/
	public static final String NAMED_SMS_TITLE = "&title=";
	/**
	 * 短信平台地址
	 */
	public static final String SMS_URL = "SMS_URL";
	/**
	 * 短信代理地址
	 */
	public static final String SMS_PROXY_URL = "SMS_PROXY_URL";
	/**
	 * 短信来源系统参数，由统一信息平台注册系统信息时生成
	 */
	public static final String SMS_FROMSYS = "SMS_FROMSYS";
}
