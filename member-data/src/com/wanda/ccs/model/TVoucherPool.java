package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * 库券实体类
 * @author yaoguoqing
 *
 */
@Entity
@Table(name="T_VOUCHER_POOL")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TVoucherPool extends BlameableEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 3060303261154403459L;
	
	/**
	 * 券库id
	 */
	private Long id;
	
	/**
	 * 券名称
	 */
	private String name;
	
	/**
	 * 库类型
	 */
	private String poolType;
	
	/**
	 * 是否已导入文件
	 */
	private String hasImport;
	
	/**
	 * 发送锁定
	 */
	private String sendLock;
	
	/**
	 * 导入数量
	 */
	private Long importCount;
	
	/**
	 * 券库明细
	 */
	private Set<TVoucherPoolDetail> tVoucherPoolDetails = new HashSet<TVoucherPoolDetail>();
	
	/**
	 * 券发放明细
	 */
	private Set<TMemVoucherRule> tMemVoucherRule = new HashSet<TMemVoucherRule>();
	

	public TVoucherPool() {
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_VOUCHER_POOL",allocationSize=1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "VOUCHER_POOL_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="POOL_TYPE")
	public String getPoolType() {
		return poolType;
	}

	public void setPoolType(String poolType) {
		this.poolType = poolType;
	}

	@Column(name="HAS_IMPORT")
	public String getHasImport() {
		return hasImport;
	}

	public void setHasImport(String hasImport) {
		this.hasImport = hasImport;
	}

	@Column(name="SEND_LOCK")
	public String getSendLock() {
		return sendLock;
	}

	public void setSendLock(String sendLock) {
		this.sendLock = sendLock;
	}

	@Column(name="IMPORT_COUNT")
	public Long getImportCount() {
		return importCount;
	}

	public void setImportCount(Long importCount) {
		this.importCount = importCount;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tVoucherPool")
	public Set<TVoucherPoolDetail> gettVoucherPoolDetails() {
		return tVoucherPoolDetails;
	}

	public void settVoucherPoolDetails(Set<TVoucherPoolDetail> tVoucherPoolDetails) {
		this.tVoucherPoolDetails = tVoucherPoolDetails;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tVoucherPool")
	public Set<TMemVoucherRule> gettMemVoucherRule() {
		return tMemVoucherRule;
	}

	public void settMemVoucherRule(Set<TMemVoucherRule> tMemVoucherRule) {
		this.tMemVoucherRule = tMemVoucherRule;
	}
}
