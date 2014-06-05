package com.wanda.mms.control.stream.vo;



public class Send {
	
	public  String fromSys;//：*信息来源系统代码（该代码由统一信息平台注册系统信息时生成）
	public  String target;//：*信息目标接收人的RTXNO或者接受人手机号，多个接收人，中间使用逗号(,)分割
	public  String mSTitle;//：发送短信的标题，标题可为""
	public  String mSContent;//：*发送内容，信息内容不能超过490个字
	public  String targetTime;//：目标发送时间，可以为""，如果为"",则默认为立即发送           格式"yyyy-MM-dd HH:mm:ss"
	public  String priority;//发送优先级，可以为"",如果为"",则默认为1
	
	//返回值：信息接收成功：OK
	//        信息接收失败：错误提示参看2.2.3及2.2.9短信错误提示部分。
	//注意：ashx短信调用，最终也调用的是wcf服务接口，当Target（接收人）是多个事，ashx会自动解析使用逗号分隔的接收人员，并将其转换为JSON格式。
	public String getFromSys() {
		return fromSys;
	}
	public void setFromSys(String fromSys) {
		this.fromSys = fromSys;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getmSTitle() {
		return mSTitle;
	}
	public void setmSTitle(String mSTitle) {
		this.mSTitle = mSTitle;
	}
	public String getmSContent() {
		return mSContent;
	}
	public void setmSContent(String mSContent) {
		this.mSContent = mSContent;
	}
	public String getTargetTime() {
		return targetTime;
	}
	public void setTargetTime(String targetTime) {
		this.targetTime = targetTime;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	

	
	
}
