package com.wanda.ccs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.basemgt.service.TCityService;
import com.wanda.ccs.basemgt.service.TDimTypeDefService;
import com.wanda.ccs.basemgt.service.TProvinceService;
import com.wanda.ccs.basemgt.service.TPublisherService;
import com.wanda.ccs.basemgt.service.TSettingService;
import com.wanda.ccs.film.service.TFilmService;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TCity;
import com.wanda.ccs.model.TFilm;
import com.wanda.ccs.model.THall;
import com.wanda.ccs.model.TPriceBase;
import com.wanda.ccs.model.TProvince;
import com.wanda.ccs.model.TPublisher;
import com.wanda.ccs.price.service.TPriceBaseService;
import com.xcesys.extras.util.SpringContextUtil;

/**
 * 
 * Spring容器相关的公共数据读取服务.
 * 
 * @author Danne
 * 
 */
public class SpringCommonService {
	/**
	 * 日志log,使用Commons-logging.
	 */
	protected static final Log log = LogFactory
			.getLog(SpringCommonService.class);

	/**
	 * 获取所有的维数据定义内容,结果集为一个二维Map。
	 * <p>
	 * 其中，Long的key为维数据类型id，其对应value为该类型所有维数据的Map<String,
	 * String>，而此Map的key是维数据编码，value是维数据名称.
	 * </p>
	 * 
	 * @return 二维Map
	 */
	public static final Map<String, Map<String, String>> getAllDimDefs() {
		try {
			TDimTypeDefService dimService = SpringContextUtil
					.getBean(TDimTypeDefService.class);
			if (dimService != null) {
				return dimService.getAllDimDefs();
			}
		} catch (Exception e) {
			log.error("读取维数据Map时发生异常。", e);
		}
		return new HashMap<String, Map<String, String>>();
	}

	/**
	 * 获取系统参数设定的数据 关于放映制式的数据
	 * 
	 * @return
	 */
	public static final Map<String, List<String>> getAllTSettings() {
		TSettingService service = SpringContextUtil
				.getBean(TSettingService.class);
		return service.getAllTSettings();
	}

	/**
	 * 所有未删除影院
	 * 
	 * @author Benjamin
	 * @return
	 */
	public static final Map<Long, String> getAllUnDeletedCinemas() {
		TCinemaService service = SpringContextUtil
				.getBean(TCinemaService.class);
		List<TCinema> lst = service.findUnDeletedCinemas();
		Map<Long, String> map = new HashMap<Long, String>();
		for (TCinema gh : lst) {
			map.put(gh.getId(), gh.getShortName());
		}
		return map;
	}

	/**
	 * 所有未删除影院并根据名称排序
	 * 
	 * @author chenxm
	 * @return
	 */
	public static final List<TCinema> getUnDeletedCinemasOrderByName(String area) {
		TCinemaService service = SpringContextUtil
				.getBean(TCinemaService.class);
		List<TCinema> lst = service.findCinemasOrderByName(area);
		return lst;
	}
	
	/**
	 * 获取区域下属影院列表
	 * 
	 * @param area
	 *            区域编码
	 * @return
	 * @author Danne
	 */
	public static final Map<Long, String> getCinemaMapByArea(String area) {
		List<TCinema> list = SpringCommonService.getCinemasByArea(area);
		Map<Long, String> map = new HashMap<Long, String>();
		for (TCinema gh : list) {
			map.put(gh.getId(), gh.getShortName());
		}
		return map;
	}
	
	/**
	 * 获取影城内码-影城内部名称
	 * @return
	 */
	public static final Map<String,String> getAllCinemaMap(String area){
		List<TCinema> list = SpringContextUtil.getBean(TCinemaService.class).findCinemasOrderByName(area);
		Map<String,String> map = new LinkedHashMap<String, String>();
		for(TCinema c:list){
			map.put(c.getInnerCode(), c.getInnerName());
		}
		return map;
	}
	
	
	/**
	 * 获取影城编码-影城内部名称
	 * @return
	 */
	public static final Map<String,String> getAllCodeAndInnerNameCinemaMap(){
		List<TCinema> list = SpringContextUtil.getBean(TCinemaService.class).findCinemasByArea(null);
		Map<String,String> map = new HashMap<String, String>();
		for(TCinema c:list){
			map.put(c.getCode(), c.getInnerName());
		}
		return map;
	}

	/**
	 * 获取区域下属影院列表
	 * 
	 * @param area
	 *            区域编码
	 * @return
	 * @author Danne
	 */
	public static final List<TCinema> getCinemasByArea(String area) {
		TCinemaService service = SpringContextUtil
				.getBean(TCinemaService.class);
		return service.findCinemasByArea(area);
	}

	/**
	 * 获取城市
	 * 
	 * @return
	 */
	public static final Map<Long, String> getCity() {
		TCityService provinceService = SpringContextUtil
				.getBean(TCityService.class);
		List<TCity> list = provinceService.findUnDeletedCity();
		Map<Long, String> cityMap = new HashMap<Long, String>();
		if (list.size() > 0 || list != null) {
			for (TCity city : list) {
				if (city.getName() != null) {
					cityMap.put(city.getId(), city.getName());
				}
			}
		}
		return cityMap;
	}

	/**
	 * 根据省Id获取城市的联动
	 * 
	 * @return
	 */
	public static final List<TCity> getCityByProviceId(Long provinceId) {
		TCityService cityService = SpringContextUtil
				.getBean(TCityService.class);
		List<TCity> list = cityService.findByProvinceId(provinceId);

		return list;
	}

