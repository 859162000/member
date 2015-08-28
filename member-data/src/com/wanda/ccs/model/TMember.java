package com.wanda.ccs.model;

// Generated 2013-4-26 16:06:19 by Hibernate Tools 3.3.0.GA

import static javax.persistence.GenerationType.SEQUENCE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.xcesys.extras.core.dao.model.BlameableEntity;
import com.xcesys.extras.core.util.StringUtil;

/**
 * 会员
 */
@Entity
@Table(name = "T_MEMBER")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TMember extends BlameableEntity implements java.io.Serializable {

	private static final long serialVersionUID = 5991158056553829783L;
	private Long memberId;
	private String channelId;// 'BO:影城','POS:POS','WEB:电影网','Mobile:移动终端','SinaWeibo:合作渠道'
	private String channelExtId;//招募渠道扩展
	private String memberNo;// 会员序列号
	private String mobile;// 会员唯一标识，非空，不允许重复
	private String phone;
	private String email;
	private String name;// 会员名称
	private String gender = "O";// M:男,F:女,O:未指定
	private Date birthday;// 会员生日
//	private String strStartDtime;
	private Long registType;// 注册类型 1:新会员;2:卡转换;3:合作伙伴
	private String registOpNo;// 注册号
	private String registOpName;// 注册名
	private Long registCinemaId;// 影院全国长码
	private Long status;// 1:有效;0:禁用 ;-1冻结
	private TMemFavFilmtype tMemFavFilmtypes = new TMemFavFilmtype();
	private TMemFavContact tMemFavContacts = new TMemFavContact();
	private TMemberAddr tMemberAddrs = new TMemberAddr();
	private Set<TMemCardRel> tMemCardRels = new HashSet<TMemCardRel>();
	private Set<TMember3rdRel> tMember3rdRels = new HashSet<TMember3rdRel>();
	private Set<TMemberWebRel> tMemberWebRels = new HashSet<TMemberWebRel>();
	private TMemberPoint tMemberPoint = new TMemberPoint();
	private TMemberLevel tMemberLevel = new TMemberLevel();
	private TCinema tCinema;
	private TMemberInfo tMemberInfo = new TMemberInfo();
	// private Long memberInfoId;
	private Long contactable = 2l;//1：希望被联络 2：未指定  0 不希望被联络
	private TMackDaddyCard tMackDaddyCard;//万人迷卡
	private String changeStatusResion;//会员调整状态原因
	private Long isDeleted;//是否删除
	private Set<TVoucherPoolDetail> tVoucherPoolDetails = new HashSet<TVoucherPoolDetail>();

	private String strBackDate;
	private Date registDate;
	private String arrivalType;//C:开车，F:步行，B:公共方式
	private String oftenChannel;//WEB:万达官网，APP:万达手机平台，POS:万达影城，THIRD：团购以及第三方平台，OTHER;其他
	public TMember() {

	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_MEMBER", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "MEMBER_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.memberId;
	}

	public void setId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "MEMBER_NO", nullable = false, length = 256)
	public String getMemberNo() {
		return this.memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	@Column(name = "MOBILE", nullable = false, length = 128)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Column(name = "PHONE", nullable = false, length = 128)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "NAME", length = 128)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "GENDER", nullable = false, precision = 22, scale = 0)
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "BIRTHDAY")
	public Date getBirthday() {
//		if(birthday != null) {
//			birthday = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(birthday));
//		}
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
//		if(birthday != null){
//			this.birthday = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(birthday));
//		}else
			this.birthday = birthday;
		
	}
	
