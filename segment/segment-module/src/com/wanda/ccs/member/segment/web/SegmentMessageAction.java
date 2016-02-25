/**  
 * @Title: SegmentMessageAction.java
 * @Package com.wanda.ccs.member.segment.web
 * @Description: 客群短信发送控制器
 * @author 许雷
 * @date 2015年5月21日 上午10:08:51
 * @version V1.0  
 */
package com.wanda.ccs.member.segment.web;

import static com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper.parseSimple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.util.ValueUtils;
import com.google.code.pathlet.vo.QueryResultVo;
import com.google.code.pathlet.web.widget.JqgridQueryConverter;
import com.google.code.pathlet.web.widget.JqgridQueryParamVo;
import com.google.code.pathlet.web.widget.JqgridQueryResult;
import com.google.code.pathlet.web.widget.ResponseLevel;
import com.google.code.pathlet.web.widget.ResponseMessage;
import com.google.code.pathlet.web.widget.ResultRowMapper;
import com.wanda.ccs.member.ap2in.AuthUserHelper;
import com.wanda.ccs.member.ap2in.UserLevel;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.service.SegmentMessageService;
import com.wanda.ccs.member.segment.service.SegmentService;
import com.wanda.ccs.member.segment.vo.MessageApproveVo;
import com.wanda.ccs.member.segment.vo.SegmentMessageVo;
import com.wanda.ccs.member.segment.vo.SegmentVo;
import com.wanda.ccs.sqlasm.expression.CompositeValue;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;

/**
 * @ClassName: SegmentMessageAction
 * @Description: 客群短信发送控制器
 * @author 许雷
 * @date 2015年5月21日 上午10:08:51
 *
 */
public class SegmentMessageAction {

	private static Log log = LogFactory.getLog(SegmentMessageAction.class);

	private final String SEGM_FUHE = "复合";

	@InstanceIn(path = "SegmentMessageService")
	private SegmentMessageService segmentMessageService;

	@InstanceIn(path = "SegmentService")
	private SegmentService segmentService;

	private Long segmMessageId;

	private Long segmentId;

	private String segmentType;

	private String approve;

	private String json;

	private JqgridQueryParamVo queryParam;

	private String queryData;
	
	private String segmentIds;
	
	private String[] deletes;
	
	public String getApprove() {
		return approve;
	}

	public void setApprove(String approve) {
		this.approve = approve;
	}

	public Long getSegmMessageId() {
		return segmMessageId;
	}

	public void setSegmMessageId(Long segmMessageId) {
		this.segmMessageId = segmMessageId;
	}

	public String getSegmentType() {
		return segmentType;
	}

	public void setSegmentType(String segmentType) {
		this.segmentType = segmentType;
	}

	public String getQueryData() {
		return queryData;
	}

	public void setQueryData(String queryData) {
		this.queryData = queryData;
	}

	public JqgridQueryParamVo getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(JqgridQueryParamVo queryParam) {
		this.queryParam = queryParam;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}
	
	public String getSegmentIds() {
		return segmentIds;
	}

	public void setSegmentIds(String segmentIds) {
		this.segmentIds = segmentIds;
	}
	
	public String[] getDeletes() {
		return deletes;
	}
	
	public void setDeletes(String[] deletes) {
		this.deletes = deletes;
	}
	/**
	 * @Fields APPROVE_PIZHUN :提交状态
	 */
	private final String TIJIAO = "1";
	/**
	 * @Fields APPROVE_PIZHUN :保存状态
	 */
	private final String BAOCUN = "0";
	/**
	 * @Title: get
	 * @Description: 获得与前台交互对象方法
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return SegmentMessageDo 返回类型
	 * @throws
	 */
	public SegmentMessageDo get() throws Exception {
		SegmentMessageVo vo = segmentMessageService.get(segmMessageId);
		SegmentVo segmVo = segmentService.get(Long.valueOf(vo.getSegmentId()));
		SegmentMessageDo segment = new SegmentMessageDo(vo, segmVo);

		// 设置权限控制标准位allowModify，如果为true表示允许修改。
		// 目前控制为创建人，和授权修改人可以进行修改，其他人只能查看。
		UserProfile userProfile = AuthUserHelper.getUser();
		if (userProfile != null) {
			if (userProfile.getId().equals(segment.getCreateBy())) {
				segment.setAllowModify(true);
			} else {
				List<String> allowedUsers = segment.getCompAllowModifier()
						.getSelections().getValue();
				for (String userLoginId : allowedUsers) {
					if (userProfile.getId().equals(userLoginId)) {
						segment.setAllowModify(true);
						break;
					}
				}
			}
		} else if (ValueUtils.isEmpty(segment.getCreateBy())) {
			segment.setAllowModify(true); // 用于开发测试使用，当创建用户为空时的可群也可以被修改。
		}

		return segment;
	}

