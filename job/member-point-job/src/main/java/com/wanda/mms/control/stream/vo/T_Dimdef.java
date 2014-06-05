package com.wanda.mms.control.stream.vo;

/**
 *  
 * 维表
 * @author wangshuai 
 * @date 2013-05-28
 */
public class T_Dimdef {
	/**
	 *维表代码
	 */
	private String code;
	/**
	 *维表名称
	 */
	private String name;
	/**
	 *维表ID
	 */
	private long seqid;
	/**
	 *维表类型id
	 */
	private long typeid;
	/**
	 *逻辑删除标识#ISDELETE
	 */
	private int isdelete;
	/**
	 *创建时间
	 */
	private String create_date;
	/**
	 *版本号#VERSION
	 */
	private int version;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSeqid() {
		return seqid;
	}
	public void setSeqid(long seqid) {
		this.seqid = seqid;
	}
	public long getTypeid() {
		return typeid;
	}
	public void setTypeid(long typeid) {
		this.typeid = typeid;
	}
	public int getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	
	

}