//	@Transient
//	public String getStrStartDtime() {
//		if(birthday != null) {
//			strStartDtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(birthday);
//		}
//		return strStartDtime;
//	}
//
//	public void setStrStartDtime(String strStartDtime) {
//		try {
//			if(!StringUtil.isNullOrBlank(strStartDtime)) {
//				birthday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strStartDtime);
//			}else {
//				birthday = null;
//			}
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		this.strStartDtime = strStartDtime;
//	}

	@Column(name = "REGIST_TYPE", nullable = false, precision = 22, scale = 0)
	public Long getRegistType() {
		return this.registType;
	}

	public void setRegistType(Long registType) {
		this.registType = registType;
	}

	@Column(name = "REGIST_OP_NO", nullable = false, length = 256)
	public String getRegistOpNo() {
		return this.registOpNo;
	}

	public void setRegistOpNo(String registOpNo) {
		this.registOpNo = registOpNo;
	}

	@Column(name = "REGIST_OP_NAME", nullable = false, length = 128)
	public String getRegistOpName() {
		return this.registOpName;
	}

	public void setRegistOpName(String registOpName) {
		this.registOpName = registOpName;
	}

	@Column(name = "REGIST_CINEMA_ID", nullable = false, length = 256)
	public Long getRegistCinemaId() {
		return this.registCinemaId;
	}

	public void setRegistCinemaId(Long registCinemaId) {
		this.registCinemaId = registCinemaId;
	}

	@Column(name = "STATUS", nullable = false, precision = 22, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tMember")
	public Set<TMemCardRel> gettMemCardRels() {
		return this.tMemCardRels;
	}

	public void settMemCardRels(Set<TMemCardRel> tMemCardRels) {
		this.tMemCardRels = tMemCardRels;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tMember")
	public Set<TMember3rdRel> gettMember3rdRels() {
		return this.tMember3rdRels;
	}

	public void settMember3rdRels(Set<TMember3rdRel> tMember3rdRels) {
		this.tMember3rdRels = tMember3rdRels;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tMember")
	public Set<TMemberWebRel> gettMemberWebRels() {
		return this.tMemberWebRels;
	}

	public void settMemberWebRels(Set<TMemberWebRel> tMemberWebRels) {
		this.tMemberWebRels = tMemberWebRels;
	}

	@Column(name = "REGIST_CHNID")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Column(name = "REGIST_CHN_EXT_ID")
	public String getChannelExtId() {
		return channelExtId;
	}

	public void setChannelExtId(String channelExtId) {
		this.channelExtId = channelExtId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGIST_CINEMA_ID", insertable = false, updatable = false)
	public TCinema gettCinema() {
		return tCinema;
	}

	public void settCinema(TCinema tCinema) {
		this.tCinema = tCinema;
	}

	
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "tMember")
	public TMemberPoint gettMemberPoint() {
		return tMemberPoint;
	}

	public void settMemberPoint(TMemberPoint tMemberPoint) {
		this.tMemberPoint = tMemberPoint;
	}
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "tMember")
	public TMemberLevel gettMemberLevel() {
		return tMemberLevel;
	}

	public void settMemberLevel(TMemberLevel tMemberLevel) {
		this.tMemberLevel = tMemberLevel;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "tMember")
	public TMemberInfo gettMemberInfo() {
		return tMemberInfo;
	}

	public void settMemberInfo(TMemberInfo tMemberInfo) {
		this.tMemberInfo = tMemberInfo;
	}

	// @Column(name = "MEMBER_INFO_ID")
	// public Long getMemberInfoId() {
	// return memberInfoId;
	// }
	//
	// public void setMemberInfoId(Long memberInfoId) {
	// this.memberInfoId = memberInfoId;
	// }

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "tMember")
	public TMemberAddr gettMemberAddrs() {
		return tMemberAddrs;
	}

	public void settMemberAddrs(TMemberAddr tMemberAddrs) {
		this.tMemberAddrs = tMemberAddrs;
	}

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "tMember")
	public TMemFavFilmtype gettMemFavFilmtypes() {
		return tMemFavFilmtypes;
	}

	public void settMemFavFilmtypes(TMemFavFilmtype tMemFavFilmtypes) {
		this.tMemFavFilmtypes = tMemFavFilmtypes;
	}

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "tMember")
	public TMemFavContact gettMemFavContacts() {
		return tMemFavContacts;
	}

	public void settMemFavContacts(TMemFavContact tMemFavContacts) {
		this.tMemFavContacts = tMemFavContacts;
	}
    @Column(name = "ISCONTACTABLE")
	public Long getContactable() {
		return contactable;
	}

	public void setContactable(Long contactable) {
		this.contactable = contactable;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tMember")
	public Set<TVoucherPoolDetail> gettVoucherPoolDetails() {
		return tVoucherPoolDetails;
	}

	public void settVoucherPoolDetails(Set<TVoucherPoolDetail> tVoucherPoolDetails) {
		this.tVoucherPoolDetails = tVoucherPoolDetails;
	}

	@Column(name = "CHANGE_STATUS_RESION")
	public String getChangeStatusResion() {
		return changeStatusResion;
	}

	public void setChangeStatusResion(String changeStatusResion) {
		this.changeStatusResion = changeStatusResion;
	}
	@Column(name = "ISDELETE")
	public Long getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "tMember")
	public TMackDaddyCard gettMackDaddyCard() {
		return tMackDaddyCard;
	}

	public void settMackDaddyCard(TMackDaddyCard tMackDaddyCard) {
		this.tMackDaddyCard = tMackDaddyCard;
	}
	
	@Transient
	public String getSmobile() {
		if(!StringUtil.isNullOrBlank(mobile)){
			if(isDeleted == 1){
				return mobile.substring(0, 3)+"*****"+mobile.substring(mobile.length()-17, mobile.length()-14);
			}else
			return mobile.substring(0, 3)+"*****"+mobile.substring(mobile.length()-3);
		}
		return mobile;
	}

	@Transient
	public String getStrBackDate() {
		if(birthday != null)
			strBackDate = new SimpleDateFormat("yyyy-MM-dd").format(birthday);
		return strBackDate;
	}

	public void setStrBackDate(String strToldTime) {
		try {
			if(!StringUtil.isNullOrBlank(strToldTime))
				birthday = new SimpleDateFormat("yyyy-MM-dd").parse(strToldTime);
			else
				birthday = null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.strBackDate = strToldTime;
	}

	@Column(name = "REGIST_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	@Column(name = "ARRIVAL_TYPE")
	public String getArrivalType() {
		return arrivalType;
	}

	public void setArrivalType(String arrivalType) {
		this.arrivalType = arrivalType;
	}

	@Column(name = "OFTEN_CHANNEL")
	public String getOftenChannel() {
		return oftenChannel;
	}

	public void setOftenChannel(String oftenChannel) {
		this.oftenChannel = oftenChannel;
	}
	
	
}
