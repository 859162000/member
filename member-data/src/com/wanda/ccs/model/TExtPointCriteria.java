package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * 特殊积分条件
 */
@Entity
@Table(name = "T_EXT_POINT_CRITERIA")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TExtPointCriteria extends BlameableEntity implements
		java.io.Serializable {

	private static final long serialVersionUID = -344893235611822108L;

	private Long id;// 特殊积分条件ID
	private String name;// 特殊积分条件名称
	private String code;// 特殊积分条件编码
	private int type;

	public TExtPointCriteria() {
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_EXT_POINT_CRITERIA", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "EXT_POINT_CRITERIA_ID", unique = true, nullable = false, precision = 22, scale = 0)
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

	@Transient
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

}
