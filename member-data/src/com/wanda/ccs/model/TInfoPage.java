package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;


import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
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

import com.xcesys.extras.core.dao.model.VersionableEntity;

/**
 * 
 * @author Chenxm 2012-11-05
 *
 */
@Entity
@Table(name = "T_INFO_PAGE")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TInfoPage extends VersionableEntity implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1243259215605905817L;
	private Long id;//信息编号
	private Long infoChannelId;//频道编号
	private String title;//信息标题
	private String content;//信息内容
	private String status;//信息状态E编辑中、P已发布、X已删除、H隐藏
	private Date publishDate;//发布日期
	private String isRichFormat;//是否为富文本，'Y'是，'N'否
	private TInfoChannel infoChannel;
	private Set<TInfoPageAttach> infoPageAttach;
	public TInfoPage() {
	}
	 

	@SequenceGenerator(name = "generator", sequenceName = "S_T_INFO_PAGE" ,allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "INFO_PAGE_ID", unique = true, nullable = false, length = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TITLE", length = 1024)
	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}


	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}


	@Column(name = "STATUS")
	public String getStatus() {
		if(status == null ){
			status = "E";
		}
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}


	@Column(name = "PUBLISH_DATE")
	public Date getPublishDate() {
		return publishDate;
	}



	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}


	@Column(name = "IS_RICH_FORMAT")
	public String getIsRichFormat() {
		if(isRichFormat == null ){
			isRichFormat = "N";
		}
		return isRichFormat;
	}



	public void setIsRichFormat(String isRichFormat) {
		this.isRichFormat = isRichFormat;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INFO_CHANNEL_ID" , insertable = false, updatable = false)
	public TInfoChannel getInfoChannel() {
		return infoChannel;
	}



	public void setInfoChannel(TInfoChannel infoChannel) {
		this.infoChannel = infoChannel;
	}

	@Column(name = "INFO_CHANNEL_ID")
	public Long getInfoChannelId() {
		return infoChannelId;
	}


	public void setInfoChannelId(Long infoChannelId) {
		this.infoChannelId = infoChannelId;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "infoPage", cascade = CascadeType.ALL,orphanRemoval = true)
	public Set<TInfoPageAttach> getInfoPageAttach() {
		return infoPageAttach;
	}


	public void setInfoPageAttach(Set<TInfoPageAttach> infoPageAttach) {
		this.infoPageAttach = infoPageAttach;
	}

	
}
