package com.wanda.ccs.film.service;


import com.wanda.ccs.model.TFilmCopySchedule;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 拷贝调度设定相关的业务逻辑Service.
 * 
 * @author Danne
 * 
 */
public interface TFilmCopyScheduleService extends ICrudService<TFilmCopySchedule> {
	/**
	 * 根据拷贝编号，影片ID检查是否数据库中已经存在对应记录。
	 * 
	 * @param copyNo
	 *            编码
	 *  @param filmId
	 *            影片Id       
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	boolean checkExisted(String copyNo,String filmId);
	
	/**
	 * 修改调度时的状态
	 */
	public void receive(Long[] ids);
	
	
	/**
	 * 提交调度
	 * @param ids 调度id数组
	 */
	public void commitSchedule(Long[] ids);
	/**
	 * 取消调度
	 * @param ids 调度id数组
	 */
	public void cancelSchedule(Long[] ids);
	
	
	public void sendBackPublisher(Long[] ids);
	
	
}
