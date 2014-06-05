package com.wanda.member.basic.service;

import com.wanda.member.basic.model.BasicPointTrans;

public interface TPointHistoryMapper {
	public void insertBasicPoint(BasicPointTrans basicPointTrans);
	public void updateBasicPoint(BasicPointTrans basicPointTrans);
	public void rollbackPointHistory(BasicPointTrans basicPointTrans);
	public void czPointHistory(BasicPointTrans basicPointTrans);
}
