package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xcesys.extras.core.dao.model.BlameableEntity;

/**
 * TIntfClientinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_INTF_CLIENTINFO")
public class TIntfClientinfo extends BlameableEntity implements java.io.Serializable {
	private static final long serialVersionUID = -5621919842338439527L;
	
	private Long clientid;
	private Long clienttype = 3L;
	private String cinemacode;
	private String clientcode;
	private String clientname;
	private String remark;
	private String ipaddress;
	private String connuser;
	private String connpwd;
	private Long isopen = 1L;

	/** default constructor */
	public TIntfClientinfo() {
	}

	/** full constructor */
	public TIntfClientinfo(Long clientid, Long clienttype,
			String cinemacode, String clientcode, String clientname,
			String remark, String ipaddress, String connuser, String connpwd,
			Long isopen) {
		this.clientid = clientid;
		this.clienttype = clienttype;
		this.cinemacode = cinemacode;
		this.clientcode = clientcode;
		this.clientname = clientname;
		this.remark = remark;
		this.ipaddress = ipaddress;
		this.connuser = connuser;
		this.connpwd = connpwd;
		this.isopen = isopen;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_INTF_CLIENTINFO", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "CLIENTID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getClientid() {
		return this.clientid;
	}

	public void setClientid(Long clientid) {
		this.clientid = clientid;
	}

	@Column(name = "CLIENTTYPE", precision = 22, scale = 0)
	public Long getClienttype() {
		return this.clienttype;
	}

	public void setClienttype(Long clienttype) {
		this.clienttype = clienttype;
	}

	@Column(name = "CINEMACODE", length = 20)
	public String getCinemacode() {
		return this.cinemacode;
	}

	public void setCinemacode(String cinemacode) {
		this.cinemacode = cinemacode;
	}

	@Column(name = "CLIENTCODE", length = 20)
	public String getClientcode() {
		return this.clientcode;
	}

	public void setClientcode(String clientcode) {
		this.clientcode = clientcode;
	}

	@Column(name = "CLIENTNAME", length = 30)
	public String getClientname() {
		return this.clientname;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	@Column(name = "REMARK", length = 50)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "IPADDRESS", length = 50)
	public String getIpaddress() {
		return this.ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	@Column(name = "CONNUSER", length = 20)
	public String getConnuser() {
		return this.connuser;
	}

	public void setConnuser(String connuser) {
		this.connuser = connuser;
	}

	@Column(name = "CONNPWD", length = 500)
	public String getConnpwd() {
		return this.connpwd;
	}

	public void setConnpwd(String connpwd) {
		this.connpwd = connpwd;
	}

	@Column(name = "ISOPEN", precision = 22, scale = 0)
	public Long getIsopen() {
		return this.isopen;
	}

	public void setIsopen(Long isopen) {
		this.isopen = isopen;
	}
	
	@Transient
	@Override
	public Long getId() {
		return null;
	}

	@Override
	public void setId(Long arg0) {
	}

}