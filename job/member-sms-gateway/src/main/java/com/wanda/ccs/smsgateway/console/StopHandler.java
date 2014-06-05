package com.wanda.ccs.smsgateway.console;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class StopHandler extends IoHandlerAdapter {

	private static final Log logger = LogFactory.getLog(StopHandler.class);

	public StopHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org.apache
	 * .mina.core.session.IoSession, java.lang.Object)
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String mgs = (String) message;
		if (logger.isDebugEnabled())
			logger.debug("Socket messageReceived[" + mgs + "]");
		session.close(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.apache
	 * .mina.core.session.IoSession)
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("Socket sessionCreated");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#sessionIdle(org.apache.
	 * mina.core.session.IoSession, org.apache.mina.core.session.IdleStatus)
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("Socket sessionIdle");
		if (status == IdleStatus.READER_IDLE) {
			session.close(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#sessionOpened(org.apache
	 * .mina.core.session.IoSession)
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("Socket sessionOpened");
		session.write("4\n");
	}

}
