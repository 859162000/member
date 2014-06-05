package com.wanda.ccs.jobhub.member.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.dao.ActTargetDao;
import com.wanda.ccs.jobhub.member.dao.CreateContactHistoryDao;
import com.wanda.ccs.jobhub.member.service.CreateContactHistoryService;
import com.wanda.ccs.jobhub.member.vo.TargetVo;


/**
 * @author YangJb
 *
 */
public class CreateContactHistoryServiceImpl implements CreateContactHistoryService {

	@InstanceIn(path = "CreateContactHistoryDao")
	private CreateContactHistoryDao createContactHistoryDAO;

	@InstanceIn(path = "ActTargetDao")	
	private ActTargetDao actTargetDAO;

	public TargetVo getTargetVo(Long actTargetId) {
		return actTargetDAO.getTargetVo(actTargetId);
	}

	@Transactional
	public void saveContactHistory(TargetVo targetVo, String userId) {
		if(targetVo != null){
			//删除历史生成记录
			createContactHistoryDAO.delContactHistory(targetVo.getActTargetId().toString());
			
			//受众数量
			int maxCount = Integer.valueOf(targetVo.getMaxCount().toString());
			//控制组数量
			int controlCount = Integer.valueOf(targetVo.getControlCount().toString());
			
			createContactHistoryDAO.saveContactHistoryFromTarget(targetVo,userId, maxCount,controlCount);
	    }
	}
	
}
