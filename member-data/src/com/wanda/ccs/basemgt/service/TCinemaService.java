package com.wanda.ccs.basemgt.service;

import java.util.List;
import java.util.Map;

import com.wanda.ccs.model.TCinema;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 影院相关的业务逻辑Service.
 * 
 * @author Chen
 * @date 2011-10-21
 */
public interface TCinemaService extends ICrudService<TCinema> {
	/**
	 * 根据编码加载
	 * 
	 * @param code
	 * @return
	 */
	public TCinema findByCode(String code);

	/**
	 * 根据编码，检查是否数据库中已经存在对应记录。
	 * 
	 * @param code
	 *            编码
	 * 
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	boolean checkExisted(String code);

	/**
	 * 根据影院简称，检查是否数据库中已经存在对应记录。
	 * 
	 * 
	 * @param shortName
	 *            影院简称名称
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	boolean checkExistedByShortName(String shortName);

	/**
	 * 根据影院全称，检查是否数据库中已经存在对应记录。
	 * 
	 * 
	 * @param outName
	 *            影院外部名称
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	public boolean checkExistedByOutName(String outName);

	/**
	 * 根据影院全称，检查是否数据库中已经存在对应记录。
	 * 
	 * 
	 * @param innerName
	 *            影院外部名称
	 * @return 返回true，如果记录存在，否则返回false.
	 */
	public boolean checkExistedByInnerName(String innerName);

	/**
	 * 查询影院
	 * 
	 * @author Benjamin
	 * @return
	 */
	public List<TCinema> findUnDeletedCinemas(Long... city);
	
	
	/**
	 * 查询影院
	 * 
	 * @author chenxm
	 * @return
	 */
	public List<TCinema> findCinemasOrderByName(String area);

	/**
	 * 查询未删除状态之影院,返回Map<cinema's Id,TCinema>
	 * 
	 * @return
	 */
	public Map<Long, TCinema> findUnDeletedCinemasMap();
	
	/**
	 * 获取区域下属影院列表
	 * 
	 * @param area
	 *            区域编码
	 * @return
	 */
	public List<TCinema> findCinemasByArea(String area);

	/**
	 * 获取区域下属影院,返回Map<cinema's Id,cinema's Name>
	 * 
	 * @param area
	 *            区域编码
	 * @return
	 */
	public Map<Long, String> getCinamasByArea(String area);

	/**
	 * 获取所有未删除的影院列表(价格策略的不可用规则中使用)
	 * 
	 * @author lujx
	 * @return
	 */
	public Map<String, String> findUnDeletedCinema();
	
	public Map<Long, TCinema> findUnDeletedCinemasMapByReqgion(String region);

	/**
	 * 获取影院的最大内码
	 * 
	 * @return
	 */
	public String findMaxInnerCode();
	
	/**
	 * 获取所有的维数据定义内容,结果集为一个二维Map。
	 * <p>
	 * 其中，Long的key为维数据类型id，其对应value为该类型所有维数据的Map<String,
	 * String>，而此Map的key是维数据编码，value是维数据名称.
	 * </p>
	 * 
	 * @return 二维Map
	 */
	public Map<String, Map<String, String>> getAllCinemas();
	
	public List<TCinema> findByLikeCodeOrName(String like);

	public List<TCinema> getCinamasByRegion(String region);
	/**
	 * 查询所有的影院  add by Chenxm 2012-04-11
	 * @return
	 */
	public Map<Long,String> findAllCinemas();
	
	/**
	 * 获取所有影城监控列表
	 * @param month
	 * @return
	 */
	public List< Map<String, ?>> getMonitorLoadFile(QueryCriteria<String, Object> query);
	
	/**
	 * 根据日期和影城内码获取所选日期的文件列表
	 * @param day
	 * @param innerCode
	 * @return
	 */
	public List< Map<String, ?>> getLoadFileByDayAndCinemaCode(String day,String innerCode);
	
	/**
	 * 根据月份获取影城票房卖品数据正确
	 * @param moth
	 * @return
	 */
	public List<Map<String, ?>> getCinemaDaysGroup(String moth);
	
	public List< Map<String, ?>> getAllOnlineCinema();
	/**
	 * 更新影城IP中的影城编码
	 * 
	 * @param oldCode
	 * @param newCode
	 */
	public int updateCodeIntfClientInfo(String oldCode , String newCode);


}
