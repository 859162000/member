package com.wanda.ccs.jobhub.member.vo;

/**
 * 积分操作源系统
 * @author Charlie Zhang
 *
 */
public enum PointOperationSys {
	POS("1"),  //POS
	WEB("2"),    //网站
	MBR("3"),      //会员系统
	;
	
	private String code;
	
	private PointOperationSys(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static PointOperationSys fromCode(String code) {
		for (PointOperationSys l : PointOperationSys.values()) {
			if (l.code.equals(code)) {
				return l;
			}
		}
		return null;
	}
}
