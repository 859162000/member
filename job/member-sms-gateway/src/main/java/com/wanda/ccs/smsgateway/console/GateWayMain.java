package com.wanda.ccs.smsgateway.console;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.util.Log4jConfigurer;

import com.wanda.ccs.smsgateway.utils.TelnetConfUtil;

/**
 * 
 * <b>Application name:</b>SMS GateWay<br>
 * <b>class desc:</b> {} <br>
 * <b>Date:</b>2013-11-03<br>
 * 
 * @author yuchuang
 * @version 0.0.1
 */
public class GateWayMain {

	private static final Log logger = LogFactory.getLog(GateWayMain.class);

	public GateWayMain() {
	}

	/**
	 * 
	 * @function <h>{desc for this method}</h>
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Log4jConfigurer
					.initLogging("classpath:conf/log4j.properties");
			if (logger.isDebugEnabled())
				logger.debug("[日志] 初始化日志引擎成功");
		} catch (FileNotFoundException e) {
			System.err
					.println("[错误] 找不到 classpath:conf/log4j.properties 文件，初始化日志引擎失败");
			System.exit(0);
		} // end_try

		try {
			TelnetConfUtil.getInstance();
			if (logger.isDebugEnabled())
				logger.debug("[日志] 读取配置文件成功");
		} catch (Exception e) {
			System.err.println("[错误] 读取配置文件失败");
			logger.error("[错误] 读取配置文件失败", e);
			System.exit(0);
		}

		int port = 9123;
		try {
			port = TelnetConfUtil.getInstance().getInt("listen.port", 9123);
			IoAcceptor acceptor = new NioSocketAcceptor();
			acceptor.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(new TextLineCodecFactory(Charset
							.forName("UTF-8"))));
			acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.setHandler(new CmdHandler(TelnetConfUtil.getInstance()));
			acceptor.getSessionConfig().setReadBufferSize(1024);
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 100);
			acceptor.bind(new InetSocketAddress(port));
			if (logger.isDebugEnabled())logger.debug("[日志] 初始化配置成功，开始监听(" + port + ")端口");
		} catch (Exception e) {
			System.err.println("[错误] (" + port + ")端口被占用或者已经启动，请联系开发人人员修改端口");
			logger.error("[错误] (" + port + ")端口被占用或者已经启动，请联系开发人人员修改端口", e);
			System.exit(0);
		}

		if (!GateWayApp.getInstance().isStarted()) {
			try {
				GateWayApp.getInstance().startApp();
				if (logger.isInfoEnabled())
					logger.info("[日志] 启动引擎成功.....");
			} catch (Exception e) {
				System.err.println("[错误] 启动引擎发生异常，请查看日志文件");
				logger.error("[错误] 启动引擎发生异常，请查看日志文件", e);
				System.exit(0);
			}
		}
	}

}
