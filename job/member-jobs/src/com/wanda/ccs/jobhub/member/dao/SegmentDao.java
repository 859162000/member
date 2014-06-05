/**
 * 
 */
package com.wanda.ccs.jobhub.member.dao;

import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.newParser;
import static com.wanda.ccs.sqlasm.CriteriaParserBuilder.notEmpty;
import static com.wanda.ccs.sqlasm.expression.ExpressionClauseBuilder.newPlain;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.member.DataAccessException;
import com.wanda.ccs.jobhub.member.SegmentCalCountException;
import com.wanda.ccs.jobhub.member.SegmentExcelExporter;
import com.wanda.ccs.jobhub.member.SegmentExcelExporter.ColumnDef;
import com.wanda.ccs.jobhub.member.SystemException;
import com.wanda.ccs.jobhub.member.vo.CombineSegmentSubVo;
import com.wanda.ccs.jobhub.member.vo.SegmentExportVo;
import com.wanda.ccs.jobhub.member.vo.SegmentVo;
import com.wanda.ccs.member.segment.service.CriteriaQueryResult;
import com.wanda.ccs.member.segment.service.impl.CriteriaQueryServiceImpl;
import com.wanda.ccs.sqlasm.Clause;
import com.wanda.ccs.sqlasm.ClauseParagraph;
import com.wanda.ccs.sqlasm.Condition;
import com.wanda.ccs.sqlasm.CriteriaParser;
import com.wanda.ccs.sqlasm.CriteriaResult;
import com.wanda.ccs.sqlasm.Criterion;
import com.wanda.ccs.sqlasm.DataType;
import com.wanda.ccs.sqlasm.expression.SingleExpCriterion;
import com.wanda.ccs.sqlasm.impl.DefaultClauseParagraph;

/**
 * @author YangJianbin
 *
 */
public class SegmentDao {
	
	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;
	
	@InstanceIn(path = "/dataSourceOds")
	private DataSource dataSourceOds;
	
	@InstanceIn(path = "/transactionManager")
	private DataSourceTransactionManager transactionManager;
	
	private ExtJdbcTemplate extJdbcTemplate = null;
	
	private ExtJdbcTemplate extJdbcTemplateDw = null;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	private final static int FETCH_SIZE = 200;
	private final static int BATCH_FETCH_SIZE = 1000;
	
	private final static String FILE_REF_OBJECT_TYPE = "T_SEGMENT_EXPORT";
	
	private final static String XLSX_CONTENT_TYPE = "application/vnd.ms-excel.12";
	
	public ExtJdbcTemplate getJdbcTemplate()  {
		if(this.extJdbcTemplate == null) {
			this.extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.extJdbcTemplate;
	}
	
	public ExtJdbcTemplate getJdbcTemplateDw()  {
		if(this.extJdbcTemplateDw == null) {
			this.extJdbcTemplateDw = new ExtJdbcTemplate(dataSourceOds);
		}
		return this.extJdbcTemplateDw;
	}
	
	public boolean lockSegment(Long segmentId, String occupiedBy) {
		int rows = getJdbcTemplate().update("update T_SEGMENT t set t.OCCUPIED=? where t.SEGMENT_ID=? and t.OCCUPIED=?", 
				new Object[] {occupiedBy, segmentId, SegmentVo.OCCUPIED_NONE});

		return (rows==1); //更新一条记录且仅一条，表示锁定成功
	}
	
	public boolean unlockSegment(Long segmentId, String occupiedBy) {
		int rows = getJdbcTemplate().update("update T_SEGMENT t set t.OCCUPIED=? where t.SEGMENT_ID=? and t.OCCUPIED=?", 
				new Object[] {SegmentVo.OCCUPIED_NONE, segmentId, occupiedBy});
		return (rows==1); //更新一条记录且仅一条，表示解锁成功
	}
	
	
	/**
	 * 通过客群ID得到客群查询对象,只有客群数量等于-1的客群才返回查询对象
	 * @param segmentId
	 * @return 查询对象其中包含查询的SQL和参数
	 * @throws DataAccessException
	 */
	public CriteriaQueryResult getCriteriaResult(Long segmentId) throws DataAccessException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT s.SEGMENT_ID, s.CRITERIA_SCHEME, s.SORT_NAME, s.SORT_ORDER, s.CAL_COUNT");
		sql.append(" FROM T_SEGMENT s ");
		sql.append(" WHERE s.SEGMENT_ID=?"); 

		Map<String, Object> resultMap = getJdbcTemplate().queryForMap(sql.toString(), new Object[]{segmentId});

		CriteriaQueryServiceImpl segmentCriteria = new CriteriaQueryServiceImpl();
		return segmentCriteria.getSegmentQuery(
				resultMap.get("CRITERIA_SCHEME").toString(), 
				resultMap.get("SORT_NAME").toString(), 
				resultMap.get("SORT_ORDER").toString());
	}
	
