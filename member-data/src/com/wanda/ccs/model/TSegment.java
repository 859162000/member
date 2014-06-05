package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * 客群实体类
 */
@Entity
@Table(name = "T_SEGMENT")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TSegment extends BlameableEntity implements java.io.Serializable {

	private static final long serialVersionUID = -344893235611822108L;
	
	private Long id;//客群ID
	private String name;//客群名称
	private String code;//客群编码
	private Long maxCount;//最大数量
	private Long calCount;//实际数量
	private Date calCountTime;//实际数量计算时间
	private String ownerLevel;//级别
	private String ownerRegion;//所属区域
	private Long ownerCinema;//所属影城

	public TSegment() {
	}
	
	@SequenceGenerator(name = "generator", sequenceName = "S_T_SEGMENT",allocationSize=1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "SEGMENT_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}



	@Column(name="CAL_COUNT")
	public Long getCalCount() {
		return calCount;
	}

	public void setCalCount(Long calCount) {
		this.calCount = calCount;
	}

	@Column(name="CAL_COUNT_TIME")
	public Date getCalCountTime() {
		return calCountTime;
	}

	public void setCalCountTime(Date calCountTime) {
		this.calCountTime = calCountTime;
	}

	@Column(name="MAX_COUNT")
	public Long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Long maxCount) {
		this.maxCount = maxCount;
	}
	
	@Column(name = "OWNER_LEVEL")
	public String getOwnerLevel() {
		return ownerLevel;
	}

	public void setOwnerLevel(String ownerLevel) {
		this.ownerLevel = ownerLevel;
	}
	
	@Column(name = "OWNER_REGION")
	public String getOwnerRegion() {
		return ownerRegion;
	}

	public void setOwnerRegion(String ownerRegion) {
		this.ownerRegion = ownerRegion;
	}
	
	@Column(name = "OWNER_CINEMA")
	public Long getOwnerCinema() {
		return ownerCinema;
	}

	public void setOwnerCinema(Long ownerCinema) {
		this.ownerCinema = ownerCinema;
	}


}
