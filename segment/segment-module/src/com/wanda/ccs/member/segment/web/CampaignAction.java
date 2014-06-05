package com.wanda.ccs.member.segment.web;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.pathlet.config.anno.InstanceIn;
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
import com.wanda.ccs.member.segment.service.CampaignService;
import com.wanda.ccs.member.segment.vo.CampaignCriteriaVo;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;

public class CampaignAction {
	
	@InstanceIn(path="CampaignService")
	private CampaignService service;
	/* 查询数据 */
	private String queryData;
	/* 查询参数 */
	private JqgridQueryParamVo queryParam;
	/* 表单数据 */
	private String json; 
	/* 推荐响应规则 */
	private String criteriaScheme;
	/* 影城范围 */
	private String cinemaScheme;
	/* 影城范围 */
	private String cinemaRange;
	/* 营销活动ID */
	private Integer cid;
	/* 营销活动状态 */
	private Integer status;
	
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
				
				String status = "草稿";
				switch(Integer.parseInt(row.get("STATUS").toString())) {
				case 10:
					status = "草稿";
					break;
				case 20:
					status = "生效";
					break;
				case 30:
					status = "执行";
					break;
				case 40:
					status = "结束";
					break;
				}
				row.put("STATUS_INFO", status);
				
				return row;
			}
		});
	}
	
	public JqgridQueryResult<Map<String, Object>> queryReport() throws Exception {
		final UserProfile userinfo = AuthUserHelper.getUser();
		
		ObjectMapper mapper = new ObjectMapper();
		final Map map = mapper.readValue(queryData, Map.class);
		
		List<ExpressionCriterion> criterionList = JsonCriteriaHelper.parseSimple(this.queryData);
		JqgridQueryConverter converter = new JqgridQueryConverter();
		QueryResultVo<Map<String, Object>> result = service.queryReportList(converter.convertParam(queryParam), criterionList, userinfo);
		
		final boolean editRight = userinfo.getRights().contains("member.extpointruleconditon.edit");
		
		return converter.convertResult(result, new ResultRowMapper<Map<String, Object>>() {
			public Map<String, Object> convert(Map<String, Object> row) {
				if("1".equals(map.get("type"))) {
					boolean readable = false;
					if(editRight == true && userinfo.getId().equals(row.get("CREATE_BY"))) {
						readable = true;
					}
					row.put("READABLE", readable);
					
					String status = "草稿";
					switch(Integer.parseInt(row.get("STATUS").toString())) {
					case 10:
						status = "草稿";
						break;
					case 20:
						status = "生效";
						break;
					case 30:
						status = "执行";
						break;
					case 40:
						status = "结束";
						break;
					}
					row.put("STATUS_INFO", status);
				}
				
				row.put("RECOMMEND", row.get("RECOMMEND_NUM") + " (" + getRate(Double.parseDouble(row.get("RECOMMEND_RATE").toString()) * 100) + "%)");
				row.put("CONTROL", row.get("CONTROL_NUM") + " (" + getRate(Double.parseDouble(row.get("CONTROL_RATE").toString()) * 100) + "%)");
				row.put("SUM", row.get("SUM_NUM") + " (" + getRate(Double.parseDouble(row.get("SUM_RATE").toString()) * 100) + "%)");
				row.put("RECOMMEND_COUNT", Integer.parseInt(row.get("CAL_COUNT").toString()) - Integer.parseInt(row.get("CONTROL_COUNT").toString()));
				
				return row;
			}
		});
	}
	
	private String getRate(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(Double.valueOf(value));
    }
	
	public CampaignCriteriaVo get() {
		CampaignCriteriaVo vo = null;
		
		if(cid != null) {
			vo = service.queryById(cid);
		}
		
		return vo;
	}
	
	public ResponseMessage status() {
		try {
			if(cid != null && status != null) {
				if(status == 20 || status == 10) {
					service.updateStatus(cid, status.toString());
					return new ResponseMessage(ResponseLevel.INFO, "营销活动状态更新成功！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseMessage(ResponseLevel.ERROR, e.getMessage());
		}
		
		return new ResponseMessage(ResponseLevel.ERROR, "营销活动状态更新失败！");
	}
	
	public ResponseMessage updateTime() throws Exception {
		try {
			CampaignCriteriaVo ccv = JsonCriteriaHelper.parseSimple(json, CampaignCriteriaVo.class);
			if(ccv.getCampaignId() != null) {
				CampaignCriteriaVo vo = service.queryById(Integer.parseInt(ccv.getCampaignId().toString()));
				if(vo != null && ccv.getEndDate() != null && !"".equals(ccv.getEndDate())) {
					String[] newEndDate = ccv.getEndDate().split("-");
					String[] endDate = vo.getEndDate().split("-");
					
					if(Integer.parseInt(newEndDate[0]+""+newEndDate[1]+""+newEndDate[2]) >= Integer.parseInt(endDate[0]+""+endDate[1]+""+endDate[2])) {
						UserProfile userProfile = AuthUserHelper.getUser();
						ccv.setUpdateBy(userProfile.getId());
						ccv.setUpdateDate(new Timestamp(System.currentTimeMillis()));
						service.updateTime(ccv);
						
						return new ResponseMessage(ResponseLevel.INFO, "营销活动结束日期更新成功！");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseMessage(ResponseLevel.ERROR, "营销活动结束日期更新失败！");
	}
	
	public ResponseMessage save() throws Exception {
		CampaignCriteriaVo ccv = JsonCriteriaHelper.parseSimple(json, CampaignCriteriaVo.class);
		ccv.setCriteriaScheme(criteriaScheme);
		if("[]".equals(ccv.getCriteriaScheme())) {
			ccv.setCriteriaScheme("");
		}
		ccv.setCriteriaScheme(ccv.getCriteriaScheme()+"%$%"+cinemaScheme);
		ccv.setCinemaScheme(" ");
		ccv.setCinemaRange(cinemaRange);
		//ccv.setCinemaRange(parseCinemaRange(cinemaScheme));
		
		if(ccv.getCampaignId() == null) {
			return insert(ccv);
		} else {
			return update(ccv);
		}
	}
	
	private ResponseMessage insert(CampaignCriteriaVo ccv) throws Exception {
		if(service.hasSameName(ccv.getName(), null)) {
			return new ResponseMessage(ResponseLevel.ERROR, "已有相同名称的营销活动,请修改名称！");
		} /*else if("[]".equals(ccv.getCriteriaScheme())){
			return new ResponseMessage(ResponseLevel.ERROR, "已选择条件中不能为空,请修改！");
		} */else {
			UserProfile userProfile = AuthUserHelper.getUser();
			if(userProfile != null) {
				//新建用户时保存用户的级别，区域，所属影城信息
				ccv.setOwnerLevel(userProfile.getLevelName());
				if(UserLevel.CINEMA == userProfile.getLevel()) {
					ccv.setOwnerRegion((userProfile.getRegionCode()));
					ccv.setOwnerCinema(userProfile.getCinemaId());
				}
				else if(UserLevel.REGION == userProfile.getLevel()) {
					ccv.setOwnerRegion((userProfile.getRegionCode()));
				}
			}
			
			Timestamp time = new Timestamp(System.currentTimeMillis());
			ccv.setCode("C"+System.currentTimeMillis());
			ccv.setCreateDate(time);
			ccv.setCreateBy(userProfile.getId());
			ccv.setUpdateDate(time);
			ccv.setUpdateBy(userProfile.getId());
			ccv.setVersion(1L);
			
			service.insert(ccv);
			return new VersionResponseMessage(ResponseLevel.INFO, "营销活动创建成功！", ccv.getVersion());
		}
	}
	
	private ResponseMessage update(CampaignCriteriaVo ccv) throws Exception {
		if(service.hasSameName(ccv.getName(), ccv.getCampaignId())) {
			return new ResponseMessage(ResponseLevel.ERROR, "已有相同名称的营销活动,请修改名称！");
		} /*else if("[]".equals(ccv.getCriteriaScheme())){
			return new ResponseMessage(ResponseLevel.ERROR, "已选择条件中不能为空,请修改！");
		} */else {
			UserProfile userProfile = AuthUserHelper.getUser();
			if(userProfile != null) {
				//新建用户时保存用户的级别，区域，所属影城信息
				ccv.setOwnerLevel(userProfile.getLevelName());
				if(UserLevel.CINEMA == userProfile.getLevel()) {
					ccv.setOwnerRegion((userProfile.getRegionCode()));
					ccv.setOwnerCinema(userProfile.getCinemaId());
				}
				else if(UserLevel.REGION == userProfile.getLevel()) {
					ccv.setOwnerRegion((userProfile.getRegionCode()));
				}
			}
			
			ccv.setUpdateDate(new Timestamp(System.currentTimeMillis()));
			ccv.setUpdateBy(userProfile.getId());
			
			service.update(ccv);
			return new VersionResponseMessage(ResponseLevel.INFO, "营销活动修改成功！", ccv.getVersion());
		}
	}
	
	public ResponseMessage delete() throws Exception {
		if(cid != null) {
			service.delete(cid);
		} else {
			return new ResponseMessage(ResponseLevel.ERROR, "营销活动删除失败！");
		}
		
		return new ResponseMessage(ResponseLevel.INFO, "营销活动删除成功！");
	}
	
	private String parseCinemaRange(String criteria) throws JsonParseException, JsonMappingException, IOException {
		//criteria = "[{\"inputId\":\"gender\",\"groupId\":\"memberLevel\",\"label\":\"性别\",\"groupLabel\":\"会员基本信息\",\"operator\":\"in\",\"value\":[\"M\"]},{\"inputId\":\"registerCinema\",\"groupId\":\"memberLevel\",\"label\":\"注册影城\",\"groupLabel\":\"会员基本信息\",\"operator\":\"in\",\"value\":{\"selTarget\":false,\"criteria\":[{\"inputId\":\"innerName\",\"label\":\"影城内部名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"area\",\"label\":\"区域\",\"operator\":\"in\",\"value\":[\"01\",\"02\"],\"valueLabel\":\"北京;上海\"},{\"inputId\":\"cityLevel\",\"label\":\"城市级别\",\"operator\":\"in\",\"value\":[\"01\",\"02\"],\"valueLabel\":\"一线;二线\"},{\"inputId\":\"cityName\",\"label\":\"城市名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"}],\"selections\":{\"value\":[],\"valueLabel\":[]}}},{\"inputId\":\"registerDate\",\"groupId\":\"memberLevel\",\"label\":\"入会时间\",\"groupLabel\":\"会员基本信息\",\"operator\":\"between\",\"value\":[\"2014-04-01 00:00:00\",\"2014-04-04 00:00:00\"]}]";
		//criteria = "[{\"inputId\":\"registerCinema\",\"groupId\":\"memberLevel\",\"label\":\"注册影城\",\"groupLabel\":\"会员基本信息\",\"operator\":\"in\",\"value\":{\"selTarget\":true,\"criteria\":[{\"inputId\":\"innerName\",\"label\":\"影城内部名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"},{\"inputId\":\"area\",\"label\":\"区域\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityLevel\",\"label\":\"城市级别\",\"operator\":\"in\",\"value\":[],\"valueLabel\":\"\"},{\"inputId\":\"cityName\",\"label\":\"城市名称\",\"operator\":\"eq\",\"value\":\"\",\"valueLabel\":\"\"}],\"selections\":{\"value\":[\"848\"],\"valueLabel\":[\"蚌埠万达广场店;蚌埠万达广场店;蚌埠万达广场店\"]}}},{\"inputId\":\"registerDate\",\"groupId\":\"memberLevel\",\"label\":\"入会时间\",\"groupLabel\":\"会员基本信息\",\"operator\":\"between\",\"value\":[\"2014-03-24 00:00:00\",\"2014-03-30 00:00:00\"]}]";
		
		String cinemaRange = "";
		Map<String, Object> map = new ObjectMapper().readValue(criteria, Map.class);
		map = (Map) map.get("compositeCinema");
		if(!"true".equals(map.get("selTarget").toString())) {
			List<LinkedHashMap<String, Object>> criteriaList = (List<LinkedHashMap<String, Object>>) map.get("criteria");
			String area = null, cityName = null, cityLevel = null, innerName = null;
			for(int j=0,jlen=criteriaList.size();j<jlen;j++) {
				map = (LinkedHashMap<String, Object>) criteriaList.get(j);
				String inputId =  map.get("inputId").toString();
				if("innerName".equals(inputId)) {
					innerName = map.get("valueLabel").toString();
				} else if("area".equals(inputId)) {
					area = map.get("valueLabel").toString();
				} else if("cityLevel".equals(inputId)) {
					cityLevel = map.get("valueLabel").toString();
				} else if("cityName".equals(inputId)) {
					cityName = map.get("valueLabel").toString();
				}
			}
			cinemaRange = "{\"地区\":\""+area+(cityName!=null && !"".equals(cityName)?"\",\"城市名称\":\""+cityName:"")+(cityLevel!=null && !"".equals(cityLevel)?"\",\"城市等级\":\""+cityLevel:"")+(innerName!=null && !"".equals(innerName)?"\",\"影城名称\":\""+innerName:"")+"\"}";
		} else {
			LinkedHashMap<String, Object> selections = (LinkedHashMap<String, Object>) map.get("selections");
			cinemaRange = "{\"影城名称\":\""+selections.get("valueLabel").toString().substring(1, selections.get("valueLabel").toString().length() - 1).toString().replace(",", ";")+"\"}";
		}
		System.out.println(cinemaRange);
		
		return cinemaRange;
	}
	
	/*public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		parseCinemaRange("");
	}*/

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

	public String getCriteriaScheme() {
		return criteriaScheme;
	}

	public void setCriteriaScheme(String criteriaScheme) {
		this.criteriaScheme = criteriaScheme;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCinemaScheme() {
		return cinemaScheme;
	}

	public void setCinemaScheme(String cinemaScheme) {
		this.cinemaScheme = cinemaScheme;
	}

	public String getCinemaRange() {
		return cinemaRange;
	}

	public void setCinemaRange(String cinemaRange) {
		this.cinemaRange = cinemaRange;
	}
	
}