package com.wanda.mms.control.stream.vo;
/**
 *  
 * 会员卖品交易表
 * @author wangshuai 
 * @date 2013-05-29
 */
public class T_goods_trans_order {
	
	/**
	 * 卖品交易表ID
	 */
	private long goods_trans_id;
	/**
	 * POS 订单ID
	 */
	private String order_id;
	/**
	 * 卖品 金额
	 */
	private long total_amount;
	/**
	 * 会员手机号
	 */
	private String member_num;
	/**
	 *是否计算 -- 状态
	 */
	private String is_point;
	/**
	 *退货--状态
	 */
	private String TRANS_TYPE;
	/**
	 *积分兑换发生在哪个影城，积分规则计算是哪个影城的交易送的积分
	 */
	private String cinema_inner_code;
	
	private long memberId;
	/**
	 *积分数
	 */
	private long point;
	
	
	public long getGoods_trans_id() {
		return goods_trans_id;
	}
	public void setGoods_trans_id(long goodsTransId) {
		goods_trans_id = goodsTransId;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String orderId) {
		order_id = orderId;
	}
	public long getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(long totalAmount) {
		total_amount = totalAmount;
	}
	public String getMember_num() {
		return member_num;
	}
	public void setMember_num(String memberNum) {
		member_num = memberNum;
	}
	public String getIs_point() {
		return is_point;
	}
	public void setIs_point(String isPoint) {
		is_point = isPoint;
	}
	public String getTRANS_TYPE() {
		return TRANS_TYPE;
	}
	public void setTRANS_TYPE(String tRANSTYPE) {
		TRANS_TYPE = tRANSTYPE;
	}
	public String getCinema_inner_code() {
		return cinema_inner_code;
	}
	public void setCinema_inner_code(String cinemaInnerCode) {
		cinema_inner_code = cinemaInnerCode;
	}
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	public long getPoint() {
		return point;
	}
	public void setPoint(long point) {
		this.point = point;
	}
	
	
	
	

}
