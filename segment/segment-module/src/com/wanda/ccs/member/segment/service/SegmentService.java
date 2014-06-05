package com.wanda.ccs.member.segment.service;

import java.util.List;
import java.util.Map;

import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.vo.CombineSegmentSubVo;
import com.wanda.ccs.member.segment.vo.SegmentVo;
import com.wanda.ccs.member.segment.web.SegmentAction.CombineSegmentDo;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;



public interface SegmentService {
	
	SegmentVo get(Long segmentId);
	
	SegmentVo getByCode(String code);

	QueryResultVo<Map<String, Object>> queryList(QueryParamVo parameters, List<ExpressionCriterion> criterionList, UserProfile userProfile);

	void insert(SegmentVo segment);
	
	void update(SegmentVo segment);
	
	void delete(String[] segmentIds);
	
	void logicDelete(String[] segmentIds);
	
	/**
	 * 更新客群数量
	 * @param segmentId 客群id
	 * @param calCount 新的客群数量
	 * @return
	 * @throws Exception 
	 */
	void updateStartCalCount(Long segmentId) throws Exception;
	
	String segmentOccupied(Long segmentId);
	
	/**
	 * 重置客群的状态为 "10" 未计算 状态。只有"20"计算中"40"计算失败状态才可以更改计算状态为10 
	 * @param segmentId
	 * @return 如果更新成功返回true, 失败返回false；一般失败是因为客群状态不是"20"计算中"40"计算失败状态
	 */
	boolean updateResetStatus(Long segmentId);
	
	SegmentVo getCount(Long segmentId);
	
	/**
	 * 判断是否有重名客群。
	 * @param segmentName
	 * @param selfSegmentId 如果是更新前的排重检查，则需要排除当前更新客群后再检查是否有重复。
	 * @return
	 */
	boolean hasSameName(String segmentName, Long selfSegmentId);
	
	boolean hasReferenceSegment(Long segmentId);
	
	Map<String, Object> getCombineSegments(Long segmentId);
	
	/**
	 * 根据客群活动ID查询数据
	 * 
	 * @param segmentIds
	 * @return
	 */
	List<CombineSegmentSubVo> getSelectedSegment(Long[] segmentIds);
	
	void insertCombineSegment(SegmentVo segment, List<CombineSegmentSubVo> sub);
	
	void updateCombineSegment(CombineSegmentDo combineSegmentDo);
	
	/**
	 * 根据主客群ID获取复合客群当前差值计算状态
	 * 
	 * @param segmentId
	 * @return
	 */
	List<Map<String, Object>> getCountAlter(Long segmentId);
	
}