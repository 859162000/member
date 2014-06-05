package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xcesys.extras.core.dao.model.VersionableEntity;
/**
 * TConExtProperty entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_CON_EXT_PROP_VALUE")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TConExtPropValue extends VersionableEntity implements java.io.Serializable {
	private static final long serialVersionUID = 8992177334731902931L;
	
	private Long id;
	private Long conCatExtPropId;//扩展属性id
	private Long conItemId;//品项id
	private String propValue;//属性值
	private TConCatExtProp tConCatExtProp;//扩展属性
	private TConItem tConItem;//品项
	private Date date;
	

	/** default constructor */
	public TConExtPropValue() {
	}
	
	public void copy(TConExtPropValue propValue) {
		this.propValue = propValue.getPropValue();
	}
	
	public TConExtPropValue(TConExtPropValue propValue, TConItem conItem) {
		this.conCatExtPropId = propValue.getConCatExtPropId();
		this.propValue = propValue.getPropValue();
		this.tConCatExtProp = propValue.gettConCatExtProp();
		this.conItemId = conItem.getId();
		this.tConItem = conItem;
	}

	// Property accessors
	@SequenceGenerator(name = "generator", sequenceName = "S_T_CON_EXT_PROP_VALUE", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "CON_EXT_PROP_VALUE_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CON_CAT_EXT_PROP_ID")
	public Long getConCatExtPropId() {
		return conCatExtPropId;
	}

	public void setConCatExtPropId(Long conCatExtPropId) {
		this.conCatExtPropId = conCatExtPropId;
	}

	@Column(name = "CON_ITEM_ID",insertable = false, updatable = false)
	public Long getConItemId() {
		return conItemId;
	}

	public void setConItemId(Long conItemId) {
		this.conItemId = conItemId;
	}

	@Column(name = "PROP_VALUE")
	public String getPropValue() {
		return propValue;
	}

	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CON_CAT_EXT_PROP_ID", insertable = false, updatable = false)
	public TConCatExtProp gettConCatExtProp() {
		return tConCatExtProp;
	}

	public void settConCatExtProp(TConCatExtProp tConCatExtProp) {
		this.tConCatExtProp = tConCatExtProp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CON_ITEM_ID")
	public TConItem gettConItem() {
		return tConItem;
	}

	public void settConItem(TConItem tConItem) {
		this.tConItem = tConItem;
	}
	
	@Transient
	public Date getDate() {
		if(this.propValue != null)
			try {
				date = new SimpleDateFormat("yyyy-mm-dd").parse(propValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return date;
	}

	public void setDate(Date date) {
		if(date == null)
			this.propValue = null;
		else
			this.propValue = new SimpleDateFormat("yyyy-mm-dd").format(date);
		this.date = date;
	}
	
}