package com.wanda.ccs.member.segment.service;

import java.util.List;

import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.wanda.ccs.member.segment.vo.ConCategoryVo;

public interface ConCategoryService {
	
	public List<ConCategoryVo> getSubCategories(Long pConCategoryId);
	
	public List<ConCategoryVo> getAllCategories();
	
}
