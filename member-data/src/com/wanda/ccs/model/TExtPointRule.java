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

import com.xcesys.extras.core.dao.model.BlameableEntity;
import com.xcesys.extras.core.util.StringUtil;

/**
 * 特殊积分规则
 */
@Entity
@Table(name = "T_EXT_POINT_RULE")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TExtPointRule extends BlameableEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = -2970533503248962896L;
	
	private Long id;//特殊积分id
	private TExtPointCriteria tExtPointCriteria;//特殊积分规则条件
	private String code;//编码
	private String name;//名称
	private String status;//状态
	private Date startDtime;//开始时间(精确到秒)
	private Date endDtime;//结束时间(精确到秒)
	private Long additionPercent;//额外积分-百分比
	private Long additionCode;//额外积分-积分值
	private String additionType;//可兑换积分类型
	
	private String strStartDtime;
	private String strEndDtime;
	
	public TExtPointRule() {
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_EXT_POINT_RULE",allocationSize=1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "EXT_POINT_RULE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXT_POINT_CRITERIA_ID", nullable = false)
	public TExtPointCriteria gettExtPointCriteria() {
		return tExtPointCriteria;
	}

	public void settExtPointCriteria(TExtPointCriteria tExtPointCriteria) {
		this.tExtPointCriteria = tExtPointCriteria;
	}

	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "START_DTIME")
	public Date getStartDtime() {
		return startDtime;
	}

	public void setStartDtime(Date startDtime) {
		this.startDtime = startDtime;
	}

	@Column(name = "END_DTIME")
	public Date getEndDtime() {
		return endDtime;
	}

	public void setEndDtime(Date endDtime) {
		this.endDtime = endDtime;
	}

	@Column(name = "ADDITION_PERCENT")
	public Long getAdditionPercent() {
		return additionPercent;
	}

	public void setAdditionPercent(Long additionPercent) {
		this.additionPercent = additionPercent;
	}

	@Column(name = "ADDITION_CODE")
	public Long getAdditionCode() {
		return additionCode;
	}

	public void setAdditionCode(Long additionCode) {
		this.additionCode = additionCode;
	}

	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Transient
	public String getAdditionType() {
		if(additionType == null){
			if(additionCode != null )//积分值
				additionType = "C";
			else if(additionPercent != null)
				additionType = "P";
		}
		return additionType;
	}

	public void setAdditionType(String additionType) {
		this.additionType = additionType;
	}

	@Transient
	public String getStrStartDtime() {
		if(startDtime != null)
			strStartDtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDtime);
		return strStartDtime;
	}

	public void setStrStartDtime(String strStartDtime) {
		try {
			if(!StringUtil.isNullOrBlank(strStartDtime))
				startDtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strStartDtime);
			else
				startDtime = null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.strStartDtime = strStartDtime;
	}
	
	@Transient
	public String getStrEndDtime() {
		if(endDtime != null)
			strEndDtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDtime);
		return strEndDtime;
	}

	public void setStrEndDtime(String strEndDtime) {
		try {
			if(!StringUtil.isNullOrBlank(strEndDtime))
				endDtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strEndDtime);
			else
				endDtime = null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.strEndDtime = strEndDtime;
	}
}
