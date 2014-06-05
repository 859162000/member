package com.wanda.ccs.data.price;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.basemgt.service.TCityService;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TCity;
import com.xcesys.extras.util.SpringContextUtil;

public class RuleCondCity extends RuleCondList implements RuleCondScope {
	Map<String, String> CITYMAP;
	Map<String, Map<String, String>> AREACITY;

	public RuleCondCity(EnumRuleCondType dim) {
		super(dim);
	}

	public Map<String, String> getList() {
		if (CITYMAP == null) {
			CITYMAP = new LinkedHashMap<String, String>();

			Vector<Long> ids = new Vector<Long>();
			for (String id : set)
				ids.add(Long.parseLong(id));

			for (TCity city : SpringContextUtil.getBean(TCityService.class)
					.findByIds(ids.toArray(new Long[ids.size()])))
				CITYMAP.put(Long.toString(city.getId()), city.getName());
		}

		return CITYMAP;
	}

	public Map<String, Map<String, String>> getAreaCity() {
		return AREACITY;
	}

	public boolean isNotAvailable() {
		return CITYMAP == null || CITYMAP.size() == 0;
	}

	/**
	 * 按区域设置可选城市
	 */
	public void setRegionScope(Vector<String> regions) {
		CITYMAP = null;

		if (regions == null || regions.size() == 0)
			return;

		AREACITY = new LinkedHashMap<String, Map<String, String>>();
		CITYMAP = new LinkedHashMap<String, String>();

		for (String reg : regions) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			List<TCity> list = SpringContextUtil.getBean(TCityService.class)
					.findByArea(reg);
			if (list != null)
				for (TCity c : list)
					map.put(c.getId().toString(), c.getName());

			CITYMAP.putAll(map);
			AREACITY.put(reg, map);
		}
	}

	/**
	 * 按影城设置可选城市
	 */
	public void setCinemaScope(Vector<Long> cinemas) {
		CITYMAP = null;

		if (cinemas == null || cinemas.size() == 0)
			return;

		CITYMAP = new LinkedHashMap<String, String>();

		// 分两次查询提高效率
		Vector<Long> cityIds = new Vector<Long>();
		for (TCinema cinema : SpringContextUtil.getBean(TCinemaService.class)
				.findByIds(cinemas.toArray(new Long[cinemas.size()])))
			cityIds.add(cinema.getCityId());

		for (TCity city : SpringContextUtil.getBean(TCityService.class)
				.findByIds(cityIds.toArray(new Long[cityIds.size()])))
			CITYMAP.put(city.getId().toString(), city.getName());
	}

	@Override
	Object getEveluateParam(Map<String, Object> params) {
		Object obj = params.get(RuleParamTypes.CITY_ID);
		return obj == null ? null : obj.toString();
	}
	
	
	public List<Map<String, Map<String, String>>> getAreaCityList() {
		List<Map<String, Map<String, String>>> list = new ArrayList<Map<String, Map<String, String>>>();
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		int i=0;
		int j=AREACITY.size();
		for(String keyset:AREACITY.keySet()){
			i++;
			j--;
			map.put(keyset, AREACITY.get(keyset));
			if(i==4 || j==0){
				list.add(map);
				map = new HashMap<String, Map<String,String>>();
				i=0;
			}
		}
		return list;
	}
}
