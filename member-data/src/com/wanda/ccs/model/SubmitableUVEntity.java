package com.wanda.ccs.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.xcesys.extras.core.dao.model.BlameableEntity;

@MappedSuperclass
public abstract class SubmitableUVEntity extends BlameableEntity {
	private static final long serialVersionUID = 5556801463057820098L;

	public SubmitableUVEntity(){
		 submit = Boolean.valueOf(false);
	}
	
	private String status;
    private Boolean submit;
    private String submitBy;
    private Date submitTime;
    
    @Column(name="STATUS",length=2)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name="SUBMIT")
	public Boolean getSubmit() {
		return submit;
	}
	public void setSubmit(Boolean submit) {
		this.submit = submit;
	}
	@Column(name="SUBMIT_BY",length=40)
	public String getSubmitBy() {
		return submitBy;
	}
	public void setSubmitBy(String submitBy) {
		this.submitBy = submitBy;
	}
	@Column(name="SUBMIT_TIME")
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	public void initialize()
    {
        super.initialize();
        setSubmit(Boolean.valueOf(false));
        setSubmitBy(null);
        setSubmitTime(null);
    }
}
