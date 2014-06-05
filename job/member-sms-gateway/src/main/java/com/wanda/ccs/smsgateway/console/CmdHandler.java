package com.wanda.ccs.smsgateway.console;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.wanda.ccs.smsgateway.utils.OSUtils;

/**
 * 
 * <b>Application name:</b>SMS GateWay<br>
 * <b>class desc:</b> {} <br>
 * <b>Date:</b>2013-11-03<br>
 * 
 * @author yuchuang
 * @version 0.0.1
 */
public class CmdHandler extends IoHandlerAdapter {

	private static final Log logger = LogFactory.getLog(CmdHandler.class);

	public CmdHandler() {
	}

	public CmdHandler(Configuration configuration) {
		this.configuration = configuration;
	}

	private Configuration configuration;

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
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
		String _command = message.toString();
		if (_command.trim().equalsIgnoreCase("q")) {
			session.close(true);
			return;
		}
		StringBuffer _output = new StringBuffer(OSUtils.getEnterKey());
		if (NumberUtils.isNumber(_command)) {
			int _num = NumberUtils.toInt(_command);
			switch (_num) {
			case 1:
				_output.append(doCheckStatus());
				break;
			case 2: // 启动服务
				_output.append(doStart());
				break;
			case 3: // 停止服务
				_output.append(doStop());
				break;
			case 4:
				doClose();
				break;
			case 5:
				_output.append(commandDes());
				break;
			default:
				_output.append(errorDes());
				break;
			}
		} else {
			_output.append(errorDes());
		}
		_output.append(OSUtils.getEnterKey())
				.append(configuration.getString("command.info"))
				.append(OSUtils.getEnterKey());
		session.write(_output.toString());
	}

	/**
	 * 
	 * @function <h>{desc for this method}</h>
	 * @return
	 */
	private Object doStop() {
		StringBuffer sb = new StringBuffer();
		if (GateWayApp.getInstance().isStarted()) {
			try {
				GateWayApp.getInstance().stopApp();
				if (logger.isDebugEnabled())
					logger.debug("SMS GateWay is stoping");
				sb.append(configuration.getString("command.stop"));
			} catch (Exception e) {
				logger.error("doStop SMS GateWay has error", e);
				sb.append(OSUtils.getEnterKey())
						.append(configuration.getString("command.errsplit"))
						.append(OSUtils.getEnterKey()).append(e.getMessage())
						.append(configuration.getString("command.errsplit"));
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @function <h>{desc for this method}</h>
	 */
	private void doClose() {
		System.exit(0);
	}

	/**
	 * 
	 * @function <h>{desc for this method}</h>
	 * @param session
	 * @return
	 */
	private String doStart() {
		StringBuffer sb = new StringBuffer();
		if (GateWayApp.getInstance().isStarted()) {
			sb.append(configuration.getString("command.started"));
		} else {
			try {
				GateWayApp.getInstance().startApp();
				sb.append(configuration.getString("command.startok"))
						.append(OSUtils.getEnterKey())
						.append(configuration.getString("command.looklog"));
			} catch (Exception e) {
				logger.error("Start SMS GateWay has error", e);
				sb.append(configuration.getString("command.startfl"));
				sb.append(OSUtils.getEnterKey())
						.append(configuration.getString("command.errsplit"))
						.append(OSUtils.getEnterKey()).append(e.getMessage())
						.append(configuration.getString("command.errsplit"));
			}
		} // end_if
		return sb.toString();
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
		StringBuffer _output = new StringBuffer();
		_output.append(commandDes()).append(OSUtils.getEnterKey())
				.append(configuration.getString("command.info"))
				.append(OSUtils.getEnterKey()); 
		session.write(_output.toString());
	} // end_fun

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
		if (GateWayApp.getInstance().isStarted()) {
			StringBuffer sb = new StringBuffer();
			sb.append(configuration.getString("command.runsplit"))
					.append(OSUtils.getEnterKey())
					.append(GateWayApp.getInstance().getRunInfo())
					.append(OSUtils.getEnterKey())
					.append(configuration.getString("command.runsplit"));
			session.write(sb.toString());
		} // end_if
	} // end_fun

	/**
	 * 
	 * @function <h>{desc for this method}</h>
	 * @param session
	 * @return
	 */
	private String doCheckStatus() {
		StringBuffer sb = new StringBuffer();
		sb.append(configuration.getString("engine.run.status"));
		if (GateWayApp.getInstance().isStarted()) {
			sb.append(" Runing").append(OSUtils.getEnterKey());
			sb.append(configuration.getString("command.cfgsplit"))
					.append(OSUtils.getEnterKey())
					.append(GateWayApp.getInstance().getClientInfo())
					.append(configuration.getString("command.cfgsplit"))
					.append(OSUtils.getEnterKey());
			sb.append(configuration.getString("command.runsplit"))
					.append(OSUtils.getEnterKey())
					.append(GateWayApp.getInstance().getRunInfo())
					.append(OSUtils.getEnterKey())
					.append(configuration.getString("command.runsplit"));
		} else {
			sb.append(" Stopping");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @function <h>{desc for this method}</h>
	 * @return
	 */
	private String errorDes() {
		StringBuffer sb = new StringBuffer(
				configuration.getString("command.err")).append(OSUtils
				.getEnterKey());
		sb.append(commandDes());
		return sb.toString();
	}

	/**
	 * 
	 * @function <h>{desc for this method}</h>
	 * @return
	 */
	private String commandDes() {
		List<?> list = configuration.getList("command.des");
		StringBuffer sb = new StringBuffer();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			String cmd = (String) iterator.next();
			sb.append(cmd).append(OSUtils.getEnterKey());
		}
		return sb.toString();
	}

}
