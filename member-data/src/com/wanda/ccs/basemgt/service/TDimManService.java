
package com.wanda.ccs.basemgt.service;

import java.util.List;

import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TCity;
import com.wanda.ccs.model.TDimDef;
import com.wanda.ccs.model.TProvince;
import com.xcesys.extras.core.service.ICrudService;

/**
 *维数据相关的业务逻辑Service.
 * 
 * @author Chen
 * @date 2012-01-16
 */
public interface TDimManService extends ICrudService<TDimDef> {

	/**
	 * 查询未删除状态的维数据
	 * 
	 * @return
	 */
	public List<TDimDef> findUnDeletedDim();
	/**
	 * 在编辑或添加的时候校验输入的名称是否已经存在
	 * @param name
	 * @return
	 */
	boolean checkExistedByName(String name,Long typeId);
	/**
	 * 在编辑或添加的时候校验输入的编码是否已经存在
	 * @param code
	 * @return
	 */
	boolean checkExistedByCode(String code,Long typeId);
	/**
	 * 逻辑删除
	 */
	void deleteDims(Long[] ids);

	/**
	 * 根据typeid查询
	 * @param typeId
	 * @return
	 */
	public List<TDimDef> findByTypeId(Long typeId);
}
