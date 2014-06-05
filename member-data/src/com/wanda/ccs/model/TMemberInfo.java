package com.wanda.ccs.model;

// Generated 2013-4-26 16:06:19 by Hibernate Tools 3.3.0.GA

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * 会员详细信息
 */
@Entity
@Table(name = "T_MEMBER_INFO")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TMemberInfo extends BlameableEntity implements
		java.io.Serializable {
	private static final long serialVersionUID = 1516518967179893077L;
	private Long id;
	private TMember tMember;
	private Long memberId;

	private Long manageCinema;
	private Integer education;// 初中及以下:1;高中或职高:2;大学本科或专科:3;硕士生:4;博士生或更高:5;
	private Integer occupation;// "高管:1;全职职员:2;自由职业者:3;私营业主:4;学生:5 ;家庭主妇:6;退休:7 ;其他:8"
	private Integer income;// 1000元以下:1;1001-2999元:2;3000-5999元:3;6000-10000元:4;10000以上:5;
	private String marryStatus="O";// 未婚:N;已婚:Y
	private Integer childNumber;// 无孩:0;1个孩子:1;2个孩子:2;3个及以上:3;
	private Integer fqCinemaDist;// "1：<2公里,2:2-4公里,3:4-6公里,4:>6公里
	private Integer fqCinemaTime;// "1:<15分钟;2:15-30分钟,3:30分钟-1小时,4:>1小时
	private String mobileOpten;// "N:未订阅,Y已订阅;C已退订
	private Integer idCardType;// 1=身份证、2=护照3=军官证4=警官证5=士兵证 6=其它
	private String idCardNo;// 证件号码/掩码
	private String idCardHashNo;// 证件号码，MD5码
	private String weibo;
	private Long qq;
	private String douban;

	public TMemberInfo() {
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_MEMBER_INFO", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "MEMBER_INFO_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID", insertable = false, updatable = false)
	public TMember gettMember() {
		return this.tMember;
	}

	public void settMember(TMember tMember) {
		this.tMember = tMember;
	}

	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "MANAGE_CINEMA_ID")
	public Long getManageCinema() {
		return manageCinema;
	}

	public void setManageCinema(Long manageCinema) {
		this.manageCinema = manageCinema;
	}

	@Column(name = "EDUCATION")
	public Integer getEducation() {
		return education;
	}

	public void setEducation(Integer education) {
		this.education = education;
	}

	@Column(name = "OCCUPATION")
	public Integer getOccupation() {
		return occupation;
	}

	public void setOccupation(Integer occupation) {
		this.occupation = occupation;
	}

	@Column(name = "INCOME")
	public Integer getIncome() {
		return income;
	}

	public void setIncome(Integer income) {
		this.income = income;
	}

	@Column(name = "MARRY_STATUS")
	public String getMarryStatus() {
		return marryStatus;
	}

	public void setMarryStatus(String marryStatus) {
		this.marryStatus = marryStatus;
	}

	@Column(name = "CHILD_NUMBER")
	public Integer getChildNumber() {
		return childNumber;
	}

	public void setChildNumber(Integer childNumber) {
		this.childNumber = childNumber;
	}

	@Column(name = "FQ_CINEMA_DIST")
	public Integer getFqCinemaDist() {
		return fqCinemaDist;
	}

	public void setFqCinemaDist(Integer fqCinemaDist) {
		this.fqCinemaDist = fqCinemaDist;
	}

	@Column(name = "FQ_CINEMA_TIME")
	public Integer getFqCinemaTime() {
		return fqCinemaTime;
	}

	public void setFqCinemaTime(Integer fqCinemaTime) {
		this.fqCinemaTime = fqCinemaTime;
	}

	@Column(name = "MOBILE_OPTIN")
	public String getMobileOpten() {
		return mobileOpten;
	}

	public void setMobileOpten(String mobileOpten) {
		this.mobileOpten = mobileOpten;
	}

	@Column(name = "IDCARD_TYPE")
	public Integer getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(Integer idCardType) {
		this.idCardType = idCardType;
	}

	@Column(name = "IDCARD_NO")
	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	@Column(name = "IDCARD_HASHNO")
	public String getIdCardHashNo() {
		return idCardHashNo;
	}

	public void setIdCardHashNo(String idCardHashNo) {
		this.idCardHashNo = idCardHashNo;
	}
	@Column(name = "WEIBO")
	public String getWeibo() {
		return weibo;
	}

	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	@Column(name = "QQ")
	public Long getQq() {
		return qq;
	}

	public void setQq(Long qq) {
		this.qq = qq;
	}
	@Column(name = "DOUBAN")
	public String getDouban() {
		return douban;
	}

	public void setDouban(String douban) {
		this.douban = douban;
	}

}
