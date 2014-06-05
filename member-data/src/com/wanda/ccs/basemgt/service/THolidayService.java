package com.wanda.ccs.basemgt.service;

import java.util.Date;

import com.wanda.ccs.model.THolidays;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 假期相关的业务逻辑Service.
 * 
 * @author Chen
 * @date 2011-10-21
 */
public interface THolidayService extends ICrudService<THolidays> {
	public THolidays findByDate(Date date);
}
