package com.wanda.ccs.data.price;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class RuleCondList extends RuleCond {
	protected final Log log = LogFactory.getLog(getClass());

	HashSet<String> set = new HashSet<String>();

	public RuleCondList(EnumRuleCondType dim) {
		super(dim);
	}

	public boolean hasValue() {
		return set.size() > 0;
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
			if (s != null && !s.equals(""))
				set.add(s);
		return true;
	}

	@Override
	public String genRule() {
		StringBuffer sb = new StringBuffer();
		sb.append(dim.name());
		for (String s : set)
			sb.append(DELIMITER).append(s);
		return sb.toString();
	}

	@Override
	public String genDesc() {
		StringBuffer sb = new StringBuffer();
		if (set.size() > 0) {
			sb.append(dim.title);
			sb.append("为");
			boolean bFirst = true;
			Map<String, String> list = getList();
			for (String key : list.keySet())
				if (set.contains(key)) {
					if (bFirst)
						bFirst = false;
					else
						sb.append("或");
					sb.append(list.get(key));
				}
		}
		return sb.toString();
	}

	abstract public Map<String, String> getList();

	public Set<String> getKeys() {
		return getList().keySet();
	}

	public Set<String> getSelected() {
		return set;
	}

	public boolean getEditValue(String key) {
		return set.contains(key);
	}

	public void setEditValue(String key, boolean add) {
		if (add)
			set.add(key);
		else
			set.remove(key);
	}

	Object getEveluateParam(Map<String, Object> params) {
		return params.get(Long.toString(dim.dimType));
	}

	@Override
	public boolean matches(Map<String, Object> params) {
		Object val = getEveluateParam(params);
		if (val == null)
			return false;

		if (val instanceof String) {
			return set.contains(val);
		} else if (val instanceof String[]) {
			for (String str : (String[]) val)
				if (set.contains(str))
					return true;
			return false;
		}

		log.error("Unsupported param type for RulecondDim.");
		return false;
	}
}
