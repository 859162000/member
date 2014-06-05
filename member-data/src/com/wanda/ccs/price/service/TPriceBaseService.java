package com.wanda.ccs.price.service;

import java.util.List;

import com.wanda.ccs.model.TPriceBase;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 基础价格设定相关的业务逻辑Service.
 * 
 * @author Danne
 * 
 */
public interface TPriceBaseService extends ICrudService<TPriceBase> {

	/**
	 * 根据编码或者名称，检查是否数据库中已经存在对应记录。
	 * 
	 * @param code
	 *            编码
	 * @param name
	 *            名称
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	boolean checkExisted(String code, String name);

	public List<TPriceBase> getPriceBaseList();

	/**
	 * 获取最接近指定价格的的基础价格
	 * 
	 * @param price
	 * @return
	 */
	public TPriceBase getClosestPriceBase(int price);
	/**
	 * 获得最大的code用于新增
	 * @return
	 */
	public String getMaxCodeThatBeCreated();
}
