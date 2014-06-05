/**
 * 
 */
package com.wanda.mrb.intf.member.vo;

/**
 * @author xuesi
 *
 */
public class TContent {
	private String posInnerContent;
	private String posOutterContent;
	private String posIntfCount;
	private String posMsgContent;
	private String webIntfContent;
	private String webMsgContent;
	private String priority;
	private String img;
	private String imgParam;
	private String eventCode;
	private String chnlCode;
	private String redeemContent;
	private String posOutterTitle;
	private String posOutterSubtitle;
	private String posOutterSubject;
	private String posInnerTitle;
	private String posInnerSubtitle;
	private String posInnerSubject;
	
	public String getPosOutterSubject() {
		return posOutterSubject;
	}
	public void setPosOutterSubject(String posOutterSubject) {
		this.posOutterSubject = posOutterSubject;
	}
	public String getPosInnerSubject() {
		return posInnerSubject;
	}
	public void setPosInnerSubject(String posInnerSubject) {
		this.posInnerSubject = posInnerSubject;
	}
	public String getPosInnerTitle() {
		return posInnerTitle;
	}
	public void setPosInnerTitle(String posInnerTitle) {
		this.posInnerTitle = posInnerTitle;
	}
	public String getPosInnerSubtitle() {
		return posInnerSubtitle;
	}
	public void setPosInnerSubtitle(String posInnerSubtitle) {
		this.posInnerSubtitle = posInnerSubtitle;
	}
	public String getPosOutterTitle() {
		return posOutterTitle;
	}
	public void setPosOutterTitle(String posOutterTitle) {
		this.posOutterTitle = posOutterTitle;
	}
	public String getPosOutterSubtitle() {
		return posOutterSubtitle;
	}
	public void setPosOutterSubtitle(String posOutterSubtitle) {
		this.posOutterSubtitle = posOutterSubtitle;
	}
	public String getRedeemContent() {
		return redeemContent;
	}
	public void setRedeemContent(String redeemContent) {
		this.redeemContent = redeemContent;
	}
	public String getPosIntfCount() {
		return posIntfCount;
	}
	public void setPosIntfCount(String posIntfCount) {
		this.posIntfCount = posIntfCount;
	}
	public String getChnlCode() {
		return chnlCode;
	}
	public void setChnlCode(String chnlCode) {
		this.chnlCode = chnlCode;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public String getWebIntfContent() {
		return webIntfContent;
	}
	public void setWebIntfContent(String webIntfContent) {
		this.webIntfContent = webIntfContent;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getWebMsgContent() {
		return webMsgContent;
	}
	public void setWebMsgContent(String webMsgContent) {
		this.webMsgContent = webMsgContent;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getImgParam() {
		return imgParam;
	}
	public void setImgParam(String imgParam) {
		this.imgParam = imgParam;
	}
	public String getPosMsgContent() {
		return posMsgContent;
	}
	public void setPosMsgContent(String posMsgContent) {
		this.posMsgContent = posMsgContent;
	}
	public String getPosInnerContent() {
		return posInnerContent;
	}
	public void setPosInnerContent(String posInnerContent) {
		this.posInnerContent = posInnerContent;
	}
	public String getPosOutterContent() {
		return posOutterContent;
	}
	public void setPosOutterContent(String posOutterContent) {
		this.posOutterContent = posOutterContent;
	}
	
	//替换营销话术内容中的变量，生成最终的营销话术内容
	//${时间}${渠道}${积分数}${订单号}
	public String getFialOfferContent(String offerContent){
		String finalOfferContent = "";
		finalOfferContent = offerContent.replace("", "");
		return finalOfferContent;
	}
}
