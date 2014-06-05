
package com.wanda.ccs.basemgt.service;

import java.util.List;

import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TCity;
import com.wanda.ccs.model.TProvince;
import com.xcesys.extras.core.service.ICrudService;

/**
 *省相关的业务逻辑Service.
 * 
 * @author Chen
 * @date 2011-10-21
 */
public interface TProvinceService extends ICrudService<TProvince> {

	/**
	 * 查询未删除状态之省
	 * 
	 * @return
	 */
	public List<TProvince> findUnDeletedProvince();
	/**
	 *根据区域查询该区域所包含的省
	 * @param areaCode
	 * @return
	 */
	public List<TProvince> findByArea(String areaCode);
	/**
	 * 在编辑或添加的时候校验输入的名称是否已经存在
	 * @param name
	 * @return
	 */
	boolean checkExistedByName(String name);
	/**
	 * 在编辑或添加的时候校验输入的编码是否已经存在
	 * @param code
	 * @return
	 */
	boolean checkExistedByCode(String code);

}
