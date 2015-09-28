package com.wanda.mrb.intf;

import java.util.concurrent.atomic.AtomicLong;

public class ConstDef {
	public static AtomicLong REQ_ID=new AtomicLong(0);
	public static final String XML_ATTR_INTFCODE = "icode";
	public static final String XML_ATTR_INTFVER = "iver";
	public static final String XML_ATTR_CLIENTTYPE = "clienttype";
	public static final String XML_ATTR_SERIALNUM = "serialnum";
	public static final String XML_ATTR_USERNAME = "user";
	public static final String XML_ATTR_PASSWD = "pwd";
	
	public static final String REQ_PARAM_XML = "xml";/*输入xml参数的名字*/
	public final static String CONST_INTFCODE_M_REGISTER = "M1001";
	public final static String CONST_INTFCODE_M_UPDATEMEMBER = "M1002";
	public final static String CONST_INTFCODE_M_CHECKMEMBER = "M1003";
	public final static String CONST_INTFCODE_M_GETTRANSHISTORY = "M1004";
	public final static String CONST_INTFCODE_M_GETPOINTSHISTORY = "M1005";
	public final static String CONST_INTFCODE_M_GETPOINTSBALANCE = "M1006";
	public final static String CONST_INTFCODE_M_REDEEMONLINE = "M1007";
	public final static String CONST_INTFCODE_M_REDEEMRESULT = "M1008";
	public final static String CONST_INTFCODE_M_BINDVOUCHER = "M1009";
	public final static String CONST_INTFCODE_M_QUERYVOUCHERLIST = "M1010";
	public final static String CONST_INTFCODE_M_QUERYCARDLIST = "M1011";
	public final static String CONST_INTFCODE_M_ISSUEMEMBERCARD = "M1012";
	
	public final static String CONST_INTFCODE_M_SENDCHECKCODE = "M1013";
	public final static String CONST_INTFCODE_M_PASSCHECKCODE = "M1014";
	public final static String CONST_INTFCODE_M_MEMBERJOINCARD = "M1015";
	public final static String CONST_INTFCODE_M_WEBTRANSORDER = "M1016";
	public final static String CONST_INTFCODE_M_REWARD = "M1018";
	public final static String CONST_INTFCODE_M_BATCHBIND = "M1019";
	public final static String CONST_INTFCODE_M_BING_BYMOBILE = "M1020";
	
	//业务参数标签名
	public final static String CONST_INTFCODE_M_REGISTER_NAME = "NAME";
	public final static String CONST_INTFCODE_M_REGISTER_MOBILE = "MOBILE";
	public final static String CONST_INTFCODE_M_REGISTER_TEL = "TEL";
	public final static String CONST_INTFCODE_M_REGISTER_IDCARD_NO = "ID_CARD_NO";
	public final static String CONST_INTFCODE_M_REGISTER_IDCARD_HASHNO = "ID_CARD_HASH_NO";
	public final static String CONST_INTFCODE_M_REGISTER_IDENTITY_TYPE = "IDENTITY_TYPE";
	public final static String CONST_INTFCODE_M_REGISTER_EMAIL = "EMAIL";
	public final static String CONST_INTFCODE_M_REGISTER_ADDRESS = "ADDRESS";
	public final static String CONST_INTFCODE_M_REGISTER_PROVINCEID = "PROVINCE_ID";
	public final static String CONST_INTFCODE_M_REGISTER_CITYID = "CITY_ID";
	
	public final static String CONST_INTFCODE_M_REGISTER_ZIP = "ZIP";
	public final static String CONST_INTFCODE_M_REGISTER_CONTACTMEANS = "CONTACT_MEANS";
	public final static String CONST_INTFCODE_M_REGISTER_BIRTHDAY = "BIRTHDAY";
	public final static String CONST_INTFCODE_M_REGISTER_GENDER = "GENDER";
	public final static String CONST_INTFCODE_M_REGISTER_MARITALSTATUS = "MARITAL_STATUS";
	public final static String CONST_INTFCODE_M_REGISTER_CHILDNUMBER = "CHILD_NUMBER";
	public final static String CONST_INTFCODE_M_REGISTER_EDUCATION = "EDUCATION";
	public final static String CONST_INTFCODE_M_REGISTER_OCCUPATION = "OCCUPATION";
	public final static String CONST_INTFCODE_M_REGISTER_INCOME = "INCOME";
	public final static String CONST_INTFCODE_M_REGISTER_FILMTYPES = "FILM_TYPES";
	public final static String CONST_INTFCODE_M_REGISTER_FQCINEMADIST = "FQ_CINEMA_DIST";
	public final static String CONST_INTFCODE_M_REGISTER_FQCINEMATIME = "FQ_CINEMA_TIME";
	public final static String CONST_INTFCODE_M_REGISTER_MOBILEOPTION = "MOBILE_OPTIN";
	public final static String CONST_INTFCODE_M_REGISTER_COUNTER = "COUNTER";
	public final static String CONST_INTFCODE_M_REGISTER_OPERATOR = "OPERATOR";
	public final static String CONST_INTFCODE_M_REGISTER_OPERATORNAME = "OPERATOR_NAME";
	public final static String CONST_INTFCODE_M_REGISTER_DTSID = "DTSID";
	public final static String CONST_INTFCODE_M_REGISTER_SOURCEFOR = "SOURCE_FOR";
	public final static String CONST_INTFCODE_M_REGISTER_OTHERNO = "OTHER_NO";
	public final static String CONST_INTFCODE_M_REGISTER_ISTALK = "ISTALK";
	public final static String CONST_INTFCODE_M_REGISTER_MEMBERNO = "MEMBER_NO";
	public final static String CONST_INTFCODE_M_REGISTER_CARD_NUMBER = "CARD_NUMBER";
	public final static String CONST_INTFCODE_M_REGISTER_CARD_TYPE = "CARD_TYPE";
	public final static String CONST_INTFCODE_M_REGISTER_FAV_CINEMA = "FAV_CINEMA";
	public final static String CONST_INTFCODE_M_REGISTER_DATE = "REGIST_DATE";
	public final static String CONST_INTFCODE_M_REGISTER_SOURCEFOR_EXT = "SOURCE_FOR_EXT";
	
	public final static String CONST_INTFCODE_M_GET_TRANS_HISTORY_START_DATE = "START_DATE";
	public final static String CONST_INTFCODE_M_GET_TRANS_HISTORY_END_DATE = "END_DATE";
	
	public final static String CONST_RESPCODE_MOBILE_NOT_RIGHT = "C010001"; //手机号不正确
	public final static String CONST_RESPCODE_MOBILE_IS_EXIST = "C010002"; //手机号存在
	public final static String CONST_RESPCODE_EMAIL_NOT_RIGHT = "C010003"; //邮箱不正确
	public final static String  CONST_RESPCODE_PARAM_EMPTY			= "C010005"; //参数值为空
	public final static String  CONST_RESPCODE_AESEN_EXCEPTION      = "PT90001"; //AES 加密错误
	public final static String  CONST_RESPCODE_AESDEC_EXCEPTION      = "PT90002"; //AES 解密错误
	
	public final static String CONST_BIZPARAM_FILM_INFO = "FILM_INFO";//场次购票信息
	public final static String CONST_BIZPARAM_GOODSINFO = "GOODS_INFO";//卖品信息
}