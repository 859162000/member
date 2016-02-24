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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.EntityInsertDef;
import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.google.code.pathlet.jdbc.EntityUpdateDef;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.google.code.pathlet.util.ValueUtils;
import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.jobhub.client.JobScheduleService;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.AuditJdbcTemplate;
import com.wanda.ccs.member.segment.service.SegmentService;
import com.wanda.ccs.member.segment.vo.CombineSegmentSubVo;
import com.wanda.ccs.member.segment.vo.SegmentVo;
import com.wanda.ccs.member.segment.web.SegmentAction.CombineSegmentDo;
import com.wanda.ccs.sqlasm.Clause;
import com.wanda.ccs.sqlasm.ClauseParagraph;
import com.wanda.ccs.sqlasm.Condition;
import com.wanda.ccs.sqlasm.CriteriaParser;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.DataType;
import com.wanda.ccs.sqlasm.expression.ArrayExpCriterion;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.Operator;
import com.wanda.ccs.sqlasm.expression.SingleExpCriterion;
import com.wanda.ccs.sqlasm.impl.DefaultClauseParagraph;


public class SegmentServiceImpl implements SegmentService {
	
	private ExtJdbcTemplate queryTemplate = null;
	
	private AuditJdbcTemplate auditJdbcTemplate = null;
	
	@InstanceIn(path = "/dataSource")
	public void setDataSource(DataSource dataSource) {
		queryTemplate = new ExtJdbcTemplate(dataSource);
		
		auditJdbcTemplate = new AuditJdbcTemplate(dataSource, "T_SEGMENT");
		auditJdbcTemplate.registerInsertEntity(new EntityInsertDef("insert", SegmentVo.class, "T_SEGMENT", null, new String[] {"status", "occupied"}));
		auditJdbcTemplate.registerUpdateEntity(new EntityUpdateDef(
				"update", SegmentVo.class, "T_SEGMENT",
				new String[] { "segmentId" }, new String[] { "segmentId", 
						"code", "calCount", "calCountTime", "createBy", "createDate", "status", "occupied",
						"ownerLevel", "ownerRegion", "ownerCinema"}));
		auditJdbcTemplate.registerDelete("delete", "DELETE from T_SEGMENT where SEGMENT_ID= ? ");
		auditJdbcTemplate.registerLogicDelete("logicDelete", "update t_segment set ISDELETE='1' where SEGMENT_ID=?");
	}

	
	@InstanceIn(path = "/jobScheduleService")
	JobScheduleService scheduleService;
	
	private ExtJdbcTemplate getJdbcTemplate() {
		return this.queryTemplate;
	}
	
	private AuditJdbcTemplate getAuditTemplate() {
		return this.auditJdbcTemplate;
	}


