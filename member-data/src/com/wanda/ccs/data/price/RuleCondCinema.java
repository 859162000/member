package com.wanda.ccs.data.price;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.model.TCinema;
import com.xcesys.extras.util.SpringContextUtil;

public class RuleCondCinema extends RuleCondList implements RuleCondScope {
	Map<String, String> CINEMAMAP;
	Map<String, Map<String, String>> AREACINEMA;

	public RuleCondCinema(EnumRuleCondType dim) {
		super(dim);
	}

	public Map<String, String> getList() {
		if (CINEMAMAP == null) {
			CINEMAMAP = new LinkedHashMap<String, String>();

			Vector<Long> ids = new Vector<Long>();
			for (String id : set)
				ids.add(Long.parseLong(id));

			for (TCinema cinema : SpringContextUtil.getBean(
					TCinemaService.class).findByIds(
					ids.toArray(new Long[ids.size()])))
				CINEMAMAP.put(Long.toString(cinema.getId()),
						cinema.getShortName());
		}

		return CINEMAMAP;
	}

	public Map<String, Map<String, String>> getAreaCinema() {
		return AREACINEMA;
	}

	public boolean isNotAvailable() {
		return CINEMAMAP == null || CINEMAMAP.size() == 0;
	}

	/**
	 * 按区域设置可选影城
	 */
	public void setRegionScope(Vector<String> regions) {
		CINEMAMAP = null;

		if (regions == null || regions.size() == 0)
			return;

		AREACINEMA = new LinkedHashMap<String, Map<String, String>>();
		CINEMAMAP = new LinkedHashMap<String, String>();
		for (String reg : regions) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			List<TCinema> list = SpringContextUtil
					.getBean(TCinemaService.class).findCinemasByArea(reg);
			if (list != null)
				for (TCinema c : list)
					map.put(c.getId().toString(), c.getShortName());

			CINEMAMAP.putAll(map);
			AREACINEMA.put(reg, map);
		}
	}

	/**
	 * 按影城设置可选影城
	 */
	public void setCinemaScope(Vector<Long> cinemas) {
		CINEMAMAP = null;

		if (cinemas == null || cinemas.size() == 0)
			return;

		CINEMAMAP = new LinkedHashMap<String, String>();

		// 分两次查询提高效率
		for (TCinema cinema : SpringContextUtil.getBean(TCinemaService.class)
				.findByIds(cinemas.toArray(new Long[cinemas.size()])))
			CINEMAMAP.put(cinema.getId().toString(), cinema.getShortName());
	}

	@Override
	Object getEveluateParam(Map<String, Object> params) {
		Object obj = params.get(RuleParamTypes.CINEMA_ID);
		return obj == null ? null : obj.toString();
	}
	
	public List<Map<String, Map<String, String>>> getAreaCinemaList() {
		List<Map<String, Map<String, String>>> list = new ArrayList<Map<String, Map<String, String>>>();
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		int i=0;
		int j=AREACINEMA.size();
		for(String keyset:AREACINEMA.keySet()){
			i++;
			j--;
			map.put(keyset, AREACINEMA.get(keyset));
			if(i==4 || j==0){
				list.add(map);
				map = new HashMap<String, Map<String,String>>();
				i=0;
			}
		}
		return list;
	}
}
