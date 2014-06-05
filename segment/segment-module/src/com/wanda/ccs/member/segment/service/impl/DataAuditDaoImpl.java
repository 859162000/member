package com.wanda.ccs.member.segment.service.impl;

import java.sql.Timestamp;

import javax.sql.DataSource;

import org.springframework.dao.DataRetrievalFailureException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.pathlet.jdbc.EntityInsertDef;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.service.DataAuditDao;
import com.wanda.ccs.member.segment.vo.BaseAuditVo;
import com.wanda.ccs.member.segment.vo.DataAuditVo;
import com.wanda.ccs.member.segment.vo.DataAuditVo.ActionType;

public class DataAuditDaoImpl implements DataAuditDao {
	
	private ExtJdbcTemplate jdbcTemplate;
	
	public DataAuditDaoImpl(DataSource dataSource) {
		jdbcTemplate = new ExtJdbcTemplate(dataSource);
		jdbcTemplate.registerInsertEntity(new EntityInsertDef("insert", DataAuditVo.class, "T_DATA_AUDIT"));
		//ExtJdbcTemplate.LOG_SQL = true;
	}

	/**
	 * 添加更新记录
	 * @param actionName 操作的具体名称，如："更新计算数量"
	 * @param vo
	 * @return
	 */
	public DataAuditVo addUpdating(String dataType, String actionName, BaseAuditVo vo) {
		return add(dataType, actionName, ActionType.UPDATE, vo);
	}
	
	
	/**
	 * 添加插入记录
	 * @param dataType
	 * @param actionName
	 * @param vo
	 * @return
	 */
	public DataAuditVo addInserting(String dataType, String actionName, BaseAuditVo vo) {
		return add(dataType, actionName, ActionType.INSERT, vo);
	}
	
	private DataAuditVo add(String dataType, String actionName, ActionType actionType, BaseAuditVo vo) {
		
		DataAuditVo audit = new DataAuditVo();
		
		ObjectMapper objectMapper = new ObjectMapper();
		String dataContent;
		try {
			dataContent = objectMapper.writeValueAsString(vo);
		} 
		catch (JsonProcessingException e) {
			throw new DataRetrievalFailureException("Failed to convert the bean object into json string!", e);
		}
		

		audit.setDataType(dataType);
		audit.setDataSeqId(vo.retrieveSeqId());
		audit.setDataCode(vo.retrieveCode());
		audit.setDataContent(dataContent);
		audit.setActionType(actionType.name());
		audit.setActionName(actionName);
		audit.setActionUserId(vo.getUpdateBy());
		audit.setActionDate(vo.getUpdateDate());
		audit.setVersion(vo.getVersion());
		
		audit.setDataAuditId(jdbcTemplate.queryForLong("select S_T_DATA_AUDIT.NEXTVAL from DUAL"));
		int rows = jdbcTemplate.insertEntity("insert", audit);
		if(rows != 1) {
			throw new DataRetrievalFailureException("Failed to insert one audit row!");
		}
		
		return audit;
	}
	
	/**
	 * 添加一条删除记录
	 * @param dataType
	 * @param actionName
	 * @param seqId
	 * @param user
	 * @return
	 */
	public DataAuditVo addDeleting(String dataType, String actionName, Long seqId, UserProfile user) {
		DataAuditVo audit = new DataAuditVo();
		
		audit.setDataType(dataType);
		audit.setDataSeqId(seqId);
		audit.setDataCode(null);
		audit.setDataContent(null);
		audit.setActionType(ActionType.DELETE.name());
		audit.setActionName(actionName);
		audit.setActionUserId(user.getId());
		
		Timestamp actionDate = new Timestamp(System.currentTimeMillis());
		audit.setActionDate(actionDate);
		audit.setVersion(0L);
		
		audit.setDataAuditId(jdbcTemplate.queryForLong("select S_T_DATA_AUDIT.NEXTVAL from DUAL"));
		int rows = jdbcTemplate.insertEntity("insert", audit);
		if(rows != 1) {
			throw new DataRetrievalFailureException("Failed to insert one audit row!");
		}
		
		return audit;
	}
	
	/**
	 * 添加一条逻辑删除记录
	 * @param dataType
	 * @param actionName
	 * @param seqId
	 * @param user
	 * @return
	 */
	public DataAuditVo addLogicDeleting(String dataType, String actionName, Long seqId, UserProfile user) {
		DataAuditVo audit = new DataAuditVo();
		
		audit.setDataType(dataType);
		audit.setDataSeqId(seqId);
		audit.setDataCode(null);
		audit.setDataContent(null);
		audit.setActionType(ActionType.LOGIC_DELETE.name());
		audit.setActionName(actionName);
		audit.setActionUserId(user.getId());
		
		Timestamp actionDate = new Timestamp(System.currentTimeMillis());
		audit.setActionDate(actionDate);
		audit.setVersion(0L);
		
		audit.setDataAuditId(jdbcTemplate.queryForLong("select S_T_DATA_AUDIT.NEXTVAL from DUAL"));
		int rows = jdbcTemplate.insertEntity("insert", audit);
		if(rows != 1) {
			throw new DataRetrievalFailureException("Failed to insert one audit row!");
		}
		
		return audit;
	}
	

}