	/**
	 * @Title: createMessage
	 * @Description: 创建信息方法
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return ResponseMessage 返回类型
	 * @throws
	 */
	public ResponseMessage createMessage() throws Exception {
		UserProfile userProfile = AuthUserHelper.getUser();
		SegmentMessageVo segment = new SegmentMessageVo();
		segment.setSegmentId(String.valueOf(segmentId));
		segment.setCreateBy(userProfile.getId());
		if (UserLevel.CINEMA.equals(userProfile.getLevel())) {
			segment.setCinema(userProfile.getCinemaId()+"");
			segment.setArea(userProfile.getRegionCode());
		} else if (UserLevel.REGION.equals(userProfile.getLevel())) {
			segment.setArea(userProfile.getRegionCode());
		}
		try {
			if (SEGM_FUHE.equals(segmentType)) {
				return new ResponseMessage(ResponseLevel.INFO,
						"请选择普通客群创建客群信息项！");
			}
			segmentMessageService.insert(segment);
			return new ResponseMessage(ResponseLevel.INFO, "客群短信新建成功！");
		} catch (Throwable t) {
			log.error("Start calculate count error! segmentId="
					+ this.segmentId, t);
			return new ResponseMessage(ResponseLevel.WARNING,
					"客群短信新建失败！请与系统管理员联系。");
		}
	}

	/**
	 * @Title: approve
	 * @Description: 审批方法
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return ResponseMessage 返回类型
	 * @throws
	 */
	public ResponseMessage approve() throws Exception {
		UserProfile userProfile = AuthUserHelper.getUser();
		MessageApproveVo segment = JsonCriteriaHelper.parseSimple(json,
				MessageApproveVo.class);
		segmentMessageService.approve(segment, userProfile, approve);
		return new ResponseMessage(ResponseLevel.INFO, "更新短信审批成功！");
	}

