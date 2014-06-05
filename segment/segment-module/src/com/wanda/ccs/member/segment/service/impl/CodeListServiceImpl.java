package com.wanda.ccs.member.segment.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.member.segment.SegmentSysException;
import com.wanda.ccs.member.segment.service.CodeList;
import com.wanda.ccs.member.segment.service.CodeListService;
import com.wanda.ccs.member.segment.service.MdmCache;
import com.wanda.ccs.member.segment.service.MdmCacheService;
import com.wanda.ccs.member.segment.vo.CodeEntryVo;

public class CodeListServiceImpl implements CodeListService {
	
	private final static String CACHE_PREFIX = "CodeList.";

	private ExtJdbcTemplate jdbcTemplate = null;
	
	private Map<String, String> sourceIdInfos;
	
	@InstanceIn(path = "MdmCacheService")
	private MdmCacheService mdmCacheService;

	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;
	
	private Map<String, String> dimdefOrderByMap;//保存T_DIMDEF查询时的排序语句和typeId的对应关系，不同的typeId可以使用不同的排序。
	
	public CodeListServiceImpl() {
		sourceIdInfos = new LinkedHashMap<String, String>();
		sourceIdInfos.put("dimdef", "来自应用的T_DIMDEF表中的数据");
		sourceIdInfos.put("paymethod", "来自应用的T_D_CON_PAY_METHOD表中的数据");
		sourceIdInfos.put("channel", "来自应用的T_D_CON_CHANNEL表中的数据");
		
		dimdefOrderByMap = new HashMap<String, String>();
		String orderBySeqid = "order by SEQID asc";
		dimdefOrderByMap.put("2001", orderBySeqid);
		dimdefOrderByMap.put("2003", orderBySeqid);
	}

	private MdmCache<CodeList> getCache(String sourceId) {
		return mdmCacheService.getCache(CACHE_PREFIX + sourceId);
	}
	
	public ExtJdbcTemplate getJdbcTemplate()  {
		if(this.jdbcTemplate == null) {
			this.jdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.jdbcTemplate;
	}
	
	public Map<String, String> getSourceIdInfos() {
		return sourceIdInfos;
	}

	public synchronized CodeList getCodeList(String sourceId, String typeId) {
		if(sourceIdInfos.get(sourceId) == null) {
			throw new SegmentSysException("The sourceId=" + sourceId + " has not defined!");
		}
		
		CodeList mdmEnum = getCache(sourceId).get(typeId);
		if (mdmEnum == null) {
			if("dimdef".equals(sourceId)) {
				mdmEnum = getDimdefCodeList(typeId);
			}
			else if("paymethod".equals(sourceId)) {
				mdmEnum = getPayMethodCodeList(typeId);
			}else if("channel".equals(sourceId)){
				mdmEnum = getChannelCodeList(typeId);
			}
			else {
				throw new SegmentSysException("The sourceId=" + sourceId + " has not implementated!");
			}
			
			if (mdmEnum != null) {
				getCache(sourceId).put(typeId, mdmEnum);
			}
		}
		return mdmEnum;
	}

	private CodeList getDimdefCodeList(String typeid) {
		String orderBySql = dimdefOrderByMap.get(typeid);
		if(orderBySql == null) {
			orderBySql = "order by t.code";
		}
		String sql = "select t.name, t.code, t.description from T_DIMDEF t where t.isdelete=? and t.typeid=? "
				+ orderBySql;
		
		List<CodeEntryVo> listResult = getJdbcTemplate().query(sql, new Object[]{"0", typeid}, 
				new EntityRowMapper<CodeEntryVo>(CodeEntryVo.class, null) );
		if(listResult != null && listResult.size() > 0) {
			return new DefaultCodeList(typeid, listResult);
		}
		else {
			return null;
		}
	}
	
	private CodeList getPayMethodCodeList(String typeid)  {
		//这里typeId其实不起作用，因为T_D_CON_PAY_METHOD表中只有套枚举数据。
		List<CodeEntryVo> listResult = getJdbcTemplate().query("select t.PAY_METHOD_CODE as code, t.PAY_METHOD_NAME as name from T_D_CON_PAY_METHOD t order by t.PAY_METHOD_CODE", 
				new EntityRowMapper<CodeEntryVo>(CodeEntryVo.class, new String[]{"description"}) );
		
		if(listResult != null && listResult.size() > 0) {
			return new DefaultCodeList(typeid, listResult);
		}
		else {
			return null;
		}
	}
	
	private CodeList getChannelCodeList(String typeid)  {
		//这里typeId其实不起作用，因为T_D_CON_CHANNEL表中只有套枚举数据。
		List<CodeEntryVo> listResult = getJdbcTemplate().query("select t.CHANNEL_CODE as code, t.CHANNEL_NAME as name from T_D_CON_CHANNEL t order by t.CHANNEL_CODE", 
				new EntityRowMapper<CodeEntryVo>(CodeEntryVo.class, new String[]{"description"}) );
		
		if(listResult != null && listResult.size() > 0) {
			return new DefaultCodeList(typeid, listResult);
		}
		else {
			return null;
		}
	}

	public synchronized void flush(String sourceId, String name) {
		getCache(sourceId).remove(name);
	}
	
	public synchronized void flush(String sourceId) {
		getCache(sourceId).clear();
	}

}
