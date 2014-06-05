package com.wanda.ccs.film.service;

import com.wanda.ccs.model.TFilmCopyTrackLog;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 影片拷贝行踪管理相关的业务逻辑Service.
 * 
 * @author Chen
 * 
 */
public interface TFilmCopyTrackLogService extends ICrudService<TFilmCopyTrackLog> {

	/**
	 * 根据编码或者名称，检查是否数据库中已经存在对应记录。
	 * @param sendFrom TODO
	 * @param sendTo TODO
	 * @param status TODO
	 * @param code
	 *            编码
	 * @param name
	 *            名称
	 * @return 返回true，如果记录存在，否则返回false.
	 */
//	boolean checkExisted(String code, String name);
	
	
	void deleteByFilmIdAndCopyId(Long filmId,Long copyId);
}
