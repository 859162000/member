package com.wanda.mrb.intf;

public class ReCodeDef {
	public final static String  CONST_RESPCODE_SUCCESS = "0";
	public final static String  CONST_RESPCODE_PARAM_EMPTY = "C010005";          //参数值为空
	public final static String  CONST_RESPCODE_LACK_OF_PARAM = "C010003";        //缺少参数
	public final static String  CONST_RESPCODE_PARAM_TYPE_ERROR	= "C010006";     //参数值类型错误
	public final static String  CONST_RESPCODE_PARAM_LENGTH_ERROR  = "C010014";  //参数长度错误
	public final static String  CONST_RESPCODE_PARAM_VALUE_ERROR  = "C010015";   //参数值错误

	public final static String  RESPCODE_OTHER_EXCEPTION = "PT90000";            /*其他未知系统exception*/
	public final static String	RESPCODE_GET_DBCONNECTION_FAILED = "PT91001";	 /* 取数据库连接失败	*/
	public final static String	RESPCODE_CLOSE_DBCONNECTION_FAILED = "PT91002";	 /* 关闭数据库连接失败	*/
	public final static String 	RESPCODE_ACCESSLOG_CREATE_FAILED = "PT91003";    /* 接口访问日志创建错误 */
	public final static String 	RESPCODE_TIME_OUT_VALUE = "PT91004";		     /* 业务访问响应超时*/

	public final static String	RESPCODE_CLIENTIP_IN_BLACKLIST 	  = "PT92001";	 /* 客户端IP在黑名单   */
	public final static String	RESPCODE_CLIENTTYPE_NOTIN_WHITELIST = "PT92002"; /* 客户端类型不在白名单     */
	public final static String	RESPCODE_CLIENT_CONNUSER_NOTVALID   = "PT92003"; /* 客户端用户名或密码错误    */
	
	public final static String  RESPCODE_CARD_NOT_EXIST = "M010001";
	
}
