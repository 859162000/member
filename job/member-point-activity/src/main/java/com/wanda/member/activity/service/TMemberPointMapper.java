package com.wanda.member.activity.service;

import com.wanda.member.activity.model.ActivityPointUpdataParameter;


public interface TMemberPointMapper {
	public void updateActivityPoint(ActivityPointUpdataParameter param);
	public void rollbackMemberPoint(ActivityPointUpdataParameter param);
}