	/**
	 * 在数据仓库中计算客群数量
	 * @return
	 */
	public Long getCountInDw(CriteriaQueryResult criteriaResult, int timeout) {
		StringBuilder bsql=new StringBuilder();
		bsql.append("select count(*) from (SELECT distinct member.MEMBER_KEY ");
		bsql.append(criteriaResult.getComposedText() + ")");
		
		getJdbcTemplateDw().setQueryTimeout(timeout);
		
		//TODO fot testing, output the query SQL with parameters. 
		//StringBuilder tsql=new StringBuilder();
		//tsql.append("select count(*) from (SELECT distinct member.MEMBER_KEY ");
		//tsql.append(criteriaResult.getParameterizeText() + ")");
		//System.out.println("~~~~~~~~~~~~~~~~" + tsql);
				
		return getJdbcTemplateDw().queryForLong(bsql.toString(), criteriaResult.getParameters().toArray());
	}
	
	public SegmentExportVo getSegmentExport(Long segmentExportId) {
		SegmentExportVo vo = (SegmentExportVo) getJdbcTemplate().queryForObject(
				"select * from T_SEGMENT_EXPORT where SEGMENT_EXPORT_ID=?",
				new Object[] { segmentExportId },
				new EntityRowMapper<SegmentExportVo>(SegmentExportVo.class));
		return vo;
	}
	
	
	public SegmentVo getSegment(Long segmentId) {
		SegmentVo segment = (SegmentVo) getJdbcTemplate().queryForObject(
				"select * from T_SEGMENT where SEGMENT_ID=?",
				new Object[] { segmentId },
				new EntityRowMapper<SegmentVo>(SegmentVo.class));
		return segment;
	}
	
	public void insertExportFile(SegmentExportVo exportVo, String fileName, final SegmentExcelExporter exporter) {
		
		final Long fileAttachId = getJdbcTemplate().queryForLong("select S_T_FILE_ATTACH.NEXTVAL from DUAL");

		getJdbcTemplate().update("insert into T_FILE_ATTACH(" +
				"FILE_ATTACH_ID, REF_OBJECT_ID, REF_OBJECT_TYPE, FILE_NAME, FILE_DATA, FILE_SIZE, CONTENT_TYPE)" +
				"values(?, ?, ?, ?, empty_blob(), ?, ?)", 
				fileAttachId, exportVo.getSegmentExportId(), FILE_REF_OBJECT_TYPE, fileName, 0, XLSX_CONTENT_TYPE);
		
		final Blob[] blob = new Blob[1];
		getJdbcTemplate().query(
				"Select FILE_DATA from T_FILE_ATTACH where FILE_ATTACH_ID=?",
				new Object[] { fileAttachId }, new RowCallbackHandler() {

					public void processRow(ResultSet rs) throws SQLException {
						blob[0] = rs.getBlob("FILE_DATA");
						OutputStream out = null;
						try {
							out = blob[0].setBinaryStream(0L);
							exporter.write(out);
						} 
						catch (IOException ioe) {
							throw new SQLException(ioe);
						} 
						finally {
							if (out != null) {
								try { out.close(); } 
								catch (IOException ioe2) { throw new SQLException(ioe2); }
							}
							exporter.close();
						}
					}
				});

		getJdbcTemplate().update("update T_FILE_ATTACH set FILE_DATA=? where FILE_ATTACH_ID=?", 
			    new PreparedStatementSetter() {
					public void setValues(PreparedStatement pstmt)
							throws SQLException {
						pstmt.setBlob(1, blob[0]);
						pstmt.setLong(2, fileAttachId);
					}
			    });

		
	}
	
