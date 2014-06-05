package com.wanda.ccs.data.schedule;

import java.util.Date;

import com.icebean.core.adb.ADB;

/**
 * 排片信息表明细
 * 
 * @author Danne Leung
 */
public class BScheduleInfo extends ADB {
	/**
	 * 上映开始日期
	 */
	private Date begin;
	/**
	 * 影院
	 */
	private long cinemaId;
	/**
	 * 拷贝来源
	 */
	private String copySource;
	/**
	 * 上映结束日期
	 */
	private Date end;
	private HScheduleInfo header;
	/**
	 * 头ID
	 */
	private long headerId;
	/**
	 * 首周最低场次
	 */
	private int minCount;
	/**
	 * 影片
	 */
	private long movieId;
	/**
	 * 发行商
	 */
	private long publisherId;
	/**
	 * ID
	 */
	private long seqId;
	/**
	 * 上映日期
	 */
	private Date startDate;
	/**
	 * 是否重点影片
	 */
	private boolean keyMovie;

	public BScheduleInfo() {
	}

	public Date getBegin() {
		return begin;
	}

	public long getCinemaId() {
		return cinemaId;
	}

	public String getCopySource() {
		return copySource;
	}

	public Date getEnd() {
		return end;
	}

	public HScheduleInfo getHeader() {
		return header;
	}

	public long getHeaderId() {
		return headerId;
	}

	public int getMinCount() {
		return minCount;
	}

	public long getMovieId() {
		return movieId;
	}

	public long getPublisherId() {
		return publisherId;
	}

	public long getSeqId() {
		return seqId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public boolean isKeyMovie() {
		return keyMovie;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public void setCinemaId(long cinemaId) {
		this.cinemaId = cinemaId;
	}

	public void setCopySource(String copySource) {
		this.copySource = copySource;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setHeader(HScheduleInfo header) {
		this.header = header;
	}

	public void setHeaderId(long headerId) {
		this.headerId = headerId;
	}

	public void setKeyMovie(boolean keyMovie) {
		this.keyMovie = keyMovie;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}

	public void setMovieId(long movieId) {
		this.movieId = movieId;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
