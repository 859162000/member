package com.wanda.ccs.model;

// Generated 2013-4-26 16:06:19 by Hibernate Tools 3.3.0.GA

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * 会员地址
 */
@Entity
@Table(name = "T_MEMBER_ADDR")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TMemberAddr extends BlameableEntity implements java.io.Serializable {
	private static final long serialVersionUID = -7923341555347364139L;
	private Long memberAddrId;
	private TMember tMember;
	private String zipcode;
	private String address1;
	private String address2;
	private String address3;
	private String address4;
	private Long countryId;
	private Long provinceId;
	private Long cityId;
	private TCity tCity;
	private TProvince tProvince;
	private Long memberId;

	public TMemberAddr() {
	}

	public TMemberAddr(Long memberAddrId, TMember tMember, String zipcode,
			String address1, String address2, String address3, String address4,
			Long countryId, Long provinceId, Long cityId) {
		super();
		this.memberAddrId = memberAddrId;
//		this.tMember = tMember;
		this.zipcode = zipcode;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.address4 = address4;
		this.countryId = countryId;
		this.provinceId = provinceId;
		this.cityId = cityId;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_MEMBER_ADDR",allocationSize=1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "MEMBER_ADDR_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.memberAddrId;
	}

	public void setId(Long memberAddrId) {
		this.memberAddrId = memberAddrId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID", nullable = false, insertable = false, updatable = false)
	public TMember gettMember() {
		return this.tMember;
	}

	public void settMember(TMember tMember) {
		this.tMember = tMember;
	}

	@Column(name = "ZIPCODE", length = 128)
	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Column(name = "ADDRESS1", nullable = false, length = 4000)
	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@Column(name = "ADDRESS2", length = 4000)
	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Column(name = "ADDRESS3", length = 4000)
	public String getAddress3() {
		return this.address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	@Column(name = "ADDRESS4", length = 4000)
	public String getAddress4() {
		return this.address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	@Column(name = "COUNTRY_ID", nullable = false, precision = 22, scale = 0)
	public Long getCountryId() {
		return this.countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	@Column(name = "PROVINCE_ID", nullable = false, precision = 22, scale = 0)
	public Long getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	@Column(name = "CITY_ID", nullable = false, precision = 22, scale = 0)
	public Long getCityId() {
		return this.cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CITY_ID", nullable = false, insertable = false, updatable = false)
	public TCity gettCity() {
		return tCity;
	}

	public void settCity(TCity tCity) {
		this.tCity = tCity;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROVINCE_ID", nullable = false, insertable = false, updatable = false)
	public TProvince gettProvince() {
		return tProvince;
	}

	public void settProvince(TProvince tProvince) {
		this.tProvince = tProvince;
	}
     @Column(name="MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
	
}
