/**
 * 
 */
package com.wanda.mrb.intf.member.vo;


/**
 * @author xuesi
 *
 */
public class TMember {
	public Long memberID;
	public String memberNo;// 会员序列号
	public String mobile;// 会员唯一标识，非空，不允许重复
	public String tel;
	public String idCardNo;
	public String idCardHashNo;
	public String name;// 会员名称
	public String gender;// M:男,F:女
	public String birthday;// 会员生日
	public Long registType;// 注册类型 1:新会员;2:卡转换;3:合作伙伴
	public String registChnID;//招募渠道
	public String sourceType;//会员来源
	public String registOpNo;// 注册员工号
	public String registOpName;// 注册员工姓名
	public Long registCinemaId;// 影院全国长码的id	
	public String registCinemaCode;// 影院全国长码的id	
	public Long status;// 1:有效;0:无效
	public Long idDelete;
	public String email;
	public Long memberAddrID;
	public String zipCode;
	public String address1;
	public String address2;
	public String address3;
	public String address4;
	public Long countryID;
	public Long provinceID;
	public Long cityID;
	public int isDelete;
	public String contactMeans;
	public long favContactID;
	public int identityType;
	public String maritalStatus;
	public int childNumber;
	public int education;
	public int occupation;
	public int income;
	public String favFilmType;
	public int fqCinemaDist;
	public int fqCinemaTime;
	public String mobileOptin;
	public String operator;
	public String operatorName;
	public int istalk;
	public long iscontactable;
	public String cardNumber;
	public String cardType;//0：会员卡。	1：会员关联卡
	public String counter;//注册影城
	public String counterName;//注册影城
	public String dtsId;//会员来源
	public String otherNo;
	public int levelPointTotal;//定级积分
	public int activityPoint;//非定级积分
	public int exgPointBalance;//可消费积分
	public int exgExpirePointBal;//年底到期积分
	public String levelCode;
	public String expireDate;
	public String orgLevel;//会员上次级别
	public String setTime;//级别变更时间
	public String targetLevel;
	public String levelPointOffset;
	public String favCinema;//常驻影城
	public String favCinemaName;//常驻影城名称
	public String manCinema;//管理影城
	public String manCinemaName;//管理影城名称
	public String registDate;//注册时间
	public String talk;
	public String ticketOffset;
	public String newMobile;
	public String oldMobile;
	public String cardTypeName;
	public String cardTypeCode;
	public String ccsCardCinema;
	public String ccsCardStatus;
	public String ccsCardExpireDate;
	public String ccsCardBalance;
	public long ccsCardInfoId;
	public String ccsCardValueType;
	public String arrivalType="C";//到达影城方式C:开车，F:步行，B:公共方式
	public String oftenChannel="WEB";//常用购票渠道'WEB:万达官网，APP:万达手机平台，POS:万达影城，THIRD：团购以及第三方平台，OTHER;其他'; 
	
