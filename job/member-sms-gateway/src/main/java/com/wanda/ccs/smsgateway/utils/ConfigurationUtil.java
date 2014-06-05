package com.wanda.ccs.smsgateway.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ConfigurationUtil extends PropertiesConfiguration implements
		InitializingBean {

	private static final Log logger = LogFactory
			.getLog(ConfigurationUtil.class);

	public ConfigurationUtil() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(resource,
				"please init config file path for this class in Spring Ioc");
		Assert.isTrue(resource.exists(),
				"config can't load file,because it is not exits");
		try {
			super.load(resource.getFile());
			if(this.getProperty("GATEWAY_PATH")==null){
				this.setProperty("GATEWAY_PATH", System.getProperty("GATEWAY_PATH"));
			}
			if (logger.isInfoEnabled()) {
				logger.info("日志：加载参数配置成功 ["
						+ resource.getFile().getAbsolutePath() + "]");
			}
		} catch (ConfigurationException e) {
			logger.error("日志：读取配置文件发送异常", e);
			throw e;
		}
	} // end_fun

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	/** 资源文件句柄 */
	private Resource resource;

} // end_fun
