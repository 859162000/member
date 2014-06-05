package com.wanda.ccs.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.xcesys.extras.core.util.StringUtil;

/**
 * 卖品有审批流程的单据状态枚举类
 * 
 * @author xiaofeng
 *
 */
public enum EnumConState {

	CANCEL {
		@Override
		public String getReadName() {
			return "已撤销";
		}

		@Override
		public EnumConState next() {
			return SUBMIT;
		}
	},
	SAVED {
		public String getReadName() {
			return "草稿";
		}

		@Override
		public EnumConState next() {
			return SUBMIT;
		}
	},
	SUBMIT {
		@Override
		public String getReadName() {
			return "已提交";
		}

		@Override
		public EnumConState next() {
			return APPROVED;
		}
	},
	APPROVED {
		@Override
		public String getReadName() {
			return "已审批";
		}

		@Override
		public EnumConState next() {
			throw new IllegalStateException(this + "状态为最大状态，没有下一个状态。");
		}
	},
	REJECTED {
		@Override
		public String getReadName() {
			return "已拒绝";
		}

		@Override
		public EnumConState next() {
			return SUBMIT;
		}
	};

	static Map<EnumConState, EnumSet<EnumConState>> prevStateMap = new HashMap<EnumConState, EnumSet<EnumConState>>();
	static {
		prevStateMap.put(CANCEL, EnumSet.of(CANCEL, SAVED, SUBMIT));
		prevStateMap.put(SAVED, EnumSet.of(CANCEL, SAVED, REJECTED));
		prevStateMap.put(SUBMIT, EnumSet.of(CANCEL, SAVED, SUBMIT, REJECTED));
		prevStateMap.put(APPROVED, EnumSet.of(SUBMIT, APPROVED));
		prevStateMap.put(REJECTED, EnumSet.of(SUBMIT, REJECTED));
	}

	public static EnumConState fromOrdinal(int ordinal) {
		for (EnumConState s : EnumConState.values()) {
			if (ordinal == s.ordinal()) {
				return s;
			}
		}
		return null;
	}

	public static EnumConState fromOrdinal(String ordinal) {
		try {
			if (StringUtil.isNullOrBlank(ordinal)) {
				return EnumConState.SAVED;
			}
			Integer i = NumberUtils.toInt(ordinal);
			return fromOrdinal(i);
		} catch (Exception e) {
		}
		return EnumConState.SAVED;
	}

	/**
	 * 试图将状态转移到参数指定状态。
	 * 
	 * @param EnumConState
	 * @return
	 */
	public EnumConState desire(EnumConState state) {
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
	public EnumSet<EnumConState> getQualifiedMap() {
		EnumSet<EnumConState> enumSet = prevStateMap.get(this);
		return enumSet == null ? EnumSet.noneOf(EnumConState.class) : enumSet;
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
	public boolean isQualified(EnumConState status) {
		EnumSet<EnumConState> set = prevStateMap.get(status);
		return set != null && set.contains(this);
	}

	/**
	 * 获取正常状态流程的默认下一个状态。
	 * 
	 * @return
	 */
	public abstract EnumConState next();

}
