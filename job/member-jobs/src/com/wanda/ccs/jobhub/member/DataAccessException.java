/**
 * This class represents DataAccess layer exception
 */
package com.wanda.ccs.jobhub.member;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Administrator
 *
 */
public class DataAccessException extends NestedRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1389439792967051120L;

	/**
	 * @param msg
	 */
	public DataAccessException(String msg) {
		super(msg);
	}
	
	public DataAccessException(Exception e){
		this(e.getMessage());
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public DataAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
