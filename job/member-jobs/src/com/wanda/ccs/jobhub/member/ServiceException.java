/**
 * 
 */
package com.wanda.ccs.jobhub.member;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Administrator
 *
 */
public class ServiceException extends NestedRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -141797227387549840L;

	/**
	 * @param msg
	 */
	public ServiceException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
	public ServiceException(Exception e){
		this(e.getMessage());
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public ServiceException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

}
