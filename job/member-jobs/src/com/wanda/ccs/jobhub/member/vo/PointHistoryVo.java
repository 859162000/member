package com.wanda.ccs.jobhub.member.vo;

import java.sql.Timestamp;

/**
 *  
 * 会员积分历史对象
 * 
 * @author Charlie Zhang 
 * @date 2013-12-21
 */
public class PointHistoryVo extends BaseAuditVo {

	private Long pointHistoryId;

	private Long memberId;
	
	private Long memberPointId;

	private Timestamp setTime;

	private Long levelPoint = 0L;
	/**
	 *升降级判定票数#TICKET_COUNT
	 */
	private Integer ticketCount = 0;
	/**
	 *非定级积分#ACTIVITY_POINT
	 */
	private Long activityPoint = 0L;
	/**
	 *可兑换积分#EXCHANGE_POINT
	 */
	private Long exchangePoint = 0L;
	/**
	 *可兑换积分有效期#EXCHANGE_POINT_EXPIRE_TIME
	 */
	private Timestamp exchangePointExpireTime;
	/**
	 *积分操作类型#POINT_TYPE
	 */
	private String pointType;
	/**
	 *积分操作源系统#POINT_SYS
	 */
	private String pointSys;
	/**
	 *积分操作源单类型#POINT_TRANS_TYPE
	 */
	private String pointTransType;
	/**
	 *积分操作源唯一标识#POINT_TRANS_CODE
	 */
	private String pointTransCode;
	/**
	 *积分操作源唯一标识WEBPOS#POINT_TRANS_CODE_WEB
	 */
	private String pointTransCodeWeb;
	/**
	 *积分调整原因#ADJ_RESION
	 */
	private String adjResion;
	/**
	 *变化前可兑换积分余额#ORG_POINT_BALANCE
	 */
	private Long orgPointBalance = 0L;
	/**
	 *变化后可兑换积分余额#POINT_BALANCE
	 */
	private Long pointBalance = 0L;
	/**
	 *是否同步积分账户#IS_SYNC_BALANCE
	 */
	private Long isSyncBalance = 0L;
	/**
	 *逻辑删除标识#ISDELETE
	 */
	private Boolean isdelete = false;
	/**
	 *积分调整的原因
	 */	
	private String adjReason;
	/**
	 *积分调整原因类型
	 */	
	private String adjReasonType;
	/**
	 *当年即将过期可兑换积分
	 */	
	private Long exgExpirePointBalance;
	/**
	 *接口兑换积分记录pos订单号，礼品订单号
	 */		
	private String orderId;
	/**
	 *接口传入,网站兑换礼品时，记录产品命名
	 */		
	private String productName;
	/**
	 *交易订单是否成功 1代表成功，0代表订单失效，同时成一条回退记录
	 */
	private Boolean isSucceed;
	/**
	 *积分兑换发生在哪个影城，积分规则计算是哪个影城的交易送的积分
	 */
	private String cinemaInnerCode;
	
	private String recalcuStatus;
	
	private Timestamp recalcuDate;
	
	private String pointExtRuleId;
	
	private String transType;
	
	private Long tTransOrderId;
	
	private Long tTransDetailId;
	
	/**
	 * 安克诚历史导入
	 */
	private String isHistory = "0";
	
