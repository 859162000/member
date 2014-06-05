package com.wanda.ccs.jobhub.smsreport.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * freemarker
 * 
 * @author zhurui
 * @date 2014年1月9日 上午9:50:49
 */
public class FreeMarkerUtil {
	
	public static final Logger logger = Logger.getLogger(FreeMarkerUtil.class);
	
	private Configuration config = null;
	
	private FreeMarkerUtil() {
		try {
			config = new Configuration();
			config.setClassForTemplateLoading(FreeMarkerUtil.class.getClassLoader().getClass(), "/sms_template");
			config.setLocale(Locale.CHINA);
			config.setDefaultEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class FreeMarkerUtilHandler {
		public static FreeMarkerUtil util = new FreeMarkerUtil();
	}
	
	public static FreeMarkerUtil getInstance() {
		return FreeMarkerUtilHandler.util;
	}
	
	public Configuration getConfig() {
		return config;
	}

	/**
	 * 根据模板生产内容
	 * 
	 * @param templateFileName
	 * @param propMap
	 * @return
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public synchronized String genFileString(String templateFileName,
			Map<String, Object> propMap) throws TemplateException, IOException {
		String result = null;

		if(config != null) {
			BufferedWriter out = null;
			try {
				Template t = config.getTemplate(templateFileName);
				StringWriter stringWriter = new StringWriter();
				out = new BufferedWriter(stringWriter);
				t.process(propMap, out);
				out.flush();
				result = stringWriter.toString();
			} finally {
				try {
					if (out != null) { out.close(); }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
	
	/*public static void main(String[] args) throws TemplateException, IOException {
		FreeMarkerUtil util = FreeMarkerUtil.getInstance();
		util.genFileString("template.ftl", new HashMap<String, Object>());
	}*/

}
