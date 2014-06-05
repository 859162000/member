package com.wanda.ccs.jobhub.member.vo;

public enum PointOperationType {
	PURCHASE("1"),  //购买, 一般指购买影票或卖品等 
	GIFT("2"),      //赠送, 安客诚导入是使用
	REWARD("3"),    //奖励, 特殊积分时给的额外积分
	ADJUST("4"),    //调整，可正可负
	EXPIRE("5"),     //过期, 年底积分清零时使用
	EXCHANGE("6"),  // 线下兑换
	EXCHANGE_ONLINE("7"),     // 网站积分兑换(未用)
	OTHER("8"),    //其他 
	;
	
	private String code;
	
	private PointOperationType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static PointOperationType fromCode(String code) {
		for (PointOperationType l : PointOperationType.values()) {
			if (l.code.equals(code)) {
				return l;
			}
		}
		return null;
	}
}
