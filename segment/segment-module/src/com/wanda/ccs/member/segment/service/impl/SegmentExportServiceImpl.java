package com.wanda.ccs.member.segment.service.impl;

import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.SELECT_PARAGRAPHS;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.newParser;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.notEmpty;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newExpression;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newPlain;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.EntityInsertDef;
import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.google.code.pathlet.vo.QueryParamVo;
import com.google.code.pathlet.vo.QueryResultVo;
import com.wanda.ccs.member.segment.service.FileAttachService;
import com.wanda.ccs.member.segment.service.SegmentExportService;
import com.wanda.ccs.member.segment.vo.FileAttachVo;
import com.wanda.ccs.member.segment.vo.SegmentExportVo;
import com.wanda.ccs.sqlasm.Clause;
import com.wanda.ccs.sqlasm.Condition;
import com.wanda.ccs.sqlasm.CriteriaParser;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.DataType;
import com.wanda.ccs.sqlasm.expression.ArrayExpCriterion;
import com.wanda.ccs.sqlasm.expression.ExpressionCriterion;
import com.wanda.ccs.sqlasm.expression.Operator;
import com.wanda.ccs.sqlasm.expression.ValueClause.ValueMapper;

public class SegmentExportServiceImpl implements SegmentExportService {

	private ExtJdbcTemplate queryTemplate = null;

	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;
	
	@InstanceIn(path = "FileAttachService")
	private FileAttachService fileAttachService;

	private ExtJdbcTemplate getJdbcTemplate() {
		if (queryTemplate == null) {
			queryTemplate = new ExtJdbcTemplate(dataSource);
			queryTemplate.registerInsertEntity(new EntityInsertDef("insert", SegmentExportVo.class, "T_SEGMENT_EXPORT"));
		}

		return this.queryTemplate;
	}
	

