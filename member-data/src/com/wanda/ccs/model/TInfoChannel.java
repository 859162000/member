package com.wanda.ccs.model;

import com.xcesys.extras.core.dao.model.VersionableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author Chenxm 2012-11-05
 *
 */
@Entity
@Table(name = "T_INFO_CHANNEL")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TInfoChannel extends VersionableEntity implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5891810806303257591L;
	private Long id;//频道编号
	private String channelName;//频道名称
	private String homeUrl;//频道URL
	
	public TInfoChannel(){} 
	
	   
	public TInfoChannel(Long id, String channelName, String homeUrl) {
		super();
		this.id = id;
		this.channelName = channelName;
		this.homeUrl = homeUrl;
	}
	@SequenceGenerator(name = "generator", sequenceName = "S_T_INFO_CHANNEL" ,allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "INFO_CHANNEL_ID", unique = true, nullable = false, length = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CHANNEL_NAME", length = 512)
	public String getChannelName() {
		return channelName;
	}


	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Column(name = "HOME_URL", length = 1024)
	public String getHomeUrl() {
		return homeUrl;
	}


	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}
	
}
