package com.wanda.ccs.mbr.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.wanda.ccs.mbr.tag.service.TagBuildService;

public class TagBuildExector {
	static Log log = LogFactory.getLog(TagBuildExector.class);
	protected static ApplicationContext context= new FileSystemXmlApplicationContext("classpath*:spring/application*.xml");
	
	public static ApplicationContext getInstance() {
		return context;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TagBuildService tagBuildService  = context.getBean("tagBuildService",TagBuildService.class);
		try {
			tagBuildService.buildAbnormalConsumption();
			tagBuildService.buildEchannelPreference();
			tagBuildService.buildFamilyComposition();
			tagBuildService.buildFilmPreferences();
			tagBuildService.buildPriceSensitive();
		} catch (Exception e) {
			e.printStackTrace();
			log.equals(e.getMessage());
		}
		
	}

}
