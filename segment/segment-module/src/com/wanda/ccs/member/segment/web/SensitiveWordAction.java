package com.wanda.ccs.member.segment.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.web.widget.JqgridQueryParamVo;
import com.wanda.ccs.member.segment.service.CriteriaQueryResult;
import com.wanda.ccs.member.segment.service.CriteriaQueryService;
import com.wanda.ccs.member.segment.service.SensitiveService;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.google.code.pathlet.util.ValueUtils;
import com.google.code.pathlet.vo.QueryResultVo;
import com.google.code.pathlet.web.widget.JqgridQueryConverter;
import com.google.code.pathlet.web.widget.JqgridQueryResult;
import com.google.code.pathlet.web.widget.ResponseLevel;
import com.google.code.pathlet.web.widget.ResponseMessage;
import com.google.code.pathlet.web.widget.ResultRowMapper;
import com.wanda.ccs.member.ap2in.AuthUserHelper;
import com.wanda.ccs.member.ap2in.UserLevel;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.vo.SensitiveWordVo;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;

public class SensitiveWordAction {
	private static Log log = LogFactory.getLog(SensitiveWordAction.class);

	@InstanceIn(path = "SensitiveService")
	private SensitiveService sensitiveService;
	@InstanceIn(path = "CriteriaQueryService")
	private CriteriaQueryService criteriaQueryService;
	private Long wordId;

	private String json;

	private JqgridQueryParamVo queryParam;

	private String queryData;

	public Long getWordId() {
		return wordId;
	}

