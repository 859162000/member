package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.mem.dao.ITMemberExgPointDao;
import com.wanda.ccs.mem.dao.ITMemberLevelDao;
import com.wanda.ccs.model.TMemberExgPoint;
import com.wanda.ccs.model.TMemberLevel;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
/**
 * 会员级别
 * @author chenxm
 *
 */
@Repository
public class TMemberExgPointDaoImpl extends BaseDaoImpl<TMemberExgPoint> implements
		ITMemberExgPointDao{

}
