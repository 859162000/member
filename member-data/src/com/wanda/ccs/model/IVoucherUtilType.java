package com.wanda.ccs.model;

public interface IVoucherUtilType extends IDimType{
	/** 券类型状态  可用*/
	public static final String VOUCHER_TYPE_STATUS_E = "E";
	
	/** 券类型状态 不 可用*/
	public static final String VOUCHER_TYPE_STATUS_D = "D";
	
	/** 券类型状态  删除*/
	public static final String VOUCHER_TYPE_STATUS_X = "X";
	
	/** 券类型适用方式  代金*/
	public static final String VOUCHER_TYPE_USERTYPE_M = "M";
	
	/** 券类型使用方式  兑换*/
	public static final String VOUCHER_TYPE_USERTYPE_T = "T";
	
	/** 券类型使用方式  折扣*/
	public static final String VOUCHER_TYPE_USERTYPE_D = "D";
	
	/** 券类型的级别 院线级*/
	public static final String VOUCHER_TYPE_LEVEL_GROUP = "G";
	
	/** 券类型的级别 区域级*/
	public static final String VOUCHER_TYPE_LEVEL_REGION = "R";
	
	/** 票类类型:卡 */
	public static final String TICKET_TYPE_CATEGORY_CARD = "C";
	/** 票类类型:券 */
	public static final String TICKET_TYPE_CATEGORY_VOUCHER = "V";
	/** 票类类型:其他 */
	public static final String TICKET_TYPE_CATEGORY_OTHERS = "O";
	/** 票类类型:代金券 */
	public static final String TICKET_TYPE_CATEGORY_V = "V";
	
	
	
	/** 请求类型：新建 */
	public static final String REQ_TYPE_NEW = "N";
	/** 请求类型：编辑 */
	public static final String REQ_TYPE_EDIT = "E";
	/** 请求类型：删除 */
	public static final String REQ_TYPE_DELETE = "X";
	/** 请求类型：券 */
	public static final String REQ_TYPE_VOUCHER = "V";
	
	public static final String FLAG_YES = "Y";
	public static final String FLAG_NO = "N";
	
	/** 劵销售单审批权限*/
	public static final String VOUCHER_ORDER_APPROVE = "voucher.order.approve";
}