	public void updateExport(Long segmentExportId, String exportStatus, Integer rowCount) {
		getJdbcTemplate().update("update T_SEGMENT_EXPORT set EXPORT_STATUS=?, ROW_COUNT=? where SEGMENT_EXPORT_ID=?", 
				exportStatus, rowCount, segmentExportId);
	}
	
	public SegmentExcelExporter export(SegmentVo segmentVo, Long segmentId, final SegmentExportVo exportVo, final Long maxCount, int timeout)  {
		
		List<Map<String, String>> exportColumns = null;
		try {
			exportColumns = jsonMapper.readValue(exportVo.getColumnSetting(),  new TypeReference<List<Map<String, String>>>() {});
		} catch (Exception e) {
			throw new SystemException(e);
		}

		final LinkedHashMap<String, ColumnDef> exportColumnDefs = new LinkedHashMap<String, ColumnDef>();

		ClauseParagraph[] groups = {
				new DefaultClauseParagraph("select",  " select ",   " ",  ","),
				new DefaultClauseParagraph("from",    " from ",     " ",  ","),
				new DefaultClauseParagraph("join","",      "",  " "),
				new DefaultClauseParagraph("where",   " where ",    " ",  " and "),
				new DefaultClauseParagraph("having",  " having ",   " ",  " and "),
				new DefaultClauseParagraph("groupby", " group by ", " ",  ","),
				new DefaultClauseParagraph("orderby", " order by ", " ",  ","),
			};
		
		Clause memberInfoJoin = newPlain().in("join").output("inner join T_MEMBER_INFO i on i.MEMBER_ID=m.MEMBER_ID");
		Clause regCinemaJoin = newPlain().in("join").output("left join T_CINEMA regCinema on regCinema.SEQID = m.REGIST_CINEMA_ID"); 
		Clause mgrCinemaJoin = newPlain().in("join").output("left join T_CINEMA mgrCinema on mgrCinema.SEQID = i.MANAGE_CINEMA_ID").depends(memberInfoJoin);
		Clause regAreaDimJoin = newPlain().in("join").output("left join T_DIMDEF areaDim on regCinema.AREA =areaDim.CODE and areaDim.TYPEID=104").depends(regCinemaJoin); 
		Clause generDimJoin = newPlain().in("join").output("left join T_DIMDEF generDim on m.GENDER=generDim.CODE and generDim.TYPEID=2008"); 
		Clause regTypeDimJoin = newPlain().in("join").output("left join T_DIMDEF regTypeDim on m.REGIST_TYPE=regTypeDim.CODE and regTypeDim.TYPEID=201"); 
		Clause regChnidDimJoin = newPlain().in("join").output("left join T_DIMDEF regChnidDim on m.REGIST_CHNID=regChnidDim.CODE and regChnidDim.TYPEID=216"); 
		
		Map<Condition, Clause> exportClauseMap = new LinkedHashMap<Condition, Clause>();
		exportClauseMap.put(notEmpty("MEMBER_ID"), newPlain().in("select").output("m.MEMBER_ID")); //会员ID
		exportColumnDefs.put("MEMBER_ID", new ColumnDef(2500, DataType.LONG));
		
		exportClauseMap.put(notEmpty("MEMBER_NO"), newPlain().in("select").output("m.MEMBER_NO")); //会员序列号
		exportColumnDefs.put("MEMBER_NO", new ColumnDef(6000, DataType.STRING));
		
		exportClauseMap.put(notEmpty("MOBILE"), newPlain().in("select").output("m.MOBILE")); //手机号
		exportColumnDefs.put("MOBILE", new ColumnDef(3500, DataType.STRING));
		
		exportClauseMap.put(notEmpty("NAME"), newPlain().in("select").output("m.NAME")); //姓名
		exportColumnDefs.put("NAME", new ColumnDef(2500, DataType.STRING));
		
		exportClauseMap.put(notEmpty("GENDER"), newPlain().in("select").output("generDim.NAME as GENDER").depends(generDimJoin)); //性别  dim
		exportColumnDefs.put("GENDER", new ColumnDef(1800, DataType.STRING));
		
		exportClauseMap.put(notEmpty("BIRTHDAY"), newPlain().in("select").output("m.BIRTHDAY")); //生日 
		exportColumnDefs.put("BIRTHDAY", new ColumnDef(3300, DataType.DATE));
		
		exportClauseMap.put(notEmpty("REGIST_TYPE"), newPlain().in("select").output("regTypeDim.NAME as REGIST_TYPE").depends(regTypeDimJoin)); //注册类型 
		exportColumnDefs.put("REGIST_TYPE", new ColumnDef(3000, DataType.STRING));
		
		exportClauseMap.put(notEmpty("REGIST_CHNID"), newPlain().in("select").output("regChnidDim.NAME as REGIST_CHNID").depends(regChnidDimJoin)); //招募渠道
		exportColumnDefs.put("REGIST_CHNID", new ColumnDef(3000, DataType.STRING));
		
		
		exportClauseMap.put(notEmpty("REGIST_CINEMA_NAME"), newPlain().in("select").output("regCinema.INNER_NAME as REGIST_CINEMA_NAME").depends(regCinemaJoin)); //注册影城
		exportColumnDefs.put("REGIST_CINEMA_NAME", new ColumnDef(8000, DataType.STRING));
		
		exportClauseMap.put(notEmpty("MANAGE_CINEMA_NAME"), newPlain().in("select").output("mgrCinema.INNER_NAME as MANAGE_CINEMA_NAME").depends(mgrCinemaJoin)); //管理影城 
		exportColumnDefs.put("MANAGE_CINEMA_NAME", new ColumnDef(8000, DataType.STRING));
		
		exportClauseMap.put(notEmpty("REGIST_AREA"), newPlain().in("select").output("areaDim.NAME as REGIST_AREA").depends(regAreaDimJoin)); //注册所属区域
		exportColumnDefs.put("REGIST_AREA", new ColumnDef(3500, DataType.STRING));
		
		exportClauseMap.put(notEmpty("REGIST_DATE"), newPlain().in("select").output("m.REGIST_DATE as REGIST_DATE")); //注册时间
		exportColumnDefs.put("REGIST_DATE", new ColumnDef(3300, DataType.DATE));
		
		
		//设置ColumnDef中的name 和 label属性
		for(Map<String, String> expColumn : exportColumns) {
			String name = expColumn.get("name");
			String label = expColumn.get("label");
			ColumnDef def = exportColumnDefs.get(name);
			def.setName(name);
			def.setLabel(label);
		}

		
		List<Criterion> criteria = new ArrayList<Criterion>();
		for(Map<String, String> expColumn : exportColumns) {
			String name = expColumn.get("name");
			String label = expColumn.get("label");
			criteria.add(new SingleExpCriterion(name, label, null, null, label));
		}
		
		CriteriaParser exportParser = newParser(groups).add(newPlain().in("from").output("T_MEMBER m")).add(exportClauseMap);
		
		final CriteriaResult exportCriteriaResult = exportParser.parse(criteria);
		
		StringBuilder segmentSql = new StringBuilder();
		
		List<Object> args = new ArrayList<Object>();
		//System.out.println("segmentVo.getCombineSegment()" + segmentVo.getCombineSegment());
		if(segmentVo.getCombineSegment() == false) {
			segmentSql.append("SELECT  s.MEMBER_ID from T_SEGM_MEMBER s where s.SEGMENT_ID = ?");
			args.add(segmentId);
			//System.out.println("~~~~~~~~~~~" + segmentSql);
		}
		else if(segmentVo.getCombineSegment() == true){
			List<CombineSegmentSubVo> list = getCombineSegment(segmentId);
			if(list != null && !list.isEmpty()){
				CombineSegmentSubVo segmentSubVo = list.get(0);
				args.add(segmentSubVo.getSubSegmentId());
				segmentSql.append(" (select MEMBER_ID from T_SEGM_MEMBER where segment_id = ?) ");
				for(int i = 1; i< list.size(); i++){
					CombineSegmentSubVo nextSegmentSubVo = list.get(i);
					segmentSql.insert(0, "(").append(nextSegmentSubVo.getSetRelation()).append(" (select MEMBER_ID from T_SEGM_MEMBER where segment_id = ?) ").append(")");
					args.add(nextSegmentSubVo.getSubSegmentId());
				}
			}
			segmentSql.insert(0, "select distinct MEMBER_ID from ( ").append(" )");
		}
		
		//logHelper.info("~~maxCount:" + maxCount);
		//logHelper.info("~~SQL:" + segmentSql.toString());
		//logHelper.info("~~PARAM:" + args);
		
		getJdbcTemplate().setQueryTimeout(timeout);

//		return getJdbcTemplateDw().query(segmentSql.toString(), segmentCriteriaResult.getParameters().toArray(), new ResultSetExtractor<SegmentExcelExporter>() {
		return getJdbcTemplate().query(segmentSql.toString(), args.toArray(), new ResultSetExtractor<SegmentExcelExporter>() {
			public SegmentExcelExporter extractData(ResultSet resultset) throws SQLException,
					org.springframework.dao.DataAccessException {
				int selectedRows = 0;
				int exportedRows = 0;
				resultset.setFetchSize(FETCH_SIZE);
				SegmentExcelExporter exporter = new SegmentExcelExporter(FETCH_SIZE, exportColumnDefs);
				String fetchSizeExportSql = exportCriteriaResult.getComposedText() + " where M.MEMBER_ID in (" + getMemberSqlPlaceHolder(FETCH_SIZE) + ")";
				
				List<Long> memberIds = new ArrayList<Long>(FETCH_SIZE);
				while(resultset.next()) {
					selectedRows++;
					Long memberId = resultset.getLong(1);
					memberIds.add(memberId);
					
					if(memberIds.size() >= FETCH_SIZE) {				
						List<Map<String, Object>> exportRows = getJdbcTemplate().query(fetchSizeExportSql, memberIds.toArray(), new ColumnMapRowMapper());
						exporter.appendRows(exportRows);
						memberIds.clear();
						
						//logHelper.warn("~~Segment Exporting: segmentExportId=" + exportVo.getSegmentExportId() 
						//		+ ", selected rows=" + selectedRows
						//		+ ", export excel rows=" + (exporter.getRowIndex() - 1));
					}
					

					if(selectedRows >= maxCount) {
						//logHelper.info("~~reach the MAX Count, selectedRows:" + selectedRows);
						break;
					}
				}
				
				if(memberIds.size() > 0) {
					String exportSql = exportCriteriaResult.getComposedText() + " where M.MEMBER_ID in (" +getMemberSqlPlaceHolder(memberIds.size()) + ")";					
					List<Map<String, Object>> exportRows = getJdbcTemplate().query(exportSql, memberIds.toArray(), new ColumnMapRowMapper());
					exporter.appendRows(exportRows);
					memberIds.clear();
					
					//logHelper.warn("~~Segment Exporting: segmentExportId=" + exportVo.getSegmentExportId() 
					//		+ ", selected rows=" + selectedRows
					//		+ ", export excel rows=" + (exporter.getRowIndex() - 1));
				}
				
				exportedRows = exporter.getRowIndex() - 1; // exporter.getRowIndex() 为下一个要添加行的索引，同时表中的表头也在其中算为一行。
				if( exportedRows != selectedRows) {
					//logHelper.warn("~~Segment Export Warning: segmentExportId=" + exportVo.getSegmentExportId() + ". selected rows=" + selectedRows + 
					//		", export excel rows=" + exportedRows + " Some member could not be found int T_MEMBER table! ");
				}
					
				return exporter;
			}
			
		});

	}
	
	
	public String getMemberSqlPlaceHolder(int count) {
		StringBuilder buf = new StringBuilder(count * 2 + 1);
		for(int i=0 ; i<count ; i++) {
			if(i != 0) { 
				buf.append(','); 
			}
			buf.append('?');
		}
		return buf.toString();
	}
	
