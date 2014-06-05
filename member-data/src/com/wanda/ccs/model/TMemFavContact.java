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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * TMemFavContact generated by hbm2java
 */
@Entity
@Table(name = "T_MEM_FAV_CONTACT")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TMemFavContact extends BlameableEntity implements java.io.Serializable {

	private static final long serialVersionUID = 377788094268066144L;
	private Long id;
	private TMember tMember;
	private Long memberId;
	private String contactMeans;//"电子邮件:1;直邮邮件:2;电话:3手机:4;其他:5;"

	public TMemFavContact() {
	}


	@SequenceGenerator(name = "generator", sequenceName = "S_T_MEM_FAV_CONTACT",allocationSize=1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "FAV_CONTACT_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID", nullable = false, insertable = false, updatable = false)
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
	@Column(name="CONTACT_MEANS")
	public String getContactMeans() {
		return contactMeans;
	}

	public void setContactMeans(String contactMeans) {
		this.contactMeans = contactMeans;
	}
	
	

}
