package com.wanda.ccs.member.segment.service.impl;

import java.util.List;

import javax.sql.DataSource;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.member.segment.service.ConCategoryService;
import com.wanda.ccs.member.segment.vo.ConCategoryVo;

public class ConCategoryServiceImpl implements ConCategoryService {

	private ExtJdbcTemplate jdbcTemplate = null;

	@InstanceIn(path="/dataSource")  
	private DataSource dataSource;

	public ExtJdbcTemplate getJdbcTemplate()  {
		if(this.jdbcTemplate == null) {
			this.jdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.jdbcTemplate;
	}
	
	
	public List<ConCategoryVo> getSubCategories(Long pConCategoryId) {
		List<ConCategoryVo> listResult = getJdbcTemplate().query(
				"select t.CON_CATEGORY_ID, t.P_CON_CATEGORY_ID, t.CATEGORY_CODE, t.ITEM_TYPE, t.CATEGORY_NAME, t.CATEGORY_DESC from T_CON_CATEGORY t where t.P_CON_CATEGORY_ID=? order by t.CATEGORY_CODE asc",
				new Object[]{pConCategoryId}, new EntityRowMapper<ConCategoryVo>(ConCategoryVo.class, null));
		return listResult;
	}

	
	public List<ConCategoryVo> getAllCategories() {		
		List<ConCategoryVo> listResult = getJdbcTemplate().query(
				"select t.CON_CATEGORY_ID, t.P_CON_CATEGORY_ID, t.CATEGORY_CODE, t.ITEM_TYPE, t.CATEGORY_NAME, t.CATEGORY_DESC from T_CON_CATEGORY t order by t.CATEGORY_CODE asc",
				new EntityRowMapper<ConCategoryVo>(ConCategoryVo.class, null));
		return listResult;
	}

}
