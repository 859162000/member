package com.wanda.ccs.data.basemgt;

import java.util.Date;
import java.util.Vector;

import com.icebean.core.adb.ADB;


/**
 * 影厅
 * @author Benjamin
 * @date 2011-10-11
 */
public class Hall extends ADB {
	
	private long seqId;
	private long cinemaId;
	private int hallType;//影厅类型(VIP、普通)
	private long hallId;
	private String name;
	private int seatCount;
	private int disabledSeatCount;
	private int audioType;//音响类型
	private String belongsTo;//设备归属(自有、租赁)
	private int projectType;//放映制式
	private String serverBrand;//服务器品牌
	private String projectBrand;//放映机品牌
	
	private boolean isDigital;
	private boolean is3D;
	private boolean isIMAX;
	private boolean isRealD;
	private boolean isDelete;
	private Date updateTime;
	
	
	public Hall() {
	}
	
	public long getSeqId() {
		return seqId;
	}
	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}
	public long getCinemaId() {
		return cinemaId;
	}
	public void setCinemaId(long cinemaid) {
		this.cinemaId = cinemaid;
	}
	public int getHallType() {
		return hallType;
	}
	public void setHallType(int hallType) {
		this.hallType = hallType;
	}
	public long getHallId() {
		return hallId;
	}
	public void setHallId(long hallId) {
		this.hallId = hallId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSeatCount() {
		return seatCount;
	}
	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}
	public int getDisabledSeatCount() {
		return disabledSeatCount;
	}
	public void setDisabledSeatCount(int disabledSeatCount) {
		this.disabledSeatCount = disabledSeatCount;
	}
	public int getAudioType() {
		return audioType;
	}
	public void setAudioType(int audioType) {
		this.audioType = audioType;
	}
	public String getBelongsTo() {
		return belongsTo;
	}
	public void setBelongsTo(String belongsTo) {
		this.belongsTo = belongsTo;
	}
	public int getProjectType() {
		return projectType;
	}
	public void setProjectType(int projectType) {
		this.projectType = projectType;
	}
	public String getServerBrand() {
		return serverBrand;
	}
	public void setServerBrand(String serverBrand) {
		this.serverBrand = serverBrand;
	}
	public String getProjectBrand() {
		return projectBrand;
	}
	public void setProjectBrand(String projectBrand) {
		this.projectBrand = projectBrand;
	}
	public int getIsDigital() {
		return isDigital ? 1 : 0;
	}
	public void setIsDigital(int i) {
		this.isDigital = i != 0;
	}
	public int getIs3D() {
		return is3D ? 1 : 0;
	}
	public void setIs3D(int i) {
		is3D =  i != 0;
	}
	public int getIsIMAX() {
		return isIMAX ? 1 : 0;
	}
	public void setIsIMAX(int i) {
		this.isIMAX = i != 0;
	}
	public int getIsRealD() {
		return isRealD ? 1 : 0;
	}
	public void setIsRealD(int i) {
		this.isRealD = i != 0;
	}
	public int getIsDelete() {
		return isDelete ? 1 : 0;
	}
	public void setIsDelete(int i) {
		this.isDelete = i != 0;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getSearchName() {
		return name;
	}

	public void setSearchName(String str) {
		name = str;

		if (str == null || str.equals(""))
			clearFlag("name");
		else
			setFlag("name");
	}
	
	public long getSearchCinemaId() {
		return cinemaId;
	}

	public void setSearchCinemaId(long i) {
		this.cinemaId = i;
	}
	
	/** 测试用数据 */
	/*public static Vector<Cinema> cinemas = new Vector<Cinema>();

	static {
		for (int i = 4; i < 8; i++) {
			Cinema c = new Cinema();
			c.setSeqId(i);
			c.setName("CBD" + i);
			cinemas.add(c);
		}
	}*/
	

}
