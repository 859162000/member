package com.wanda.ccs.smsgateway.console;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.util.Log4jConfigurer;

import com.wanda.ccs.smsgateway.utils.TelnetConfUtil;

public class GateWayStop {

	private static final Log logger = LogFactory.getLog(GateWayStop.class);

	public GateWayStop() {
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

		try {
			int port = TelnetConfUtil.getInstance().getInt("listen.port",
					DEF_PORT);
			NioSocketConnector connector = new NioSocketConnector();
			// 30 秒等待超时设置
			connector.setConnectTimeoutMillis(30 * 1000L);
			connector.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(new TextLineCodecFactory(Charset
							.forName("UTF-8"))));
			connector.setHandler(new StopHandler());
			String ip = TelnetConfUtil.getInstance().getString("local.ip",
					DEF_IP);
			if (logger.isDebugEnabled())
				logger.debug("尝试链接 [" + ip + ":" + port + "]");
			ConnectFuture cf = connector
					.connect(new InetSocketAddress(ip, port));
			cf.awaitUninterruptibly();
			cf.getSession().getCloseFuture().awaitUninterruptibly();
			connector.dispose();
			System.out.println("关闭SMS GateWay成功");
		} catch (Exception e) {
			System.out.println("关闭SMS GateWay失败，请联系开发人员处理......");
		}finally{
			System.exit(0);
		}

	}

	public static int DEF_PORT = 9123;
	public static String DEF_IP = "127.0.0.1";

}
