package com.wanda.mms.control.response;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.wanda.mms.data.TActResult;

public interface MarketingEffectMapper {
	public java.math.BigDecimal getAlterResponseCount(@Param("resultId") java.math.BigDecimal actResultId);

	public java.math.BigDecimal getRelResponseCount(@Param("resultId") java.math.BigDecimal actResultId);

	public java.math.BigDecimal getContractConunt(@Param("cmnActivityID") java.math.BigDecimal cmnActivityID);
	
	public java.math.BigDecimal getControlcount(@Param("cmnActivityID") java.math.BigDecimal cmnActivityID);
	
	public int updateResult(@Param("record") TActResult result);

	public BigDecimal getControlResCount(@Param("cmnActivityID") BigDecimal cmnActivity,	@Param("resSegment") BigDecimal resSegment);

	public TActResult getActResult(@Param("resultId") java.math.BigDecimal actResultId);
	
}
