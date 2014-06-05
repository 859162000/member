package com.wanda.ccs.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.xcesys.extras.core.util.StringUtil;

/**
 * 状态控制的状态机枚举类。
 * 
 * @author danne
 */
public enum State {
	CANCEL {
		@Override
		public String getReadName() {
			return "已取消";
		}

		@Override
		public State next() {
			return SUBMIT;
		}
	},
	SAVED {
		public String getReadName() {
			return "未提交";
		}

		@Override
		public State next() {
			return SUBMIT;
		}
	},
	SUBMIT {
		@Override
		public String getReadName() {
			return "未审核";
		}

		@Override
		public State next() {
			return APPROVED;
		}
	},
	APPROVED {
		@Override
		public String getReadName() {
			return "已审核";
		}

		@Override
		public State next() {
			return PUBLISHED;
		}
	},
	REJECTED {
		@Override
		public String getReadName() {
			return "审核退回";
		}

		@Override
		public State next() {
			return SUBMIT;
		}
	},
	PUBLISHED {
		@Override
		public String getReadName() {
			return "已下发";
		}

		@Override
		public State next() {
			return LOCKED;
		}
	},
	LOCKED {
		@Override
		public String getReadName() {
			return "已锁定";
		}

		@Override
		public State next() {
			throw new IllegalStateException(this + "状态为最大状态，没有下一个状态。");
		}
	};

	static Map<State, EnumSet<State>> prevStateMap = new HashMap<State, EnumSet<State>>();
	static {
		prevStateMap.put(CANCEL, EnumSet.of(CANCEL, SAVED, SUBMIT));
		prevStateMap.put(SAVED, EnumSet.of(CANCEL, SAVED, REJECTED));
		prevStateMap.put(SUBMIT, EnumSet.of(CANCEL, SAVED, SUBMIT, REJECTED));
		prevStateMap.put(APPROVED, EnumSet.of(SUBMIT, APPROVED));
		prevStateMap.put(PUBLISHED, EnumSet.of(APPROVED, PUBLISHED));
		prevStateMap.put(REJECTED, EnumSet.of(SUBMIT, REJECTED));
		prevStateMap.put(LOCKED, EnumSet.of(PUBLISHED, LOCKED));
	}

	public static State fromOrdinal(int ordinal) {
		for (State s : State.values()) {
			if (ordinal == s.ordinal()) {
				return s;
			}
		}
		return null;
	}

	public static State fromOrdinal(String ordinal) {
		try {
			if (StringUtil.isNullOrBlank(ordinal)) {
				return State.SAVED;
			}
			Integer i = NumberUtils.toInt(ordinal);
			return fromOrdinal(i);
		} catch (Exception e) {
		}
		return State.SAVED;
	}

	/**
	 * 试图将状态转移到参数指定状态。
	 * 
	 * @param state
	 * @return
	 */
	public State desire(State state) {
		if (!isQualified(state)) {
			throw new IllegalStateException(this + "状态不能转换到下一个状态:" + state);
		}
		return state;
	}

	public String getOrdinal() {
		return "" + this.ordinal();
	}

	/**
	 * 获取满足前提条件的EnumSet.
	 * 
	 * @return
	 */
	public EnumSet<State> getQualifiedMap() {
		EnumSet<State> enumSet = prevStateMap.get(this);
		return enumSet == null ? EnumSet.noneOf(State.class) : enumSet;
	}

	/**
	 * 获取可读名称。
	 * 
	 * @return
	 */

	public abstract String getReadName();

	/**
	 * 判断参数Status是否满足当前Stutus的前提Status状态。
	 * 
	 * @param status
	 * @return
	 */
	public boolean isQualified(State status) {
		EnumSet<State> set = prevStateMap.get(status);
		return set != null && set.contains(this);
	}

	/**
	 * 获取正常状态流程的默认下一个状态。
	 * 
	 * @return
	 */
	public abstract State next();
}
