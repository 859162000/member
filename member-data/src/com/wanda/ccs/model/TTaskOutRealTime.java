package com.wanda.ccs.model;


import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.xcesys.extras.core.dao.model.AbstractEntity;

@Entity
@Table(name = "T_TASK_OUT_REALTIME")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TTaskOutRealTime extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String code;//影城编码
	private String taskStatus;//任务状态
	private Date createDate;//创建时间
	
	@SequenceGenerator(name = "generator", sequenceName = "S_T_TASK_OUT_REALTIME",allocationSize=1)
	@Id
	@GeneratedValue(strategy = SEQUENCE,generator = "generator")
	@Column(name = "SEQID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "CODE",length=20)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "TASK_STATUS",length=1)
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
}
