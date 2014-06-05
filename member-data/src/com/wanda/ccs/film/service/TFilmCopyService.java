package com.wanda.ccs.film.service;


import java.util.List;

import com.wanda.ccs.model.TFilmCopy;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 拷贝设定相关的业务逻辑Service.
 * 
 * @author Danne
 * 
 */
public interface TFilmCopyService extends ICrudService<TFilmCopy> {
	/**
	 * 通过拷贝编号、影片ID，获得拷贝信息
	 */
	public TFilmCopy getByCopyNoAndFilmId(String copyNo,Long filmId);
	
	/**
	 * 通过拷贝Id，获得是否有外键关联
	 */
	public TFilmCopy checkFKFromFilmCopy(Long[] ids);
	
	/**
	 * 影片ID获得拷贝信息
	 */
	public List<TFilmCopy> findByFilmId(Long filmId);
}
