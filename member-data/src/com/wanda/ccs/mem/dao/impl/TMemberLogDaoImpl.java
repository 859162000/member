package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.mem.dao.ITMemberLogDao;
import com.wanda.ccs.model.TMemberLog;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
/**
 * 会员日志
 * @author chenxm
 *
 */
@Repository
public class TMemberLogDaoImpl extends BaseDaoImpl<TMemberLog> implements
		ITMemberLogDao{

}
