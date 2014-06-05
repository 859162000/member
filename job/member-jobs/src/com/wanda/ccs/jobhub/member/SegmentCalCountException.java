package com.wanda.ccs.jobhub.member;

/**
 * 客群计算失败异常
 * @author Charlie Zhang
 *
 */
public class SegmentCalCountException extends RuntimeException {

	private static final long serialVersionUID = -7837298803061334150L;

	public SegmentCalCountException() {
		super();
	}

	public SegmentCalCountException(String message) {
		super(message);
	}

	public SegmentCalCountException(Throwable cause) {
		super(cause);
	}

	public SegmentCalCountException(String message, Throwable cause) {
		super(message, cause);
	}

//	public SegmentCalCountException(String message, Throwable cause,
//			boolean enableSuppression, boolean writableStackTrace) {
//		super(message, cause, enableSuppression, writableStackTrace);
//	}

}
