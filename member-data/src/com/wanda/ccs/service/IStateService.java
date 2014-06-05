package com.wanda.ccs.service;

import com.wanda.ccs.model.AbstractStateEntity;
import com.xcesys.extras.core.service.ICrudService;

public interface IStateService<T extends AbstractStateEntity> extends
		ICrudService<T> , IApprovableService<T>{

	/**
	 * 执行审核处理的方法，需要预先判断记录是否处于可允许审核操作的状态。
	 * 
	 * @param id
	 *            数据ID
	 * @param okStatus
	 *            审核通过后状态
	 * @param allowsStatus
	 *            允许执行审核操作的数据的状态，多个，可null。
	 */
	void approve(Long id);

	/**
	 * 执行提交待审核的方法，需要预先判断记录是否处于可允许提交审核操作的状态。
	 * 
	 * @param id
	 *            数据ID
	 * @param okStatus
	 *            提交审核成功状态
	 * @param allowsStatus
	 *            允许执行审核操作的数据的状态，多个，可null。
	 */
	void submit(Long id);

	/**
	 * 执行取消提交操作。
	 * <p>
	 * 提交后的数据如果未被执行审核等后续操作，允许其取回再修改。
	 * </p>
	 * 
	 * @param id
	 * @param cancelStatus
	 * @param allowsStatus
	 */
	public void cancelSubmit(Long id);
	/**
	 * 执行驳回审核的操作
	 * @param id
	 * 数据ID
	 */
	void cancelApprove(Long id);
	/**
	 * 执行批售
	 * @param ids
	 */
	public void published(Long[] ids);

}
