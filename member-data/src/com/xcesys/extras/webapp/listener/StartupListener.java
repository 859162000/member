package com.xcesys.extras.webapp.listener;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wanda.ccs.basemgt.service.TDimTypeDefService;
import com.wanda.ccs.service.SpringCommonService;
import com.xcesys.extras.util.SpringContextUtil;

/**
 * 
 * Initializing application scope parameters.
 * 
 * @author Danne Leung
 * 
 */
public class StartupListener implements ServletContextListener {
	private static final String PARAM_DEBUG_MODE = "debugMode";
	private static final Log log = LogFactory.getLog(StartupListener.class);
	private static Thread loadDimThread = null;

	/**
	 * Shutdown servlet context (currently a no-op method).
	 * 
	 * @param servletContextEvent
	 *            The servlet context event
	 */
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// LogFactory.release(Thread.currentThread().getContextClassLoader());
		// Commented out the above call to avoid warning when SLF4J in
		// classpath.
		// WARN: The method class
		// org.apache.commons.logging.impl.SLF4JLogFactory#release() was
		// invoked.
		// WARN: Please see http://www.slf4j.org/codes.html for an explanation.
		loadDimThread = null;
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if ("RMI Reaper".equals(t.getName())) {
				t.interrupt();
			}
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.info("Initializing web context ...");
		final ServletContext context = event.getServletContext();
		String dm = context.getInitParameter(PARAM_DEBUG_MODE);
		boolean debug = (dm == null || "".equals(dm.trim())) ? false : Boolean
				.valueOf(dm);
		context.setAttribute("debug", debug);
		if (debug) {
			log.info("System is running in debug model ... ");
		}

		Map<String, Map<String, String>> defs = SpringCommonService
				.getAllDimDefs();
		context.setAttribute("DIMS", defs);

		final TDimTypeDefService dimTypeDefService = SpringContextUtil
				.getBean(TDimTypeDefService.class);
		final Object[] countAndUpd = dimTypeDefService
				.obtainTDimDefCountAndUpdateDate();

		final long sleep = 1000 * 5;

		loadDimThread = new Thread() {
			public void run() {
				// 上次总数和更新时间
				long orig_count = (Long) countAndUpd[0];
				Date orig_updatedatetime = (Date) countAndUpd[1];
				long orig_updatetime = orig_updatedatetime == null ? 0
						: orig_updatedatetime.getTime();

				// 无限循环
				while (loadDimThread != null) {
					try {
						loadDimThread.sleep(sleep);

						// 最新总数和更新时间
						Object[] countAndUpd = dimTypeDefService
								.obtainTDimDefCountAndUpdateDate();
						long current_count = (Long) countAndUpd[0];
						Date current_updatedatetime = (Date) countAndUpd[1];
						long current_updatetime = current_updatedatetime == null ? 0
								: current_updatedatetime.getTime();

						// 比较上述两次值
						if (orig_count != current_count
								|| (orig_updatetime != current_updatetime)) {
							Map<String, Map<String, String>> defs = SpringContextUtil
									.getBean(TDimTypeDefService.class).getAllDimDefs(Boolean.FALSE);
							context.setAttribute("DIMS", defs);
							orig_count = current_count;
							orig_updatetime = current_updatetime;
							log.info("重新加载维数据完毕");
						} else {
							// log.info("维数据无更新，故无需重新加载");
						}

					} catch (Exception e) {
						e.printStackTrace();
						if (log.isDebugEnabled()) {
							log.error(e);
						}
					}
				}
			}
		};
		loadDimThread.setDaemon(true);
		loadDimThread.start();

		if (log.isDebugEnabled()) {
		}
		log.info("Initialized extras web context successfully.");
	}
}
