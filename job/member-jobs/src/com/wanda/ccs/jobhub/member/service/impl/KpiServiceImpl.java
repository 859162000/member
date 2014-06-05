package com.wanda.ccs.jobhub.member.service.impl;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.dao.KpiDao;
import com.wanda.ccs.jobhub.member.service.KpiService;


public class KpiServiceImpl implements KpiService {

	@InstanceIn(path = "KpiDao")	
	private KpiDao kpiDao;
	
	/* (non-Javadoc)
	 * @see com.wanda.ccs.jobhub.member.service.impl.KpiService#execute(java.lang.Long)
	 */
	public void execute(Long fileId) {
		kpiDao.getFile("com.wanda.ccs.model.TMemberKPI", "W",fileId);
	}

}
