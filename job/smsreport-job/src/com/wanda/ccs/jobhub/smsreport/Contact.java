package com.wanda.ccs.jobhub.smsreport;

public class Contact {
	
	/* 执行中 */
	public final static int EXECUTE = 1;
	/* 成功 */
	public final static int SUCCESS = 2;
	/* 失败 */
	public final static int FIAL = 3;
	
	// 短信发送编码
	public static final String FROM_SYS = "YXZBXT001";
	
	public static final String VAILD_SMS = "{1}的数据校验发现错误，请到T_IM_SENDTOMSG表中进行修正";
	
	public static final boolean VALID_FALG = false;
}
