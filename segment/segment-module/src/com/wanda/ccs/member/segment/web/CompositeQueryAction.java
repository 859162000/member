package com.wanda.ccs.member.segment.web;

import java.util.List;
import java.util.Map;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.util.ValueUtils;
import com.google.code.pathlet.vo.QueryResultVo;
import com.google.code.pathlet.web.widget.JqgridQueryConverter;
import com.google.code.pathlet.web.widget.JqgridQueryParamVo;
import com.google.code.pathlet.web.widget.JqgridQueryResult;
import com.google.code.pathlet.web.widget.ResultRowMapper;
import com.wanda.ccs.member.ap2in.AuthUserHelper;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.service.CompositeQueryService;
import com.wanda.ccs.member.segment.service.impl.CodeListServiceImpl;
import com.wanda.ccs.member.segment.vo.SegmentVo;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.JsonCriteriaHelper;

public class CompositeQueryAction {
 
	@InstanceIn(path="CompositeQueryService")
	private CompositeQueryService service;
	
	@InstanceIn(path="CodeListService")  
	private CodeListServiceImpl codeListService;

	private JqgridQueryParamVo queryParam;
	
	private String criteria;
	
	private String segmentType;

	public JqgridQueryParamVo getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(JqgridQueryParamVo queryParam) {
		this.queryParam = queryParam;
	}

	public String getSegmentType() {
		return segmentType;
	}

