package com.wanda.ccs.model;

// Generated 2011-10-17 18:01:57 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.xcesys.extras.core.dao.model.AbstractEntity;

/**
 * TScheduleInfoB generated by hbm2java
 */
@Entity
@Table(name = "T_SCHEDULE_INFO_B", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"SCHEDULE_INFO_H_ID","CINEMA_ID", "FILM_ID" }) })
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TScheduleInfoB extends AbstractEntity implements
		java.io.Serializable {

	private static final long serialVersionUID = -6343856916422455063L;
	
	private Long id;//
	private TCinema tCinema;
	private Long cinemaId;
	private TScheduleInfoH tScheduleInfoH;
	private Long tScheduleInfoHID;
	private Long filmId;
	private Boolean keyFilm;
	private Boolean careFilm;//关注影片
	private TPublisher tPublisher;
	private Long TPublisherId;
	private Long minCount;
	private String copySource;
	private Date startDate;
	private Date endDate;
	private Date onlineDate;
	private TFilm tFilm;
	private String remark;//备注

	public TScheduleInfoB() {
	}
	public TScheduleInfoB(TScheduleInfoH infoH) {
		this.tScheduleInfoH = infoH;
	}
	
	public TScheduleInfoB(Long cinemaId, Long filmId, Boolean keyFilm,
			Boolean careFilm, String copySource, Date startDate, Date endDate,
			Date onlineDate) {
		super();
		this.cinemaId = cinemaId;
		this.filmId = filmId;
		this.keyFilm = keyFilm;
		this.careFilm = careFilm;
		this.copySource = copySource;
		this.startDate = startDate;
		this.endDate = endDate;
		this.onlineDate = onlineDate;
	}

	@Column(name = "CINEMA_ID")
	public Long getCinemaId() {
		return cinemaId;
	}

	@Column(name = "COPY_SOURCE", length = 2)
	public String getCopySource() {
		return this.copySource;
	}

//	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return this.endDate;
	}
	
	@Transient
	public String getEndTime() {
		String hour = "00";
		Date e = getEndDate();
		if(e==null){
			return hour;
		}
		Calendar t = Calendar.getInstance();
        t.setTime(e);
        int h = t.get(Calendar.HOUR_OF_DAY);
        if(h!=23){
        	hour = h + "";
        }
		return hour;
	}

	@Column(name = "FILM_ID", precision = 22, scale = 0)
	public Long getFilmId() {
		return this.filmId;
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_SCHEDULE_INFO_B")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "SEQID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	@Column(name = "KEY_FILM", length = 1)
	public Boolean getKeyFilm() {
		return this.keyFilm;
	}

	@Column(name = "MIN_COUNT", precision = 38, scale = 0)
	public Long getMinCount() {
		return this.minCount;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ONLINE_DATE")
	public Date getOnlineDate() {
		return this.onlineDate;
	}

//	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return this.startDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CINEMA_ID", insertable = false, updatable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public TCinema gettCinema() {
		return this.tCinema;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILM_ID", insertable = false, updatable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public TFilm gettFilm() {
		return this.tFilm;
	}

	@Column(name = "PUBLISHER_ID", precision = 38, scale = 0)
	public Long getTPublisherId() {
		return this.TPublisherId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCHEDULE_INFO_H_ID")
	public TScheduleInfoH gettScheduleInfoH() {
		return this.tScheduleInfoH;
	}

	public void setCinemaId(Long cinemaId) {
		this.cinemaId = cinemaId;
	}

	public void setCopySource(String copySource) {
		this.copySource = copySource;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setFilmId(Long TFilmId) {
		this.filmId = TFilmId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setKeyFilm(Boolean keyFilm) {
		this.keyFilm = keyFilm;
	}

	public void setMinCount(Long minCount) {
		this.minCount = minCount;
	}

	public void setOnlineDate(Date onlineDate) {
		this.onlineDate = onlineDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void settCinema(TCinema tCinema) {
		this.tCinema = tCinema;
	}

	public void settFilm(TFilm tFilm) {
		this.tFilm = tFilm;
	}

	public void setTPublisherId(Long TPublisherId) {
		this.TPublisherId = TPublisherId;
	}

	public void settScheduleInfoH(TScheduleInfoH tScheduleInfoH) {
		this.tScheduleInfoH = tScheduleInfoH;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PUBLISHER_ID", insertable = false, updatable = false)
	public TPublisher gettPublisher() {
		return tPublisher;
	}

	public void settPublisher(TPublisher tPublisher) {
		this.tPublisher = tPublisher;
	}

	@Column(name = "CARE_FILM", length = 1)
	public Boolean getCareFilm() {
		return careFilm;
	}

	public void setCareFilm(Boolean careFilm) {
		this.careFilm = careFilm;
	}
	
	@Column(name = "SCHEDULE_INFO_H_ID",insertable=false,updatable=false)
	public Long gettScheduleInfoHID() {
		return tScheduleInfoHID;
	}

	public void settScheduleInfoHID(Long tScheduleInfoHID) {
		this.tScheduleInfoHID = tScheduleInfoHID;
	}
	
	@Column(name="REMARK",length=300)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}


}
