package com.wanda.ccs.model;

// Generated 2013-4-26 16:06:19 by Hibernate Tools 3.3.0.GA

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.AbstractEntity;

/**
 * 会员票房消费信息
 */
@Entity
@Table(name = "T_TICKET_TRANS_ORDER")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TTicketTransOrder extends AbstractEntity implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8789053850767616939L;

	private Long id;
	private String orderId;
	private Long totalAmount;
	private String memberNum;
	private String point;
	private String tickNum;
	private Long memberId;
	private TMember tMember;
	private String filmCode;// 影片编码
	private String filmName;// 影片名称
	private String hallNum;// 影厅编码
	private Date showTime;// 放映时间
	private Date bizDate;// 放映营业日
	private Date transTime;// 交易时间
	private String cinemaInnerCode;//影城内码

	@SequenceGenerator(name = "generator", sequenceName = "S_T_TICKET_TRANS_ORDER", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "TRANS_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ORDER_ID")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(name = "TOTAL_AMOUNT")
	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "MEMBER_NUM")
	public String getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(String memberNum) {
		this.memberNum = memberNum;
	}

	@Column(name = "IS_POINT")
	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	@Column(name = "TICKET_NUM")
	public String getTickNum() {
		return tickNum;
	}

	public void setTickNum(String tickNum) {
		this.tickNum = tickNum;
	}

	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID", nullable = false, insertable = false, updatable = false)
	public TMember gettMember() {
		return tMember;
	}

	public void settMember(TMember tMember) {
		this.tMember = tMember;
	}

	@Column(name = "FILM_CODE")
	public String getFilmCode() {
		return filmCode;
	}

	public void setFilmCode(String filmCode) {
		this.filmCode = filmCode;
	}

	@Column(name = "FILM_NAME")
	public String getFilmName() {
		return filmName;
	}

	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}

	@Column(name = "HALL_NUM")
	public String getHallNum() {
		return hallNum;
	}

	public void setHallNum(String hallNum) {
		this.hallNum = hallNum;
	}

	@Column(name = "SHOW_TIME")
	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	@Column(name = "BIZ_DATE")
	public Date getBizDate() {
		return bizDate;
	}

	public void setBizDate(Date bizDate) {
		this.bizDate = bizDate;
	}

	@Column(name = "TRANS_TIME")
	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	@Column(name = "CINEMA_INNER_CODE")
	public String getCinemaInnerCode() {
		return cinemaInnerCode;
	}

	public void setCinemaInnerCode(String cinemaInnerCode) {
		this.cinemaInnerCode = cinemaInnerCode;
	}

	
}
