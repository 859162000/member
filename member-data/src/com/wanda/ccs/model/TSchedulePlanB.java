package com.wanda.ccs.model;

// Generated 2011-10-26 10:19:09 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.IndexColumn;

import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;
import com.xcesys.extras.util.ConvertUtil;

/**
 * TSchedulePlanB generated by hbm2java
 */
@Entity
@Table(name = "T_SCHEDULE_PLAN_B")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TSchedulePlanB extends AbstractStateUVEntity implements
		Comparable<TSchedulePlanB> {

	private static final long serialVersionUID = 3370405084436815869L;
	private Long id;
	/**
	 * 连场影片
	 */
	private THall tHall;
	private Long hallId;
	private Long filmId;
	private TFilm tFilm;
	private TSchedulePlanH header;
	private Long headerId;
	private String roundNo;
	private String hallName;
	private String type;
	private String language;
	/**
	 * 连场名称
	 */
	private String joinName;
	/**
	 * 开放销售渠道
	 */
	private String channel;
	/**
	 * 销售渠道的数组表现形式
	 */
	private String[] channels;
	private Integer sortSeq;
	private Long runningTime;
	private Integer cleanTime;
	private Integer adTime;
	private Date startTime;
	private Date oldStartTime;
	private Date endTime;
	private int startTimeOffset;
	private int endTimeOffset;
	private Boolean published;
	/**
	 * 固定场次，不受其他场次时间变化影响而跟随变化。
	 */
	private Boolean fixed = false;
	/**
	 * 分账比例
	 * 
	 */
	private Integer rate;

	/**
	 * 会员优惠
	 */
	private Boolean memberDiscount;

	/**
	 * 对号入座
	 */
	private Boolean matchSeat = false;

	/**
	 * 场次对应可用票类设定。
	 */
	private Set<TRoundTicketType> ticketTypes = new HashSet<TRoundTicketType>(0);

	/**
	 * 连场影片
	 */
	private List<TRoundFilm> roundFilms = new ArrayList<TRoundFilm>(0);

	/**
	 * 场次有错误
	 */
	private Boolean error = false;

	/**
	 * 锁定场次可编辑
	 */
	private Boolean locked = false;

	/**
	 * 是否有初始化的票类票价。
	 */
	private Boolean priced = false;

	/**
	 * 场次是否已经广播到POS系统
	 * 
	 */
	private Boolean broadcast = false;
	/**
	 * randomId格式timeline_item_filmid_newid
	 */
	private String randomId;
	/**
	 * 临时排映日期
	 */
	private Date ymd;
	/**
	 * 标准票价
	 */
	private Integer stdPrice;
	
	/**
	 * 排片数据下发文件名称
	 */
	private String tranFn;
	/**
	 * 排片数据下发异常信息
	 */
	private String tranMsg;
	/**
	 * 排片数据下发状态
	 * 0：POS端已经将下发到DET上的文件取走
	 * 1：排片数据已经下发到DET，等待POS取走
	 * 2：排片数据已经生成，还没下发到DET
	 */
	private String tranSts;
	/**
	 * 排片数据下发状态更改时间
	 */
	private Date tranTime;
	
	@Transient
	public boolean isTakedByPOS(){
		return "0".equalsIgnoreCase(this.getTranSts());
	}
	@Transient
	public boolean isArrivedDET(){
		return "1".equalsIgnoreCase(this.getTranSts());
	}
	@Transient
	public boolean isGoingToDET(){
		return "2".equalsIgnoreCase(this.getTranSts());
	}

	public TSchedulePlanB() {
	}

	public TSchedulePlanB(String roundNo) {
		this.roundNo = roundNo;
	}

	public TSchedulePlanB(TSchedulePlanH h) {
		this.header = h;
	}

	@Override
	public int compareTo(TSchedulePlanB o) {
		String hall1 = this.getHallName();
		String hall2 = o.getHallName();
		if (hall1 != null && hall2 != null) {
			int ret = hall1.compareTo(hall2);
			if (ret != 0) {
				return ret;
			}
		}
		Date st1 = this.getStartTime();
		Date st2 = o.getStartTime();
		// Integer seq1 = this.getSortSeq();
		// if (seq1 == null) {
		// seq1 = 1;
		// }
		// Integer seq2 = o.getSortSeq();
		// if (seq2 == null) {
		// seq2 = 1;
		// }
		int ret = 0;
		if (st1 == null) {
			ret = -1;
		} else if (st2 == null) {
			ret = 1;
		} else {
			ret = DateUtil.compareDateToField(st1, st2, Calendar.MINUTE);
		}
		return ret;
	}

	/**
	 * 实际片长是影片片长+广告时长。
	 * 
	 * @return
	 */
	@Transient
	public long getActualRunningTime() {
		return (this.getRunningTime() == null ? 0 : this.getRunningTime())
				+ (this.getAdTime() == null ? 0 : this.getAdTime());
	}

	@Column(name = "AD_TIME", precision = 38, scale = 0)
	public Integer getAdTime() {
		return adTime;
	}

	@Column(name = "broadcast")
	public Boolean getBroadcast() {
		return broadcast;
	}

	@Column(name = "CHANNEL", length = 50)
	public String getChannel() {
		return channel;
	}

	@Transient
	public String[] getChannels() {
		if (!StringUtil.isNullOrBlank(this.channel)) {
			this.channels = this.channel.split(",");
			Arrays.sort(this.channels);
		}
		return channels;
	}

	@Column(name = "CLEAN_TIME", precision = 38, scale = 0)
	public Integer getCleanTime() {
		return this.cleanTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")
	public Date getEndTime() {
		return this.endTime;
	}

	@Transient
	public int getEndTimeOffset() {
		return endTimeOffset;
	}

	@Column(name = "ERROR", length = 1)
	public Boolean getError() {
		return error;
	}

	@Column(name = "FILM_ID")
	public Long getFilmId() {
		return filmId;
	}

	/**
	 * 获取当前场次的影片列表，包括连场时的多部影片。
	 * 
	 * @return
	 */
	@Transient
	public List<TFilm> getFilms() {
		List<TFilm> films = new ArrayList<TFilm>();
		if (this.getFilmId() != null && this.getFilmId() > 0) {
			films.add(this.gettFilm());
		} else {
			List<TRoundFilm> list = this.getRoundFilms();
			if (list != null && !list.isEmpty()) {
				for (TRoundFilm rf : list) {
					films.add(rf.getFilm());
				}
			}
		}
		return films;
	}

	@Column(name = "FIXED", length = 1)
	public Boolean getFixed() {
		return fixed;
	}

	@Column(name = "HALL_ID")
	public Long getHallId() {
		return hallId;
	}

	@Column(name = "HALL_CODE")
	public String getHallName() {
		if (StringUtil.isNullOrBlank(hallName) && this.tHall != null) {
			this.hallName = this.tHall.getName();
		}
		return hallName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCHEDULE_PLAN_H_ID")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public TSchedulePlanH getHeader() {
		return this.header;
	}

	@Column(name = "SCHEDULE_PLAN_H_ID", insertable = false, updatable = false)
	public Long getHeaderId() {
		return this.headerId;
	}

	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
	}

	@Override
	@SequenceGenerator(name = "generator", sequenceName = "S_T_SCHEDULE_PLAN_B")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "SEQID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	@Column(name = "LANGUAGE")
	public String getLanguage() {
		return language;
	}

	@Column(name = "LOCKED", length = 1)
	public Boolean getLocked() {
		return locked;
	}

	@Column(name = "MATCH_SEAT")
	public Boolean getMatchSeat() {
		return matchSeat;
	}

	@Column(name = "MEMBER_DISCOUNT")
	public Boolean getMemberDiscount() {
		return memberDiscount;
	}

	@Transient
	public Date getOldStartTime() {
		if (oldStartTime == null) {
			return startTime;
		}
		return oldStartTime;
	}

	@Column(name = "PRICED")
	public Boolean getPriced() {
		if (priced == null) {
			priced = getTicketTypes() != null && getTicketTypes().size() > 0;
		}
		return priced;
	}

	@Column(name = "PUBLISHED", length = 1)
	public Boolean getPublished() {
		return published;
	}

	@Column(name = "RATE")
	public Integer getRate() {
		return rate;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "schedulePlanB")
	@IndexColumn(name = "IDX")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<TRoundFilm> getRoundFilms() {
		return this.roundFilms;
	}

	@Column(name = "ROUND_NO", nullable = false, length = 20)
	public String getRoundNo() {
		return this.roundNo;
	}

	@Column(name = "RUNNING_TIME", precision = 38, scale = 0)
	public Long getRunningTime() {
		if (this.runningTime == null || this.runningTime == 0) {
			this.runningTime = (this.gettFilm() == null || this.gettFilm()
					.getRunningTime() == null) ? 0L : this.gettFilm()
					.getRunningTime();
		}
		return this.runningTime;
	}

	@Column(name = "SORT_SEQ", precision = 38, scale = 0)
	public Integer getSortSeq() {
		return sortSeq;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return this.startTime;
	}

	@Transient
	public int getStartTimeOffset() {
		return startTimeOffset;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FILM_ID", insertable = false, updatable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public TFilm gettFilm() {
		return this.tFilm;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "HALL_ID", insertable = false, updatable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public THall gettHall() {
		return this.tHall;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tSchedulePlanB", cascade = CascadeType.ALL, orphanRemoval = true)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<TRoundTicketType> getTicketTypes() {
		return this.ticketTypes;
	}

	@Column(name = "TYPE", length = 2)
	public String getType() {
		return this.type;
	}

	@Column(name = "TRAN_STS", length = 1)
	public String getTranSts() {
		return tranSts;
	}

	@Column(name = "TRAN_FN", length = 20)
	public String getTranFn() {
		return tranFn;
	}
	
	@Column(name = "TRAN_MSG", length = 80)
	public String getTranMsg() {
		return tranMsg;
	}
	
	@Column(name = "TRAN_TIME")
	public Date getTranTime() {
		return tranTime;
	}
	
	@Column(name = "JOIN_NAME")
	public String getJoinName() {
		return joinName;
	}

	@Transient
	public boolean isEditable() {
		boolean editable = true;
		// 开始时间在未来时间的可编辑
		editable = isFuture();
		// 未锁定的只能为提交的可编辑
		if (locked != null && locked.booleanValue()) {
		} else {
			editable = editable
					&& !((getSubmit() == null ? false : getSubmit()
							.booleanValue()) || (getApproved() == null ? false
							: getApproved().booleanValue()));
		}
		return editable;
	}

	/**
	 * 判断场次是否为未来日期。
	 * 
	 * @return
	 */
	@Transient
	public boolean isFuture() {
		Date currentDate = DateUtil.getCurrentDate();
		return currentDate.before(this.startTime);
	}

	public void setAdTime(Integer adTime) {
		this.adTime = adTime;
	}

	public void setBroadcast(Boolean broadcast) {
		this.broadcast = broadcast;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setChannels(String[] channels) {
		this.channels = channels;
		if (this.channels != null) {
			Arrays.sort(channels);
			this.channel = ConvertUtil.convertArrayToCommaString(this.channels);
		} else {
			this.channel = null;
		}
	}

	public void setCleanTime(Integer cleanTime) {
		this.cleanTime = cleanTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setEndTimeOffset(int endTimeOffset) {
		this.endTimeOffset = endTimeOffset;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public void setFilmId(Long filmId) {
		this.filmId = filmId;
	}

	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}

	public void setHallId(Long hallId) {
		this.hallId = hallId;
	}

	public void setHallName(String hallName) {
		this.hallName = hallName;
	}

	public void setHeader(TSchedulePlanH tSchedulePlanH) {
		this.header = tSchedulePlanH;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setLocked(Boolean editable) {
		this.locked = editable;
	}

	public void setMatchSeat(Boolean matchSeat) {
		this.matchSeat = matchSeat;
	}

	public void setMemberDiscount(Boolean memberDiscount) {
		this.memberDiscount = memberDiscount;
	}

	public void setOldStartTime(Date oldStartTime) {
		this.oldStartTime = oldStartTime;
	}

	public void setPriced(Boolean priced) {
		this.priced = priced;
	}

	public void setPublished(Boolean published) {
		this.published = published;
		if (this.published == null) {
			this.published = false;
		}
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	public void setRoundFilms(List<TRoundFilm> tRoundFilms) {
		this.roundFilms = tRoundFilms;
	}

	public void setRoundNo(String roundNo) {
		this.roundNo = roundNo;
	}

	public void setRunningTime(Long runningTime) {
		this.runningTime = runningTime;
	}

	public void setSortSeq(Integer sortSeq) {
		this.sortSeq = sortSeq;
	}

	public void setStartTime(Date startTime) {
		if (this.startTime != null) {
			setOldStartTime((Date) this.startTime.clone());
		} else {
			setOldStartTime(null);
		}
		this.startTime = startTime;
	}

	public void setStartTimeOffset(int startTimeOffset) {
		this.startTimeOffset = startTimeOffset;
	}

	public void settFilm(TFilm tFilm) {
		this.tFilm = tFilm;
	}

	public void settHall(THall tHall) {
		this.tHall = tHall;
	}

	public void setTicketTypes(Set<TRoundTicketType> tRoundTicketTypes) {
		this.ticketTypes = tRoundTicketTypes;
	}

	public void setType(String roundSeq) {
		this.type = roundSeq;
	}

	@Override
	public String toString() {
		return "场次[厅"
				+ this.gettHall().getName()
				+ "， "
				+ StringUtil.avoidNull(roundNo)
				+ (StringUtil.isNullOrBlank(roundNo) ? "" : "， ")
				+ (tFilm == null ? "连场" : tFilm)
				+ "， "
				+ DateUtil.formatDate(startTime,
						DateUtil.PATTERN_DATETIME_YYYY_MM_DD_HH_MM)
				+ " ~ "
				+ DateUtil.formatDate(endTime,
						DateUtil.PATTERN_DATETIME_YYYY_MM_DD_HH_MM) + "]";
	}

	@Transient
	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

	@Transient
	public Date getYmd() {
		if (this.getHeader() != null && this.getHeader().getYmd() != null) {
			ymd = this.header.getYmd();
		}
		return ymd;
	}

	public void setYmd(Date ymd) {
		this.ymd = ymd;
	}

	public void setTranSts(String tranSts) {
		this.tranSts = tranSts;
	}

	public void setTranFn(String tranOKFileName) {
		this.tranFn = tranOKFileName;
	}

	public void setTranMsg(String tranMsg) {
		this.tranMsg = tranMsg;
	}
	
	public void setTranTime(Date tranTime) {
		this.tranTime = tranTime;
	}
	
	public void setJoinName(String joinName) {
		this.joinName = joinName;
	}
	public void setStdPrice(Integer stdPrice) {
		this.stdPrice = stdPrice;
	}
	
	@Column(name = "STD_PRICE")
	public Integer getStdPrice() {
		return stdPrice == null ? 0 : stdPrice;
	}
}
