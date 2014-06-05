package com.wanda.mms.control.stream;

import org.apache.log4j.Logger;

/***
 * 常量数据
 * @author wangshuai 
 * @date 2013-06-18
 *
 */
public class Dimdef {
	static Logger logger = Logger.getLogger(Dimdef.class.getName());

	
	//积分操作类型#POINT_TYPE
	public static final String POINT_TYPE="1021";
	public static final String POINT_TYPE_GM = "1";//1:购买;
	public static final String POINT_TYPE_LP = "2";//2:礼品;
	public static final String POINT_TYPE_JL = "3";//3:奖励;
	public static final String POINT_TYPE_TZ = "4";//4调整;
	public static final String POINT_TYPE_HYZZ = "5";//5:会员终止;
	public static final String POINT_TYPE_JFDH = "6";//6:积分兑换;
	public static final String POINT_TYPE_QT = "99";//2:其他
	//积分操作源系统#POINT_SYS
	public static final String POINT_SYS = "1022"; 
	public static final String POINT_SYS_POS = "1";//1:POS;
	public static final String POINT_SYS_WZ = "2";//2:网站;
	public static final String POINT_SYS_HYXT = "3";//3:会员系统;
	public static final String POINT_SYS_QT = "99";//Others 其他
	//积分操作源单类型#POINT_TRANS_TYPE
	public static final String POINT_TRANS_TYPE = "1023"; 
	public static final String POINT_TRANS_TYPE_JY = "1";//1:交易;)
	public static final String POINT_TRANS_TYPE_JFDH = "2";//2:积分兑换;
	public static final String POINT_TRANS_TYPE_ = "3";//3:特殊积分活动;
	public static final String POINT_TRANS_TYPE_JFQL = "4";//4积分清零;
	
	public static final String POINT = "1024"; //基本积分计算规则
	public static final String POINT_QJGZ = "百分比基本积分规则"; //"1.0";//百分比基本积分规则
	//1024
	//积分调整原因类型#ADJ_REASON_TYPE
	
	//变更原因类型#RESON_TYPE
	public static final String RESON_TYPE = "1";// 
	public static final String RESON_TYPE_LHY = "1";//1:老会员回馈
	public static final String RESON_TYPE_HYTS = "2";//2:会员投诉:
	public static final String RESON_TYPE_CZK = "3";//3储值卡会员转化
	public static final String RESON_TYPE_QT = "4";//4:其他
	
//	会员级别
	public static final String MEM_LEVEL_YI = "1";//一星会员
	public static final String MEM_LEVEL_ER = "2";//二星会员
	public static final String MEM_LEVEL_SAN = "3";//三星会员
	public static final String MEM_LEVEL_SHI = "4";//四星会员

	public static final String IS_POINT = "2";//没有计算过的数据
	
	public static final String MEMBER_SYS = "member_sys";//系统自动
	
	public static final String UP ="UP";//升级
	public static final String DOWN="DOWN"; //降级 级别变更类型(UP:升级,DOWN:降级)
}
