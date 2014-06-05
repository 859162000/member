package com.wanda.member.basic.processor;

import org.springframework.batch.item.ItemProcessor;

import com.wanda.member.basic.model.MbrLevel;

public class DegradeProcessor implements
		ItemProcessor<MbrLevel, MbrLevel> {
	private String year;
	private String level;

	@Override
	public MbrLevel process(MbrLevel mbrLevel) throws Exception {
		if (!String.valueOf(mbrLevel.getMemLevel()).equals(this.getLevel())) {
			return null;
		}
		boolean needDegrade = this.isNeedDegrade(mbrLevel.getMemLevel(), mbrLevel.getLevelPointTotal(), mbrLevel.getTicketCount());
		if(!needDegrade){
			//过滤掉不需要降级的会员
			return null;
		}
		mbrLevel.setYear(year);
		return mbrLevel;
	}
	
	private boolean isNeedDegrade(int nowLv, int nowLvPoint, int nowTicketCount){
		boolean flag = false;
		switch (nowLv) {
		case 2:
			flag = this.checkSecond(nowLvPoint, nowTicketCount);
			break;
		case 3:
			flag = this.checkThird(nowLvPoint, nowTicketCount);
			break;
		case 4:
			flag = this.checkFourth(nowLvPoint, nowTicketCount);
			break;
		default:
			break;
		}
		return flag;
	}
	
	private boolean checkSecond(int nowLvPoint, int nowTicketCount){
		if(nowLvPoint >= 500 || nowTicketCount >= 12){
			return false;
		}
		return true;
	}
	private boolean checkThird(int nowLvPoint, int nowTicketCount){
		if(nowLvPoint >= 1000 || nowTicketCount >= 24){
			return false;
		}
		return true;
	}
	private boolean checkFourth(int nowLvPoint, int nowTicketCount){
		if(nowLvPoint >= 3000 || nowTicketCount >= 48){
			return false;
		}
		return true;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
