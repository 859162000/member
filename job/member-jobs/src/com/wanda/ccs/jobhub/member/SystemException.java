/**
 * This class represents the unrecoverable system exception
 */
package com.wanda.ccs.jobhub.member;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Administrator
 *
 */
public class SystemException extends NestedRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9137517122391023237L;

	/**
	 * @param msg
	 */
	public SystemException(String msg) {
		super(msg);
	}
	
	public SystemException(Exception e){
		this(e.getMessage());
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public SystemException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
