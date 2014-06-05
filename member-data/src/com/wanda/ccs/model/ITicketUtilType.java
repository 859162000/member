package com.wanda.ccs.model;

import com.wanda.ccs.model.IDimType;

public interface ITicketUtilType extends IDimType {
	/** 营销活动级别 院线级 */
	public static final String CAMPAIGN_LEVEL_GROUP = "G";

	/** 营销活动级别 区域级 */
	public static final String CAMPAIGN_LEVEL_REGION = "R";

	/** 营销活动级别 影城级 */
	public static final String CAMPAIGN_LEVEL_CINEMA = "C";

	/** 票类级别 - 院线级 */
	public static final String TICKET_TYPE_LEVEL_GROUP = "G";

	/** 票类级别 - 区域级 */
	public static final String TICKET_TYPE_LEVEL_REGION = "R";

	/** 票类级别 - 影城级 */
	public static final String TICKET_TYPE_LEVEL_CINEMA = "C";
	
	/** 票类类别 - 储值卡 */
	public static final String TICKET_TYPE_CATEGORY_CARD_M = "MC";
	/** 票类类别 - 记次卡   */
	public static final String TICKET_TYPE_CATEGORY_CARD_T = "TC";
	/** 票类类别 - 权益卡   */
	public static final String TICKET_TYPE_CATEGORY_CARD_D = "DC";
	/** 票类类别 - 兑换券 */
	public static final String TICKET_TYPE_CATEGORY_VOUCHER_T = "TV";
	/** 票类类别 - 折扣券 */
	public static final String TICKET_TYPE_CATEGORY_VOUCHER_D = "DV";
	/** 票类类别 - 代金券 */
	public static final String TICKET_TYPE_CATEGORY_VOUCHER_M = "MV";
	/** 票类类别 - 关联储值卡 */
	public static final String TICKET_TYPE_CATEGORY_CM = "CM";
	/** 票类类别 - 关联记次卡*/
	public static final String TICKET_TYPE_CATEGORY_CT = "CT";
	/** 票类类别 - 关联权益卡*/
	public static final String TICKET_TYPE_CATEGORY_CD = "CD";
	/** 票类类别 - 关联兑换券*/
	public static final String TICKET_TYPE_CATEGORY_VT = "VT";
	/** 票类类别 - 关联折扣券*/
	public static final String TICKET_TYPE_CATEGORY_VD = "VD";
	/** 票类类别 - 其他 */
	public static final String TICKET_TYPE_CATEGORY_OTHERS = "O";
	/** 票类类别 - 积分票 */
	public static final String TICKET_TYPE_CATEGORY_POINT = "P";

	/** 请求类型：新建 */
	public static final String REQ_TYPE_NEW = "N";
	/** 请求类型：编辑 */
	public static final String REQ_TYPE_EDIT = "E";
	/** 请求类型：删除 */
	public static final String REQ_TYPE_DELETE = "X";
	
	/** 缺省的使用限制规则*/
	public static final String TICKET_TYPE_DEFAULT_LIMIT_RULE = "CHANNEL!#!04!#!05!#!06!#!07!#!02!#!03";
}
