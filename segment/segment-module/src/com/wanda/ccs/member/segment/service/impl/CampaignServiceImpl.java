package com.wanda.ccs.member.segment.service.impl;

import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.SELECT_PARAGRAPHS;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.equalsValue;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.newParser;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.notEmpty;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newExpression;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newPlain;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.service.CampaignService;
import com.wanda.ccs.member.segment.vo.CampaignCriteriaVo;
import com.wanda.ccs.sqlasm.Clause;
import com.wanda.ccs.sqlasm.Condition;
import com.wanda.ccs.sqlasm.CriteriaParser;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.DataType;
import com.wanda.ccs.sqlasm.expression.ArrayExpCriterion;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.Operator;
import com.wanda.ccs.sqlasm.expression.SingleExpCriterion;

@Transactional(rollbackFor={java.lang.Exception.class})
public class CampaignServiceImpl implements CampaignService {

	private ExtJdbcTemplate jdbcTemplate = null;
	
	@InstanceIn(path="/dataSource")  
	private DataSource dataSource;
	
	private ExtJdbcTemplate getJdbcTemplate()  {
		if(jdbcTemplate == null) {
			jdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		
		return this.jdbcTemplate;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public QueryResultVo<Map<String, Object>> queryList(QueryParamVo queryParam, List<ExpressionCriterion> criteria, UserProfile userinfo) {
		
		criteria.add(new SingleExpCriterion("userLevel", userinfo.getLevel().name()));
		criteria.add(new ArrayExpCriterion("userInfo",
				new String[] {userinfo.getId(), Long.toString(userinfo.getCinemaId()), userinfo.getRegionCode()}));
		
		criteria.add(new ArrayExpCriterion("orderby", null, null, null, 
				new String[]{queryParam.getSortName(), queryParam.getSortOrder()}));
		
		Clause mainTable = newPlain().in("from").output("T_CAMPAIGN_BASE s");

		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		// 编码
		clauseMap.put(notEmpty("code"), newExpression().in("where").output("s.CODE", DataType.STRING, Operator.EQUAL));
		// 名称
		clauseMap.put(notEmpty("name"), newExpression().in("where").output("s.NAME", DataType.STRING, Operator.LIKE));
		// 开始时间
		clauseMap.put(notEmpty("startDateFrom"), newExpression().in("where", false).output("s.START_DATE", DataType.DATE, Operator.GREAT_THAN_EQUAL));
		clauseMap.put(notEmpty("startDateTo"), newExpression().in("where", false).output("s.START_DATE", DataType.DATE, Operator.LESS_THAN_EQUAL));
		// 结束时间
		clauseMap.put(notEmpty("endDateFrom"), newExpression().in("where", false).output("s.END_DATE", DataType.DATE, Operator.GREAT_THAN_EQUAL));
		clauseMap.put(notEmpty("endDateTo"), newExpression().in("where", false).output("s.END_DATE", DataType.DATE, Operator.LESS_THAN_EQUAL));
		// 用户权限
		clauseMap.put(equalsValue("userLevel", "REGION"), newValue().from("userInfo").in("where")
				.output("(s.OWNER_LEVEL='REGION' or s.OWNER_LEVEL='CINEMA') and s.OWNER_REGION={2}", DataType.STRING, false));
		clauseMap.put(equalsValue("userLevel", "CINEMA"), newValue().from("userInfo").in("where")
				.output("s.OWNER_LEVEL='CINEMA' and s.OWNER_CINEMA={1}", DataType.LONG, false));
		// 状态
		clauseMap.put(notEmpty("status"), newExpression().in("where", false).output("s.STATUS", DataType.STRING, Operator.EQUAL));
		// 排序
		clauseMap.put(notEmpty("orderby"), newValue().in("orderby").output("s.{0} {1}", DataType.SQL, true));
		// count语句
		CriteriaParser countParser = newParser(SELECT_PARAGRAPHS)
				.add(newPlain().in("select").output("count(s.CAMPAIGN_ID)"))
				.add(newPlain().in("where").output("s.ISDELETE='0'"))
				.add(mainTable)
				.add(clauseMap);
		CriteriaResult countResult = countParser.parse(criteria);

		// 查询语句
		CriteriaParser listParser = newParser(SELECT_PARAGRAPHS)
			.add(newPlain().in("select").output("s.CAMPAIGN_ID,s.CODE,s.NAME,to_char(s.START_DATE,'yyyy-MM-dd') as START_DATE,to_char(s.END_DATE,'yyyy-MM-dd') as END_DATE,s.STATUS,s.CINEMA_RANGE,s.CREATE_BY,s.CREATE_DATE"))
			.add(newPlain().in("where").output("s.ISDELETE='0'"))
			.add(mainTable)
			.add(clauseMap);
		CriteriaResult listResult = listParser.parse(criteria);
		
		logSql(countResult.getComposedText(), listResult.getComposedText(), listResult.getParameters());
		
		Long rowCount = getJdbcTemplate().queryForLong(countResult.getComposedText(), countResult.getParameters().toArray());
		List<Map<String, Object>> listQueryResult = getJdbcTemplate()
				.query(queryParam.getStartIndex(), queryParam.getFetchSize(), listResult.getComposedText(), listResult.getParameters().toArray(), new ColumnMapRowMapper());

		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public QueryResultVo<Map<String, Object>> queryReportList(QueryParamVo queryParam, List<ExpressionCriterion> criteria, UserProfile userinfo) {
		
		criteria.add(new SingleExpCriterion("userLevel", userinfo.getLevel().name()));
		criteria.add(new ArrayExpCriterion("userInfo",
				new String[] {userinfo.getId(), Long.toString(userinfo.getCinemaId()), userinfo.getRegionCode()}));
		
		criteria.add(new ArrayExpCriterion("orderby", null, null, null, 
				new String[]{queryParam.getSortName(), queryParam.getSortOrder()}));
		
		Clause mainTable = newPlain().in("from").output("T_CAMPAIGN_BASE s");
		
		Clause detailTable = newPlain().in("from").output("T_CAMPAIGN_DETAIL d")
				.depends(newPlain().in("where").output("d.CAMPAIGN_ID=s.CAMPAIGN_ID"));

		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		// 编码
		clauseMap.put(notEmpty("code"), newExpression().in("where").output("s.CODE", DataType.STRING, Operator.EQUAL));
		// 名称
		clauseMap.put(notEmpty("name"), newExpression().in("where").output("s.NAME", DataType.STRING, Operator.LIKE));
		// 开始时间
		clauseMap.put(notEmpty("startDateFrom"), newExpression().in("where", false).output("s.START_DATE", DataType.DATE, Operator.GREAT_THAN_EQUAL));
		clauseMap.put(notEmpty("startDateTo"), newExpression().in("where", false).output("s.START_DATE", DataType.DATE, Operator.LESS_THAN_EQUAL));
		// 结束时间
		clauseMap.put(notEmpty("endDateFrom"), newExpression().in("where", false).output("s.END_DATE", DataType.DATE, Operator.GREAT_THAN_EQUAL));
		clauseMap.put(notEmpty("endDateTo"), newExpression().in("where", false).output("s.END_DATE", DataType.DATE, Operator.LESS_THAN_EQUAL));
		// 用户权限
		clauseMap.put(equalsValue("userLevel", "REGION"), newValue().from("userInfo").in("where")
				.output("(s.OWNER_LEVEL='REGION' or s.OWNER_LEVEL='CINEMA') and s.OWNER_REGION={2}", DataType.STRING, false));
		clauseMap.put(equalsValue("userLevel", "CINEMA"), newValue().from("userInfo").in("where")
				.output("s.OWNER_LEVEL='CINEMA' and s.OWNER_CINEMA={1}", DataType.LONG, false));
		// 状态
		clauseMap.put(notEmpty("status"), newExpression().in("where").output("s.STATUS", DataType.STRING, Operator.EQUAL));
		// 类型
		clauseMap.put(notEmpty("type"), newExpression().in("where").output("d.TYPE", DataType.STRING, Operator.EQUAL));
		// 类型
		clauseMap.put(notEmpty("cid"), newExpression().in("where").output("s.CAMPAIGN_ID", DataType.STRING, Operator.EQUAL));
		// 排序
		clauseMap.put(notEmpty("orderby"), newValue().in("orderby").output("{0} {1}", DataType.SQL, true));
		// count语句
		CriteriaParser countParser = newParser(SELECT_PARAGRAPHS)
				.add(newPlain().in("select").output("count(s.CAMPAIGN_ID)"))
				.add(mainTable)
				.add(detailTable)
				.add(newPlain().in("where").output("s.ISDELETE='0'"))
				.add(clauseMap);
		CriteriaResult countResult = countParser.parse(criteria);
		
		// 查询语句
		CriteriaParser listParser = newParser(SELECT_PARAGRAPHS)
			.add(newPlain().in("select").output("s.CAMPAIGN_ID,s.CODE,s.NAME,to_char(s.START_DATE,'yyyy-mm-dd') as START_DATE,to_char(s.END_DATE,'yyyy-mm-dd') as END_DATE,s.STATUS,s.CINEMA_RANGE,s.CAL_COUNT,s.CONTROL_COUNT,s.CREATE_BY,"
					+ "d.YMD,d.RECOMMEND_NUM,d.RECOMMEND_RATE,d.CONTROL_NUM,d.CONTROL_RATE,d.SUM_NUM,d.SUM_RATE,d.RECOMMEND_INCOME,d.CONTROL_INCOME,d.SUM_INCOME,d.TYPE"))
			.add(mainTable)
			.add(detailTable)
			.add(newPlain().in("where").output("s.ISDELETE='0'"))
			.add(clauseMap);
		CriteriaResult listResult = listParser.parse(criteria);
		
		logSql(countResult.getComposedText(), listResult.getComposedText(), listResult.getParameters());
		Long rowCount = getJdbcTemplate().queryForLong(countResult.getComposedText(), countResult.getParameters().toArray());
		List<Map<String, Object>> listQueryResult = getJdbcTemplate()
				.query(queryParam.getStartIndex(), queryParam.getFetchSize(), listResult.getComposedText(), listResult.getParameters().toArray(), new ColumnMapRowMapper());

		return new QueryResultVo<Map<String, Object>>(rowCount, listQueryResult);
	}
	
	public void insert(CampaignCriteriaVo c) {
		String sql = "insert into T_CAMPAIGN_BASE(campaign_id,code,name,cinema_range,description,start_date,end_date,create_date,create_by,update_date,update_by,owner_level,owner_region,owner_ciname,criteria_scheme,segment_id) values(S_T_CAMPAIGN_BASE.NEXTVAL,?,?,?,?,to_date(?,'yyyy-MM-dd'),to_date(?,'yyyy-MM-dd'),?,?,?,?,?,?,?,?,?)";
		
		getJdbcTemplate().update(sql, c.getCode(), c.getName(), c.getCinemaRange(), c.getDescription(), c.getStartDate(), c.getEndDate(), c.getCreateDate(), c.getCreateBy(), c.getUpdateDate(),c.getUpdateBy(),c.getOwnerLevel(),c.getOwnerRegion(),c.getOwnerCinema(),c.getCriteriaScheme(),c.getSegmentId());
	}
	
	public void update(CampaignCriteriaVo vo) {
		String sql = "update T_CAMPAIGN_BASE t"+
		" set t.name=?,t.cinema_range=?,t.description=?,t.start_date=to_date(?,'yyyy-MM-dd'),"+
		" t.end_date=to_date(?,'yyyy-MM-dd'),t.update_date=?,t.update_by=?,t.version=t.version+1,"+
		" t.segment_id=?,t.criteria_scheme=?,t.cinema_scheme=?"+
		" where t.campaign_id=?";
		
		getJdbcTemplate().update(sql, new Object[] {vo.getName(),vo.getCinemaRange(),vo.getDescription(),vo.getStartDate(),vo.getEndDate(),vo.getUpdateDate(),vo.getUpdateBy(),vo.getSegmentId(),vo.getCriteriaScheme(),vo.getCinemaScheme(),vo.getCampaignId()});
	}
	
	@Override
	public void updateTime(CampaignCriteriaVo vo) {
		String sql = "update T_CAMPAIGN_BASE t set"+
				" t.end_date=to_date(?,'yyyy-MM-dd'),t.update_date=?,t.update_by=?,t.version=t.version+1"+
				" where t.campaign_id=?";
		
		getJdbcTemplate().update(sql, new Object[] {vo.getEndDate(), vo.getUpdateDate(), vo.getUpdateBy(), vo.getCampaignId()});
	}
	
	public void updateStatus(int campaignId, String status) throws Exception {
		String updateStatus = "update T_CAMPAIGN_BASE t set t.status=?,t.cal_count=?,t.cal_count_time=?,t.control_count=?,t.segment_version=? where t.campaign_id=?";
		String getSegment = "select t.campaign_id,t.start_date,s.segment_id,s.cal_count,s.cal_count_time,s.control_count,s.version as segment_version from T_CAMPAIGN_BASE t, T_SEGMENT s where t.segment_id=s.segment_id and t.campaign_id=?";
		String saveMember = "insert into T_CAMPAIGN_SEGMENT(MEMBER_ID,CAMPAIGN_ID,IS_CONTROL) select s.member_id,{1},IS_CONTROL from T_SEGM_MEMBER s where s.segment_id=?";
		String delMember = "delete from T_CAMPAIGN_SEGMENT t where t.CAMPAIGN_ID=?";
		
		if(CampaignCriteriaVo.STATUS_PREPARE.equals(status)) { //更新为草稿状态
			// 更新状态
			int i = getJdbcTemplate().update(updateStatus, new Object[] {status, null, null, null, null, campaignId});
			if(i != 1) {
				throw new Exception("活动状态更新失败");
			}
			// 删除落地客群
			getJdbcTemplate().update(delMember, new Object[] {campaignId});
		} else if(CampaignCriteriaVo.STATUS_COMMIT.equals(status)){ //更新为提交状态并且客群落地
			CampaignCriteriaVo vo = getJdbcTemplate().queryForObject(getSegment, new Object[] {campaignId}, new EntityRowMapper<CampaignCriteriaVo>(CampaignCriteriaVo.class));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			if(format.parse(vo.getStartDate()).before(format.parse(format.format(new Date())))) {
				throw new Exception("活动开始时间不能小于当前时间！");
			}
			
			if(vo != null && vo.getSegmentId() != null) {
				// 客群落地
				int i = getJdbcTemplate().update(saveMember.replace("{1}", campaignId+""), new Object[] {vo.getSegmentId()});
				if(i < 1) {
					throw new Exception("客群落地数量为0");
				}
				// 更新状态
				i = getJdbcTemplate().update(updateStatus, new Object[] {status,vo.getCalCount(),vo.getCalCountTime(),vo.getControlCount(),vo.getSegmentVersion(), campaignId});
				if(i < 1) {
					throw new Exception("活动状态更新失败");
				}
			}
		} else { //直接更新
			int i = getJdbcTemplate().update("update T_CAMPAIGN_BASE t set t.status=? where t.campaign_id=?", new Object[] {status, campaignId});
			if(i != 1) {
				throw new Exception("活动状态更新失败");
			}
		}
	}
	
	public void delete(int campaignId) {
		getJdbcTemplate().update("update T_CAMPAIGN_BASE t set t.ISDELETE=1 where t.CAMPAIGN_ID=?", campaignId);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public CampaignCriteriaVo queryById(int campaignId) {
		String sql = " select t.campaign_id,t.code,t.name,t.cinema_range,t.status,t.description,to_char(t.start_date,'yyyy-MM-dd') as start_date,to_char(t.end_date,'yyyy-MM-dd') as end_date,t.create_date,t.create_by,t.update_date,t.update_by,t.version,t.segment_id,s.name as segment_name,t.cal_count as campaign_cal_count,t.cal_count_time as campaign_cal_count_time,"+
						" s.cal_count as segment_cal_count,s.cal_count_time as segment_cal_count_time,t.criteria_scheme"+
						" from T_CAMPAIGN_BASE t, T_SEGMENT s"+
						" where t.segment_id=s.segment_id and t.campaign_id=? and t.isdelete=0";
		
		CampaignCriteriaVo vo = getJdbcTemplate().query(sql, new Object[] {campaignId}, new ResultSetExtractor<CampaignCriteriaVo>() {
			
			public CampaignCriteriaVo extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				if(rs != null && rs.next()) {
					CampaignCriteriaVo vo = new CampaignCriteriaVo(rs.getLong("CAMPAIGN_ID"),
									rs.getString("NAME"),rs.getString("CODE"),rs.getString("CINEMA_RANGE"),
									rs.getString("CREATE_BY"), rs.getTimestamp("CREATE_DATE"), rs.getString("UPDATE_BY"),
									rs.getTimestamp("UPDATE_DATE"), rs.getString("START_DATE"),rs.getString("END_DATE"),
									rs.getString("STATUS"), rs.getString("DESCRIPTION"),rs.getLong("SEGMENT_ID"),
									rs.getString("SEGMENT_NAME"));
					String scheme = rs.getString("CRITERIA_SCHEME");
					String[] temp  = scheme.split("%\\$%");
					vo.setCriteriaScheme(temp[0]);
					vo.setCinemaScheme(temp[1]);
					
					if(rs.getString("CAMPAIGN_CAL_COUNT") != null) {
						vo.setCalCount(rs.getString("CAMPAIGN_CAL_COUNT"));
						vo.setCalCountTime(rs.getTimestamp("CAMPAIGN_CAL_COUNT_TIME"));
					} else {
						vo.setCalCount(rs.getString("SEGMENT_CAL_COUNT"));
						vo.setCalCountTime(rs.getTimestamp("SEGMENT_CAL_COUNT_TIME"));
					}
					
					switch(Integer.parseInt(vo.getStatus())) {
					case 10:
						vo.setStatus("草稿");
						break;
					case 20:
						vo.setStatus("提交");
						break;
					case 30:
						vo.setStatus("执行");
						break;
					case 40:
						vo.setStatus("结束");
						break;
					}
					
					return vo;
				}
				
				return null;
			}
		});
		
		return vo;
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public boolean hasSameName(String name, Long selfCriteriaId) {
		String sql = "select count(t.campaign_id) from T_CAMPAIGN_BASE t where t.NAME=? and t.ISDELETE=0";
		List<Object> param = new ArrayList<Object>();
		param.add(name);
		if(selfCriteriaId != null) {
			sql += " and t.campaign_id<>?";
			param.add(selfCriteriaId);
		}
		int exist = getJdbcTemplate().queryForInt(sql, param.toArray());
		if(exist > 0) {
			return true;
		}
		
		return false;
	}
	
	private void logSql(String countSql, String listSql, List<Object> paramemters) {
		System.out.println("CountSQL:" + countSql);
		System.out.println("ListSQL:" + listSql);
		System.out.println("ParametersCount:" + paramemters.size());
		System.out.println(paramemters);
	}
	
}