	public QueryResultVo<Map<String, Object>> queryList(
			QueryParamVo queryParam, List<ExpressionCriterion> criteria) {
		
		criteria.add(new ArrayExpCriterion("orderby", null, null, null, 
				new String[]{queryParam.getSortName(), queryParam.getSortOrder()}));
		Map<Object, Object> orderMapper = new HashMap<Object, Object>();
		orderMapper.put("EXPORT_TIME", "e.EXPORT_TIME");
		orderMapper.put("NAME", "s.NAME");
		orderMapper.put("CODE", "s.CODE");	
		
		Clause segmentTable = newPlain().in("from").output("T_SEGMENT s")
				.depends(newPlain().in("where").output("e.SEGMENT_ID=s.SEGMENT_ID"));
		
		Clause segmentExportTable = newPlain().in("from").output("T_SEGMENT_EXPORT e");

		Map<Condition, Clause> clauseMap = new LinkedHashMap<Condition, Clause>();

		clauseMap.put(notEmpty("code"), 
				newExpression().in("where").output("s.CODE", DataType.STRING, Operator.EQUAL)
				.depends(segmentTable));
		clauseMap.put(notEmpty("name"), 
				newExpression().in("where").output("s.NAME", DataType.STRING, Operator.LIKE)
				.depends(segmentTable));
		clauseMap.put(notEmpty("createBy"), 
				newExpression().in("where").output("s.CREATE_BY", DataType.STRING, Operator.EQUAL)
				.depends(segmentTable));
		clauseMap.put(notEmpty("exportUserId"), 
				newExpression().in("where").output("e.EXPORT_USER_ID", DataType.STRING, Operator.EQUAL));
		clauseMap.put(notEmpty("exportTimeFrom"), 
				newExpression().in("where").output("e.EXPORT_TIME", DataType.DATE_TIME, Operator.GREAT_THAN_EQUAL)
				.depends(segmentExportTable));
		clauseMap.put(notEmpty("exportTimeTo"), 
				newExpression().in("where").output("e.EXPORT_TIME", DataType.DATE_TIME, Operator.LESS_THAN_EQUAL)
				.depends(segmentExportTable));
		clauseMap.put(notEmpty("orderby"), 
				newValue().in("orderby").output("{0} {1}", DataType.SQL, true).addValueMapper(0, orderMapper));
		
		
		CriteriaParser countParser =  newParser(SELECT_PARAGRAPHS)
			.add(newPlain().in("select").output("count(e.SEGMENT_EXPORT_ID)"))
			.add(segmentExportTable)
			.add(segmentTable)
			.add(clauseMap);
		
		CriteriaResult countResult = countParser.parse(criteria);

		CriteriaParser listParser = newParser(SELECT_PARAGRAPHS)
			.add(newPlain().in("select").output("e.SEGMENT_EXPORT_ID, s.SEGMENT_ID, s.CODE, s.NAME, s.COMBINE_SEGMENT, s.CREATE_BY, s.UPDATE_BY, e.COLUMN_SETTING, e.EXPORT_STATUS, e.EXPORT_TIME "))
			.add(segmentExportTable)
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

	
	private void logSql(String countSql, String listSql,
			List<Object> paramemters) {
		 System.out.println("CountSQL:" + countSql);
		 System.out.println("ListSQL:" + listSql);
		 System.out.println("ParametersCount:" + paramemters.size());
		 System.out.println(paramemters);
	}


	public SegmentExportVo get(Long segmentExportId) {
		SegmentExportVo vo = (SegmentExportVo) getJdbcTemplate().queryForObject(
				"select * from T_SEGMENT_EXPORT where SEGMENT_EXPORT_ID=?",
				new Object[] { segmentExportId },
				new EntityRowMapper<SegmentExportVo>(SegmentExportVo.class));
		return vo;
	}
	
	public List<FileAttachVo> getFiles(Long segmentExportId) {
		return fileAttachService.getFiles(segmentExportId, "T_SEGMENT_EXPORT");
	}

	@Transactional
	public void insert(SegmentExportVo exportVo) {
		// Get total records count
		Long segmentExportId = getJdbcTemplate().queryForLong(
				"select S_T_SEGMENT_EXPORT.NEXTVAL from DUAL");
		exportVo.setSegmentExportId(segmentExportId);

		
		getJdbcTemplate().insertEntity("insert", exportVo);
	}
	
	@Transactional
	public void delete(String[] segmentExportIds) {
		for (String segmentExportIdStr : segmentExportIds) {
			Long segmentExportId = new Long(segmentExportIdStr.trim());
			getJdbcTemplate().update("delete from T_SEGMENT_EXPORT where SEGMENT_EXPORT_ID=?", segmentExportId);
		}
	}

	/**
	 * 得到对应客群和对应用户正在导出的数量
	 * @param segmentId
	 * @param userId
	 * @return
	 */
	public Long getExportingCount(Long segmentId, String userId) {
		Long rowCount = getJdbcTemplate().queryForLong(
				"select count(t.SEGMENT_EXPORT_ID) from T_SEGMENT_EXPORT t where t.EXPORT_STATUS='10' and t.SEGMENT_ID=? and t.EXPORT_USER_ID=?",
				segmentId, userId);
		return rowCount;
	}
	
	public void updateControl(Long segmentId, Long oldControlCount, Long controlCount) throws Exception {
		//Map<String, Object> map = getJdbcTemplate().queryForMap("select t.control_count from T_SEGMENT t  where t.segment_id=?", new Object[] { segmentId });
		//if(map != null && map.size() > 0) {
			//Long cc = Long.parseLong(map.get("CONTROL_COUNT").toString());
		if(controlCount.longValue() == oldControlCount.longValue()) {
			return ;
		} else if(controlCount.longValue() != oldControlCount.longValue()) {
			Object[] param = null;
			Long t = 0L;
			if(oldControlCount.longValue() == -1) {
				t = controlCount;
				param = new Object[] {1, 0 ,segmentId, t+1};
			} else if(controlCount > oldControlCount) {
				t = controlCount - oldControlCount;
				// 添加差值
				param = new Object[] {1 , 0, segmentId, t+1};
			} else if(controlCount < oldControlCount) {
				t = oldControlCount - controlCount;
				// 减去差值
				param = new Object[] {0 , 1, segmentId, t+1};
			}
			
			int index = getJdbcTemplate().update("update T_SEGM_MEMBER t set t.is_control=? where t.is_control=? and t.segment_id=? and rownum<?", param);
			if(index != t) {
				throw new Exception("更新控制组数量与实际数量不符");
			}
			// 更新控制组数量
			getJdbcTemplate().update("update T_SEGMENT t set t.control_count=? where t.segment_id=?", new Object[] {controlCount, segmentId});
		}
	}
	
}
