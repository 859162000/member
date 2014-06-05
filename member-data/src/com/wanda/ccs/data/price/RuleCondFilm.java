package com.wanda.ccs.data.price;

import java.util.Map;

import com.wanda.ccs.film.service.TFilmService;
import com.xcesys.extras.util.SpringContextUtil;

public class RuleCondFilm extends RuleCondList {
	StringBuffer filmStr = new StringBuffer();

	public RuleCondFilm(EnumRuleCondType dim) {
		super(dim);
	}

	public Map<String, String> getList() {
		return SpringContextUtil.getBean(TFilmService.class).findLatelyFilms(
				filmStr.toString());
	}

	@Override
	public boolean parseRule(String rule) {
		if (rule == null)
			return false;
		if (!rule.startsWith(dim.name() + DELIMITER))
			return false;
		rule = rule.substring(dim.name().length() + DELIMITER.length());

		set.clear();
		String[] list = rule.split(DELIMITER);
		for (String s : list)
			if (s != null && !s.equals("")) {
				set.add(s);
				filmStr.append(s).append(DELIMITER);
			}
		return true;
	}

	@Override
	Object getEveluateParam(Map<String, Object> params) {
		return params.get(RuleParamTypes.FILM_CODE);
	}
}
