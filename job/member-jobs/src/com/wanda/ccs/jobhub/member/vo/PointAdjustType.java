package com.wanda.ccs.jobhub.member.vo;

/**
 * 积分调整原因类型
 * @author Charlie Zhang
 * 
 */
public enum PointAdjustType {
	AGE_REWARD("1"),        //老会员回馈
	COMPLAIN("2"),          //会员投诉
	ACTIVITY_REWARD("3"),    //活动赠送
	OTHER("4"),              //其他
	MEMBER_DISABLE("6"),     //会员终止
	OFFSET_ZERO("7"),        //负积分回零（新加2013-12-26)
	;
	
	private String code;
	
	private PointAdjustType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static PointAdjustType fromCode(String code) {
		for (PointAdjustType l : PointAdjustType.values()) {
			if (l.code.equals(code)) {
				return l;
			}
		}
		return null;
	}
}
