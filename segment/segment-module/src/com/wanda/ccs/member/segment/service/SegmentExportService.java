package com.wanda.ccs.member.segment.service;

import java.util.List;
import java.util.Map;

import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.member.segment.vo.FileAttachVo;
import com.wanda.ccs.member.segment.vo.SegmentExportVo;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;


public interface SegmentExportService {

	QueryResultVo<Map<String, Object>> queryList(QueryParamVo queryParam, List<ExpressionCriterion> criteria);

	SegmentExportVo get(Long segmentExportId);

	void insert(SegmentExportVo exportVo);
	
	Long getExportingCount(Long segmentId, String userId);
	
	void delete(String[] segmentExportIds);
	
	List<FileAttachVo> getFiles(Long segmentExportId);

}