	public Long getPointHistoryId() {
		return pointHistoryId;
	}
	public void setPointHistoryId(Long pointHistoryId) {
		this.pointHistoryId = pointHistoryId;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Long getMemberPointId() {
		return memberPointId;
	}
	public void setMemberPointId(Long memberPointId) {
		this.memberPointId = memberPointId;
	}
	public Timestamp getSetTime() {
		return setTime;
	}
	public void setSetTime(Timestamp setTime) {
		this.setTime = setTime;
	}
	public Long getLevelPoint() {
		return levelPoint;
	}
	public void setLevelPoint(Long levelPoint) {
		this.levelPoint = levelPoint;
	}
	public Integer getTicketCount() {
		return ticketCount;
	}
	public void setTicketCount(Integer ticketCount) {
		this.ticketCount = ticketCount;
	}
	public Long getActivityPoint() {
		return activityPoint;
	}
	public void setActivityPoint(Long activityPoint) {
		this.activityPoint = activityPoint;
	}
	public Long getExchangePoint() {
		return exchangePoint;
	}
	public void setExchangePoint(Long exchangePoint) {
		this.exchangePoint = exchangePoint;
	}
	public Timestamp getExchangePointExpireTime() {
		return exchangePointExpireTime;
	}
	public void setExchangePointExpireTime(Timestamp exchangePointExpireTime) {
		this.exchangePointExpireTime = exchangePointExpireTime;
	}
	public String getPointType() {
		return pointType;
	}
	public void setPointType(String pointType) {
		this.pointType = pointType;
	}
	public String getPointSys() {
		return pointSys;
	}
	public void setPointSys(String pointSys) {
		this.pointSys = pointSys;
	}
	public String getPointTransType() {
		return pointTransType;
	}
	public void setPointTransType(String pointTransType) {
		this.pointTransType = pointTransType;
	}
	public String getPointTransCode() {
		return pointTransCode;
	}
	public void setPointTransCode(String pointTransCode) {
		this.pointTransCode = pointTransCode;
	}
	public String getPointTransCodeWeb() {
		return pointTransCodeWeb;
	}
	public void setPointTransCodeWeb(String pointTransCodeWeb) {
		this.pointTransCodeWeb = pointTransCodeWeb;
	}
	public String getAdjResion() {
		return adjResion;
	}
	public void setAdjResion(String adjResion) {
		this.adjResion = adjResion;
	}
	public Long getOrgPointBalance() {
		return orgPointBalance;
	}
	public void setOrgPointBalance(Long orgPointBalance) {
		this.orgPointBalance = orgPointBalance;
	}
	public Long getPointBalance() {
		return pointBalance;
	}
	public void setPointBalance(Long pointBalance) {
		this.pointBalance = pointBalance;
	}
	public Long getIsSyncBalance() {
		return isSyncBalance;
	}
	public void setIsSyncBalance(Long isSyncBalance) {
		this.isSyncBalance = isSyncBalance;
	}
	public Boolean getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(Boolean isdelete) {
		this.isdelete = isdelete;
	}
	public String getAdjReason() {
		return adjReason;
	}
	public void setAdjReason(String adjReason) {
		this.adjReason = adjReason;
	}
	public String getAdjReasonType() {
		return adjReasonType;
	}
	public void setAdjReasonType(String adjReasonType) {
		this.adjReasonType = adjReasonType;
	}
	public Long getExgExpirePointBalance() {
		return exgExpirePointBalance;
	}
	public void setExgExpirePointBalance(Long exgExpirePointBalance) {
		this.exgExpirePointBalance = exgExpirePointBalance;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Boolean getIsSucceed() {
		return isSucceed;
	}
	public void setIsSucceed(Boolean isSucceed) {
		this.isSucceed = isSucceed;
	}
	public String getCinemaInnerCode() {
		return cinemaInnerCode;
	}
	public void setCinemaInnerCode(String cinemaInnerCode) {
		this.cinemaInnerCode = cinemaInnerCode;
	}
	public String getIsHistory() {
		return isHistory;
	}
	public void setIsHistory(String isHistory) {
		this.isHistory = isHistory;
	}
	public String retrieveCode() {
		return this.getPointHistoryId().toString();
	}

	public Long retrieveSeqId() {
		return this.getPointHistoryId();
	}
	public String getRecalcuStatus() {
		return recalcuStatus;
	}
	public void setRecalcuStatus(String recalcuStatus) {
		this.recalcuStatus = recalcuStatus;
	}
	public Timestamp getRecalcuDate() {
		return recalcuDate;
	}
	public void setRecalcuDate(Timestamp recalcuDate) {
		this.recalcuDate = recalcuDate;
	}
	public String getPointExtRuleId() {
		return pointExtRuleId;
	}
	public void setPointExtRuleId(String pointExtRuleId) {
		this.pointExtRuleId = pointExtRuleId;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public Long getTTransOrderId() {
		return tTransOrderId;
	}
	public void setTTransOrderId(Long tTransOrderId) {
		this.tTransOrderId = tTransOrderId;
	}
	public Long getTTransDetailId() {
		return tTransDetailId;
	}
	public void setTTransDetailId(Long tTransDetailId) {
		this.tTransDetailId = tTransDetailId;
	}
	
	

}
