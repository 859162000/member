package com.wanda.mms.control.stream.vo;

/**
 *  
 * 会员积分历史归档
 * @author wangshuai 
 * @date 2013-05-23
 */
public class PointHistoryArchive {
	
	/**
	 *积分历史ID#POINT_HISTORY_ID
	 */
	private long pointHistoryid;
	/**
	 *会员ID#MEMBER_ID
	 */
	private long memberid;
	/**
	 *积分操作时间#SET_TIME
	 */	
	private String setTime;
	/**
	 *定级积分#LEVEL_POINT
	 */
	private long level_Point;
	/**
	 *升降级判定票数#TICKET_COUNT
	 */
	private int ticket_Count;
	/**
	 *非定级积分#ACTIVITY_POINT
	 */
	private long activity_Point;
	/**
	 *可兑换积分#EXCHANGE_POINT
	 */
	private long exchange_Point;
	/**
	 *可兑换积分有效期#EXCHANGE_POINT_EXPIRE_TIME
	 */
	private String exchange_Point_Expire_Time;
	/**
	 *积分操作类型#POINT_TYPE
	 */
	private String point_Type;
	/**
	 *积分操作源系统#POINT_SYS
	 */
	private String point_Sys;
	/**
	 *积分操作源单类型#POINT_TRANS_TYPE
	 */
	private String point_Trans_Type;
	/**
	 *积分操作源唯一标识#POINT_TRANS_CODE
	 */
	private String point_Trans_Code;
	/**
	 *积分操作源唯一标识WEBPOS#POINT_TRANS_CODE_WEB
	 */
	private String point_Trans_Code_Web;
	/**
	 *积分调整原因#ADJ_RESION
	 */
	private String adj_Resion;
	/**
	 *变化前可兑换积分余额#ORG_POINT_BALANCE
	 */
	private long org_Point_Balance;
	/**
	 *变化后可兑换积分余额#POINT_BALANCE
	 */
	private long point_Balance;
	/**
	 *是否同步积分账户#IS_SYNC_BALANCE
	 */
	private long is_Sync_Balance;
	/**
	 *逻辑删除标识#ISDELETE
	 */
	private int isdelete;
	/**
	 *创建者#CREATE_BY
	 */
	private String create_By;
	/**
	 *创建时间#CREATE_DATE
	 */
	private String create_Date;
	/**
	 *更新者#UPDATE_BY
	 */
	private String update_By;
	/**
	 *更新时间#UPDATE_DATE
	 */
	private String update_Date;
	/**
	 *版本号#VERSION
	 */
	private int version;
	/**
	 *系统自动归档为"member_sys";其他情况归档员工ID
	 */	
	private String archieve_By;
	/**
	 *归档时间
	 */
	private String archive_Time;
	/**
	 *会员积分历史ID
	 */
	private long member_point_id;
	/**
	 *维表.积分调整原因类型
	 */
	private String adj_reason_type;
	/**
	 *积分调整的原因
	 */
	private String adj_reason;
	/**
	 *接口兑换积分记录pos订单号，礼品订单号
	 */
	private String order_id;
	/**
	 *接口传入,网站兑换礼品时，记录产品命名
	 */
	private String product_name;
	/**
	 *交易订单是否成功 1代表成功，0代表订单失效，同时成一条回退记录
	 */
	private String is_succeed;
	/**
	 *积分兑换发生在哪个影城，积分规则计算是哪个影城的交易送的积分
	 */
	private String cinema_inner_code;
	
	
	public long getPointHistoryid() {
		return pointHistoryid;
	}
	public void setPointHistoryid(long pointHistoryid) {
		this.pointHistoryid = pointHistoryid;
	}
	public long getMemberid() {
		return memberid;
	}
	public void setMemberid(long memberid) {
		this.memberid = memberid;
	}
	public String getSetTime() {
		return setTime;
	}
	public void setSetTime(String setTime) {
		this.setTime = setTime;
	}
	public long getLevel_Point() {
		return level_Point;
	}
	public void setLevel_Point(long levelPoint) {
		level_Point = levelPoint;
	}
	public int getTicket_Count() {
		return ticket_Count;
	}
	public void setTicket_Count(int ticketCount) {
		ticket_Count = ticketCount;
	}
	public long getActivity_Point() {
		return activity_Point;
	}
	public void setActivity_Point(long activityPoint) {
		activity_Point = activityPoint;
	}
	public long getExchange_Point() {
		return exchange_Point;
	}
	public void setExchange_Point(long exchangePoint) {
		exchange_Point = exchangePoint;
	}
	public String getExchange_Point_Expire_Time() {
		return exchange_Point_Expire_Time;
	}
	public void setExchange_Point_Expire_Time(String exchangePointExpireTime) {
		exchange_Point_Expire_Time = exchangePointExpireTime;
	}
	public String getPoint_Type() {
		return point_Type;
	}
	public void setPoint_Type(String pointType) {
		point_Type = pointType;
	}
	public String getPoint_Sys() {
		return point_Sys;
	}
	public void setPoint_Sys(String pointSys) {
		point_Sys = pointSys;
	}
	public String getPoint_Trans_Type() {
		return point_Trans_Type;
	}
	public void setPoint_Trans_Type(String pointTransType) {
		point_Trans_Type = pointTransType;
	}
	public String getPoint_Trans_Code() {
		return point_Trans_Code;
	}
	public void setPoint_Trans_Code(String pointTransCode) {
		point_Trans_Code = pointTransCode;
	}
	public String getPoint_Trans_Code_Web() {
		return point_Trans_Code_Web;
	}
	public void setPoint_Trans_Code_Web(String pointTransCodeWeb) {
		point_Trans_Code_Web = pointTransCodeWeb;
	}
	public String getAdj_Resion() {
		return adj_Resion;
	}
	public void setAdj_Resion(String adjResion) {
		adj_Resion = adjResion;
	}
	public long getOrg_Point_Balance() {
		return org_Point_Balance;
	}
	public void setOrg_Point_Balance(long orgPointBalance) {
		org_Point_Balance = orgPointBalance;
	}
	public long getPoint_Balance() {
		return point_Balance;
	}
	public void setPoint_Balance(long pointBalance) {
		point_Balance = pointBalance;
	}
	public long getIs_Sync_Balance() {
		return is_Sync_Balance;
	}
	public void setIs_Sync_Balance(long isSyncBalance) {
		is_Sync_Balance = isSyncBalance;
	}
	public int getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public String getCreate_By() {
		return create_By;
	}
	public void setCreate_By(String createBy) {
		create_By = createBy;
	}
	public String getCreate_Date() {
		return create_Date;
	}
	public void setCreate_Date(String createDate) {
		create_Date = createDate;
	}
	public String getUpdate_By() {
		return update_By;
	}
	public void setUpdate_By(String updateBy) {
		update_By = updateBy;
	}
	public String getUpdate_Date() {
		return update_Date;
	}
	public void setUpdate_Date(String updateDate) {
		update_Date = updateDate;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getArchieve_By() {
		return archieve_By;
	}
	public void setArchieve_By(String archieveBy) {
		archieve_By = archieveBy;
	}
	public String getArchive_Time() {
		return archive_Time;
	}
	public void setArchive_Time(String archiveTime) {
		archive_Time = archiveTime;
	}
	public long getMember_point_id() {
		return member_point_id;
	}
	public void setMember_point_id(long memberPointId) {
		member_point_id = memberPointId;
	}
	public String getAdj_reason_type() {
		return adj_reason_type;
	}
	public void setAdj_reason_type(String adjReasonType) {
		adj_reason_type = adjReasonType;
	}
	public String getAdj_reason() {
		return adj_reason;
	}
	public void setAdj_reason(String adjReason) {
		adj_reason = adjReason;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String productName) {
		product_name = productName;
	}
	public String getIs_succeed() {
		return is_succeed;
	}
	public void setIs_succeed(String isSucceed) {
		is_succeed = isSucceed;
	}
	public String getCinema_inner_code() {
		return cinema_inner_code;
	}
	public void setCinema_inner_code(String cinemaInnerCode) {
		cinema_inner_code = cinemaInnerCode;
	}

	
	
	
	
	
}
