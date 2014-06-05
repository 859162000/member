/**
 * 
 */
package com.wanda.mrb.intf.bean;

/**
 * @author xuesi
 *
 */
public class MemberTransHistoryBean {
	private String orderNO;//订单编号
	private String purTime;//交易时间
	private String cinemaCode;//影城编码
	private String cinemaName;//影城名称
	private String prdType;//产品类别
	private String prdName;//商品名称
	private String amount;//实际消费金额
	private String isPoint;//是否计算积分
	private String point;//消费积分
	private String pointExchange;//消费积分
	
	public String getPointExchange() {
		return pointExchange;
	}
	public void setPointExchange(String pointExchange) {
		this.pointExchange = pointExchange;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getOrderNO() {
		return orderNO;
	}
	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}
	public String getPurTime() {
		return purTime;
	}
	public void setPurTime(String purTime) {
		this.purTime = purTime;
	}
	public String getCinemaCode() {
		return cinemaCode;
	}
	public void setCinemaCode(String cinemaCode) {
		this.cinemaCode = cinemaCode;
	}
	public String getCinemaName() {
		return cinemaName;
	}
	public void setCinemaName(String cinemaName) {
		this.cinemaName = cinemaName;
	}
	public String getPrdType() {
		return prdType;
	}
	public void setPrdType(String prdType) {
		this.prdType = prdType;
	}
	public String getPrdName() {
		return prdName;
	}
	public void setPrdName(String prdName) {
		this.prdName = prdName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getIsPoint() {
		return isPoint;
	}
	public void setIsPoint(String isPoint) {
		this.isPoint = isPoint;
	}
}
