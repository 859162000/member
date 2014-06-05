package com.wanda.ccs.basemgt.service;

import java.util.List;
import java.util.Map;

import com.wanda.ccs.model.THall;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 影厅相关的业务逻辑Service.
 * 
 * @author Chenxm
 * @date 2011-10-26
 */
public interface THallService extends ICrudService<THall> {
	
	/**
	 * 根据影厅编码和影院id，检查是否数据库中已经存在对应记录。
	 * 
	 * @param cinemaId
	 *            影院id
	 * @param hallCode
	 *            影厅编码
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	boolean checkExisted(long cinemaId,String hallCode);
	
	/**
	 * 查询未删除状态之影厅
	 * 
	 * @author Benjamin
	 * @return
	 */
	public Map<Long,String> findUnDeletedHalls();
	
	/**
	 * 查询出所用影厅号,供票类管理调用
	 * 
	 * @return Map<String,String>
	 */
	public List<String> findAllHallNo();
}