	public void updateCalCount(Long segmentId, Long calCount, String status) throws DataAccessException {
		getJdbcTemplate().update("update T_SEGMENT s set s.CAL_COUNT=?,s.CAL_COUNT_TIME =?, s.status =? WHERE s.SEGMENT_ID=?", calCount, new Timestamp(System.currentTimeMillis()),status,segmentId);
	}
	
	/**
	 * 保存落地会员
	 * @param list
	 * @param segmentId
	 * @throws Exception 
	 */
	public void saveSegmMember(final List<Long> list, final Long segmentId) throws Exception{
		if(list == null || list.isEmpty())
			return;
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// explicitly setting the transaction name is something that can only be done programmatically
		def.setName("SomeTxName");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String sql = " insert into T_SEGM_MEMBER(SEGM_MEMBER_ID, SEGMENT_ID, MEMBER_ID) values (S_T_SEGM_MEMBER.NEXTVAL, ?, ?) ";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				public int getBatchSize() {
					return list.size();
				}
				public void setValues(PreparedStatement pst, int i)
						throws SQLException {
					pst.setLong(1, segmentId);
					pst.setLong(2, list.get(i));
				}
			});
		}catch(Exception ex) {
			transactionManager.rollback(status);
			throw ex;
		}
		transactionManager.commit(status);
		
	}
	
	/**
	 * 更新客群计算状态
	 * @param segmentId
	 * @param status
	 * @throws DataAccessException
	 */
	public void updateSegmentStatus(Long segmentId, String segmentStatus) throws SegmentCalCountException {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("SomeTxName");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			getJdbcTemplate().update("update T_SEGMENT s set s.status=?,s.CAL_COUNT_TIME =? WHERE s.SEGMENT_ID=?", segmentStatus, new Timestamp(System.currentTimeMillis()),segmentId);
		}catch(Exception ex) {
			transactionManager.rollback(status);
			throw new SegmentCalCountException(ex);
		}
		transactionManager.commit(status);
	}
	
	public void saveSegmMember(CriteriaQueryResult segmentCriteriaResult, final Long maxCount, int timeout, final Long segmentId ,String oderByName) throws Exception {
		
		StringBuilder segmentSql = new StringBuilder();
		segmentSql.append("SELECT distinct member.MEMBER_KEY " );
		if(segmentCriteriaResult.getOrderBySql() != null){
			segmentSql.append(" , ").append(oderByName);
		}
		segmentSql.append(segmentCriteriaResult.getComposedText());
		
		//拼装order by条件
		if(segmentCriteriaResult.getOrderBySql() != null) {
			segmentSql.append(" ").append(segmentCriteriaResult.getOrderBySql());
		}
		
		//logHelper.info("~~maxCount:" + maxCount);
		//logHelper.info("~~SQL:" + segmentSql.toString());
		//logHelper.info("~~PARAM:" + segmentCriteriaResult.getParameters());
		
		getJdbcTemplateDw().setQueryTimeout(timeout);
		
		getJdbcTemplateDw().query(segmentSql.toString(), segmentCriteriaResult.getParameters().toArray(), new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet resultset) throws SQLException,
					org.springframework.dao.DataAccessException {
				try {
					List<Long> memberIds = new ArrayList<Long>();
					int selectedRows = 0;
					resultset.setFetchSize(BATCH_FETCH_SIZE);
					while(resultset.next()) {
						selectedRows++;
						Long memberId = resultset.getLong(1);
						memberIds.add(memberId);
						if( memberIds.size() >= BATCH_FETCH_SIZE){
							saveSegmMember(memberIds, segmentId);
							memberIds.clear();
							//logHelper.warn("~~SegmMember INSERT: segmentId=" + segmentId 
							//		+ ", selected rows=" + selectedRows );
						}
						if(selectedRows >= maxCount) {
							//logHelper.info("~~reach the MAX Count, selectedRows:" + selectedRows);
							break;
						}
					}
					if(memberIds.size() > 0){
						saveSegmMember(memberIds, segmentId);
						memberIds.clear();
						//logHelper.warn("~~SegmMember INSERT: segmentId=" + segmentId 
						//		+ ", selected rows=" + selectedRows );
						
					}
				} catch (Exception e) {
					//logHelper.error(e.getMessage(), e);
					throw new SQLException(e);
				}
				return null;
			}
		});
	}





	/**
	 * 删除客群中落地的会员
	 * @param segmentId
	 * @throws Exception
	 */
	public void deleteSegmMember(Long segmentId) throws Exception{
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("SomeTxName");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			getJdbcTemplate().update("delete from T_SEGM_MEMBER s where s.SEGMENT_ID=?", segmentId);
		}catch(Exception ex) {
			transactionManager.rollback(status);
			throw ex;
		}
		transactionManager.commit(status);
		
	}
	
	/**
	 * 根据客群id获取子客群集合
	 * @param segmentId 
	 * 			客群id
	 * @return
	 */
	public List<CombineSegmentSubVo> getCombineSegment(Long segmentId){
		String sql = "select * from T_COMBINE_SEGMENT_SUB sub where sub.SEGMENT_ID = ? order by SORT_INDEX";
		//this.logHelper.info("select combineSegment start");
		//this.logHelper.info("select SQL :" + sql);
		//this.logHelper.info("PARAMS :" + segmentId);
		List<CombineSegmentSubVo> list = getJdbcTemplate().query(sql, new Object[]{segmentId}, new EntityRowMapper<CombineSegmentSubVo>(CombineSegmentSubVo.class));
		//this.logHelper.info("select combineSegment end");
		return list;
	}
	
	/**
	 * 根据子客群id获取子客群集合
	 * @param segmentId 
	 * 			客群id
	 * @return
	 */
	public List<CombineSegmentSubVo> getCombineSegmentBySubId(Long subSegmentId){
		String sql = "select * from T_COMBINE_SEGMENT_SUB sub where sub.SUB_SEGMENT_ID = ? ";
		//this.logHelper.info("select combineSegment start");
		//this.logHelper.info("select SQL :" + sql);
		//this.logHelper.info("PARAMS :" + subSegmentId);
		List<CombineSegmentSubVo> list = getJdbcTemplate().query(sql, new Object[]{subSegmentId}, new EntityRowMapper<CombineSegmentSubVo>(CombineSegmentSubVo.class));
		//this.logHelper.info("select combineSegment end");
		return list;
	}
	
	/**
	 * 查询数量
	 * @param sql
	 * @param args
	 * @return
	 */
	public Long getSegmentCount(String sql, List<Object> args){
		String querySql = "select count(distinct MEMBER_ID) from (" + sql + ")";
		//this.logHelper.info("querySql : " + querySql);
		//this.logHelper.info("params : " + args);
		return getJdbcTemplate().queryForLong(querySql, args.toArray());
	}
	
	/**
	 * 查询数量
	 * @param sql
	 * @param args
	 * @return
	 */
	public void updateSubSegmentCount(Long combineSegmentSubId, Long countAlter, Long calCount){
		getJdbcTemplate().update("update T_COMBINE_SEGMENT_SUB set COUNT_ALTER = ?, CAL_COUNT = ?, CAL_COUNT_TIME = ? where COMBINE_SEGMENT_SUB_ID = ?", new Object[]{countAlter,calCount,new Timestamp(System.currentTimeMillis()), combineSegmentSubId});
	}
	
	//TODO for test only
	/*
	public static void main(String[] args) throws Exception {
		String json = "[{\"name\":\"MEMBER_ID\",\"label\":\"会员ID\"},{\"name\":\"MEMBER_NO\",\"label\":\"会员序列号\"},{\"name\":\"MOBILE\",\"label\":\"手机号\"},{\"name\":\"NAME\",\"label\":\"姓名\"}]";
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		List<Map<String, String>>  values = mapper.readValue(json,  new TypeReference<List<Map<String, String>>>() {});
		System.out.println(values.size());
		System.out.println(values);
	}
	*/

	
}
