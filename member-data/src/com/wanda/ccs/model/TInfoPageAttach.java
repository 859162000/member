package com.wanda.ccs.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.xcesys.extras.core.dao.model.VersionableEntity;

/**
 * 
 * @author Chenxm 2012-11-05
 *
 */
@Entity
@Table(name = "T_INFO_PAGE_ATTACH")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TInfoPageAttach extends VersionableEntity implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8967661413940429479L;
	private Long id;//附件编号
	private Blob fileContent;//附件内容
	private String contentType;//附件content type
	private Long fileSize;//附件大小
	private String fileName;//附件名称
	private String fileDesc;//附件说明
	private TInfoPage infoPage;//
	private String filePath;//文件保存路径
	
	public TInfoPageAttach() {
	}
	 

	public TInfoPageAttach(Long id, Blob fileContent,
			String contentType, Long fileSize, String fileName,
			String fileDesc, TInfoPage infoPage ,String filePath) {
		super();
		this.id = id;
		this.fileContent = fileContent;
		this.contentType = contentType;
		this.fileSize = fileSize;
		this.fileName = fileName;
		this.fileDesc = fileDesc;
		this.infoPage = infoPage;
		this.filePath = filePath;
	}


	@SequenceGenerator(name = "generator", sequenceName = "S_T_INFO_PAGE_ATTACH" ,allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "INFO_PAGE_ATTACH_ID", unique = true, nullable = false, length = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "FILE_CONTENT")
	public Blob getFileContent() {
		return fileContent;
	}


	public void setFileContent(Blob fileContent) {
		this.fileContent = fileContent;
	}
	@Column(name = "FILE_PATH",length=700)
	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	@Column(name = "CONTENT_TYPE")
	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Column(name = "FILE_SIZE")
	public Long getFileSize() {
		return fileSize;
	}


	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "FILE_NAME")
	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILE_DESC")
	public String getFileDesc() {
		return fileDesc;
	}


	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "INFO_PAGE_ID")
	public TInfoPage getInfoPage() {
		return infoPage;
	}


	public void setInfoPage(TInfoPage infoPage) {
		this.infoPage = infoPage;
	}

	
}
