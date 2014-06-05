package com.wanda.ccs.model;


public interface ICardUtilType extends IDimType {
	/** 卡类型的级别 院线级 */
	public static final String CARD_LEVEL_GROUP = "G";

	/** 卡类型的级别 区域级 */
	public static final String CARD_LEVEL_REGION = "R";

	/** 卡值类型 储值卡 */
	public static final String CARD_VALUE_TYPE_M = "M";

	/** 卡值类型 记次卡 */
	public static final String CARD_VALUE_TYPE_T = "T";

	/** 卡值类型 折扣 */
	public static final String CARD_VALUE_TYPE_D = "D";

	/** 卡交易类型 售票 */
	public static final String CARD_TICKET_EXTYPE = "S";

	/** 卡交易类型 售票 */
	public static final String CARD_TICKET_RETYPE = "R";

	/** 票类类型:卡 */
	public static final String TICKET_TYPE_CATEGORY_CARD = "C";
	/** 票类类型:券 */
	public static final String TICKET_TYPE_CATEGORY_VOUCHER = "V";
	/** 票类类型:其他 */
	public static final String TICKET_TYPE_CATEGORY_OTHERS = "O";

	/** 用户的级别 院线级 */
	public static final String USER_LEVEL_GROUP = "GROUP";
	/** 用户的级别 区域级 */
	public static final String USER_LEVEL_REGION = "REGION";
	/** 用户的级别 影城级 */
	public static final String USER_LEVEL_CINEMA = "CINEMA";

	/** 操作的名称 */
	/** 提交申请 */
	public static final String REQ_ACTION_NAME_SUBMITREQ = "提交申请";
	/** 审核通过 */
	public static final String REQ_ACTION_NAME_APPROVEPASS = "审核通过";
	/** 审核不通过，返回修改 */
	public static final String REQ_ACTION_NAME_APPROVERESET = "发回返修";
	/** 审核不通过，结束审核 */
	public static final String REQ_ACTION_NAME_APPROVEOVER = "结束审核";
	/** 取消审核 */
	public static final String REQ_ACTION_NAME_APPROVECANCLE = "取消审核";

	/** 请求类型：新建 */
	public static final String REQ_TYPE_NEW = "N";
	/** 请求类型：编辑 */
	public static final String REQ_TYPE_EDIT = "E";
	/** 请求类型：删除 */
	public static final String REQ_TYPE_DELETE = "X";
	/** 请求类型：卡 */
	public static final String REQ_TYPE_CARD = "C";
	
	/** 权限代码  负充*/
	public static final String RIGHT_CODE_RECHANGE = "card.subtractrechange";
	/** 权限代码 无卡操作*/
	public static final String RIGHT_CODE_NOCARD_EDIT = "card.nocard.edit";
	
	/** 权限代码  确认挂失*/
	public static final String RIGHT_CODE_LOST = "card.affirmlost";
	/** 权限代码 修改工本费*/
	public static final String RIGHT_CODE_CHANGEFEE = "card.editissuefee";
	/** 权限代码 修改激活费用*/
	public static final String RIGHT_CODE_ACTIVATIONFEE = "card.editactivationfee";
	/** 权限代码 退卡*/
	public static final String RIGHT_CODE_RETURN = "card.affirmreturn";
	/** 权限代码  后台修改卡密码*/
	public static final String RIGTH_CARD_BACK_EDITPWD = "card.back.editpwd";
	/** 权限代码  页面修改卡状态 卡激活*/
	public static final String RIGTH_CARD_BACK_EDITSTAUTS = "card.back.editstauts";
	
	public static final String[] CARD_NOT_PASSWORDS = new String[]{"000000","111111","222222","333333","444444","555555","666666","777777","888888","999999","123456"};
	/** 卡的初始密码*/
	public static final String INITIAL_CARD_PASSWORD = "888888";
	
	/** 卡消费渠道  编号BO 名称 01 前台售票窗*/
	public static final String CARD_TRANS_CHANNEL_01 = "01";
	/** 卡消费渠道  编号WEB 名称 02 WEB*/
	public static final String CARD_TRANS_CHANNEL_02 = "02";
	/** 卡消费渠道  编号ISMS 名称 03 手机购票*/
	public static final String CARD_TRANS_CHANNEL_03 = "03";
	/** 卡消费渠道  编号WAP 名称 04 手机购票*/
	public static final String CARD_TRANS_CHANNEL_04 = "04";
	/** 卡消费渠道  编号KIOSK 名称 05 自动售票机购票*/
	public static final String CARD_TRANS_CHANNEL_05 = "05";
	/** 卡消费渠道  编号IVRS 名称 06 语音订票*/
	public static final String CARD_TRANS_CHANNEL_06 = "06";
	/** 卡消费渠道  编号TUOWEI 名称 07 拓维电子商务*/
	public static final String CARD_TRANS_CHANNEL_07 = "07";
	
	/** 卡前台提示信息的字体大小*/
	public static final String FRONT_FONT_SIZE = "5";
	
	/** 卡消费类型  T-购票，G-购卖品，A-既购票也购卖品*/
	public static final String CARD_TRANS_CONTTYPE_T="T";
	public static final String CARD_TRANS_CONTTYPE_G="G";
	public static final String CARD_TRANS_CONTTYPE_A="A";
	
}
