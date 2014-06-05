package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * kpi导入实体类
 * @author yaoguoqing
 *
 */
@Entity
@Table(name="T_MEMBER_KPI")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TMemberKPI extends BlameableEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = -1742900626061724254L;
	
	/**
	 * kpi导入主键
	 */
	private Long id;
	
	/**
	 * 导入年份
	 */
	private String kpiYear;
	
	/**
	 * 导入影城（影城ID）
	 */
	private Long cinemaId;
	
	/**
	 * 导入数量
	 */
	private Long kpiValue;
	
	private String kpiName;
	
	private String kpiType;
	
	public TMemberKPI() {
	}
	
	@SequenceGenerator(name = "generator", sequenceName = "S_T_MEMBER_KPI",allocationSize=1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "MEMBER_KPI_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="KPI_YEAR")
	public String getKpiYear() {
		return kpiYear;
	}
	
	public void setKpiYear(String kpiYear) {
		this.kpiYear = kpiYear;
	}
	
	@Column(name="CINEMA_ID")
	public Long getCinemaId() {
		return cinemaId;
	}
	
	public void setCinemaId(Long cinemaId) {
		this.cinemaId = cinemaId;
	}
	
	@Column(name="KPI_VALUE")
	public Long getKpiValue() {
		return kpiValue;
	}
	
	public void setKpiValue(Long kpiValue) {
		this.kpiValue = kpiValue;
	}
	
	@Column(name="KPI_NAME")
	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	@Column(name="KPI_TYPE")
	public String getKpiType() {
		return kpiType;
	}

	public void setKpiType(String kpiType) {
		this.kpiType = kpiType;
	}
	
	
	
}
