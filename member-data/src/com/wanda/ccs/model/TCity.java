package com.wanda.ccs.model;

// Generated 2011-10-17 18:01:57 by Hibernate Tools 3.4.0.CR1

import com.xcesys.extras.core.dao.model.AbstractEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * TCity generated by hbm2java
 */
@Entity
@Table(name = "T_CITY")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TCity extends AbstractEntity implements java.io.Serializable {

	private static final long serialVersionUID = 6424815427236128052L;
	private Long id;
	private TProvince tProvince;
	private String name;
	private Long isdelete;
	private Date updatetime;
	private String code;
	private String citylevel;
	private String area;

	public TCity() {
	}

	public TCity(TProvince tProvince, String name, Long isdelete,
			Date updatetime, String code, String citylevel, String area) {
		this.tProvince = tProvince;
		this.name = name;
		this.isdelete = isdelete;
		this.updatetime = updatetime;
		this.code = code;
		this.citylevel = citylevel;
		this.area = area;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_CITY")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "SEQID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROVINCEID")
	public TProvince gettProvince() {
		return this.tProvince;
	}

	public void settProvince(TProvince tProvince) {
		this.tProvince = tProvince;
	}

	@Column(name = "NAME", length = 60)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ISDELETE", precision = 38, scale = 0)
	public Long getIsdelete() {
		return this.isdelete;
	}

	public void setIsdelete(Long isdelete) {
		this.isdelete = isdelete;
	}

	@Column(name = "UPDATETIME")
	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "CODE", length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "CITYLEVEL", length = 20)
	public String getCitylevel() {
		return this.citylevel;
	}

	public void setCitylevel(String citylevel) {
		this.citylevel = citylevel;
	}

	@Column(name = "AREA", length = 20)
	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "城市[name=" + name + "]";
	}

}