	/**
	 * @Title: query
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return JqgridQueryResult<Map<String,Object>> 返回类型
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public JqgridQueryResult<Map<String, Object>> query() throws Exception {

		final UserProfile user = AuthUserHelper.getUser();
		final boolean C_EditRight = user.getRights().contains("member.messageApprove.cinema");
		final boolean R_EditRight = user.getRights().contains("member.messageApprove.region");
		final boolean G_EditRight = user.getRights().contains("member.messageApprove.group");

		List<ExpressionCriterion> criterionList = parseSimple(this.queryData);
		JqgridQueryConverter converter = new JqgridQueryConverter();
		QueryResultVo<Map<String, Object>> result = segmentMessageService
				.queryList(converter.convertParam(queryParam), criterionList,
						user);

		return converter.convertResult(result,
				new ResultRowMapper<Map<String, Object>>() {
					public Map<String, Object> convert(Map<String, Object> row) {
						row.put("APPROVEABLE", false);// 默认为非当前审批人
						row.put("EDITABLE", false);// 默认为非创建人
						row.put("SENDABLE", false);// 默认为未发送
						if (row.get("SEND_STATUS") == null||"-1".equals(row.get("SEND_STATUS").toString())) {
							row.put("SEND_STATUS", "未发送");
						}else if("0".equals(row.get("SEND_STATUS").toString())){
							row.put("SEND_STATUS", "发送失败");
						} else{
							Integer failSend = row.get("CAL_COUNT").toString() != "-1"?Integer.parseInt(row.get("CAL_COUNT").toString())-Integer.parseInt(row.get("SEND_STATUS").toString()):0;
							row.put("SEND_STATUS", "短信发送成功"+row.get("SEND_STATUS").toString()+"条，失败"+failSend+"条");
							row.put("SENDABLE", true);// 已发送
						}
						if (row.get("APPROVE_STATUS") == null) {
							row.put("APPROVE_STATUS", "未提交审批");
						} else {
							if ("3999".equals(row.get("APPROVE_STATUS"))) {
								row.put("APPROVE_STATUS", "由院线会员经理退回修改");
							}
							if ("2999".equals(row.get("APPROVE_STATUS"))) {
								row.put("APPROVE_STATUS", "由区域经理退回修改");
							}
							if ("1999".equals(row.get("APPROVE_STATUS"))) {
								row.put("APPROVE_STATUS", "由影城经理退回修改");
							}
							if ("3000".equals(row.get("APPROVE_STATUS"))) {
								row.put("APPROVE_STATUS", "待院线会员经理审批");
								if(G_EditRight){
									row.put("APPROVEABLE", true);
								}
							}
							if ("2000".equals(row.get("APPROVE_STATUS"))) {
								row.put("APPROVE_STATUS", "待区域经理审批");
								if(R_EditRight){
									row.put("APPROVEABLE", true);
								}
							}
							if ("1000".equals(row.get("APPROVE_STATUS"))) {
								row.put("APPROVE_STATUS", "待影城经理审批");
								if(C_EditRight){
									row.put("APPROVEABLE", true);
								}
							}
							if ("9000".equals(row.get("APPROVE_STATUS"))) {
								row.put("APPROVE_STATUS", "审批完成");
								if(C_EditRight){
									row.put("APPROVEABLESEND", true);
								}
							}
							if ("9999".equals(row.get("APPROVE_STATUS"))) {
								row.put("APPROVE_STATUS", "审批不通过");
							}
						}
						if (user.getId().equals(row.get("CREATE_BY"))) {
							row.put("EDITABLE", true);
							row.put("SENDABLE", true);// 不可发送
						}
						// 当STATUS为下边三种时，“实际数量”列显示状态，而非数量。
						String status = (String) row.get("STATUS");
						if (SegmentVo.STATUS_CALCULATING.equals(status)) {
							row.put("CAL_COUNT", "计算中...");
						} else if (SegmentVo.STATUS_NONE.equals(status)) {
							row.put("CAL_COUNT", "未计算");
						} else if (SegmentVo.STATUS_FAILED.equals(status)) {
							row.put("CAL_COUNT", "计算失败");
						}
						return row;
					}
				});
	}

	/**
	 * @Title: toApprve
	 * @Description: 创建人提交调用的方法
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return ResponseMessage 返回类型
	 * @throws
	 */
	public ResponseMessage toApprove() throws Exception {
		UserProfile userProfile = AuthUserHelper.getUser();
		SegmentMessageVo segment = JsonCriteriaHelper.parseSimple(json,
				SegmentMessageVo.class);
		try {
			segmentMessageService.saveMessage(segment, userProfile,TIJIAO);//提交审批
			return new ResponseMessage(ResponseLevel.INFO, "客群短信提交成功！");
		} catch (Throwable t) {
			log.error("Message commit fail error! segmentId=" + this.segmentId,
					t);
			return new ResponseMessage(ResponseLevel.WARNING,
					"客群短信提交失败！请与系统管理员联系。");
		}
	}

	/**
	 * @Title: toSend
	 * @Description: 发送信息方法
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return ResponseMessage 返回类型
	 * @throws
	 */
	public ResponseMessage toSend() throws Exception {
		UserProfile userProfile = AuthUserHelper.getUser();
		SegmentMessageVo segment = JsonCriteriaHelper.parseSimple(json,
				SegmentMessageVo.class);
		try {
			segmentMessageService.sendMessage(segment.getSegmMessageId(),userProfile);
			return new ResponseMessage(ResponseLevel.INFO, "客群短信提交成功！");
		} catch (Throwable t) {
			log.error("Message commit fail error! segmentMessageId=" + segment.getSegmMessageId(),
					t);
			return new ResponseMessage(ResponseLevel.WARNING,
					"客群短信提交失败！请与系统管理员联系。");
		}
	}

	/**
	 * @Title: toApprve
	 * @Description: 保存修改短信信息方法
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return ResponseMessage 返回类型
	 * @throws
	 */
	public ResponseMessage toSave() throws Exception {
		UserProfile userProfile = AuthUserHelper.getUser();
		SegmentMessageVo segment = JsonCriteriaHelper.parseSimple(json,
				SegmentMessageVo.class);
		try {
			segmentMessageService.saveMessage(segment, userProfile, BAOCUN);//保存用户信息
			return new ResponseMessage(ResponseLevel.INFO, "客群短信保存成功！");
		} catch (Throwable t) {
			log.error("Message commit fail error! segmentId=" + this.segmentId,
					t);
			return new ResponseMessage(ResponseLevel.WARNING,
					"客群短信提交失败！请与系统管理员联系。");
		}
	}

	/**
	 * 用于和界面交互时的客群信息对象
	 *
	 */
	public static class SegmentMessageDo extends SegmentVo {

