package com.wanda.ccs.film.service;

import com.wanda.ccs.model.TFcAttach;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 影片合同关联文件service
 * 
 * @author Benjamin
 * @date 2011-10-19
 */
public interface TFcAttachService extends ICrudService<TFcAttach> {

	/**
	 * 根据id，检查是否数据库中已经存在对应记录。
	 * 
	 * @param id
	 *            主键id
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	boolean checkExisted(Long id);
}
