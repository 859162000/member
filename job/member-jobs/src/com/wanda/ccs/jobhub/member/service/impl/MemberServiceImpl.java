package com.wanda.ccs.jobhub.member.service.impl;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.dao.MemberDao;
import com.wanda.ccs.jobhub.member.service.MemberService;

public class MemberServiceImpl implements MemberService {

	@InstanceIn(path = "MemberDao")
	private MemberDao memberDao;
	
	/* (non-Javadoc)
	 * @see com.wanda.ccs.jobhub.member.service.impl.MemberService#execute(java.lang.Long)
	 */
	public void execute(Long fileId) {
		memberDao.getFile("com.wanda.ccs.model.TMember", "W",fileId);
	}

}