		private static final long serialVersionUID = -7945272703287772550L;

		private CompositeValue compAllowModifier;

		private boolean allowModify = false;

		private Long noSendCal;

		private String sendTime;

		private String content;

		private Long segmMessageId;

		private String approveStatus;
		
		private String wordContent;

		public String getApproveStatus() {
			return approveStatus;
		}

		public void setApproveStatus(String approveStatus) {
			this.approveStatus = approveStatus;
		}

		public SegmentMessageDo() {
			this.compAllowModifier = new CompositeValue();
		}

		public String getCalCountStr() {
			// 当STATUS为下边三种时，“实际数量”列显示状态，而非数量。
			if (SegmentVo.STATUS_CALCULATING.equals(getStatus())) {
				return "计算中...";
			} else if (SegmentVo.STATUS_NONE.equals(getStatus())) {
				return "未计算";
			} else if (SegmentVo.STATUS_FAILED.equals(getStatus())) {
				return "计算失败";
			}
			return String.valueOf(getCalCount());
		}

		public SegmentMessageDo(SegmentMessageVo vo, SegmentVo segmVo)
				throws Exception {
			super();
			PropertyUtils.copyProperties(this, segmVo);
			String usersStr = vo.getAllowModifier();
			this.noSendCal = vo.getNoSendCal();
			this.sendTime = vo.getSendTime();
			this.content = vo.getContent();
			this.segmMessageId = vo.getSegmMessageId();
			this.approveStatus = vo.getApproveStatus();
			this.wordContent = vo.getWordContent();
			if (ValueUtils.notEmpty(usersStr)) {
				List<String> users = Arrays.asList(usersStr.split(","));
				this.compAllowModifier = new CompositeValue(users, users);
			} else {
				this.compAllowModifier = new CompositeValue();
			}
		}

		// compAllowModifier
		public void convertAllowModifier() {
			List<String> sels = compAllowModifier.getSelections().getValue();
			if (sels != null && sels.size() > 0) {
				boolean first = true;
				StringBuilder buf = new StringBuilder();
				for (String user : sels) {
					if (first) {
						buf.append(user);
						first = false;
					} else {
						buf.append(",").append(user);
					}
				}
				this.setAllowModifier(buf.toString());
			}
		}

		public Long getSegmMessageId() {
			return segmMessageId;
		}

		public void setSegmMessageId(Long segmMessageId) {
			this.segmMessageId = segmMessageId;
		}

		public Long getNoSendCal() {
			return noSendCal;
		}

		public void setNoSendCal(Long noSendCal) {
			this.noSendCal = noSendCal;
		}

		public String getSendTime() {
			return sendTime;
		}

		public void setSendTime(String sendTime) {
			this.sendTime = sendTime;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public CompositeValue getCompAllowModifier() {
			return compAllowModifier;
		}

		public void setCompAllowModifier(CompositeValue compAllowModifier) {
			this.compAllowModifier = compAllowModifier;
		}

		public boolean isAllowModify() {
			return allowModify;
		}

		public void setAllowModify(boolean allowModify) {
			this.allowModify = allowModify;
		}

		public String getWordContent() {
			return wordContent;
		}

		public void setWordContent(String wordContent) {
			this.wordContent = wordContent;
		}
		
		

	}

	/**
	 * @Title: createMessage
	 * @Description: 创建信息方法
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return ResponseMessage 返回类型
	 * @throws
	 */
	public ResponseMessage saveMessages() throws Exception {
		UserProfile userProfile = AuthUserHelper.getUser();
		if (segmentIds != "" && segmentIds.length() > 0) {
			SegmentMessageVo segment = JsonCriteriaHelper.parseSimple(json,
					SegmentMessageVo.class);
			segment.setCreateBy(userProfile.getId());
			segment.setUpdateBy(userProfile.getId());
			if (UserLevel.CINEMA.equals(userProfile.getLevel())) {
				segment.setCinema(userProfile.getCinemaId()+"");
				segment.setArea(userProfile.getRegionCode());
			} else if (UserLevel.REGION.equals(userProfile.getLevel())) {
				segment.setArea(userProfile.getRegionCode());
			}
			String batchId = segment.getBatchId();
			if (batchId == null || batchId == "") {
				batchId = UUID.randomUUID().toString();
				batchId = batchId.replaceAll("-", "");
				segment.setBatchId(batchId);
			}
			try {
				if (SEGM_FUHE.equals(segmentType)) {
					return new ResponseMessage(ResponseLevel.INFO,
							"请选择普通客群创建客群信息项！");
				}
				segmentIds = segmentIds.substring(1);
				String[] segmentIdArray = segmentIds.split(",");
				for (int i = 0; i < segmentIdArray.length; i++) {
					segment.setSegmentId(segmentIdArray[i]);
					segmentMessageService.insert(segment);
				}
				return new ResponseMessage(ResponseLevel.INFO, batchId);
			} catch (Throwable t) {
				log.error("Start calculate count error! segmentId="
						+ this.segmentId, t);
				return new ResponseMessage(ResponseLevel.WARNING,
						"客群短信新建失败！请与系统管理员联系。");
			}
		} else {
			return new ResponseMessage(ResponseLevel.WARNING,
					"请选择客群信息！");
		}
		
	}
	
