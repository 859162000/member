package com.wanda.ccs.data.price;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class RuleCondTimeRange extends RuleCond {
	static final String OPTION_BEFORE = "B";
	static final String OPTION_AFTER = "A";
	static final String OPTION_BETWEEN = "M";
	static final String OPTION_NO = "N";

	private int befHour; // 之前时间的几点(小时)
	private int befMinute; // 之前时间的几点(分钟)
	private int aftHour; // 之后时间的几点(小时)
	private int aftMinute; // 之后时间的几点(分钟)
	private int sHour; // 时间点限制的开始时间(小时)
	private int sMinute; // 时间点限制的开始时间(分钟)
	private int eHour; // 时间点限制的结束时间(小时)
	private int eMinute; // 时间点限制的结束时间(分钟)
	private String rangeChoose; // 判断选择的RADIO

	public RuleCondTimeRange(EnumRuleCondType dim) {
		super(dim);
		rangeChoose = OPTION_BEFORE;
	}

	@Override
	public boolean hasValue() {
		return rangeChoose != null && !OPTION_NO.equals(rangeChoose);
	}

	public int getsHour() {
		return sHour;
	}

	public void setsHour(int sHour) {
		this.sHour = sHour;
	}

	public int getsMinute() {
		return sMinute;
	}

	public void setsMinute(int sMinute) {
		this.sMinute = sMinute;
	}

	public int geteHour() {
		return eHour;
	}

	public void seteHour(int eHour) {
		this.eHour = eHour;
	}

	public int geteMinute() {
		return eMinute;
	}

	public void seteMinute(int eMinute) {
		this.eMinute = eMinute;
	}

	public String getRangeChoose() {
		return rangeChoose;
	}

	public void setRangeChoose(String rangeChoose) {
		this.rangeChoose = rangeChoose;
	}

	public int getBefHour() {
		return befHour;
	}

	public void setBefHour(int befHour) {
		this.befHour = befHour;
	}

	public int getBefMinute() {
		return befMinute;
	}

	public void setBefMinute(int befMinute) {
		this.befMinute = befMinute;
	}

	public int getAftHour() {
		return aftHour;
	}

	public void setAftHour(int aftHour) {
		this.aftHour = aftHour;
	}

	public int getAftMinute() {
		return aftMinute;
	}

	public void setAftMinute(int aftMinute) {
		this.aftMinute = aftMinute;
	}

	@Override
	public boolean parseRule(String rule) {
		if (rule == null)
			return false;
		if (!rule.startsWith(dim.name() + DELIMITER))
			return false;
		rule = rule.substring(dim.name().length() + DELIMITER.length());

		if (rule.startsWith(OPTION_BEFORE)) {
			rangeChoose = rule.substring(0, 1);
			rule = rule.substring(1 + DELIMITER.length());
			String[] list = rule.split(DELIMITER);
			befHour = Integer.parseInt(list[0]);
			befMinute = Integer.parseInt(list[1]);
		}
		if (rule.startsWith(OPTION_AFTER)) {
			rangeChoose = rule.substring(0, 1);
			rule = rule.substring(1 + DELIMITER.length());
			String[] list = rule.split(DELIMITER);
			aftHour = Integer.parseInt(list[0]);
			aftMinute = Integer.parseInt(list[1]);
		}
		if (rule.startsWith(OPTION_BETWEEN)) {
			rangeChoose = rule.substring(0, 1);
			rule = rule.substring(1 + DELIMITER.length());
			String[] list = rule.split(DELIMITER);
			sHour = Integer.parseInt(list[0]);
			sMinute = Integer.parseInt(list[1]);
			eHour = Integer.parseInt(list[2]);
			eMinute = Integer.parseInt(list[3]);
		}
		return true;
	}

	@Override
	public String genRule() {
		StringBuffer sb = new StringBuffer();
		sb.append(dim.name());
		sb.append(DELIMITER).append(rangeChoose);
		if (OPTION_BEFORE.equals(rangeChoose)) {
			sb.append(DELIMITER).append(befHour);
			sb.append(DELIMITER).append(befMinute);
		}
		if (OPTION_AFTER.equals(rangeChoose)) {
			sb.append(DELIMITER).append(aftHour);
			sb.append(DELIMITER).append(aftMinute);
		}
		if (OPTION_BETWEEN.equals(rangeChoose)) {
			sb.append(DELIMITER).append(sHour);
			sb.append(DELIMITER).append(sMinute);
			sb.append(DELIMITER).append(eHour);
			sb.append(DELIMITER).append(eMinute);
		}
		return sb.toString();
	}

	@Override
	public String genDesc() {
		StringBuffer sb = new StringBuffer();
		if (OPTION_BEFORE.equals(rangeChoose)) {
			sb.append("时间 < ");
			sb.append(befHour);
			sb.append("点");
			if (befMinute > 0) {
				sb.append(befMinute);
				sb.append("分");
			}
		} else if (OPTION_AFTER.equals(rangeChoose)) {
			sb.append("时间 >= ");
			sb.append(aftHour);
			sb.append("点");
			if (aftMinute > 0) {
				sb.append(aftMinute);
				sb.append("分");
			}
		} else if (OPTION_BETWEEN.equals(rangeChoose)) {
			sb.append(sHour);
			sb.append("点");
			if (sMinute > 0) {
				sb.append(sMinute);
				sb.append("分");
			}
			sb.append(" <= 时间 < ");
			if (sHour * 100 + sMinute > eHour * 100 + eMinute)
				sb.append("第二天");
			sb.append(eHour);
			sb.append("点");
			if (eMinute > 0) {
				sb.append(eMinute);
				sb.append("分");
			}
		}
		return sb.toString();
	}

	@Override
	public boolean matches(Map<String, Object> params) {
		Date date = (Date) params.get(RuleParamTypes.SHOW_TIME);
		if (date == null)
			return false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int time = cal.get(Calendar.HOUR_OF_DAY) * 100
				+ cal.get(Calendar.MINUTE);

		if (OPTION_BEFORE.equals(rangeChoose)) {
			return time < befHour * 100 + befMinute;
		} else if (OPTION_AFTER.equals(rangeChoose)) {
			return time >= aftHour * 100 + aftMinute;
		} else if (OPTION_BETWEEN.equals(rangeChoose)) {
			int st = sHour * 100 + sMinute;
			int ed = eHour * 100 + eMinute;
			if (st < ed)
				return time >= st && time < ed;
			else
				return time >= st || time < ed;
		} else
			return false;
	}
}
