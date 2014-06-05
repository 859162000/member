package com.wanda.ccs.mem.service;

import java.util.Map;

import com.wanda.ccs.model.TVoucherPoolDetail;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 券库明细业务接口
 * @author yaoguoqing
 *
 */
public interface TMemberVoucherPoolDetailService extends ICrudService<TVoucherPoolDetail> {
	public PageResult<TVoucherPoolDetail> findByCriteria(
			QueryCriteria<String, Object> query, Map<String, Object> map);
}
