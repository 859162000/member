package com.wanda.ccs.data.price;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 日期范围条件
 * 
 * @author Jim
 */
public class RuleCondDateRange extends RuleCond {
	static final String OPTION_BEFORE = "B";
	static final String OPTION_AFTER = "A";
	static final String OPTION_BETWEEN = "M";
	static final String OPTION_NO = "N";

	private Date befDate; // 之前日期
	private Date aftDate; // 之后日期
	private Date sDate; // 日期点限制开始时间
	private Date eDate; // 日期点限制结束时间
	private String dateChoose; // 判断选择的RADIO

	public RuleCondDateRange(EnumRuleCondType dim) {
		super(dim);
		dateChoose = OPTION_BEFORE;
	}

	@Override
	public boolean hasValue() {
		return dateChoose != null && !OPTION_NO.equals(dateChoose);
	}

	public Date getBefDate() {
		return befDate;
	}

	public void setBefDate(Date befDate) {
		this.befDate = befDate;
	}

	public Date getAftDate() {
		return aftDate;
	}

	public void setAftDate(Date aftDate) {
		this.aftDate = aftDate;
	}

	public Date getsDate() {
		return sDate;
	}

	public void setsDate(Date sDate) {
		this.sDate = sDate;
	}

	public Date geteDate() {
		return eDate;
	}

	public void seteDate(Date eDate) {
		this.eDate = eDate;
	}

	public String getDateChoose() {
		return dateChoose;
	}

	public void setDateChoose(String dateChoose) {
		this.dateChoose = dateChoose;
	}

	@Override
	public boolean parseRule(String rule) {
		if (rule == null)
			return false;
		if (!rule.startsWith(dim.name() + DELIMITER))
			return false;
		rule = rule.substring(dim.name().length() + DELIMITER.length());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (rule.startsWith("B")) {
			dateChoose = rule.substring(0, 1);
			rule = rule.substring(1 + DELIMITER.length());
			String[] list = rule.split(DELIMITER);
			try {
				befDate = sdf.parse(list[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (rule.startsWith("A")) {
			dateChoose = rule.substring(0, 1);
			rule = rule.substring(1 + DELIMITER.length());
			String[] list = rule.split(DELIMITER);
			try {
				aftDate = sdf.parse(list[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (rule.startsWith("M")) {
			dateChoose = rule.substring(0, 1);
			rule = rule.substring(1 + DELIMITER.length());
			String[] list = rule.split(DELIMITER);
			try {
				sDate = sdf.parse(list[0]);
				eDate = sdf.parse(list[1]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public String genRule() {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sb.append(dim.name());
		sb.append(DELIMITER).append(dateChoose);
		if (OPTION_BEFORE.equals(dateChoose)) {
			sb.append(DELIMITER).append(sdf.format(befDate).toString());
		}
		if (OPTION_AFTER.equals(dateChoose)) {
			sb.append(DELIMITER).append(sdf.format(aftDate).toString());
		}
		if (OPTION_BETWEEN.equals(dateChoose)) {
			sb.append(DELIMITER).append(sdf.format(sDate).toString());
			sb.append(DELIMITER).append(sdf.format(eDate).toString());
		}
		return sb.toString();
	}

	@Override
	public String genDesc() {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (OPTION_BEFORE.equals(dateChoose)) {
			sb.append("日期 < ");
			sb.append(sdf.format(befDate).toString());
		} else if (OPTION_AFTER.equals(dateChoose)) {
			sb.append("日期 >= ");
			sb.append(sdf.format(aftDate).toString());
		} else if (OPTION_BETWEEN.equals(dateChoose)) {
			sb.append(sdf.format(sDate).toString());
			sb.append(" <= 日期 < ");
			sb.append(sdf.format(eDate).toString());
		}
		return sb.toString();
	}

	@Override
	public boolean matches(Map<String, Object> params) {
		Date date = (Date) params.get(RuleParamTypes.SHOW_TIME);
		if (date == null)
			return false;
		if (OPTION_BEFORE.equals(dateChoose))
			return date.getTime() < befDate.getTime();
		else if (OPTION_AFTER.equals(dateChoose))
			return date.getTime() >= aftDate.getTime();
		else if (OPTION_BETWEEN.equals(dateChoose))
			return date.getTime() >= sDate.getTime()
					&& date.getTime() < eDate.getTime();
		else
			return false;
	}
}
