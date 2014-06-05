package com.wanda.ccs.model;

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
 * 库券明细实体类
 * @author yaoguoqing
 *
 */
@Entity
@Table(name="T_VOUCHER_POOL_DETAIL")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TVoucherPoolDetail extends BlameableEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 3060303261154403459L;
	
	/**
	 * 券库明细ID
	 */
	private Long id;
	
	/**
	 * 券库
	 */
	private TVoucherPool tVoucherPool;
	
	/**
	 * 打印码
	 */
	private String printCode;
	
	/**
	 * 券条码
	 */
	private String barCode;
	
	/**
	 * 会员
	 */
	private TMember tMember;
	
	private Long memberId;
	
	/**
	 * 卷号码
	 */
	private String voucherNumber;
	
	private TVoucher tVoucher;
	
	private String operrateType;//OPERRATE_TYPE  券操作类型，1,系统发放 2,绑定 3,赠送

	public TVoucherPoolDetail() {
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_VOUCHER_POOL_DETAIL",allocationSize=1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "VOUCHER_POOL_DETAIL_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VOUCHER_POOL_ID", nullable = false, insertable = false, updatable = false)
	public TVoucherPool gettVoucherPool() {
		return tVoucherPool;
	}

	public void settVoucherPool(TVoucherPool tVoucherPool) {
		this.tVoucherPool = tVoucherPool;
	}

	@Column(name="PRINT_CODE")
	public String getPrintCode() {
		return printCode;
	}

	public void setPrintCode(String printCode) {
		this.printCode = printCode;
	}

	@Column(name="VOUCHER_NUMBER")
	public String getVoucherNumber() {
		return voucherNumber;
	}

	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID", nullable = false, insertable = false, updatable = false)
	public TMember gettMember() {
		return tMember;
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

	@Column(name="BAR_CODE")
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BAR_CODE", nullable = false, insertable = false, updatable = false)
	public TVoucher gettVoucher() {
		return tVoucher;
	}

	public void settVoucher(TVoucher tVoucher) {
		this.tVoucher = tVoucher;
	}

	@Column(name="OPERRATE_TYPE")
	public String getOperrateType() {
		return operrateType;
	}

	public void setOperrateType(String operrateType) {
		this.operrateType = operrateType;
	}
	
	
}
