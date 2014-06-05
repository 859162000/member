package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.mem.dao.ITMemberPointDao;
import com.wanda.ccs.model.TMemberPoint;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
/**
 * 会员积分
 * @author chenxm
 *
 */
@Repository
public class TMemberPointDaoImpl extends BaseDaoImpl<TMemberPoint> implements
		ITMemberPointDao{

}