	public String getNewMobile() {
		return newMobile;
	}
	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}
	public String getOldMobile() {
		return oldMobile;
	}
	public void setOldMobile(String oldMobile) {
		this.oldMobile = oldMobile;
	}
	public String getTalk() {
		return talk;
	}
	public void setTalk(String talk) {
		this.talk = talk;
	}
	public String getTicketOffset() {
		return ticketOffset;
	}
	public void setTicketOffset(String ticketOffset) {
		this.ticketOffset = ticketOffset;
	}
	public String getCounterName() {
		return counterName;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
	public int getExgExpirePointBal() {
		return exgExpirePointBal;
	}
	public void setExgExpirePointBal(int exgExpirePointBal) {
		this.exgExpirePointBal = exgExpirePointBal;
	}
	public String getFavCinemaName() {
		return favCinemaName;
	}
	public void setFavCinemaName(String favCinemaName) {
		this.favCinemaName = favCinemaName;
	}
	public String getManCinema() {
		return manCinema;
	}
	public void setManCinema(String manCinema) {
		this.manCinema = manCinema;
	}
	public String getManCinemaName() {
		return manCinemaName;
	}
	public void setManCinemaName(String manCinemaName) {
		this.manCinemaName = manCinemaName;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getRegistDate() {
		return registDate;
	}
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
	public String getRegistCinemaCode() {
		return registCinemaCode;
	}
	public void setRegistCinemaCode(String registCinemaCode) {
		this.registCinemaCode = registCinemaCode;
	}
	public String getFavCinema() {
		return favCinema;
	}
	public void setFavCinema(String favCinema) {
		this.favCinema = favCinema;
	}
	public String getDtsId() {
		return dtsId;
	}
	public void setDtsId(String dtsId) {
		this.dtsId = dtsId;
	}
	public String getOtherNo() {
		return otherNo;
	}
	public void setOtherNo(String otherNo) {
		this.otherNo = otherNo;
	}
	public int getLevelPointTotal() {
		return levelPointTotal;
	}
	public void setLevelPointTotal(int levelPointTotal) {
		this.levelPointTotal = levelPointTotal;
	}
	public int getActivityPoint() {
		return activityPoint;
	}
	public void setActivityPoint(int activityPoint) {
		this.activityPoint = activityPoint;
	}
	public int getExgPointBalance() {
		return exgPointBalance;
	}
	public void setExgPointBalance(int exgPointBalance) {
		this.exgPointBalance = exgPointBalance;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}
	public String getSetTime() {
		return setTime;
	}
	public void setSetTime(String setTime) {
		this.setTime = setTime;
	}
	public String getTargetLevel() {
		return targetLevel;
	}
	public void setTargetLevel(String targetLevel) {
		this.targetLevel = targetLevel;
	}
	public String getLevelPointOffset() {
		return levelPointOffset;
	}
	public void setLevelPointOffset(String levelPointOffset) {
		this.levelPointOffset = levelPointOffset;
	}
	public String getCounter() {
		return counter;
	}
	public void setCounter(String counter) {
		this.counter = counter;
	}
	public String getCardType() {
		return "".equals(cardType)?"0":cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public long getIscontactable() {
		return iscontactable;
	}
	public void setIscontactable(long iscontactable) {
		this.iscontactable = iscontactable;
	}
	public int getIstalk() {
		return istalk;
	}
	public void setIstalk(int istalk) {
		this.istalk = istalk;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getMobileOptin() {
		return "".equals(mobileOptin)?"0":mobileOptin;
	}
	public void setMobileOptin(String mobileOptin) {
		this.mobileOptin = mobileOptin;
	}
	public int getFqCinemaDist() {
		return fqCinemaDist;
	}
	public void setFqCinemaDist(int fqCinemaDist) {
		this.fqCinemaDist = fqCinemaDist;
	}
	public int getFqCinemaTime() {
		return fqCinemaTime;
	}
	public void setFqCinemaTime(int fqCinemaTime) {
		this.fqCinemaTime = fqCinemaTime;
	}
	public String getFavFilmType() {
		return favFilmType;
	}
	public void setFavFilmType(String favFilmType) {
		this.favFilmType = favFilmType;
	}
	public int getIncome() {
		return income;
	}
	public void setIncome(int income) {
		this.income = income;
	}
	public int getOccupation() {
		return occupation;
	}
	public void setOccupation(int occupation) {
		this.occupation = occupation;
	}
	public int getEducation() {
		return education;
	}
	public void setEducation(int education) {
		this.education = education;
	}
	public int getChildNumber() {
		return childNumber;
	}
	public void setChildNumber(int childNumber) {
		this.childNumber = childNumber;
	}
	public String getMaritalStatus() {
		return "".equals(maritalStatus)?"0":maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public int getIdentityType() {
		return identityType;
	}
	public void setIdentityType(int identityType) {
		this.identityType = identityType;
	}
	public String getContactMeans() {
		return contactMeans;
	}
	public void setContactMeans(String contactMeans) {
		this.contactMeans = contactMeans;
	}
	public long getFavContactID() {
		return favContactID;
	}
	public void setFavContactID(long favContactID) {
		this.favContactID = favContactID;
	}
	public String getIdCardHashNo() {
		return idCardHashNo;
	}
	public void setIdCardHashNo(String idCardHashNo) {
		this.idCardHashNo = idCardHashNo;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Long getRegistType() {
		return registType;
	}
	public void setRegistType(Long registType) {
		this.registType = registType;
	}
	public String getRegistChnID() {
		return registChnID;
	}
	public void setRegistChnID(String registChnID) {
		this.registChnID = registChnID;
	}
	public String getRegistOpNo() {
		return registOpNo;
	}
	public void setRegistOpNo(String registOpNo) {
		this.registOpNo = registOpNo;
	}
	public String getRegistOpName() {
		return registOpName;
	}
	public void setRegistOpName(String registOpName) {
		this.registOpName = registOpName;
	}
	public Long getRegistCinemaId() {
		return registCinemaId;
	}
	public void setRegistCinemaId(Long registCinemaId) {
		this.registCinemaId = registCinemaId;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public Long getIdDelete() {
		return idDelete;
	}
	public void setIdDelete(Long idDelete) {
		this.idDelete = idDelete;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Long getMemberAddrID() {
		return memberAddrID;
	}
	public void setMemberAddrID(Long memberAddrID) {
		this.memberAddrID = memberAddrID;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public String getAddress4() {
		return address4;
	}
	public void setAddress4(String address4) {
		this.address4 = address4;
	}
	public Long getCountryID() {
		return countryID;
	}
	public void setCountryID(Long countryID) {
		this.countryID = countryID;
	}
	public Long getProvinceID() {
		return provinceID;
	}
	public void setProvinceID(Long provinceID) {
		this.provinceID = provinceID;
	}
	public Long getCityID() {
		return cityID;
	}
	public void setCityID(Long cityID) {
		this.cityID = cityID;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public Long getMemberID() {
		return memberID;
	}
	public void setMemberID(Long memberID) {
		this.memberID = memberID;
	}
	public String getArrivalType() {
		return arrivalType;
	}
	public void setArrivalType(String arrivalType) {
		this.arrivalType = arrivalType;
	}
	public String getOftenChannel() {
		return oftenChannel;
	}
	public void setOftenChannel(String oftenChannel) {
		this.oftenChannel = oftenChannel;
	}
	
	
}
