package com.wanda.ccs.model;

// Generated Nov 30, 2012 1:59:30 PM by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.VersionableEntity;

/**
 * TConCatExtProp generated by hbm2java
 * 品项分类与品项属性关系表
 */
@Entity
@Table(name = "T_CON_CAT_EXT_PROP")
public class TConCatExtProp extends VersionableEntity implements
		java.io.Serializable {
	private static final long serialVersionUID = -3927777679954012515L;
	
	private Long id;
	private TConExtProperty tConExtProperty; //品项属性
	private TConCategory tConCategory;//品项分类
	private Long orderIdx = 1L;//显示顺序
	private Long tConExtPropertyId;//品项属性id
	private Long tConCategoryId;//品项分类id
	private boolean required = false;//是否必输项
	private List<TConExtPropValue> tConExtPropValues = new ArrayList<TConExtPropValue>();


	public TConCatExtProp() {
	}


	public TConCatExtProp(TConExtProperty tConExtProperty,
			TConCategory tConCategory) {
		this.tConExtProperty = tConExtProperty;
		this.tConCategory = tConCategory;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_CON_CAT_EXT_PROP")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "CON_CAT_EXT_PROP_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CON_EXT_PROPERTY_ID",insertable = false, updatable = false)
	public TConExtProperty gettConExtProperty() {
		return this.tConExtProperty;
	}

	public void settConExtProperty(TConExtProperty tConExtProperty) {
		this.tConExtProperty = tConExtProperty;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CON_CATEGORY_ID", insertable = false, updatable = false)
	public TConCategory gettConCategory() {
		return this.tConCategory;
	}

	public void settConCategory(TConCategory tConCategory) {
		this.tConCategory = tConCategory;
	}
	
	@Column(name = "ORDER_IDX")
	public Long getOrderIdx() {
		return orderIdx;
	}

	public void setOrderIdx(Long orderIdx) {
		this.orderIdx = orderIdx;
	}
	
	@Column(name = "CON_EXT_PROPERTY_ID")
	public Long gettConExtPropertyId() {
		return tConExtPropertyId;
	}

	public void settConExtPropertyId(Long tConExtPropertyId) {
		this.tConExtPropertyId = tConExtPropertyId;
	}

	@Column(name = "CON_CATEGORY_ID")
	public Long gettConCategoryId() {
		return tConCategoryId;
	}

	@Column(name = "REQUIRED")
	public void settConCategoryId(Long tConCategoryId) {
		this.tConCategoryId = tConCategoryId;
	}
	
	@Column(name = "required")
	public boolean isRequired() {
		return required;
	}


	public void setRequired(boolean required) {
		this.required = required;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tConCatExtProp")
	public List<TConExtPropValue> gettConExtPropValues() {
		return tConExtPropValues;
	}
	public void settConExtPropValues(List<TConExtPropValue> tConExtPropValues) {
		this.tConExtPropValues = tConExtPropValues;
	}
}