	public QueryResultVo<Map<String, Object>> queryList (
			QueryParamVo queryParam, List<ExpressionCriterion> criteria, UserProfile userinfo) {
		ClauseParagraph[] groups = {
				new DefaultClauseParagraph("select",  " select ",   " ",  ","),
				new DefaultClauseParagraph("from",    " from ",     " ",  ","),
				new DefaultClauseParagraph("leftjoin","",      "",  " "),
				new DefaultClauseParagraph("where",   " where ",    " ",  " and "),
				new DefaultClauseParagraph("having",  " having ",   " ",  " and "),
				new DefaultClauseParagraph("groupby", " group by ", " ",  ","),
				new DefaultClauseParagraph("orderby", " order by ", " ",  ","),
			};
		criteria.add(new SingleExpCriterion("userLevel", userinfo.getLevel().name()));
		criteria.add(new ArrayExpCriterion("userInfo",
				new String[] {userinfo.getId(), Long.toString(userinfo.getCinemaId()), userinfo.getRegionCode()}));

		criteria.add(new ArrayExpCriterion("orderby", null, null, null, 
				new String[]{queryParam.getSortName(), queryParam.getSortOrder()}));
		
		Clause segmentTable = newPlain().in("from").output("T_SEGMENT s");
		Clause memberSensitiveJion = newPlain().in("leftjoin").output("left join t_member_sensitive m on m.WORD_ID=s.WORD_ID");
		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();
		
		clauseMap.put(notEmpty("code"), newExpression().in("where").output("s.CODE", DataType.STRING, Operator.EQUAL));
		clauseMap.put(notEmpty("name"), newExpression().in("where").output("s.NAME", DataType.STRING, Operator.LIKE));
		clauseMap.put(notEmpty("segment_type"), newExpression().in("where").output("s.COMBINE_SEGMENT", DataType.INTEGER, Operator.NOT_EQUAL));
		clauseMap.put(notEmpty("createBy"), newExpression().in("where").output("s.CREATE_BY", DataType.STRING, Operator.EQUAL));
		clauseMap.put(notEmpty("updateTimeFrom"), newExpression().in("where", false).output("s.UPDATE_DATE", DataType.DATE_TIME,  Operator.GREAT_THAN_EQUAL));
		clauseMap.put(notEmpty("updateTimeTo"), newExpression().in("where", false).output("s.UPDATE_DATE", DataType.DATE_TIME, Operator.LESS_THAN_EQUAL));
		
		clauseMap.put(equalsValue("userLevel", "REGION"), newValue().from("userInfo").in("where")
				.output("(s.OWNER_LEVEL='REGION' or s.OWNER_LEVEL='CINEMA') and s.OWNER_REGION={2}", DataType.STRING, false));
		clauseMap.put(equalsValue("userLevel", "CINEMA"), newValue().from("userInfo").in("where")
				.output("s.OWNER_LEVEL='CINEMA' and s.OWNER_CINEMA={1}", DataType.LONG, false));
		clauseMap.put(notEmpty("orderby"), newValue().in("orderby").output("s.{0} {1}", DataType.SQL, true));

		CriteriaParser countParser = newParser(groups)
			.add(newPlain().in("select").output("count(s.SEGMENT_ID)"))
			.add(newPlain().in("where").output("s.ISDELETE='0'"))
			.add(segmentTable)
			.add(memberSensitiveJion)
			.add(clauseMap);
		
		CriteriaResult countResult = countParser.parse(criteria);
		
		CriteriaParser listParser = newParser(groups)
				.add(newPlain().in("select").output("s.SEGMENT_ID, s.CODE, s.NAME, s.CAL_COUNT, s.CONTROL_COUNT ,s.STATUS, s.COMBINE_SEGMENT, s.CREATE_BY, s.UPDATE_BY, s.UPDATE_DATE, s.ALLOW_MODIFIER,s.WORD_ID,m.WORD_TITLE,m.WORD_CONTENT"))
				.add(newPlain().in("where").output("s.ISDELETE='0'"))
				.add(segmentTable)
				.add(memberSensitiveJion)
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

	private void logSql(String countSql, String listSql,
			List<Object> paramemters) {
		 System.out.println("CountSQL:" + countSql);
		 System.out.println("ListSQL:" + listSql);
		 System.out.println("ParametersCount:" + paramemters.size());
		 System.out.println(paramemters);
	}

	public SegmentVo get(Long segmentId) {
		SegmentVo segment = (SegmentVo) getJdbcTemplate().queryForObject(
				"select * from T_SEGMENT where SEGMENT_ID=?",
				new Object[] { segmentId },
				new EntityRowMapper<SegmentVo>(SegmentVo.class));
		return segment;
	}

	public SegmentVo getByCode(String code) {
		SegmentVo segment = (SegmentVo) getJdbcTemplate().queryForObject(
				"select * from T_SEGMENT where CODE=?", new Object[] { code },
				new EntityRowMapper<SegmentVo>(SegmentVo.class));
		return segment;
	}

	public boolean hasSameName(String segmentName, Long selfSegmentId) {
		Long sameNameCount;

		if (selfSegmentId != null) {
			sameNameCount = getJdbcTemplate()
					.queryForLong(
							"select count(SEGMENT_ID) from T_SEGMENT where NAME=? and SEGMENT_ID<>? and ISDELETE='0'",
							segmentName, selfSegmentId);
		} else {
			sameNameCount = getJdbcTemplate().queryForLong(
					"select count(SEGMENT_ID) from T_SEGMENT where NAME=? and ISDELETE='0'",
					segmentName);
		}

		if (sameNameCount != null && sameNameCount > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasReferenceSegment(Long segmentId) {
		Long actTargetRef = getJdbcTemplate()
				.queryForLong(
						"select count(t.act_target_id) from T_ACT_TARGET t where t.segment_id=?",
						segmentId);

		Long actResultRef = getJdbcTemplate()
				.queryForLong(
						"select count(distinct t.CMN_ACTIVITY_ID) from T_ACT_RESULT t where t.RES_SEGMENT_ID=? or t.ALTER_SEGMENT_ID=?",
						segmentId, segmentId);

		if ((actTargetRef != null && actTargetRef > 0)
				|| (actResultRef != null && actResultRef > 0)) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public void insert(SegmentVo segment) {
		// Get total records count
		Long segmentId = getJdbcTemplate().queryForLong(
				"select S_T_SEGMENT.NEXTVAL from DUAL");
		String code = getSegmentCode();

		// System.out.println("New Code: " + code);
		segment.setSegmentId(segmentId);
		segment.setCode(code);
		segment.setCreateDate(new Timestamp(System.currentTimeMillis()));
		segment.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		getAuditTemplate().insertEntity("insert", segment);
	}

	@Transactional
	public void update(SegmentVo segment) {
		segment.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		getAuditTemplate().updateEntity("update", segment);
		
		// 更新对比组
		getJdbcTemplate().update("update T_SEGMENT t set t.control_count=?, t.control_count_rate=? where t.segment_id=?", new Object[] {segment.getControlCount(), segment.getControlCountRate(), segment.getSegmentId()});
	}

	@Transactional
	public void delete(String[] segmentIds) {
		for (String segmentIdStr : segmentIds) {
			Long segmentId = new Long(segmentIdStr.trim());
			getAuditTemplate().deleteEntity("delete", segmentId);
		}
	}
	
	@Transactional
	public void logicDelete(String[] segmentIds) {
		for (String segmentIdStr : segmentIds) {
			Long segmentId = new Long(segmentIdStr.trim());
			getAuditTemplate().logicDeleteEntity("logicDelete", segmentId);
		}
	}

	@Transactional
	public void updateStartCalCount(Long segmentId) throws Exception {
		String sql = "update T_SEGMENT t set t.STATUS=? where t.SEGMENT_ID=?";
		
		try {
			//设置客群状态为正在计算状态
			int rows = getJdbcTemplate()
				.update(sql, new Object[] {SegmentVo.STATUS_CALCULATING, segmentId});
			if(rows <= 0) {
				throw new EmptyResultDataAccessException("Segment Occupied or not found by conditions: STATUS<>'20' and SEGMENT_ID=" + segmentId, 1);
			}
			
			//启动客群计算job
			startCalCountJob(segmentId);
		} catch (Exception e) {
			getJdbcTemplate().update(sql, new Object[] {SegmentVo.STATUS_FAILED, segmentId});
			throw e;
		}
	}
	
	@Transactional
	public boolean updateResetStatus(Long segmentId) {
		int rows = getJdbcTemplate()
			.update("update T_SEGMENT t set t.STATUS='10'， t.OCCUPIED='0' where t.SEGMENT_ID= ? and (t.STATUS='20' or t.STATUS='40') ",
			new Object[] { segmentId });
		
		return (rows > 0);
	}

	public SegmentVo getCount(Long segmentId) {
		SegmentVo segment = getJdbcTemplate().queryForObject(
				"select t.SEGMENT_ID, t.CAL_COUNT,t.CONTROL_COUNT,t.CONTROL_COUNT_RATE, t.CAL_COUNT_TIME, t.STATUS from T_SEGMENT t where t.SEGMENT_ID= ?",
				new Object[]{segmentId},
				new RowMapper<SegmentVo>() {
					public SegmentVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						SegmentVo segment = new SegmentVo();
						segment.setSegmentId(rs.getLong("SEGMENT_ID"));
						segment.setCalCount(rs.getLong("CAL_COUNT"));
						segment.setCalCountTime(rs.getTimestamp("CAL_COUNT_TIME"));
						segment.setControlCount(rs.getLong("CONTROL_COUNT"));
						segment.setControlCountRate(rs.getInt("CONTROL_COUNT_RATE"));
						segment.setStatus(rs.getString("STATUS"));
						return segment;
					}
				});
		
		return segment;
	}
	
	/**
	 * 启动计算客群数量的后台任务
	 * @param segmentId
	 */
	private void startCalCountJob(Long segmentId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", segmentId.toString());
		scheduleService.scheduleJob("SegmentCalculateJob", "member-jobs", params);
	}
	
	private String getSegmentCode() {
		StringBuffer code = new StringBuffer();
		code.append("SEG").append(
				new SimpleDateFormat("yyyyMMdd").format(new Date(System
						.currentTimeMillis())));
		String sql = "select max(CODE) as MAXCODE from T_SEGMENT s where s.CODE like ?";

		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql,
				code.toString() + "%");

		String maxCode = "";
		if (list != null && !list.isEmpty()) {
			Map<String, Object> map = list.get(0);
			if (map.get("MAXCODE") != null)
				maxCode = map.get("MAXCODE").toString();
		}
		if (ValueUtils.notEmpty(maxCode)) {
			code.append(String.format("%04d",
					Long.valueOf(maxCode.substring(11)) + 1));
		} else {
			code.append("0001");
		}
		return code.toString();
	}

	public List<CombineSegmentSubVo> getSelectedSegment(Long[] segmentIds) {
		StringBuilder sql = new StringBuilder("select * from T_SEGMENT where SEGMENT_ID in (");
		for(int i=0,len=segmentIds.length;i<len;i++) {
			sql.append("?").append(",");
		}
		sql.deleteCharAt(sql.length() - 1).append(")");
		List<CombineSegmentSubVo> list = getJdbcTemplate().query(sql.toString(), segmentIds, new RowMapper<CombineSegmentSubVo>() {

			public CombineSegmentSubVo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				CombineSegmentSubVo subVo = new CombineSegmentSubVo();
				subVo.setSubSegmentId(rs.getLong("SEGMENT_ID"));
				subVo.setName(rs.getString("NAME"));
				subVo.setCode(rs.getString("CODE"));
				subVo.setCalCount(rs.getLong("CAL_COUNT"));
				subVo.setCalCountTime((rs.getTimestamp("CAL_COUNT_TIME")));
				subVo.setControlCount(rs.getLong("CONTROL_COUNT"));
				subVo.setStatus(rs.getString("STATUS"));
				subVo.setSegmentVersion(rs.getLong("VERSION"));
				
				return subVo;
			}
		});
		
		return list;
	}

	public Map<String, Object> getCombineSegments(Long segmentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<CombineSegmentSubVo> sub = null;
		SegmentVo vo = get(segmentId);
		String sql = "select sub.COMBINE_SEGMENT_SUB_ID,sub.SEGMENT_ID,sub.SUB_SEGMENT_ID,sub.SORT_INDEX, sub.SET_RELATION,sub.COUNT_ALTER,sub.CAL_COUNT,sub.CAL_COUNT_TIME,sub.SEGMENT_VERSION,sub.VERSION,s.NAME,s.CODE,s.CAL_COUNT SUB_CAL_COUNT,s.CAL_COUNT_TIME SUB_CAL_COUNT_TIME,s.STATUS from T_COMBINE_SEGMENT_SUB sub, T_SEGMENT s where sub.SUB_SEGMENT_ID = s.SEGMENT_ID and sub.SEGMENT_ID = ? order by sub.SORT_INDEX";
		sub = getJdbcTemplate().query(sql, new Object[] { segmentId }, new ResultSetExtractor<List<CombineSegmentSubVo>>() {
			
			public List<CombineSegmentSubVo> extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				
				List<CombineSegmentSubVo> list = new ArrayList<CombineSegmentSubVo>();
				while (rs.next()) {
					CombineSegmentSubVo combineSegmentSubVo = new CombineSegmentSubVo();
					combineSegmentSubVo.setCombineSegmentSubId(rs.getLong("COMBINE_SEGMENT_SUB_ID"));
					combineSegmentSubVo.setSegmentId(rs.getLong("SEGMENT_ID"));
					combineSegmentSubVo.setSubSegmentId(rs.getLong("SUB_SEGMENT_ID"));
					combineSegmentSubVo.setSortIndex(rs.getInt("SORT_INDEX"));
					combineSegmentSubVo.setSetRelation(rs.getString("SET_RELATION"));
					combineSegmentSubVo.setCountAlter(rs.getLong("COUNT_ALTER"));
					combineSegmentSubVo.setCalCount(rs.getLong("CAL_COUNT"));
					combineSegmentSubVo.setCalCountTime(rs.getTimestamp("CAL_COUNT_TIME"));
					combineSegmentSubVo.setName(rs.getString("NAME"));
					combineSegmentSubVo.setCode(rs.getString("CODE"));
					combineSegmentSubVo.setSegmentVersion(rs.getLong("SEGMENT_VERSION"));
					combineSegmentSubVo.setStatus(rs.getString("STATUS"));
					combineSegmentSubVo.setSegmentCalCount(rs.getInt("SUB_CAL_COUNT"));
					combineSegmentSubVo.setSegmentCalCountTime(rs.getTimestamp("SUB_CAL_COUNT_TIME"));
					combineSegmentSubVo.setVersion(rs.getLong("VERSION"));
					list.add(combineSegmentSubVo);
				}
				
				return list;
			}
			
		});
		
		map.put("vo", vo);
		map.put("sub", sub);
		
		return map;
	}
	
