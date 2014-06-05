package com.wanda.mms.control.stream.vo;
/**
 *  
 * 会员影票交易表
 * @author wangshuai 
 * @date 2013-05-28
 */
public class T_ticket_trans_order {
	
	/**
	 * 会员影票交易表ID
	 */
	private long trans_id;
	/**
	 * POS 订单ID
	 */
	private String order_id;
	/**
	 * 影票 金额
	 */
	private long total_amount;
	/**
	 * 影票 数量
	 */
	private int ticket_num;
	/**
	 * 会员手机号
	 */
	private String member_num;
	/**
	 *是否计算 -- 状态
	 */
	private String is_point;
	/**
	 *积分兑换发生在哪个影城，积分规则计算是哪个影城的交易送的积分
	 */
	private String cinema_inner_code;
	/**
	 *积分数
	 */
	private long point;
	/**
	 *计算积分金额
	 */
	private long point_amount;
	
	private long memberId;
	
	private String show_time;
	
	
	public long getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(long transId) {
		trans_id = transId;
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
	public int getTicket_num() {
		return ticket_num;
	}
	public void setTicket_num(int ticketNum) {
		ticket_num = ticketNum;
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
	public String getShow_time() {
		return show_time;
	}
	public void setShow_time(String showTime) {
		show_time = showTime;
	}
	public long getPoint_amount() {
		return point_amount;
	}
	public void setPoint_amount(long pointAmount) {
		point_amount = pointAmount;
	}
	
	
}
