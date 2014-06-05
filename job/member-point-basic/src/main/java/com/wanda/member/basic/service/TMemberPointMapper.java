package com.wanda.member.basic.service;

import com.wanda.member.basic.model.BasicPointTrans;

public interface TMemberPointMapper {
	public void updateBasicPoint(BasicPointTrans basicPointTrans);
	public void rollbackMemberPoint(BasicPointTrans basicPointTrans);
}
