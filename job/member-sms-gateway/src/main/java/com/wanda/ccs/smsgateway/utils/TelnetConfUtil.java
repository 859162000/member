package com.wanda.ccs.smsgateway.utils;

import java.io.FileNotFoundException;
import java.net.URL;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;

public final class TelnetConfUtil {

	private static Configuration instance = null;

	/**
	 * 
	 * @function <h>{desc for this method}</h>
	 * @return
	 */
	synchronized public static Configuration getInstance() throws Exception {
		if (instance == null) {
			String resolvedLocation = SystemPropertyUtils
					.resolvePlaceholders("classpath:conf/telnet.properties");
			try {
				URL url = ResourceUtils.getURL(resolvedLocation);
				CompositeConfiguration composite  = new CompositeConfiguration();
				composite.addConfiguration(new PropertiesConfiguration(url));
				
				//组合一个SystemConfiguration来解决配置的系统变量的替换
				composite.addConfiguration(new SystemConfiguration());
				instance = composite;
			} catch (FileNotFoundException e) {
				System.err
						.println("[错误] 找不到文件 classpath:conf/telnet.properties");
				throw e;
			} catch (ConfigurationException e) {
				System.err
						.println("[错误] 初始化配置文件分析引擎PropertiesConfiguration发生异常");
				throw e;
			}
		}
		return instance;
	}
}
