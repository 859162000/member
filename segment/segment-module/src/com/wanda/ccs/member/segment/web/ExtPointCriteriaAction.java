package com.wanda.ccs.member.segment.web;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

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
import com.wanda.ccs.member.segment.service.CriteriaQueryService;
import com.wanda.ccs.member.segment.service.ExtPointCriteriaService;
import com.wanda.ccs.member.segment.vo.ExtPointCriteriaVo;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;

public class ExtPointCriteriaAction {
	
	@InstanceIn(path="ExtPointCriteriaService")
	private ExtPointCriteriaService service;
	
	@InstanceIn(path="CriteriaQueryService")
	private CriteriaQueryService criteriaQueryService;
	
	private Long extPointCriteriaId;
	
	private String json; 

	private String criteriaScheme;
	
	private JqgridQueryParamVo queryParam;
	
	private String queryData;

	private String[] deletes;
	
	public Long getExtPointCriteriaId() {
		return extPointCriteriaId;
	}
	public void setExtPointCriteriaId(Long extPointCriteriaId) {
		this.extPointCriteriaId = extPointCriteriaId;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getCriteriaScheme() {
		return criteriaScheme;
	}
	public void setCriteriaScheme(String criteriaScheme) {
		this.criteriaScheme = criteriaScheme;
	}
	public String[] getDeletes() {
		return deletes;
	}
	public void setDeletes(String[] deletes) {
		this.deletes = deletes;
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
		final UserProfile userinfo = AuthUserHelper.getUser();

		List<ExpressionCriterion> criterionList = JsonCriteriaHelper.parseSimple(this.queryData);
		JqgridQueryConverter converter = new JqgridQueryConverter();
		QueryResultVo<Map<String, Object>> result = service.queryList(converter.convertParam(queryParam), criterionList, userinfo);

		final boolean editRight = userinfo.getRights().contains("member.extpointruleconditon.edit");

		return converter.convertResult(result, new ResultRowMapper<Map<String, Object>>() {
			public Map<String, Object> convert(Map<String, Object> row) {
				boolean editable = false;
				if(editRight == true && userinfo.getId().equals(row.get("CREATE_BY"))) {
					editable = true;
				}
				row.put("EDITABLE", editable);
				
				return row;
			}
		});
	}
	
	
	public ResponseMessage delete() throws Exception {
		//判断是否已经被引用，如被引用禁止删除
		for(String criteriaIdStr : deletes) {
			Long criteriaId = Long.parseLong(criteriaIdStr);
			ExtPointCriteriaVo vo = service.get(criteriaId);
			UserProfile userProfile = AuthUserHelper.getUser();
			if(userProfile != null) {
				if(userProfile.getId().equals(vo.getCreateBy()) == false) {
					return new ResponseMessage(ResponseLevel.ERROR, "您不是创建人，不能删除特殊积分条件:'" + vo.getName() + "'");
				}
			}
			else {
				//用于开发测试使用，当创建用户为允许被删除。
				if(ValueUtils.notEmpty(vo.getCreateBy())){
					return new ResponseMessage(ResponseLevel.ERROR, "您不是创建人，不能删除特殊积分条件:'" + vo.getName() + "'");
				}
			}
			
			if(service.hasReference(criteriaId)) {
				return new ResponseMessage(ResponseLevel.ERROR, "特殊积分条件:'" + vo.getName() + "' 已经被引用不能删除！");
			}
		}
		
		service.delete(deletes);
		return new ResponseMessage(ResponseLevel.INFO, "特殊积分条件删除成功！");
	}

	public ExtPointCriteriaDo get() throws Exception {
		ExtPointCriteriaVo vo = service.get(this.extPointCriteriaId);
		
		if(vo != null) {
			ExtPointCriteriaDo criteriaDo = new ExtPointCriteriaDo(vo);
			//设置权限控制标准位allowModify，如果为true表示允许修改。
			//目前控制为创建人可以进行修改，其他人只能查看。
			UserProfile userProfile = AuthUserHelper.getUser();
			if(userProfile != null) {
				if(userProfile.getId().equals(criteriaDo.getCreateBy())) {
					criteriaDo.setAllowModify(true);
				}
			}
			else if(ValueUtils.isEmpty(criteriaDo.getCreateBy())){
				criteriaDo.setAllowModify(true); //用于开发测试使用，当创建用户为空时的可群也可以被修改。
			}
			

			return criteriaDo;
		}
		else {
			return null;
		}
	}
	
	public CriteriaResult getConItemCriteriaResult() throws Exception {
		ExtPointCriteriaVo vo = service.get(this.extPointCriteriaId);
		return criteriaQueryService.getExtPointConSaleQuery(vo.getCriteriaScheme());
	}

	public CriteriaResult getTicketCriteriaResult() throws Exception {
		ExtPointCriteriaVo vo = service.get(this.extPointCriteriaId);
		return criteriaQueryService.getExtPointTicketQuery(vo.getCriteriaScheme());
	}
	
	public ResponseMessage insert() throws Exception {
		ExtPointCriteriaVo epc = JsonCriteriaHelper.parseSimple(json, ExtPointCriteriaVo.class);
		epc.setCriteriaScheme(criteriaScheme);
		
		if(service.hasSameName(epc.getName(), null)) {
			return new ResponseMessage(ResponseLevel.ERROR, "已有相同名称的特殊积分条件，请修改名称！");
		}
		else if("[]".equals(epc.getCriteriaScheme())){
			return new ResponseMessage(ResponseLevel.ERROR, "已选择条件中不能为空,请修改！");
		}
		else {
			UserProfile userProfile = AuthUserHelper.getUser();
			if(userProfile != null) {

				//新建用户时保存用户的级别，区域，所属影城信息
				epc.setOwnerLevel(userProfile.getLevelName());
				if(UserLevel.CINEMA == userProfile.getLevel()) {
					epc.setOwnerRegion((userProfile.getRegionCode()));
					epc.setOwnerCinema(userProfile.getCinemaId());
				}
				else if(UserLevel.REGION == userProfile.getLevel()) {
					epc.setOwnerRegion((userProfile.getRegionCode()));
				}
			}
			
			service.insert(epc);
			return new VersionResponseMessage(ResponseLevel.INFO, "特殊积分条件保存成功！", epc.getVersion());
		}
	}
	
	public ResponseMessage update() throws Exception {
		ExtPointCriteriaVo vo = JsonCriteriaHelper.parseSimple(json, ExtPointCriteriaVo.class);
		
		System.out.println("Updating extPointCriteriaId:" + vo.getExtPointCriteriaId() + ", Version:" + vo.getVersion());
		
		vo.setCriteriaScheme(criteriaScheme);
		
		if(service.hasSameName(vo.getName(), vo.getExtPointCriteriaId())) {
			return new ResponseMessage(ResponseLevel.ERROR, "已有相同名称的特殊积分条件，请修改名称！");
		}
		else if("[]".equals(vo.getCriteriaScheme())){
			return new ResponseMessage(ResponseLevel.ERROR, "已选择条件中不能为空,请修改！");
		}
		else {
			service.update(vo);
			return new VersionResponseMessage(ResponseLevel.INFO, "特殊积分条件更新成功！", vo.getVersion());
		}
	}
	

	/**
	 * 用于和界面交互时的特殊积分条件对象
	 * @author Charlie Zhang
	 *
	 */
	public static class ExtPointCriteriaDo extends ExtPointCriteriaVo {

		private boolean allowModify = false;
		
		public ExtPointCriteriaDo() {
		
		}

		public ExtPointCriteriaDo(ExtPointCriteriaVo vo) throws Exception {
			super();
			PropertyUtils.copyProperties(this, vo);
		}

		public boolean isAllowModify() {
			return allowModify;
		}

		public void setAllowModify(boolean allowModify) {
			this.allowModify = allowModify;
		}

	}
}