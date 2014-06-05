/**
 * 
 */
package com.wanda.mrb.intf.bean;

/**
 * @author xuesi
 *
 */
public class MemberPointHistoryBean {
	private String opTime;//积分操作日期
	private String opType;//积分类型
	private String pointCount;//积分数量
	private String exchangeTime;//积分有效期
	private String product_name;//积分商品名称
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public String getPointCount() {
		return pointCount;
	}
	public void setPointCount(String pointCount) {
		this.pointCount = pointCount;
	}
	public String getExchangeTime() {
		return exchangeTime;
	}
	public void setExchangeTime(String exchangeTime) {
		this.exchangeTime = exchangeTime;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
}
