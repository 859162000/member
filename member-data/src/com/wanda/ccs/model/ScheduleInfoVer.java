package com.wanda.ccs.model;

import org.apache.commons.lang.math.NumberUtils;


/**
 * 排片信息版本枚举类
 * @author Benjamin
 * @date 2011-12-8
 */
public enum ScheduleInfoVer {
	FIRST {
		@Override
		public String getReadName() {
			return "初稿";
		}

		@Override
		public ScheduleInfoVer next() {
			return FINAL;
		}
		@Override
		public ScheduleInfoVer prev() {
			throw new IllegalStateException(this + "状态为最低版，没有上一个版本。");
		}
	},
	FINAL {
		@Override
		public String getReadName() {
			return "正稿";
		}

		@Override
		public ScheduleInfoVer next() {
			return REVISED;
		}
		@Override
		public ScheduleInfoVer prev() {
			return FIRST;
		}
	},
	REVISED {
		@Override
		public String getReadName() {
			return "修正稿";
		}
		@Override
		public ScheduleInfoVer next() {
			throw new IllegalStateException(this + "状态为最大状态，没有下一个状态。");
		}
		@Override
		public ScheduleInfoVer prev() {
			return FINAL;
		}
	};
	
	public static ScheduleInfoVer fromOrdinal(int ordinal) {
		for (ScheduleInfoVer s : ScheduleInfoVer.values()) {
			if (ordinal-1 == s.ordinal()) {
				return s;
			}
		}
		return null;
	}

	public static ScheduleInfoVer fromOrdinal(String ordinal) {
		try {
			Integer i = NumberUtils.toInt(ordinal);
			return fromOrdinal(i);
		} catch (Exception e) {
		}
		return null;
	}

	public String getOrdinal() {
		return this.ordinal() + 1 + "";
	}


	/**
	 * 获取可读名称。
	 * 
	 * @return
	 */

	public abstract String getReadName();

	/**
	 * 获取正常状态流程的默认下一个状态。
	 * 
	 * @return
	 */
	public abstract ScheduleInfoVer next();
	/**
	 * 获取正常状态流程的默认上一个状态。
	 * 
	 * @return
	 */
	public abstract ScheduleInfoVer prev();
}