	/**
	 * @Title: toApprve
	 * @Description: 创建人提交调用的方法
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return ResponseMessage 返回类型
	 * @throws
	 */
	public ResponseMessage toApproveMassages() throws Exception {
		UserProfile userProfile = AuthUserHelper.getUser();
		SegmentMessageVo segment = JsonCriteriaHelper.parseSimple(json,
				SegmentMessageVo.class);
		try {
			if (segment.getBatchId() != null || segment.getBatchId() != "") {
				List<SegmentMessageVo> list = segmentMessageService.getSegmentMessageByBatchId(segment.getBatchId());
				for (SegmentMessageVo segmentVo : list) {
					segmentMessageService.saveMessage(segmentVo, userProfile,TIJIAO);//提交审批
				}
				return new ResponseMessage(ResponseLevel.INFO, "提交审批成功！");
			} else {
				return new ResponseMessage(ResponseLevel.WARNING,
						"请先保存客群短信信息然后在进行提交操作！");
			}
			
		} catch (Throwable t) {
			log.error("Message commit fail error! segmentId=" + this.segmentId,
					t);
			return new ResponseMessage(ResponseLevel.WARNING,
					"客群短信提交失败！请与系统管理员联系。");
		}
	}
	
	public ResponseMessage delete() throws Exception {
		try {
			String batchId = "";
			for(String segmentMessageIdStr : deletes) {
				Long segmMessageId = Long.parseLong(segmentMessageIdStr);
				
				SegmentMessageVo vo = segmentMessageService.get(segmMessageId);
				batchId = vo.getBatchId();
				UserProfile userProfile = AuthUserHelper.getUser();
				if(userProfile != null) {
					if(userProfile.getId().equals(vo.getCreateBy()) == false) {
						return new ResponseMessage(ResponseLevel.ERROR, "您不是创建人，不能删除该客群短信");
					}
				}
			}
			segmentMessageService.logicDelete(deletes);
			return new ResponseMessage(ResponseLevel.INFO, batchId);
		} catch (Throwable t) {
			log.error("Message commit fail error! segmentMessageId=" + this.segmMessageId,
					t);
			return new ResponseMessage(ResponseLevel.WARNING,
					"客群短信删除失败！请与系统管理员联系。");
		}
	}
	/**
	 * @Title: checkWord
	 * @Description: 校验敏感字方法
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return ResponseMessage 返回类型
	 * @throws
	 */
	public ResponseMessage checkWord() throws Exception {
		try {
			String[] strs = json.split("-W!O@R#D-");
			if (strs.length > 1) {
				String content = strs[0];
				String wordStr = strs[1];
				wordStr = wordStr.replace(",", "，");
				String[] wordArgs =wordStr.split("，");
				Set<String> keyWordSet = new HashSet<String>();
				for (int i =0; i < wordArgs.length; i++) {
					String word = wordArgs[i];
					keyWordSet.add(word);
				}
				SensitivewordFilter filter = new SensitivewordFilter(keyWordSet);
				Set<String> set = filter.getSensitiveWord(content, 1);
				if (set != null && set.size() > 0) {
					String massage = "短信中含有敏感字："+set+" ，请核查！";
					return new ResponseMessage(ResponseLevel.WARNING, massage);
				} else {
					return new ResponseMessage(ResponseLevel.INFO, "提交成功！");
				}
			} else {
				return new ResponseMessage(ResponseLevel.INFO, "提交成功！");
			}
			
			
		}catch (Throwable t) {
			log.error("客群短信敏感字校验失败！请与系统管理员联系。");
			return new ResponseMessage(ResponseLevel.WARNING,
					"客群短信敏感字校验失败！请与系统管理员联系。");
		}
	}
}