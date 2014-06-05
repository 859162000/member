package com.xcesys.extras.util;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.xcesys.extras.core.exception.ApplicationException;
import com.xcesys.extras.core.util.DateUtil;

public class CommonUtil {
	public static final List<String> getMonthList() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 12; i++) {
			list.add(i + 1 + "月");
		}
		return list;
	}

	/**
	 * 获得发行年月下拉列表
	 */
	public static final List<String> getYearList() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy");
		Integer year = NumberUtils.toInt(df.format(date));
		List<String> list = new ArrayList<String>();
		for (int i = year - 10; i < year + 2; i++) {
			list.add(i + "年");
		}
		return list;
	}

	/**
	 * 计算两个时间的分钟差值。
	 * 
	 * @param baseDate
	 * @param startTime
	 * @return
	 */
	public static long minutesDiff(Date baseDate, Date startTime) {
		if (baseDate == null || startTime == null) {
			throw new IllegalArgumentException("比较的两个时间均不能为NULL.");
		}
		// minutes diff
		long diff = (startTime.getTime() - baseDate.getTime()) / 1000 / 60;
		return diff;
	}

	//
	// public static final Map<String, Object> statusMap() {
	// Map<String, Object> map = new LinkedHashMap<String, Object>();
	// map.put("0", "未提交");
	// map.put("1", "未审核");
	// map.put("2", "已审核");
	// map.put("3", "已下发");
	// return map;
	// }
	//
	// public static final Map<Integer, Object> versionMap() {
	// Map<Integer, Object> map = new LinkedHashMap<Integer, Object>();
	// map.put(1, "初稿");
	// map.put(2, "定稿");
	// map.put(3, "修正稿");
	// return map;
	// }

	public static final Map<String, String> monthMap(int count) {
		if (count <= 0) {
			count = 5;
		}
		Date currentDate = DateUtil.getCurrentDate();
		currentDate = DateUtil.getMonthBegin(currentDate);
		// 下个月
		Map<String, String> monthMap = new LinkedHashMap<String, String>();
		for (int i = -4; i < count - 4; i++) {
			String v = DateUtil.formatDate(DateUtil.addMonths(currentDate, i),
					"yyyyMM");
			monthMap.put(v, v);
		}
		return monthMap;
	}

	/**
	 * 
	 * @param startMonth
	 *            起始月
	 * @param count
	 *            显示个数
	 * @return
	 */
	public static final Map<String, String> monthMap(String startMonth,
			int count) {

		Calendar cal = Calendar.getInstance();
		// 下个月
		if (StringUtils.isNotBlank(startMonth)) {
			cal.set(Calendar.MONTH,
					NumberUtils.toInt(StringUtils.right(startMonth, 2)));
		}
		cal.add(Calendar.MONTH, 1);
		Map<String, String> monthMap = new LinkedHashMap<String, String>();
		if (count < 0) {
			count = Math.abs(count);
		}
		for (int i = 0; i < count; i++) {
			String k = ("" + cal.get(Calendar.YEAR))
					+ StringUtils.leftPad("" + cal.get(Calendar.MONTH), 2, '0');
			String v = ("" + cal.get(Calendar.YEAR)) + "年"
					+ StringUtils.leftPad("" + cal.get(Calendar.MONTH), 2, '0')
					+ "月";
			monthMap.put(k, v);
			cal.add(Calendar.MONTH, -1);
		}
		return monthMap;

	}

	/**
	 * 取得下一个月的字符串(如：201110)。
	 * 
	 * @return
	 */
	public static final String nextMonth() {
		return nextMonth("");
	}

	/**
	 * 取得下一个月的字符串(如：201110或2011-10)，年月间分隔符分开，格式yyyy分隔符MM。
	 * 
	 * @return
	 */
	public static final String nextMonth(String separator) {
		Calendar cal = Calendar.getInstance();
		// 下个月
		cal.add(Calendar.MONTH, 1);
		return ("" + cal.get(Calendar.YEAR))
				+ StringUtils.defaultIfEmpty(separator, "")
				+ StringUtils.leftPad("" + (cal.get(Calendar.MONTH) + 1), 2,
						'0');
	}

	/**
	 * 为0时返回Null
	 * 
	 * @param v
	 * @return
	 */
	public static Long valueToNull(Long v) {
		if (v.longValue() == 0L) {
			return null;
		} else {
			return v;
		}
	}

	/**
	 * 为null时返回0
	 * 
	 * @param v
	 * @return
	 */
	public static int valueToZero(Integer v) {
		if (v == null) {
			return 0;
		} else {
			return v;
		}
	}

	/**
	 * 为Null时返回0
	 * 
	 * @param v
	 * @return
	 */
	public static long valueToZero(Long v) {
		if (v == null) {
			return 0L;
		} else {
			return v;
		}
	}

	/**
	 * 删除数组中空值元素。
	 * 
	 * @param values
	 * @return
	 */
	public static Long[] removeNull(Long[] values) {
		List<Long> hlist = new ArrayList<Long>();
		if (values != null) {
			for (Long hid : values) {
				if (hid != null) {
					hlist.add(hid);
				}
			}
		}
		return hlist.isEmpty() ? null : hlist.toArray(new Long[0]);
	}

	/**
	 * 为0时返回Null
	 * 
	 * @param v
	 * @return
	 */
	public static Long valueEmptyToNull(String v) {
		if (StringUtils.isBlank(v)) {
			return null;
		} else {
			return Long.parseLong(v);
		}
	}

	/**
	 * 分解排片指导精确信息为MAP键值对
	 * 
	 * @param exact
	 *            格式为:(1:3场,2:2-5场...)
	 * @return
	 */
	public static Map<String, String> splitScheduleGuideExact(String exact) {
		Map<String, String> exactMap = null;
		if (StringUtils.isNotBlank(exact) && !"null".equals(exact)) {
			exactMap = new HashMap<String, String>();
			String[] ss = StringUtils.split(exact, ',');
			if (ss != null && ss.length > 0) {
				for (String s : ss) {
					String[] ht = StringUtils.split(s, ':');
					if (ht == null || ht.length == 0) {
						throw new ApplicationException("排片指导精确型信息错误");
					} else if (ht.length == 1) {
						exactMap.put(ht[0], "");
					} else if (ht.length == 2) {
						exactMap.put(ht[0], ht[1]);
					}
				}
			}
		}
		return exactMap;
	}

	/**
	 * 比较一个数与一个数值范围
	 * 
	 * @param s
	 * @param range
	 * @return -1:小于最小值 0：在范围中 1:大于最大值
	 */
	public static int compareWithRange(String s, String range) {
		int i = 0;
		if (NumberUtils.isDigits(s)) {
			i = NumberUtils.toInt(s);
		}
		if (range.contains("-")) {
			String[] ranges = range.split("-");
			if (ranges != null && ranges.length == 2) {
				float min = NumberUtils.toFloat(ranges[0]);
				float max = NumberUtils.toFloat(ranges[1]);
				if (i > max) {
					return 1;
				}
				if (i < min) {
					return -1;
				}
			} else {
				throw new ApplicationException(
						"CommonUtil.compareWithRange方法中传入数值范围格式不正确:" + range);
			}
		} else {
			float r = NumberUtils.toFloat(range);
			if (i > r) {
				return 1;
			}
			if (i < r) {
				return -1;
			}
		}
		return 0;
	}

	/**
	 * 是否是UTF-8编码
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isUTF8String(String value) {
		boolean is = true;
		try {
			is = value.equals(new String(value.getBytes("UTF-8"), "UTF-8"));
		} catch (Exception e) {
		}
        return is;
	}

}
