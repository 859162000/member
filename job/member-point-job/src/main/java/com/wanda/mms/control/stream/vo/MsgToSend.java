package com.wanda.mms.control.stream.vo;



/**
 *  
 *信息发送内容实体类
 * @author wangshuai 
 * @date 2013-07-19
 */
public class MsgToSend {
	
	/**
	 * 信息发送内容ID
	 */
	private Long msgId;
	/**
	 * 信息发送类型ID
	 */
	private int sendTypeId;
	/**
	 * 发送内容中数据的截止时间。
	 * 比如2013年1月1日的数据，该字段取值YYYYMMDD
	 */
	private Long DataYMD;
	/**
	 * 要发送的消息内容
	 */
	private String Content;
	/**
	 * 信息生成时间。如果同一类型的信息针对同一日期生成多次
	 * ，则以最大的GenerateTime为准
	 * 类型在定
	 */
	private String generateTime;
	
	private String cinema_inner_code;
	
	private String msmflag; //默认为0  院线未发送-1 院线以发送-2 影城未发送-3 影城以发送-4 
	
	
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	public int getSendTypeId() {
		return sendTypeId;
	}
	public void setSendTypeId(int sendTypeId) {
		this.sendTypeId = sendTypeId;
	}
	public Long getDataYMD() {
		return DataYMD;
	}
	public void setDataYMD(Long dataYMD) {
		DataYMD = dataYMD;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getGenerateTime() {
		return generateTime;
	}
	public void setGenerateTime(String generateTime) {
		this.generateTime = generateTime;
	}
	public String getCinema_inner_code() {
		return cinema_inner_code;
	}
	public void setCinema_inner_code(String cinemaInnerCode) {
		cinema_inner_code = cinemaInnerCode;
	}
	public String getMsmflag() {
		return msmflag;
	}
	public void setMsmflag(String msmflag) {
		this.msmflag = msmflag;
	}
	
	
	

}
