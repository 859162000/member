/**  
 * @Title: SegmentMessageService.java
 * @Package com.wanda.ccs.member.segment.service
 * @Description: 客群信息发送接口
 * @author 许雷
 * @date 2015年5月21日 上午10:27:16
 * @version V1.0  
 */
package com.wanda.ccs.member.segment.service;

import java.util.List;
import java.util.Map;

import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.vo.MessageApproveVo;
import com.wanda.ccs.member.segment.vo.SegmentMessageVo;
import com.wanda.ccs.member.segment.vo.SendLogVo;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;

/**
 * @ClassName: SegmentMessageService
 * @Description: 客群信息发送接口
 * @author 许雷
 * @date 2015年5月21日 上午10:27:16
 *
 */
public interface SegmentMessageService {

	SegmentMessageVo get(Long segmentMessageId);

	String insert(SegmentMessageVo entity);

	String saveMessage(SegmentMessageVo entity, UserProfile userProfile, String status);

	String insertApprove(MessageApproveVo entity, UserProfile userProfile);
	
	String approve(MessageApproveVo entity, UserProfile userProfile,
			String status);

	void sendMessage(Long messageSendId, UserProfile userProfile);

	List<String> getMoible(String messageSendId);

	@SuppressWarnings("rawtypes")
	QueryResultVo<Map<String, Object>> queryList(QueryParamVo queryParam,
			List<ExpressionCriterion> criteria, UserProfile userinfo);

	String insertSendLog(SendLogVo entity);
	/**
	 * 根据批次ID获取客群短信信息
	 */
	List<SegmentMessageVo> getSegmentMessageByBatchId(String batchId);

	/**
	 * 删除短信信息
	 */
	void logicDelete(String[] deletes);
}
