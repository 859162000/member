package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xcesys.extras.core.dao.model.VersionableEntity;
import com.xcesys.extras.core.util.StringUtil;
/**
 * TConExtProperty entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_CON_EXT_PROPERTY")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TConExtProperty extends VersionableEntity implements java.io.Serializable {
	private static final long serialVersionUID = 8992177334731902931L;
	
	private Long id;
	private String propertyName;//扩展属性名称
	private String shortName;//扩展属性简称
	private String propertyDesc;//扩展属性描述
	private String dataType;//录入类型
	private Long dimTypeId;//维数据Id
	private String selectOptions;//字符串长度、小数长度、自定义值
	private Map<String,String> values = new HashMap<String, String>();
	private List<TConCatExtProp> tConCatExtProps = new ArrayList<TConCatExtProp>();
	private String strDimTypeId;
	


	/** default constructor */
	public TConExtProperty() {
	}


	// Property accessors
	@SequenceGenerator(name = "generator", sequenceName = "S_T_CON_CAT_EXT_PROP", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "CON_EXT_PROPERTY_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "PROPERTY_NAME", nullable = false, length = 2048)
	public String getPropertyName() {
		return this.propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@Column(name = "SHORT_NAME", nullable = false, length = 100)
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Column(name = "PROPERTY_DESC", length = 1024)
	public String getPropertyDesc() {
		return propertyDesc;
	}

	public void setPropertyDesc(String propertyDesc) {
		this.propertyDesc = propertyDesc;
	}

	@Column(name = "DATA_TYPE", nullable = false, length = 20)
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "DIM_TYPE_ID")
	public Long getDimTypeId() {
		return dimTypeId;
	}

	public void setDimTypeId(Long dimTypeId) {
		this.dimTypeId = dimTypeId;
	}

	@Column(name = "SELECT_OPTIONS", length = 1024)
	public String getSelectOptions() {
		return selectOptions;
	}

	public void setSelectOptions(String selectOptions) {
		this.selectOptions = selectOptions;
	}
	
	@Transient
	public Map<String, String> getValues() {
		if (dataType.equals(IConDimType.DIMDEF_CON_EXTPROPERTY_DATATYPE_USERDEFINED)
				&& !StringUtil.isNullOrBlank(selectOptions)) {
			String[] options = selectOptions.split(",");
			for(String option : options)
				values.put(option, option);
		}
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tConExtProperty")
	public List<TConCatExtProp> gettConCatExtProps() {
		return tConCatExtProps;
	}

	public void settConCatExtProps(List<TConCatExtProp> tConCatExtProps) {
		this.tConCatExtProps = tConCatExtProps;
	}
	
	@Transient
	public String getStrDimTypeId() {
		if(this.dimTypeId != null)
			strDimTypeId = this.dimTypeId.toString();
		return strDimTypeId;
	}

	public void setStrDimTypeId(String strDimTypeId) {
		if(!StringUtil.isNullOrBlank(strDimTypeId))
			this.dimTypeId = Long.valueOf(strDimTypeId);
		this.strDimTypeId = strDimTypeId;
	}

}