	/**
	 * 根据区域码获取省的联动
	 * 
	 * @return
	 */
	public static final List<TProvince> getProvinceByArea(String areaCode) {
		TProvinceService provinceService = SpringContextUtil
				.getBean(TProvinceService.class);
		List<TProvince> list = provinceService.findByArea(areaCode);

		return list;
	}

	/**
	 * 根据区域编码获取所属城市Map。
	 * 
	 * @param area
	 *            区域编码
	 * @return
	 */
	public static final Map<Long, String> getCityMapByArea(String area) {
		TCityService cityService = SpringContextUtil
				.getBean(TCityService.class);
		Map<Long, String> cityMap = new HashMap<Long, String>();
		List<TCity> list = cityService.findByArea(area);
		if (list != null && !list.isEmpty()) {
			for (TCity c : list) {
				cityMap.put(c.getId(), c.getName());
			}
		}
		return cityMap;
	}

	/**
	 * 根据维数据类型获取一个维数据Map.
	 * 
	 * @param adapter
	 *            数据库适配器
	 * @param typeId
	 *            维数据类型ID
	 * @return
	 */
	public static final Map<String, String> getDimDefByTypeId(Long typeId) {
		try {
			TDimTypeDefService dimService = SpringContextUtil
					.getBean(TDimTypeDefService.class);
			return dimService.getDimDefsByTypeId(typeId);
		} catch (Exception e) {
			log.error("读取维数据Map时发生异常。", e);
		}
		return new HashMap<String, String>();
	}

	/**
	 * 获取维数据类型Map.
	 * 
	 * @return
	 */
	public static final Map<Long, String> getDimDefType() {
		try {
			TDimTypeDefService dimService = SpringContextUtil
					.getBean(TDimTypeDefService.class);
			return dimService.getDimDefs();
		} catch (Exception e) {
			log.error("读取维数据Map时发生异常。", e);
		}
		return new HashMap<Long, String>();
	}
	/**
	 * 获取未删除影片Map<seqId,Name>
	 * 
	 * @return
	 */
	public static final Map<Long, String> getFilm() {
		TFilmService tFService = SpringContextUtil.getBean(TFilmService.class);
		Map<Long, String> filmMap = tFService.getAllFilm();
		return filmMap;
	}

	/**
	 * 获取未删除影片Map<seqId,Name>
	 * 
	 * @return
	 */
	public static final Map<Long, String> getUniqueFilms() {
		TFilmService tFService = SpringContextUtil.getBean(TFilmService.class);
		Map<Long, String> filmMap = tFService.getAllFilm();
		return filmMap;
	}

	public static Map<Long, String> getFilmMap(Date begin, Date end) {
		TFilmService tFService = SpringContextUtil.getBean(TFilmService.class);
		List<TFilm> list = tFService.getFilmMap(begin, end,null,null);
		Map<Long, String> map = new LinkedHashMap<Long, String>();
		for (TFilm f : list) {
			map.put(f.getId(), f.getFilmName());
		}
		return map;
	}

	/**
	 * 根据影院ID获取其下属所有的影厅，返回影厅ID和影厅名字的Map。
	 * 
	 * @param cinemaId
	 *            影院ID
	 * @return
	 */
	public static final List<THall> getHallsByCinemaId(Long cinemaId) {
		TCinemaService service = SpringContextUtil
				.getBean(TCinemaService.class);
		TCinema cinema = service.findById(cinemaId);
		List<THall> halls = new ArrayList<THall>();
		if (cinema != null) {
			Set<THall> hs = cinema.gettHalls();
			if (hs != null) {
				for (THall h : hs) {
					if (h.getDeleted() != null && h.getDeleted().booleanValue()) {
						// 排除逻辑删除的记录
						continue;
					}
					halls.add(h);
				}
			}
		}
		Collections.sort(halls);
		return halls;
	}

	/**
	 * 获取所有的基础价格。
	 * 
	 * @author lujx
	 * @return Map
	 */
	public static final Map<Long, Integer> getPriceBase() {
		Map<Long, Integer> map = new LinkedHashMap<Long, Integer>();
		TPriceBaseService service = SpringContextUtil
				.getBean(TPriceBaseService.class);
		List<TPriceBase> list = service.getPriceBaseList();
		Collections.sort(list);
		for (TPriceBase price : list) {
			map.put(price.getId(), price.getPrice());
		}
		return map;
	}

	/**
	 * 获取省
	 * 
	 * @return
	 */
	public static final Map<Long, String> getProvince() {
		TProvinceService provinceService = SpringContextUtil
				.getBean(TProvinceService.class);
		List<TProvince> list = provinceService.findUnDeletedProvince();
		Map<Long, String> provinceMap = new HashMap<Long, String>();
		if (list.size() > 0 || list != null) {
			for (TProvince province : list) {
				if (province.getName() != null) {
					provinceMap.put(province.getId(), province.getName());
				}
			}
		}
		return provinceMap;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public static final Map<Long, String> getPublishers() {
		Map<Long, String> publisherMap = new HashMap<Long, String>();
		TPublisherService pService = SpringContextUtil
				.getBean(TPublisherService.class);
		List<TPublisher> lst = pService.findUnDeletedPublishers();
		for (TPublisher p : lst) {
			publisherMap.put(p.getId(), p.getPublishername());
		}

		return publisherMap;
	}
	

}
