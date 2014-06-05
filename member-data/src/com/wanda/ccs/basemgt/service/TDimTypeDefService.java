package com.wanda.ccs.basemgt.service;

import java.util.Map;

import com.wanda.ccs.model.TDimTypeDef;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 维数据设定相关的业务逻辑Service.
 * 
 * @author Danne
 * 
 */
public interface TDimTypeDefService extends ICrudService<TDimTypeDef> {

	public Map<String, String> getDimDefsByTypeId(Long typeId);

	/**
	 * 获取所有的维数据定义内容,结果集为一个二维Map。
	 * <p>
	 * 其中，Long的key为维数据类型id，其对应value为该类型所有维数据的Map<String,
	 * String>，而此Map的key是维数据编码，value是维数据名称.
	 * </p>
	 * 
	 * @return 二维Map
	 */
	public Map<String, Map<String, String>> getAllDimDefs(Boolean... cacheable);
	
	/**
	 * 获得中文名字
	 * @param typeid 类型ID
	 * @param code	编码
	 * @return
	 */
	public String getNameByTypeIdCode(Long typeid,String code);
	/**
	 * 获取所有的维数据定义的类型
	 * @return
	 */
	public Map<Long, String> getDimDefs();
	
	/**
	 * 检查是否需要更新
	 * @return
	 */
	public Object[] obtainTDimDefCountAndUpdateDate();
	
	/**
	 * 获取所有的维数据定义的类型
	 * @return
	 */
	public Map<String, String> getDimDef();

	/**
	 * 获取所有的维数据定义内容,结果集为一个二维Map。
	 * <p>
	 * 其中，Long的key为维数据类型id，其对应value为该类型所有维数据的Map<String,
	 * String>，而此Map的key是维数据编码，value是维数据名称.
	 * </p>
	 * 根据code排序
	 * @return 二维Map
	 */
	public Map<String, Map<String, String>> getAllDimDefsOrderByCode();
}
