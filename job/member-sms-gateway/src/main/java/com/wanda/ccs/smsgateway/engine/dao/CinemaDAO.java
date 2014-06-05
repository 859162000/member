package com.wanda.ccs.smsgateway.engine.dao;

import org.apache.ibatis.annotations.Param;

public interface CinemaDAO {
	public int queryInnerCode(@Param("innerCode")String innerCode);
}
