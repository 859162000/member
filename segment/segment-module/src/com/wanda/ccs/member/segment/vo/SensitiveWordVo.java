package com.wanda.ccs.member.segment.vo;

public class SensitiveWordVo extends BaseAuditVo {
	private static final long serialVersionUID = 451896339250572185L;

	private Long wordId;//敏感词编号
	private String wordTitle;//敏感词标题
	private String wordContent;//敏感词内容
	private String issueRegion;//创建者所属区域:区域代码
	private long issueCinema;//创建者所属影城:影城seqid
	private String status;//状态
	private String remarks;//备注
	

	public String getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}
	private String isdelete;//是否删除标志
	
	
	@Override
	public String retrieveCode() {
		return wordTitle;
	}
	@Override
	public Long retrieveSeqId() {
		return wordId;
	}
	public Long getWordId() {
		return wordId;
	}
	public void setWordId(Long wordId) {
		this.wordId = wordId;
	}
	public String getWordTitle() {
		return wordTitle;
	}
	public void setWordTitle(String wordTitle) {
		this.wordTitle = wordTitle;
	}
	public String getWordContent() {
		return wordContent;
	}
	public void setWordContent(String wordContent) {
		this.wordContent = wordContent;
	}
	public String getIssueRegion() {
		return issueRegion;
	}
	public void setIssueRegion(String issueRegion) {
		this.issueRegion = issueRegion;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public long getIssueCinema() {
		return issueCinema;
	}
	public void setIssueCinema(long issueCinema) {
		this.issueCinema = issueCinema;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
