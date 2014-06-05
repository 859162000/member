package com.wanda.member.activity.service;

import java.util.Map;

import com.wanda.member.activity.model.ActivityPointMember;
import com.wanda.member.activity.model.ActivityPointUpdataParameter;

public interface TPointHistoryMapper {
    public void insertActivityPoint(ActivityPointUpdataParameter param);
    public void rollbackPointHistory(ActivityPointUpdataParameter param);
    public void czPointHistory(ActivityPointUpdataParameter param);
    public void deleteActivityPointHistoryByDate(Map<String, Object> param);
    public int countActivityPoint(ActivityPointMember param);
}