	public void setSegmentType(String segmentType) {
		this.segmentType = segmentType;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	public JqgridQueryResult<Map<String, Object>> querySegment() throws Exception {
		
		List<ExpressionCriterion> criterionList = JsonCriteriaHelper.parse(this.criteria);
		final UserProfile userProfile = AuthUserHelper.getUser();
		JqgridQueryConverter converter = new JqgridQueryConverter();
		QueryResultVo<Map<String, Object>> result = null;
		if(segmentType != null && "0".equals(segmentType)) { //只查询普通客群
			result = service.queryCampaignSegment(converter.convertParam(queryParam), criterionList, userProfile);
		} else {
			result = service.querySegment(converter.convertParam(queryParam), criterionList, userProfile);
		}
		
		//modified by fuby 20130728 end		
		
		//final boolean editRight = userProfile.getRights().contains("member.segment.edit");
		//final boolean exportRight = userProfile.getRights().contains("member.segment.export");
		
		return converter.convertResult(result, new ResultRowMapper<Map<String, Object>>() {
			public Map<String, Object> convert(Map<String, Object> row) {
				//设置该行记录是否可以编辑的标志
				/*boolean editable = false;
				boolean exportAble = false;
				if(editRight == true && userProfile.getId().equals(row.get("CREATE_BY"))) {
					editable = true;
				} else {
					String allowModifier = (String)row.get("ALLOW_MODIFIER");
					if(ValueUtils.notEmpty(allowModifier)) {
						List<String> users = Arrays.asList(allowModifier.split(","));
						if(users.contains(userProfile.getId())) {
							editable = true;
						}
					}
				}
				if(exportRight == true){
					exportAble = true;
				}else{
					exportAble = false;
				}
				row.put("EDITABLE", editable);
				row.put("EXPORTABLE", exportAble);*/
				
				//当STATUS为下边三种时，“实际数量”列显示状态，而非数量。
				String status = (String)row.get("STATUS");
				if(SegmentVo.STATUS_CALCULATING.equals(status)) {
					row.put("CAL_COUNT", "计算中...");
				}
				else if(SegmentVo.STATUS_NONE.equals(status)) {
					row.put("CAL_COUNT", "未计算");
				}
				else if(SegmentVo.STATUS_FAILED.equals(status)) {
					row.put("CAL_COUNT", "计算失败");
				}
				
				/*if("1".equals(row.get("COMBINE_SEGMENT")) ) {
					row.put("COMBINE_SEGMENT", "复合");
				}
				else {
					row.put("COMBINE_SEGMENT", "普通");
				}*/
				
				return row;
			}
		});
		
	}
	
	
	public JqgridQueryResult<Map<String, Object>> queryCinemas() throws Exception {
		
		List<ExpressionCriterion> criterionList = JsonCriteriaHelper.parse(this.criteria);

		JqgridQueryConverter converter = new JqgridQueryConverter();
		//modified by fuby 20130728 start		
		UserProfile userProfile = AuthUserHelper.getUser();
		QueryResultVo<Map<String, Object>> result = service.queryCinemas(converter.convertParam(queryParam), criterionList, userProfile);
		//modified by fuby 20130728 end		
		return converter.convertResult(result, new ResultRowMapper<Map<String, Object>>() {
			public Map<String, Object> convert(Map<String, Object> row) {
				//toCodeLabel(row, "AREA", "104");
				toCodeLabel(row, "CITY_LEVEL", "105");
				return row;
			}
		});
	}
	
	/**
	 * 查询活动相关信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public JqgridQueryResult<Map<String, Object>> queryActivitys() throws Exception {
		
		List<ExpressionCriterion> criterionList = JsonCriteriaHelper.parse(this.criteria);

		JqgridQueryConverter converter = new JqgridQueryConverter();
		QueryResultVo<Map<String, Object>> result = service.queryActivitys(converter.convertParam(queryParam), criterionList);	
		return converter.convertResult(result, new ResultRowMapper<Map<String, Object>>() {
			public Map<String, Object> convert(Map<String, Object> row) {
				return row;
			}
		});
	}

		
	public JqgridQueryResult<Map<String, Object>> queryFilms() throws Exception {
		
		List<ExpressionCriterion> criterionList = JsonCriteriaHelper.parse(this.criteria);

		JqgridQueryConverter converter = new JqgridQueryConverter();
		
		QueryResultVo<Map<String, Object>> result = service.queryFilms(converter.convertParam(queryParam), criterionList);

		return converter.convertResult(result, new ResultRowMapper<Map<String, Object>>() {
			public Map<String, Object> convert(Map<String, Object> row) {
				toCodeLabel(row, "SHOW_SET", "134");
				toCodeLabel(row, "FILM_CATE", "130");
				toCodeLabel(row, "COUNTRY", "148");
				return row;
			}
		});
	}
	
	public JqgridQueryResult<Map<String, Object>> queryConItems() throws Exception {
		
		List<ExpressionCriterion> criterionList = JsonCriteriaHelper.parse(this.criteria);

		JqgridQueryConverter converter = new JqgridQueryConverter();
		
		QueryResultVo<Map<String, Object>> result = service.queryConItems(converter.convertParam(queryParam), criterionList);
		
		return converter.convertResult(result, new ResultRowMapper<Map<String, Object>>() {
			public Map<String, Object> convert(Map<String, Object> row) {
				toCodeLabel(row, "ITEM_TYPE", "2004");
				toCodeLabel(row, "UNIT", "179");
				return row;
			}
		});
	}
	
	public JqgridQueryResult<Map<String, Object>> queryAuthUsers() throws Exception {
		
		List<ExpressionCriterion> criterionList = JsonCriteriaHelper.parse(this.criteria);

		JqgridQueryConverter converter = new JqgridQueryConverter();
		
		QueryResultVo<Map<String, Object>> result = service.queryAuthUsers(converter.convertParam(queryParam), criterionList);
		
		return converter.convertResult(result, new ResultRowMapper<Map<String, Object>>() {
			public Map<String, Object> convert(Map<String, Object> row) {
				String levelCode = (String)row.get("USER_LEVEL");
				String levelName = "";
				if(levelCode.equals("G")) {
					levelName = "院线";
				}
				else if(levelCode.equals("C")) {
					levelName = "影城";
				}
				else if(levelCode.equals("R")) {
					levelName = "区域";
				}	
				row.put("USER_LEVEL", levelName);
				return row;
			}
		});
	}
	
	private boolean toCodeLabel(Map<String, Object> row, String column, String typeId) {
		boolean changed = false;
		String value = (String)row.get(column);
		if(value != null) {
			String label = codeListService.getCodeList("dimdef", typeId).getEntryNameByValue(value);
			if(ValueUtils.notEmpty(label)) {
				row.put(column, label);
				changed = true;
			}
		}
		return changed;
	}
	
}