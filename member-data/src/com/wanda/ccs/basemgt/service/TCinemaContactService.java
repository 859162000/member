package com.wanda.ccs.basemgt.service;

import java.util.List;
import java.util.Map;

import com.wanda.ccs.model.TCinemaContact;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 影院联系人相关的业务逻辑Service.
 * 
 * @author Chen
 * @date 2011-10-24
 */
public interface TCinemaContactService extends ICrudService<TCinemaContact> {

	/**
	 * 根据编码或者名称，检查是否数据库中已经存在对应记录。
	 * 
	 * @param code
	 *            编码
	 * @param name
	 *            名称
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	boolean checkExisted(String code,String name);
	
	/**
	 * 查询未删除状态之影院
	 * 
	 * @author Benjamin
	 * @return
	 */
	public Map<Long,String> findUnDeletedCinemas();
	/**
	 * 根据影院Id查询联系人
	 * @return
	 */
	public List<TCinemaContact> findCinemaContacts(Long cinemaId);
}
