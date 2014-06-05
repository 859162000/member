package com.wanda.ccs.model;

/**
 * 会员权限代码
 */
public interface IMemberRight{
	/** 权限代码 查看申请单*/
	public static final String MACK_DADDY_CARD_ORDER_VIEW = "member.cardorder.view";
	
	/** 权限代码 提交申请单*/
	public static final String MACK_DADDY_CARD_ORDER_EDIT = "member.cardorder.edit";
	
	/** 权限代码 审批申请单*/
	public static final String MACK_DADDY_CARD_ORDER_APPROVE = "member.cardorder.approve";
	
	/** 权限代码 营销活动编辑*/
	public static final String CAMPAIGN_EDIT = "member.campaign.edit";
	
	/** 权限代码 营销活动查看*/
	public static final String CAMPAIGN_VIEW = "member.campaign.view";
	
	/** 权限代码 活动波次发布*/
	public static final String ACTIVITY_PUBLISH = "member.campaign.publish";
	
	
	/** 权限代码 特殊积分规则编辑*/
	public static final String EXT_POINT_RULE_EDIT = "member.extpointrule.edit";
	
	/** 权限代码 特殊积分规则查看*/
	public static final String EXT_POINT_RULE_VIEW = "member.extpointrule.view";
}
