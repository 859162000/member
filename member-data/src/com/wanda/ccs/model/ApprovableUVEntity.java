package com.wanda.ccs.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ApprovableUVEntity extends SubmitableUVEntity {

	private static final long serialVersionUID = 1832231910030262421L;

	public ApprovableUVEntity(){
		this.approved = Boolean.valueOf(false);
	}
	
	private Boolean approved;
	private String approvedBy;
	private Date approvedTime;

	@Column(name="APPROVED",length=1)
	public Boolean getApproved() {
		return approved;
	}
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}
	@Column(name="APPROVED_BY",length=40)
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	@Column(name="APPROVED_TIME")
	public Date getApprovedTime() {
		return approvedTime;
	}
	public void setApprovedTime(Date approvedTime) {
		this.approvedTime = approvedTime;
	}
	
	public void initialize()
    {
        super.initialize();
        setApproved(null);
        setApprovedBy(null);
        setApprovedTime(null);
    }

	
}