	@Transactional
	public void updateCombineSegment(CombineSegmentDo combineSegmentDo) {
		List<String> sql = new ArrayList<String>();
		
		//更新主客群
		SegmentVo vo = combineSegmentDo.getSegmentVo();
		//此方法更新有添加日志功能
		getAuditTemplate().updateEntity("update", vo);	
		
		// 更新子客群
		List<CombineSegmentSubVo> subList = combineSegmentDo.getCombineSegmentsList();
		CombineSegmentSubVo subVo = null;
		for(int i=0,len=subList.size();i<len;i++) {
			subVo = subList.get(i);
			if("new".equals(subVo.getControlStatus())) {
				StringBuilder segmentsSql = new StringBuilder("insert into T_COMBINE_SEGMENT_SUB(combine_segment_sub_id,segment_id,sub_segment_id,sort_index,set_relation,count_alter,cal_count,cal_count_time,segment_version,create_by,create_date,update_by,update_date,version) values");
				segmentsSql.append("(");
				segmentsSql.append("S_T_COMBINE_SEGMENT_SUB.NEXTVAL").append(",");
				segmentsSql.append(combineSegmentDo.getSegmentId()).append(",");
				segmentsSql.append(subVo.getSubSegmentId()).append(",");
				segmentsSql.append(subVo.getSortIndex()).append(",");
				segmentsSql.append("'").append(subVo.getSetRelation()).append("',");
				segmentsSql.append(subVo.getCountAlter()).append(",");
				segmentsSql.append(subVo.getCalCount()).append(",");
				if(vo.getCalCountTime() == null) {
					segmentsSql.append(vo.getCalCountTime()).append(",");
				} else {
					segmentsSql.append("to_date('").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(subVo.getCalCountTime())).append("','yyyy-mm-dd HH24:MI:SS'),");
				}
				segmentsSql.append("'").append(subVo.getVersion()).append("',");
				segmentsSql.append("'").append(subVo.getCreateBy()).append("',");
				segmentsSql.append("to_date('").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(subVo.getCreateDate())).append("','yyyy-mm-dd HH24:MI:SS'),");
				segmentsSql.append("'").append(subVo.getUpdateBy()).append("',");
				segmentsSql.append("to_date('").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(subVo.getUpdateDate())).append("','yyyy-mm-dd HH24:MI:SS'),");
				segmentsSql.append("0)");
				sql.add(segmentsSql.toString());
			} else if("edit".equals(subVo.getControlStatus())) {
				StringBuilder segmentsSql = new StringBuilder("update T_COMBINE_SEGMENT_SUB s set ");
				segmentsSql.append("s.sort_index=").append(subVo.getSortIndex()).append(",");
				segmentsSql.append("s.set_relation='").append(subVo.getSetRelation()).append("',");
				segmentsSql.append("s.count_alter=").append(subVo.getCountAlter()).append(",");
				segmentsSql.append("s.version=").append(subVo.getVersion());
				segmentsSql.append(" where s.combine_segment_sub_id=").append(subVo.getCombineSegmentSubId());
				sql.add(segmentsSql.toString());
			} else if("delete".equals(subVo.getControlStatus())) {
				sql.add("DELETE FROM T_COMBINE_SEGMENT_SUB S WHERE S.COMBINE_SEGMENT_SUB_ID="+subVo.getCombineSegmentSubId());
			}
			
			subVo = null;
		}
		/*for(String s : sql) {
			System.out.println("update->"+s);
		}*/
		
		getJdbcTemplate().batchUpdate(sql.toArray(new String[] {}));
		
	}
	
	@Transactional
	public void insertCombineSegment(SegmentVo segment, List<CombineSegmentSubVo> sub) {
		Long segmentId = getJdbcTemplate().queryForLong(
				"select S_T_SEGMENT.NEXTVAL from DUAL");
		String code = getSegmentCode();
		segment.setSegmentId(segmentId);
		segment.setCode(code);
		
		// 保存客群
		getAuditTemplate().insertEntity("insert", segment);
		
		// 保存子客群
		/*String insertSql = "insert into T_COMBINE_SEGMENT_SUB(combine_segment_sub_id,segment_id,sub_segment_id,sort_index,set_relation,count_alter,cal_count,cal_count_time,segment_version,create_by,create_date,update_by,update_date,version) values";
		StringBuilder segmentsSql = new StringBuilder("");
		CombineSegmentSubVo vo = null;
		String[] sql = new String[sub.size()];
		for(int i=0,len=sub.size();i<len;i++) {
			vo = sub.get(i);
			segmentsSql.append("(");
			segmentsSql.append("S_T_COMBINE_SEGMENT_SUB.NEXTVAL").append(",");
			segmentsSql.append(segmentId).append(",");
			segmentsSql.append(vo.getSubSegmentId()).append(",");
			segmentsSql.append(vo.getSortIndex()).append(",");
			segmentsSql.append("'").append(vo.getSetRelation()).append("',");
			segmentsSql.append(vo.getCountAlter()).append(",");
			segmentsSql.append(vo.getCalCount()).append(",");
			if(vo.getCalCountTime() == null) {
				segmentsSql.append(vo.getCalCountTime()).append(",");
			} else {
				segmentsSql.append("to_date('").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vo.getCalCountTime())).append("','yyyy-MM-dd HH24:MI:SS'),");
			}
			segmentsSql.append("'").append(vo.getVersion()).append("',");
			segmentsSql.append("'").append(vo.getCreateBy()).append("',");
			segmentsSql.append("to_date('").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vo.getCreateDate())).append("','yyyy-MM-dd HH24:MI:SS'),");
			segmentsSql.append("'").append(vo.getUpdateBy()).append("',");
			segmentsSql.append("to_date('").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(vo.getUpdateDate())).append("','yyyy-MM-dd HH24:MI:SS'),");
			segmentsSql.append("0)");
			System.out.println(insertSql+segmentsSql.toString());
			sql[i] =insertSql+segmentsSql.toString();
			segmentsSql = new StringBuilder("");
			vo = null;
		}
		//System.out.println(segmentsSql.toString());
		getJdbcTemplate().batchUpdate(sql);*/
		
		// 保存子客群
		String insertSql = "insert into T_COMBINE_SEGMENT_SUB(combine_segment_sub_id,segment_id,sub_segment_id,sort_index,set_relation,count_alter,cal_count,cal_count_time,segment_version,create_by,create_date,update_by,update_date,version) values(S_T_COMBINE_SEGMENT_SUB.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> param = new ArrayList<Object[]>();
		Object[] objs = null;
		CombineSegmentSubVo vo = null;
		for(int i=0,len=sub.size();i<len;i++) {
			vo = sub.get(i);
			objs = new Object[] {segmentId, vo.getSubSegmentId(), vo.getSortIndex(), vo.getSetRelation(), vo.getCountAlter(),
					vo.getCalCount(), vo.getCalCountTime(), vo.getSegmentVersion(), vo.getCreateBy(), vo.getCreateDate(), vo.getUpdateBy(),
					vo.getUpdateDate(), vo.getVersion()};
			param.add(objs);
			vo = null;
		}
		getJdbcTemplate().batchUpdate(insertSql, param);
		
	}
	
	public List<Map<String, Object>> getCountAlter(Long segmentId) {
		String sql = "select T.SEGMENT_ID,T.CODE,T.CAL_COUNT,T.CAL_COUNT_TIME,T.CONTROL_COUNT,T.STATUS,S.SUB_SEGMENT_ID,S.COUNT_ALTER,S.SET_RELATION,TT.CAL_COUNT as CAL_COUNT_SUB,TT.CAL_COUNT_TIME as CAL_COUNT_TIME_SUB,TT.STATUS as STATUS_SUB from T_SEGMENT T, T_COMBINE_SEGMENT_SUB S, T_SEGMENT TT WHERE T.SEGMENT_ID=S.SEGMENT_ID AND S.SUB_SEGMENT_ID=TT.SEGMENT_ID AND T.SEGMENT_ID=?";
		List<Map<String, Object>> list = getJdbcTemplate().query(sql, new Object[] {segmentId}, new RowMapper<Map<String, Object>>() {
			
			public Map<String, Object> mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				// 主客群
				map.put("SEGMENT_ID", rs.getLong("SEGMENT_ID"));
				map.put("CODE", rs.getString("CODE"));
				map.put("CAL_COUNT", rs.getInt("CAL_COUNT"));
				map.put("CAL_COUNT_TIME", rs.getString("CAL_COUNT_TIME"));
				map.put("CONTROL_COUNT", rs.getString("CONTROL_COUNT"));
				map.put("STATUS", rs.getString("STATUS"));
				// 子客群
				map.put("SUB_SEGMENT_ID", rs.getLong("SUB_SEGMENT_ID"));
				map.put("COUNT_ALTER", rs.getLong("COUNT_ALTER"));
				map.put("SET_RELATION", rs.getString("SET_RELATION"));
				// 子客群详细信息
				map.put("CAL_COUNT_SUB", rs.getInt("CAL_COUNT_SUB"));
				map.put("CAL_COUNT_TIME_SUB", rs.getString("CAL_COUNT_TIME_SUB"));
				map.put("STATUS_SUB", rs.getString("STATUS_SUB"));
				
				return map;
			}
			
		});
		
		return list;
	}

	@Override
	public String segmentOccupied(Long segmentId) {
		SegmentVo vo = get(segmentId);
		
		return vo.getOccupied();
	}
	
}
