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
import java.util.List;
import java.util.Map;

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
							row.put("SEND_STATUS", "成功发送短信"+row.get("SEND_STATUS").toString()+"条");
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
			log.error("Message commit fail error! segmentId=" + this.segmentId,
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

	}

}