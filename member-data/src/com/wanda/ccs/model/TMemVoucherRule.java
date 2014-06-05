package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

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

import com.xcesys.extras.core.dao.model.BlameableEntity;
import com.xcesys.extras.core.util.StringUtil;

/**
 * 券发放实体类
 * @author yaoguoqing
 *
 */
@Entity
@Table(name="T_MEM_VOUCHER_RULE")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TMemVoucherRule extends BlameableEntity implements java.io.Serializable {

	private static final long serialVersionUID = -3097250244883269852L;
	
	/**
	 * 券发放id
	 */
	private Long id;
	
	/**
	 * 编码
	 */
	private String code;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 开始时间
	 */
	private Date startDtime;
	
	/**
	 * 结束时间
	 */
	private Date endDtime;
	
	/**
	 * 规则来源
	 */
	private String sourceType;
	
	/**
	 * 券库
	 */
	private TVoucherPool tVoucherPool;
	
	/**
	 * 客群
	 */
	private TSegment tSegment;
	
	/**
	 * 最大发放数据
	 */
	private Long maxCount;
	
	/**
	 * 券发放次序
	 */
	private String sendOrder;
	
	private String strStartDtime;
	private String strEndDtime;
	
	public TMemVoucherRule() {
	}
	
	@SequenceGenerator(name = "generator", sequenceName = "S_T_MEM_VOUCHER_RULE",allocationSize=1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "MEM_VOUCHER_RULE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="START_DTIME")
	public Date getStartDtime() {
		return startDtime;
	}

	public void setStartDtime(Date startDtime) {
		this.startDtime = startDtime;
	}

	@Column(name="END_DTIME")
	public Date getEndDtime() {
		return endDtime;
	}

	public void setEndDtime(Date endDtime) {
		this.endDtime = endDtime;
	}

	@Column(name="SOURCE_TYPE")
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VOUCHER_POOL_ID", nullable = false, insertable = true, updatable = true)
	public TVoucherPool gettVoucherPool() {
		return tVoucherPool;
	}

	public void settVoucherPool(TVoucherPool tVoucherPool) {
		this.tVoucherPool = tVoucherPool;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEGMENT_ID", nullable = false, insertable = true, updatable = true)
	public TSegment gettSegment() {
		return tSegment;
	}

	public void settSegment(TSegment tSegment) {
		this.tSegment = tSegment;
	}

	@Column(name="MAX_COUNT")
	public Long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Long maxCount) {
		this.maxCount = maxCount;
	}

	@Column(name="SEND_ORDER")
	public String getSendOrder() {
		return sendOrder;
	}

	public void setSendOrder(String sendOrder) {
		this.sendOrder = sendOrder;
	}

	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Transient
	public String getStrStartDtime() {
		if(startDtime != null) {
			strStartDtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDtime);
		}
		return strStartDtime;
	}

	public void setStrStartDtime(String strStartDtime) {
		try {
			if(!StringUtil.isNullOrBlank(strStartDtime)) {
				startDtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strStartDtime);
			}else {
				startDtime = null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.strStartDtime = strStartDtime;
	}

	@Transient
	public String getStrEndDtime() {
		if(endDtime != null) {
			strEndDtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDtime);
		}
		return strEndDtime;
	}

	public void setStrEndDtime(String strEndDtime) {
		try {
			if(!StringUtil.isNullOrBlank(strEndDtime)) {
				endDtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strEndDtime);
			}else {
				endDtime = null;
			}
		}catch(ParseException e) {
			e.printStackTrace();
		}
		this.strEndDtime = strEndDtime;
	}
}
