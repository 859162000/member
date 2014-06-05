package com.wanda.ccs.film.service;

import java.util.List;

import com.wanda.ccs.model.TFilm;
import com.wanda.ccs.model.TFilmContract;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 相关的业务逻辑Service.
 * 
 * @author Benjamin
 * @date 2011-10-17
 */
public interface TFilmContractService extends ICrudService<TFilmContract> {

	/**
	 * 根据编码或者名称，检查是否数据库中已经存在对应记录。
	 * 
	 * @param code
	 *            编码
	 * @param name
	 *            名称
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	boolean checkExisted(String contractNo);
}
