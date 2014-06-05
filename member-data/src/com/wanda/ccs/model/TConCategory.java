package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.xcesys.extras.core.dao.model.VersionableEntity;
import com.xcesys.extras.core.util.StringUtil;

import java.util.HashSet;
import java.util.Set;
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
import javax.persistence.Transient;
/**
 * TConCategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_CON_CATEGORY")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TConCategory extends VersionableEntity implements java.io.Serializable,Comparable<TConCategory> {
	private static final long serialVersionUID = 7335739493601158939L;
	private Long id;
	private TConCategory pConCategory;//父类品项分类
	private String categoryCode;//品项分类编码
	private String categoryName;//品项分类名称
	private String itemType;//品项分类类型
	private boolean valid= true;//是否有效
	private String categoryDesc;//描述
	private String categoryBreadcrumbs;//面包渣，冗余字段，用于记录分类所在的目录位置（不包含其自身名称）。格式例如：”食品》包装食品“
	private Set<TConCategory> conCategories = new HashSet<TConCategory>(0);
	private Long pConCategoryId;//父类品项分类名称
	private Boolean leafNode = false;//是否为末级节点
	private Set<TConCatExtProp> tConCatExtProps = new HashSet<TConCatExtProp>(0);
	private Set<TConItem> tConItems = new HashSet<TConItem>(0);
	private Set<TConSetCatGroup> tConSetCatGroups = new HashSet<TConSetCatGroup>(
			0);
	
	private boolean hasChild;
	private String strValid;
	

	/** default constructor */
	public TConCategory() {
	}


	// Property accessors
	@SequenceGenerator(name = "generator", sequenceName = "S_T_CON_CATEGORY", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "CON_CATEGORY_ID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "P_CON_CATEGORY_ID",insertable = false, updatable=false)
	public TConCategory getpConCategory() {
		return pConCategory;
	}


	public void setpConCategory(TConCategory pConCategory) {
		this.pConCategory = pConCategory;
	}
	
	@Column(name = "CATEGORY_CODE", nullable = false, length = 20)
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	@Column(name = "CATEGORY_NAME", nullable = false, length = 2048)
	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Column(name = "ITEM_TYPE", length = 20)
	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	@Column(name = "VALID", length = 1)
	public boolean getValid() {
		return this.valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Column(name = "CATEGORY_DESC", length = 2048)
	public String getCategoryDesc() {
		return this.categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	@Column(name = "CATEGORY_BREADCRUMBS", length = 2048)
	public String getCategoryBreadcrumbs() {
		return this.categoryBreadcrumbs;
	}

	public void setCategoryBreadcrumbs(String categoryBreadcrumbs) {
		this.categoryBreadcrumbs = categoryBreadcrumbs;
	}
	
	@Column(name = "P_CON_CATEGORY_ID")
	public Long getpConCategoryId() {
		return pConCategoryId;
	}

	public void setpConCategoryId(Long pConCategoryId) {
		this.pConCategoryId = pConCategoryId;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pConCategory")
	public Set<TConCategory> getConCategories() {
		return this.conCategories;
	}

	public void setConCategories(Set<TConCategory> conCategories) {
		this.conCategories = conCategories;
	}
	
	@Column(name = "LEAF_NODE", nullable = false)
	public Boolean getLeafNode() {
		return leafNode;
	}

	public void setLeafNode(Boolean leafNode) {
		this.leafNode = leafNode;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tConCategory")
	public Set<TConCatExtProp> gettConCatExtProps() {
		return this.tConCatExtProps;
	}

	public void settConCatExtProps(Set<TConCatExtProp> tConCatExtProps) {
		this.tConCatExtProps = tConCatExtProps;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tConCategory")
	public Set<TConItem> gettConItems() {
		return this.tConItems;
	}

	public void settConItems(Set<TConItem> tConItems) {
		this.tConItems = tConItems;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tConCategory")
	public Set<TConSetCatGroup> gettConSetCatGroups() {
		return this.tConSetCatGroups;
	}

	public void settConSetCatGroups(Set<TConSetCatGroup> tConSetCatGroups) {
		this.tConSetCatGroups = tConSetCatGroups;
	}
	
	@Transient
	public boolean isHasChild() {
		if(this.conCategories == null || this.conCategories.isEmpty()){
			this.hasChild = false;
		}else{
			this.hasChild = true;
		}
		return hasChild;
	}



	@Override
	public int compareTo(TConCategory o) {
		return this.categoryCode.compareTo(o.getCategoryCode());
	}
	
	@Transient
	public String getStrValid() {
		if(valid)
			this.strValid = "Y";
		else
			this.strValid = "N";
		return strValid;
	}

	public void setStrValid(String strValid) {
		this.strValid = strValid;
		if(StringUtil.isNullOrBlank(strValid) || strValid.equals("Y"))
			this.valid = true;
		else
			this.valid = false;
	}
}