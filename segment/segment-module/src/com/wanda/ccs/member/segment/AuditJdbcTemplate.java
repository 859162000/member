package com.wanda.ccs.member.segment;

import static com.wanda.ccs.member.ap2in.AuthUserHelper.getUser;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import com.google.code.pathlet.jdbc.EntityInsertDef;
import com.google.code.pathlet.jdbc.EntityUpdateDef;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.service.DataAuditDao;
import com.wanda.ccs.member.segment.service.impl.DataAuditDaoImpl;
import com.wanda.ccs.member.segment.vo.BaseAuditVo;

/**
 * 加入审计日志记录功能JdbcTemplate， 提供给插入、更新和删除操作。
 * @author Charlie Zhang
 *
 */
public class AuditJdbcTemplate {
	
	private ExtJdbcTemplate template;
	
	private DataAuditDao dataAuditDao;
	
	private String dataType;
	
	private Map<String, String> deleteSqlMap;
	
	private Map<String, String> logicDeleteSqlMap;
	
	/**
	 * 构造函数
	 * @param dataSource 数据源
	 * @param dataType 数据类型，一般为数据库表名
	 */
	public AuditJdbcTemplate(DataSource dataSource, String dataType) {
		this.template = new ExtJdbcTemplate(dataSource);
		this.dataAuditDao = new DataAuditDaoImpl(dataSource);
		this.dataType = dataType;
		this.deleteSqlMap = Collections.synchronizedMap(new HashMap<String, String>());
		this.logicDeleteSqlMap = Collections.synchronizedMap(new HashMap<String, String>());
	}

	public void registerInsertEntity(EntityInsertDef insertDef) {
		template.registerInsertEntity(insertDef);
	}

	public void registerUpdateEntity(EntityUpdateDef updateDef) {
		if(updateDef.getAppendClause() != null) {
			throw new DataIntegrityViolationException("The appendClause property must not be defined in EntityUpdateDef!");
		}
		
		String[] appendClause = {"VERSION=?"};

		EntityUpdateDef newUpdateDef = new EntityUpdateDef(updateDef.getId(), updateDef.getEntityClass(), 
				updateDef.getTableName(), updateDef.getConditions(), updateDef.getExcludes(), appendClause);
		
		template.registerUpdateEntity(newUpdateDef);
	}
	
	public void registerDelete(String id, String deleteSql) {
		this.deleteSqlMap.put(id, deleteSql);
	}
	
	public void registerLogicDelete(String id, String deleteSql) {
		this.logicDeleteSqlMap.put(id, deleteSql);
	}

	public int insertEntity(String id, BaseAuditVo entity)
			throws DataAccessException {
		
		UserProfile user = getUser();
		if(user == null) {
			throw new DataIntegrityViolationException("Failed to found the user profile in current thread!");
		}
		
		entity.setCreateBy(user.getId());
		entity.setUpdateBy(user.getId());
		Timestamp curDate = new Timestamp(System.currentTimeMillis());
		entity.setCreateDate(curDate);
		entity.setUpdateDate(curDate);
		entity.setVersion(0L);
		
		int rows = template.insertEntity(id, entity);
		
		dataAuditDao.addInserting(dataType, id, entity);
		
		return rows;
	}

	public int updateEntity(String id, BaseAuditVo entity)
			throws DataAccessException {
		
		UserProfile user = getUser();
		if(user == null) {
			throw new DataIntegrityViolationException("Failed to found the user profile in current thread!");
		}

		entity.setUpdateBy(user.getId());
		Timestamp curDate = new Timestamp(System.currentTimeMillis());
		entity.setUpdateDate(curDate);
		
		if(entity.getVersion() == null) {
			throw new DataIntegrityViolationException("Failed to update entity in database! VERSION property must not be null!");
		}
		Long oldVersion = new Long(entity.getVersion().longValue());
		entity.setVersion(entity.getVersion() + 1);
		

		int updateRows = template.updateEntity(id, entity, new Object[] {oldVersion});
		if(updateRows == 0) {
			throw new DataIntegrityViolationException("Failed to update entity in database! May cause by  the version was changed or row has delete.");
		}
		
		dataAuditDao.addUpdating(dataType, id, entity);
		
		return updateRows;
	}
	
	
	public void deleteEntity(String id, Long seqId) throws DataAccessException {
		UserProfile user = getUser();
		if(user == null) {
			throw new DataIntegrityViolationException("Failed to found the user profile in current thread!");
		}
		
		template.update(deleteSqlMap.get(id), new Object[] { seqId });

		dataAuditDao.addDeleting(dataType, id, seqId, user);
	}
	
	public void logicDeleteEntity(String id, Long seqId) throws DataAccessException {
		UserProfile user = getUser();
		if(user == null) {
			throw new DataIntegrityViolationException("Failed to found the user profile in current thread!");
		}
		
		template.update(logicDeleteSqlMap.get(id), new Object[] { seqId });

		dataAuditDao.addDeleting(dataType, id, seqId, user);
	}
}
