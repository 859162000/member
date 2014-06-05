package com.wanda.ccs.jobhub.member.service.impl;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.dao.ChangeMemberPointDao;
import com.wanda.ccs.jobhub.member.service.ChangeMemberPointService;


public class ChangeMemberPointServiceImpl implements ChangeMemberPointService {

	@InstanceIn(path = "ChangeMemberPointDao")
	private ChangeMemberPointDao changeMemberPointDao;
	
	/* (non-Javadoc)
	 * @see com.wanda.ccs.jobhub.member.service.impl.ChangeMemberPointService#execute(java.lang.Long)
	 */
	public void execute(Long fileId) {
		changeMemberPointDao.getFile("com.wanda.ccs.model.TMemberPoint", "W",fileId);
	}

}
