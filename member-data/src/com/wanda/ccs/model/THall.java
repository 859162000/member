package com.wanda.ccs.model;

// Generated 2011-10-17 18:01:57 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.constraints.Length;

import com.xcesys.extras.core.dao.model.DeleteableEntity;

/**
 * THall generated by hbm2java
 */
@Entity
@Table(name = "T_HALL")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class THall extends DeleteableEntity implements Comparable<THall> {

	private static final long serialVersionUID = -460689785823594725L;
	private Long id;// 主键Id
	private Long cinemaId;// 外键影院ID
	private TCinema tCinema;//
	private String hallType;// 影厅类型
	private String name;// 影厅名称  厅号
	private Long seatCount;// 座位数
	private String audioType;// 音响类型
	private String belongsto;// 设备归属
	private Long projectType;// 放映制式
	private String serverBrand;// 服务器品牌
	private String projectBrand;// 放映机品牌
	private Boolean isDigital;// 是否数字
	private Boolean is3d;// 是否3D
	private Boolean isFilm;// 是否胶片
	private Boolean isImax;// 是否IMAX
	private Boolean isReald;// 是否RealD
	private Date updatetime;
	private Long disabledSeatCount;// 残疾人座位数
	private String[] projectTypes;
	private String displayName;//影厅名称

	public THall() {
	}

	@SequenceGenerator(name = "generator", sequenceName = "S_T_HALL")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "SEQID", unique = true, nullable = false, precision = 38, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CINEMAID", insertable = false, updatable = false)
	public TCinema gettCinema() {
		return this.tCinema;
	}

	public void settCinema(TCinema tCinema) {
		this.tCinema = tCinema;
	}

	@Column(name = "HALLTYPE")
	public String getHallType() {
		return this.hallType;
	}

	public void setHallType(String hallType) {
		this.hallType = hallType;
	}

	@Column(name = "NAME", length = 60)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Digits(integer = 5, fraction = 0, message = "座位数最大长度为5位")
	@Min(value = 0, message = "座位数必须大于等于0")
	@Column(name = "SEATCOUNT", precision = 38, scale = 0)
	public Long getSeatCount() {
		return this.seatCount;
	}

	public void setSeatCount(Long seatCount) {
		this.seatCount = seatCount;
	}

	@Column(name = "AUDIOTYPE", length = 2)
	public String getAudioType() {
		return this.audioType;
	}

	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}

	@Column(name = "BELONGSTO", length = 100)
	public String getBelongsto() {
		return this.belongsto;
	}

	public void setBelongsto(String belongsto) {
		this.belongsto = belongsto;
	}

	@Column(name = "PROJECTTYPE", precision = 38, scale = 0)
	public Long getProjectType() {
		return this.projectType;
	}

	public void setProjectType(Long projectType) {
		this.projectType = projectType;
	}

	@Length(min = 0, max = 50, message = "服务器品牌长度需要在0和50之间")
	@Column(name = "SERVERBRAND", length = 50)
	public String getServerBrand() {
		return this.serverBrand;
	}

	public void setServerBrand(String serverBrand) {
		this.serverBrand = serverBrand;
	}

	@Length(min = 0, max = 50, message = "放映机品牌长度需要在0和50之间")
	@Column(name = "PROJECTBRAND", length = 50)
	public String getProjectBrand() {
		return this.projectBrand;
	}

	public void setProjectBrand(String projectBrand) {
		this.projectBrand = projectBrand;
	}

	@Column(name = "ISDIGITAL")
	public Boolean getIsDigital() {
		return this.isDigital;
	}

	public void setIsDigital(Boolean isDigital) {
		this.isDigital = isDigital;
	}

	@Column(name = "ISFILM")
	public Boolean getIsFilm() {
		return isFilm;
	}

	public void setIsFilm(Boolean isFilm) {
		this.isFilm = isFilm;
	}

	@Column(name = "IS3D")
	public Boolean getIs3d() {
		return this.is3d;
	}

	public void setIs3d(Boolean is3d) {
		this.is3d = is3d;
	}

	@Column(name = "ISIMAX")
	public Boolean getIsImax() {
		return this.isImax;
	}

	public void setIsImax(Boolean isImax) {
		this.isImax = isImax;
	}

	@Column(name = "ISREALD")
	public Boolean getIsReald() {
		return this.isReald;
	}

	public void setIsReald(Boolean isReald) {
		this.isReald = isReald;
	}

	@Column(name = "UPDATETIME")
	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Digits(integer = 5, fraction = 0, message = "最大长度为5位")
	@Min(value = 0, message = "必须大于等于0")
	@Column(name = "DISABLEDSEATCOUNT", precision = 38, scale = 0)
	public Long getDisabledSeatCount() {
		return this.disabledSeatCount;
	}

	public void setDisabledSeatCount(Long disabledSeatCount) {
		this.disabledSeatCount = disabledSeatCount;
	}

	@Column(name = "CINEMAID", precision = 38, scale = 0)
	public Long getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(Long cinemaId) {
		this.cinemaId = cinemaId;
	}

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "T_HALL_PROJECT", joinColumns = { @JoinColumn(name = "HALL_ID") })
	@IndexColumn(name = "IDX")
	@Column(name = "PROJECTTYPE", length = 10)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public String[] getProjectTypes() {
		return projectTypes;
	}

	public void setProjectTypes(String[] projectTypes) {
		this.projectTypes = projectTypes;
	}
	@Column(name = "DISPLAYNAME", precision = 50)
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public int compareTo(THall o) {
		int ret = 0;
		try {
			int name1 = Integer.parseInt(this.getName());
			int name2 = Integer.parseInt(o.getName());
			ret = name1 > name2 ? 1 : ret;
		} catch (Exception e) {
		}
		return ret;
	}

	@Override
	public String toString() {
		return "THall [id=" + id + ", name=" + name + "]";
	}
}
