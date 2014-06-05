package com.wanda.ccs.smsgateway.engine.dao;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TransCheckCodeDAO {
	public void insertTransCheckCode(@Param("checkCode")String checkCode, @Param("mobileNo")String mobileNo);
	public Map<String, Object> queryTransCheckCode(String mobileNo);
	public void updateTransCheckCodeGenTime(BigDecimal transCheckCodeId);
}