	public void setWordId(Long wordId) {
		this.wordId = wordId;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public JqgridQueryParamVo getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(JqgridQueryParamVo queryParam) {
		this.queryParam = queryParam;
	}

	public String getQueryData() {
		return queryData;
	}

	public void setQueryData(String queryData) {
		this.queryData = queryData;
	}

	public JqgridQueryResult<Map<String, Object>> query() throws Exception {

		final UserProfile userProfile = AuthUserHelper.getUser();
		List<ExpressionCriterion> criterionList = JsonCriteriaHelper
				.parseSimple(this.queryData);
		JqgridQueryConverter converter = new JqgridQueryConverter();
		QueryResultVo<Map<String, Object>> result = sensitiveService.queryList(
				converter.convertParam(queryParam), criterionList, userProfile);
		long querycount=result.getRowCount();
		System.out.println("querycount:"+result.getRowCount());
		log.info("querycount:"+querycount);

		return converter.convertResult(result,
				new ResultRowMapper<Map<String, Object>>() {
					public Map<String, Object> convert(Map<String, Object> row) {
						// 设置该行记录是否可以编辑的标志
						System.out.println(row+"22222222");
						return row;
					}
				});
	}

	public ResponseMessage insert() throws Exception {
		SensitiveDo sensitive = JsonCriteriaHelper.parseSimple(json,
				SensitiveDo.class);

		// segment.convertAllowModifier();
		if(sensitiveService.hasSameName(sensitive.getWordTitle().trim(), null)) {
			return new ResponseMessage(ResponseLevel.ERROR, "已有相同标题的敏感词，请修改敏感词标题！");
		}else{
			UserProfile userProfile = AuthUserHelper.getUser();
			if (userProfile != null) {
				// 新建用户时保存用户的级别，区域，所属影城信息

				if (UserLevel.CINEMA == userProfile.getLevel()) {
					sensitive.setIssueRegion((userProfile.getRegionCode()));
					sensitive.setIssueCinema(userProfile.getCinemaId());
				} else if (UserLevel.REGION == userProfile.getLevel()) {
					sensitive.setIssueRegion((userProfile.getRegionCode()));
				}
			}
			sensitiveService.insert(sensitive);

			try {
				return new VersionResponseMessage(ResponseLevel.INFO, "敏感词保存成功！",
						sensitive.getVersion());
			} catch (Throwable t) {
				log.error("Start calculate count error! wordId=" + this.wordId, t);
				return new VersionResponseMessage(ResponseLevel.WARNING,
						"敏感词保存失败。", sensitive.getVersion());
			}
		}
		

	}

	/* 查看 */
	public SensitiveWordVo get() throws Exception {
		SensitiveWordVo vo = sensitiveService.get(wordId);
		return vo;
	}

	public ResponseMessage update() throws Exception {
		SensitiveDo sensitive = JsonCriteriaHelper.parseSimple(json,
				SensitiveDo.class);
		if(sensitiveService.hasSameName(sensitive.getWordTitle().trim(), sensitive.getWordId())) {
			return new ResponseMessage(ResponseLevel.ERROR, "已有相同标题的敏感词，请修改敏感词标题！");
		}else{
			sensitiveService.update(sensitive);
			return new VersionResponseMessage(ResponseLevel.INFO, "敏感词更新成功！",
					sensitive.getVersion());
		}
		
	}

	public ResponseMessage delete() throws Exception {

		UserProfile userProfile = AuthUserHelper.getUser();
		sensitiveService.logicDelete(wordId);
		return new ResponseMessage(ResponseLevel.INFO, "敏感词删除成功！");
	}

	public CriteriaQueryResult getCriteriaResult() throws Exception {
		SensitiveWordVo vo = sensitiveService.get(wordId);
		CriteriaQueryResult result = sensitiveService.getSegmentQuery(vo
				.getWordId());
		return result;
	}

	/**
	 * 用于和界面交互时的敏感词对象
	 * 
	 */
	public static class SensitiveDo extends SensitiveWordVo {

		// private static final long serialVersionUID = -7945272703287772550L;

		private boolean allowModify = false;

		public SensitiveDo() {
		}

		public SensitiveDo(SensitiveWordVo vo) throws Exception {
			// super();
			PropertyUtils.copyProperties(this, vo);

		}

		// compAllowModifier
		/*
		 * public void convertAllowModifier () { List<String> sels =
		 * compAllowModifier.getSelections().getValue(); if(sels != null &&
		 * sels.size() > 0) { boolean first = true; StringBuilder buf = new
		 * StringBuilder(); for(String user : sels) { if(first) {
		 * buf.append(user); first = false; } else {
		 * buf.append(",").append(user); } }
		 * this.setAllowModifier(buf.toString()); } }
		 */

		/*
		 * public CompositeValue getCompAllowModifier() { return
		 * compAllowModifier; }
		 * 
		 * public void setCompAllowModifier(CompositeValue compAllowModifier) {
		 * this.compAllowModifier = compAllowModifier; }
		 */
		public boolean isAllowModify() {
			return allowModify;
		}

		public void setAllowModify(boolean allowModify) {
			this.allowModify = allowModify;
		}

	}

	/**
	 * 用于和界面交互时的对象
	 * 
	 * @author
	 * 
	 */
	public static class CombineSegmentDo {
		/* 敏感词ID */
		private Long wordId;
		/* 敏感词标题 */
		private String wordTitle;
		/* 敏感词内容 */
		private String wordContent;
		/* 状态 */
		private String status;
		/* 更新人 */
		private String updateBy;
		/* 更新时间 */
		private Timestamp updateDate;
		/*  */
		private String allowModifier;
		/* 版本号 */
		private Long version;
		private String isDelete;// 是否删除标志

		public Long getWordId() {
			return wordId;
		}

		public void setWordId(Long wordId) {
			this.wordId = wordId;
		}

		public String getWordTitle() {
			return wordTitle;
		}

		public void setWordTitle(String wordTitle) {
			this.wordTitle = wordTitle;
		}

		public String getWordContent() {
			return wordContent;
		}

		public void setWordContent(String wordContent) {
			this.wordContent = wordContent;
		}

		public String getIsDelete() {
			return isDelete;
		}

		public void setIsDelete(String isDelete) {
			this.isDelete = isDelete;
		}

		/**
		 * 获取实体
		 * 
		 * @return
		 */
		public SensitiveWordVo getSensitiveWordVo() {
			SensitiveWordVo vo = new SensitiveWordVo();
			vo.setWordId(wordId);
			vo.setWordTitle(wordTitle);
			vo.setWordContent(wordContent);
			vo.setStatus(status);
			vo.setUpdateBy(updateBy);
			vo.setUpdateDate(updateDate);
			// vo.setAllowModifier(allowModifier);
			vo.setUpdateBy(updateBy);
			vo.setUpdateDate(updateDate);
			vo.setVersion(version);
			return vo;
		}

		public Long getVersion() {
			return version;
		}

		public void setVersion(Long version) {
			this.version = version;
		}

		public String getUpdateBy() {
			return updateBy;
		}

		public void setUpdateBy(String updateBy) {
			this.updateBy = updateBy;
		}

		public Timestamp getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(Timestamp updateDate) {
			this.updateDate = updateDate;
		}

		public String getAllowModifier() {
			return allowModifier;
		}

		public void setAllowModifier(String allowModifier) {
			this.allowModifier = allowModifier;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

	}
}
