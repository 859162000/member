package com.wanda.ccs.member.segment.service.impl;

import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.*;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ColumnMapRowMapper;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.member.ap2in.UserLevel;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.SegmentConstants;
import com.wanda.ccs.member.segment.service.CompositeQueryService;
import com.wanda.ccs.member.segment.web.ConCategoryAction;
import com.wanda.ccs.sqlasm.Clause;
import com.wanda.ccs.sqlasm.ClauseParagraph;
import com.wanda.ccs.sqlasm.Condition;
import com.wanda.ccs.sqlasm.CriteriaParser;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.DataType;
import com.wanda.ccs.sqlasm.expression.ArrayExpCriterion;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.SingleExpCriterion;
import com.wanda.ccs.sqlasm.impl.DefaultClauseParagraph;

public class CompositeQueryServiceImpl implements CompositeQueryService {

	private ExtJdbcTemplate jdbcTemplate = null;
	
	@InstanceIn(path="/dataSource")  
	private DataSource dataSource;

	private ExtJdbcTemplate getJdbcTemplate()  {
		if(this.jdbcTemplate == null) {
			this.jdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.jdbcTemplate;
	}
	
	public QueryResultVo<Map<String, Object>> queryActivitys(QueryParamVo queryParam, List<ExpressionCriterion> criteria) {
		// 主表 活动
		Clause campaign = newPlain().in("from").output("T_CAMPAIGN campaign");
		// 活动波次
		Clause cmnActivityJoin = newPlain().in("from").output("T_CMN_ACTIVITY activity")
				.depends(newPlain().in("where").output("activity.CAMPAIGN_ID=campaign.CAMPAIGN_ID"));
		// 波次阶段
		Clause cmnPhaseJoin = newPlain().in("from").output("T_CMN_PHASE phase")
				.depends(newPlain().in("where").output("activity.CMN_PHAISE_ID=phase.CMN_PHAISE_ID"));
		// 波次目标
		Clause targetJoin = newPlain().in("from").output("T_ACT_TARGET target")
				.depends(newPlain().in("where").output("activity.ACT_TARGET_ID=target.ACT_TARGET_ID"))
				.depends(cmnActivityJoin);
		// 客户群目标
		Clause segmentJoin = newPlain().in("from").output("T_SEGMENT segment")
				.depends(newPlain().in("where").output("target.SEGMENT_ID=segment.SEGMENT_ID"))
				.depends(targetJoin);
		
		// 添加查询条件
		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		// 波次名称
		clauseMap.put(notEmpty("cmnName"), newExpression().in("where").output("activity.NAME", DataType.STRING));
		// 波次开始时间
		clauseMap.put(notEmpty("cmnStartDate"), newExpression().in("where").output("activity.START_DTIME", DataType.DATE));
		// 波次结束时间
		clauseMap.put(notEmpty("cmnEndDate"), newExpression().in("where").output("activity.END_DTIME", DataType.DATE));
		// 目标客群名称
		clauseMap.put(notEmpty("sengentName"), newExpression().in("where").output("segment.NAME", DataType.STRING));
		// 阶段名称
		clauseMap.put(notEmpty("phaseName"), newExpression().in("where").output("phase.NAME", DataType.STRING));
		// 活动名称
		clauseMap.put(notEmpty("campaignName"), newExpression().in("where").output("campaign.NAME", DataType.STRING));
		
		// 拼装count解析条件
		CriteriaParser countParser = newParser(QUERY_PARAGRAPHS)
				.add(newPlain().in("select").output("count(activity.CMN_ACTIVITY_ID)"))
				.add(campaign)	// 添加主表
				.add(cmnPhaseJoin)
				.add(segmentJoin)
				.add(clauseMap);
		// 解析count参数
		CriteriaResult countResult = countParser.parse(criteria);
		
		// 拼装list解析条件
		CriteriaParser listParser = newParser(QUERY_PARAGRAPHS)
				.add(newPlain().in("select").output("activity.CMN_ACTIVITY_ID as INNER_CODE,activity.name as INNER_NAME,campaign.NAME as CAMPAIGN_NAME,activity.START_DTIME,activity.END_DTIME,phase.NAME as PHASE_NAME,segment.NAME as SENGENT_NAME"))
				.add(campaign)	// 添加主表
				.add(cmnPhaseJoin)
				.add(segmentJoin)
				.add(clauseMap);
		// 解析list参数
		CriteriaResult listResult = listParser.parse(criteria);
		
		StringBuilder listSql = new StringBuilder(listResult.getComposedText());
		if("INNER_NAME".equals(queryParam.getSortName())) {
			listSql.append(" order by campaign.NAME ").append(queryParam.getSortOrder());
		}
		
		Long rowCount = getJdbcTemplate().queryForLong(countResult.getComposedText(), countResult.getParameters().toArray());
		List<Map<String, Object>> listQueryResult = getJdbcTemplate()
				.query(queryParam.getStartIndex(), queryParam.getFetchSize(), listSql.toString(), listResult.getParameters().toArray(), new ColumnMapRowMapper());
		
		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
	}
	
	public QueryResultVo<Map<String, Object>> queryCinemas(QueryParamVo queryParam, List<ExpressionCriterion> criteria, UserProfile userProfile) {
		
		Clause cinemaTable = newPlain().in("from").output("T_CINEMA C");
		
		// 根据用户信息决定选择的影城
		UserLevel level = userProfile.getLevel();
		String regionCode = userProfile.getRegionCode();
		long cinemaId = userProfile.getCinemaId();
		Clause userCaluse = null;
		if (UserLevel.GROUP == level) {
			// 院线用户:可以选择全部影城
			userCaluse = newPlain().in("where").output("1=1");
		} else if (UserLevel.REGION == level) {
			// 区域用户：只能选择区域下的影城
			userCaluse = newPlain().in("where").output("ad.code='" + regionCode + "'");
		} else if (UserLevel.CINEMA == level) {
			// 影城用户：只能看到自己的影城
			userCaluse = newPlain().in("where").output("ad.code='" + regionCode + "'  and c.seqid=" + cinemaId);
		}
	
		//Clause defWhere = newPlain().in("where").output("C.ISOPEN='1' and C.DELETED='0'", true);
		
	
		Clause cityJoin = newPlain().in("from").output("T_CITY CY")
				.depends(newPlain().in("where").output("C.CITY=CY.SEQID"));
		Clause areaJoin = newPlain().in("from").output("T_DIMDEF AD")
				.depends(newPlain().in("where").output("C.AREA=AD.CODE and AD.TYPEID=104"));
		

		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		
		clauseMap.put(notEmpty("innerName"), newExpression().in("where").output("C.INNER_NAME", DataType.STRING));
		clauseMap.put(notEmpty("area"), newExpression().in("where").output("C.AREA", DataType.STRING));
		clauseMap.put(notEmpty("cityLevel"), newExpression().in("where").output("CY.CITYLEVEL", DataType.STRING)
				.depends(cityJoin));
		clauseMap.put(notEmpty("cityName"), newExpression().in("where").output("CY.NAME", DataType.STRING)
				.depends(cityJoin));
		
		CriteriaParser countParser = newParser(QUERY_PARAGRAPHS)
				.add(newPlain().in("select").output("count(C.SEQID)"))
				.add(cinemaTable)
				.add(cityJoin)
				//.add(defWhere)
				.add(areaJoin)
				.add(clauseMap);
		
		CriteriaResult countResult = countParser.parse(criteria);
		
		
		CriteriaParser listParser = newParser(QUERY_PARAGRAPHS)
				.add(newPlain().in("select").output("C.INNER_CODE, C.INNER_NAME, C.OUT_NAME, AD.NAME as AREA, CY.CITYLEVEL as CITY_LEVEL, CY.NAME as CITY_NAME")) 
				.add(cinemaTable)
				.add(cityJoin)
				.add(areaJoin) 
				//.add(defWhere) 
				.add(userCaluse)
				.add(clauseMap);
		

		CriteriaResult listResult = listParser.parse(criteria);
		
		StringBuilder listSql = new StringBuilder(listResult.getComposedText());
		if("INNER_NAME".equals(queryParam.getSortName())) {
			listSql.append(" order by C.INNER_NAME ").append(queryParam.getSortOrder());
		}
		//else if("AREA".equals(queryParam.getSortName())) {
		//	listSql.append(" order by AD.NAME ").append(queryParam.getSortOrder());
		//}
		
		logSql(countResult.getComposedText(), listSql.toString(), listResult.getParameters());

		Long rowCount = getJdbcTemplate().queryForLong(countResult.getComposedText(), countResult.getParameters().toArray());
		List<Map<String, Object>> listQueryResult = getJdbcTemplate()
				.query(queryParam.getStartIndex(), queryParam.getFetchSize(), listSql.toString(), listResult.getParameters().toArray(), new ColumnMapRowMapper());

		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
	}

	public QueryResultVo<Map<String, Object>> queryConItems(QueryParamVo queryParam, List<ExpressionCriterion> criteria) {
		Clause itemTable = newPlain().in("from").output("T_CON_ITEM I");
		Clause defWhere = newPlain().in("where").output("I.VALID='1' and C.ITEM_TYPE<>'" + SegmentConstants.MARTERIAL_TYPE + "'");
		
		Clause cateJoin = newPlain().in("from").output("T_CON_CATEGORY C")
		.depends(newPlain().in("where").output("C.CON_CATEGORY_ID=I.CON_CATEGORY_ID"));

		
		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		clauseMap.put(notEmpty("itemCode"), newExpression().in("where").output("I.ITEM_CODE", DataType.STRING));
		clauseMap.put(notEmpty("itemName"), newExpression().in("where").output("I.ITEM_NAME", DataType.STRING));
		clauseMap.put(notEmpty("conCategoryId"), newExpression().in("where").output("I.CON_CATEGORY_ID", DataType.STRING));
		clauseMap.put(notEmpty("unit"), newExpression().in("where").output("I.UNIT", DataType.STRING));

		CriteriaParser countParser = newParser(QUERY_PARAGRAPHS)
				.add(newPlain().in("select").output("count(I.CON_ITEM_ID)"))
				.add(cateJoin)
				.add(itemTable)
				.add(defWhere)
				.add(clauseMap);

		CriteriaResult countResult = countParser.parse(criteria);
		
		CriteriaParser listParser = newParser(QUERY_PARAGRAPHS)
				.add(newPlain().in("select").output("I.CON_ITEM_ID, I.ITEM_CODE, I.ITEM_NAME, I.SHORT_NAME, C.ITEM_TYPE, I.UNIT, C.CATEGORY_BREADCRUMBS"))
				.add(cateJoin)
				.add(itemTable)
				.add(defWhere)
				.add(clauseMap);		

		CriteriaResult listResult = listParser.parse(criteria);
		
		StringBuilder listSql = new StringBuilder(listResult.getComposedText());
		
		if("ITEM_NAME".equals(queryParam.getSortName())) {
			listSql.append(" order by I.ITEM_NAME ").append(queryParam.getSortOrder());
		}
		else if("ITEM_CODE".equals(queryParam.getSortName())) {
			listSql.append(" order by I.ITEM_CODE ").append(queryParam.getSortOrder());
		}
		
		logSql(countResult.getComposedText(), listSql.toString(), listResult.getParameters());
		
		Long rowCount = getJdbcTemplate().queryForLong(countResult.getComposedText(), countResult.getParameters().toArray());
		List<Map<String, Object>> listQueryResult = getJdbcTemplate()
				.query(queryParam.getStartIndex(), queryParam.getFetchSize(), listSql.toString(), listResult.getParameters().toArray(), new ColumnMapRowMapper());

		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
	}

	
	public QueryResultVo<Map<String, Object>> queryFilms(QueryParamVo queryParam, List<ExpressionCriterion> criteria) {
		Clause filmTable = newPlain().in("from").output("T_FILM F");//查询主表
		Clause defWhere = newPlain().in("where").output("F.deleted=0 and F.FILM_CODE is not null and F.SHOW_SET is not null");//默认的必要条件
	
		Clause typeJoin = newPlain().in("from").output("T_FT_TYPE FT")
				.depends(newPlain().in("where").output("FT.FILM_ID=F.SEQID"));


		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		clauseMap.put(notEmpty("filmName"), newExpression().in("where").output("F.FILM_NAME", DataType.STRING));
		clauseMap.put(notEmpty("showSet"), newExpression().in("where").output("F.SHOW_SET", DataType.STRING));
		clauseMap.put(notEmpty("filmTypes"), newExpression().in("where").output("FT.TYPE", DataType.STRING).depends(typeJoin));
		clauseMap.put(notEmpty("filmCate"), newExpression().in("where").output("F.FILM_CATE", DataType.STRING));
		clauseMap.put(notEmpty("country"), newExpression().in("where").output("F.COUNTRY", DataType.STRING));
		clauseMap.put(notEmpty("director"), newExpression().in("where").output("F.DIRECTOR", DataType.STRING));
		clauseMap.put(notEmpty("mainActors"), newExpression().in("where").output("F.MAIN_ACTORS", DataType.STRING));

		
		CriteriaParser countParser = newParser(QUERY_PARAGRAPHS)
				.add(newPlain().in("select").output("count(distinct F.FILM_CODE)"))
				.add(filmTable)
				.add(defWhere)
				.add(clauseMap);
		CriteriaResult countResult = countParser.parse(criteria);
		
		CriteriaParser listParser =newParser(QUERY_PARAGRAPHS)
				.add(newPlain().in("select").output("distinct F.FILM_CODE, F.FILM_NAME, F.SHOW_SET, F.FILM_CATE, F.COUNTRY, F.DIRECTOR, F.MAIN_ACTORS, F.PREMIERE_DATE, F.END_DATE"))
				.add(filmTable)
				.add(defWhere)
				.add(clauseMap);

		CriteriaResult listResult = listParser.parse(criteria);
		
		StringBuilder listSql = new StringBuilder(listResult.getComposedText());
		if("PREMIERE_DATE".equals(queryParam.getSortName())) {
			listSql.append(" order by F.PREMIERE_DATE ").append(queryParam.getSortOrder());
		}
		else if("FILM_NAME".equals(queryParam.getSortName())) {
		listSql.append(" order by F.FILM_NAME ").append(queryParam.getSortOrder());
		}

		logSql(countResult.getComposedText(), listSql.toString(), listResult.getParameters());
		
		Long rowCount = getJdbcTemplate().queryForLong(countResult.getComposedText(), countResult.getParameters().toArray());
		List<Map<String, Object>> listQueryResult = getJdbcTemplate()
				.query(queryParam.getStartIndex(), queryParam.getFetchSize(), listSql.toString(), listResult.getParameters().toArray(), new ColumnMapRowMapper());

		for(Map<String, Object> row : listQueryResult) {
			Object filmCode = row.get("FILM_CODE");
			List<String> types = getJdbcTemplate().queryForList(
					"select DIM.name from T_FILM IF, T_FT_TYPE IFT, T_DIMDEF DIM where DIM.typeid=131 and DIM.CODE=IFT.TYPE and IFT.FILM_ID=IF.SEQID and IF.FILM_CODE=?", 
					String.class,
					filmCode);
			StringBuilder typesBuf = new StringBuilder();
			for(String type : types) {
				typesBuf.append(type).append(',');
			}
			int len = typesBuf.length();
			if(len >= 1) {
				typesBuf.deleteCharAt(len - 1);
			}
			row.put("FILM_TYPES", typesBuf.toString());
		}
		
		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
		
	}
	

	public QueryResultVo<Map<String, Object>> queryAuthUsers(
			QueryParamVo queryParam, List<ExpressionCriterion> criteria) {
		//example query SQL
		//		select A.AUTH_USER_ID, nvl(A.LOGIN_ID,A.RTX) LOGIN_ID, A.USER_NAME, A.USER_EMAIL, A.USER_LEVEL, R.NAME as REGION_NAME, C.INNER_NAME as CINEMA_NAME, A.UPDATE_DATE UPDATE_DATE
		//		 from T_AUTH_USER A
		//		 left join T_CINEMA C on C.SEQID = A.CINEMA
		//		 left join T_DIMDEF R on R.CODE = A.REGION and R.TYPEID=104
		//		 where A.STATUS='E' and (A.LOGIN_ID is not null or A.RTX is not null) and
		//		 A.LOGIN_ID='sytyj' or A.RTX='sytyj'
		
		ClauseParagraph[] groups = {
				new DefaultClauseParagraph("select",  " select ",   " ",  ","),
				new DefaultClauseParagraph("from",    " from ",     " ",  ","),
				new DefaultClauseParagraph("leftjoin","",      "",  " "),
				new DefaultClauseParagraph("where",   " where ",    " ",  " and "),
				new DefaultClauseParagraph("having",  " having ",   " ",  " and "),
				new DefaultClauseParagraph("groupby", " group by ", " ",  ","),
				new DefaultClauseParagraph("orderby", " order by ", " ",  ","),
			};
		
		Clause mainTable = newPlain().in("from").output("T_AUTH_USER A");
		Clause cinemaJion = newPlain().in("leftjoin").output("left join T_CINEMA C on C.SEQID = A.CINEMA");
		Clause regionJion = newPlain().in("leftjoin").output("left join T_DIMDEF R on R.CODE = A.REGION and R.TYPEID=104");
		
		Clause defWhere = newPlain().in("where").output("A.STATUS='E' and (A.LOGIN_ID is not null or A.RTX is not null)");
		

		
		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		clauseMap.put(notEmpty("loginId"), newValue().in("where").output("(A.LOGIN_ID={0} or A.RTX={0})", DataType.STRING, false));
		clauseMap.put(notEmpty("userName"), newExpression().in("where").output("A.USER_NAME", DataType.STRING));

		CriteriaParser countParser = newParser(groups)
			.add(newPlain().in("select").output("count(A.AUTH_USER_ID)"))
			.add(mainTable)
			.add(defWhere)
			.add(clauseMap);
				
		CriteriaResult countResult = countParser.parse(criteria);
		
		
		CriteriaParser listParser = newParser(groups)
			.add(newPlain().in("select").output("A.AUTH_USER_ID, nvl(A.LOGIN_ID,A.RTX) LOGIN_ID, A.USER_NAME, A.USER_EMAIL, A.USER_LEVEL, R.NAME as REGION_NAME, C.INNER_NAME as CINEMA_NAME, A.UPDATE_DATE UPDATE_DATE")) 
			.add(cinemaJion)
			.add(regionJion)
			.add(mainTable)
			.add(defWhere)
			.add(clauseMap);

		CriteriaResult listResult = listParser.parse(criteria);
		
		StringBuilder listSql = new StringBuilder(listResult.getComposedText());
		
		if("UPDATE_DATE".equals(queryParam.getSortName())) {
			listSql.append(" order by A.UPDATE_DATE ").append(queryParam.getSortOrder());
		}
		else if("USER_NAME".equals(queryParam.getSortName())) {
			listSql.append(" order by A.USER_NAME ").append(queryParam.getSortOrder());
		}
		else if("LOGIN_ID".equals(queryParam.getSortName())) {
			listSql.append(" order by A.LOGIN_ID ").append(queryParam.getSortOrder());
		}
		
		logSql(countResult.getComposedText(), listSql.toString(), listResult.getParameters());
		
		Long rowCount = getJdbcTemplate().queryForLong(countResult.getComposedText(), countResult.getParameters().toArray());
		List<Map<String, Object>> listQueryResult = getJdbcTemplate()
				.query(queryParam.getStartIndex(), queryParam.getFetchSize(), listSql.toString(), listResult.getParameters().toArray(), new ColumnMapRowMapper());

		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
	}

	private void logSql(String countSql, String listSql, List<Object> paramemters) {
		System.out.println("CountSQL:" + countSql);
		System.out.println("ListSQL:" + listSql);
		System.out.println("ParametersCount:" + paramemters.size());
		System.out.println(paramemters);
	}
	
	@Override
	public QueryResultVo<Map<String, Object>> querySegment(
			QueryParamVo queryParam, List<ExpressionCriterion> criteria,
			UserProfile userProfile) {
		
		criteria.add(new SingleExpCriterion("userLevel", userProfile.getLevel().name()));
		criteria.add(new ArrayExpCriterion("userInfo",
				new String[] {userProfile.getId(), Long.toString(userProfile.getCinemaId()), userProfile.getRegionCode()}));

		criteria.add(new ArrayExpCriterion("orderby", null, null, null, 
				new String[]{queryParam.getSortName(), queryParam.getSortOrder()}));
		
		Clause segmentTable = newPlain().in("from").output("T_SEGMENT s");

		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		// 客群编码
		clauseMap.put(notEmpty("code"), newExpression().in("where").output("s.CODE", DataType.STRING));
		// 客群名称
		clauseMap.put(notEmpty("segmentName"), newExpression().in("where").output("s.NAME", DataType.STRING));
		// 创建人
		clauseMap.put(notEmpty("createBy"), newExpression().in("where").output("s.CREATE_BY", DataType.STRING));
		// 更新时间
		clauseMap.put(notEmpty("updateTime"), newExpression().in("where", false).output("s.UPDATE_DATE", DataType.DATE_TIME));
		// 院线的用户
		clauseMap.put(equalsValue("userLevel", "REGION"), newValue().from("userInfo").in("where")
				.output("(s.OWNER_LEVEL='REGION' or s.OWNER_LEVEL='CINEMA') and s.OWNER_REGION={2}", DataType.STRING, false));
		// 影城用户
		clauseMap.put(equalsValue("userLevel", "CINEMA"), newValue().from("userInfo").in("where")
				.output("s.OWNER_LEVEL='CINEMA' and s.OWNER_CINEMA={1}", DataType.LONG, false));
		// 
		clauseMap.put(notEmpty("orderby"), newValue().in("orderby").output("s.{0} {1}", DataType.SQL, true));

		CriteriaParser countParser = newParser(SELECT_PARAGRAPHS)
			.add(newPlain().in("select").output("count(s.SEGMENT_ID)"))
			.add(newPlain().in("where").output("s.ISDELETE='0' and s.COMBINE_SEGMENT='0'"))
			.add(segmentTable)
			.add(clauseMap);
		
		CriteriaResult countResult = countParser.parse(criteria);
		
		CriteriaParser listParser = newParser(SELECT_PARAGRAPHS)
				.add(newPlain().in("select").output("s.SEGMENT_ID, s.CODE, s.NAME, s.CAL_COUNT,s.CONTROL_COUNT, s.STATUS, s.COMBINE_SEGMENT, s.CREATE_BY, s.UPDATE_BY, s.UPDATE_DATE, s.ALLOW_MODIFIER"))
				.add(newPlain().in("where").output("s.ISDELETE='0' and s.COMBINE_SEGMENT='0'"))
				.add(segmentTable)
				.add(clauseMap);

		CriteriaResult listResult = listParser.parse(criteria);

		logSql(countResult.getComposedText(), listResult.getComposedText(), listResult.getParameters());

		Long rowCount = getJdbcTemplate().queryForLong(
				countResult.getComposedText(),
				countResult.getParameters().toArray());
		
		List<Map<String, Object>> listQueryResult = getJdbcTemplate().query(
				queryParam.getStartIndex(), queryParam.getFetchSize(),
				listResult.getComposedText(), listResult.getParameters().toArray(),
				new ColumnMapRowMapper());

		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
	}
	
	@Override
	public QueryResultVo<Map<String, Object>> queryCampaignSegment(
			QueryParamVo queryParam, List<ExpressionCriterion> criteria,
			UserProfile userProfile) {
		
		criteria.add(new SingleExpCriterion("userLevel", userProfile.getLevel().name()));
		criteria.add(new ArrayExpCriterion("userInfo",
				new String[] {userProfile.getId(), Long.toString(userProfile.getCinemaId()), userProfile.getRegionCode()}));

		criteria.add(new ArrayExpCriterion("orderby", null, null, null, 
				new String[]{queryParam.getSortName(), queryParam.getSortOrder()}));
		
		Clause segmentTable = newPlain().in("from").output("T_SEGMENT s");

		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		// 客群编码
		clauseMap.put(notEmpty("code"), newExpression().in("where").output("s.CODE", DataType.STRING));
		// 客群名称
		clauseMap.put(notEmpty("segmentName"), newExpression().in("where").output("s.NAME", DataType.STRING));
		// 创建人
		clauseMap.put(notEmpty("createBy"), newExpression().in("where").output("s.CREATE_BY", DataType.STRING));
		// 更新时间
		clauseMap.put(notEmpty("updateTime"), newExpression().in("where", false).output("s.UPDATE_DATE", DataType.DATE_TIME));
		// 院线的用户
		clauseMap.put(equalsValue("userLevel", "REGION"), newValue().from("userInfo").in("where")
				.output("(s.OWNER_LEVEL='REGION' or s.OWNER_LEVEL='CINEMA') and s.OWNER_REGION={2}", DataType.STRING, false));
		// 影城用户
		clauseMap.put(equalsValue("userLevel", "CINEMA"), newValue().from("userInfo").in("where")
				.output("s.OWNER_LEVEL='CINEMA' and s.OWNER_CINEMA={1}", DataType.LONG, false));
		// 
		clauseMap.put(notEmpty("orderby"), newValue().in("orderby").output("s.{0} {1}", DataType.SQL, true));

		CriteriaParser countParser = newParser(SELECT_PARAGRAPHS)
			.add(newPlain().in("select").output("count(s.SEGMENT_ID)"))
			.add(newPlain().in("where").output("s.ISDELETE='0'"))
			.add(segmentTable)
			.add(clauseMap);
		
		CriteriaResult countResult = countParser.parse(criteria);
		
		CriteriaParser listParser = newParser(SELECT_PARAGRAPHS)
				.add(newPlain().in("select").output("s.SEGMENT_ID, s.CODE, s.NAME, s.CAL_COUNT,s.CONTROL_COUNT, s.STATUS, s.COMBINE_SEGMENT, s.CREATE_BY, s.UPDATE_BY, s.UPDATE_DATE, s.ALLOW_MODIFIER"))
				.add(newPlain().in("where").output("s.ISDELETE='0'"))
				.add(segmentTable)
				.add(clauseMap);

		CriteriaResult listResult = listParser.parse(criteria);

		logSql(countResult.getComposedText(), listResult.getComposedText(), listResult.getParameters());

		Long rowCount = getJdbcTemplate().queryForLong(
				countResult.getComposedText(),
				countResult.getParameters().toArray());
		
		List<Map<String, Object>> listQueryResult = getJdbcTemplate().query(
				queryParam.getStartIndex(), queryParam.getFetchSize(),
				listResult.getComposedText(), listResult.getParameters().toArray(),
				new ColumnMapRowMapper());

		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
	}
	
}
