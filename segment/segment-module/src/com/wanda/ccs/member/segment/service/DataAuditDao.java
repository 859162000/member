package com.wanda.ccs.member.segment.service;

import com.wanda.ccs.member.ap2in.UserProfile;
import com.wanda.ccs.member.segment.vo.BaseAuditVo;
import com.wanda.ccs.member.segment.vo.DataAuditVo;

public interface DataAuditDao {
	
	DataAuditVo addUpdating(String dataType, String actionName, BaseAuditVo vo);
	
	DataAuditVo addInserting(String dataType, String actionName, BaseAuditVo vo);
	
	DataAuditVo addDeleting(String dataType, String actionName, Long seqId, UserProfile user);
	
	DataAuditVo addLogicDeleting(String dataType, String actionName, Long seqId, UserProfile user);
	
}
