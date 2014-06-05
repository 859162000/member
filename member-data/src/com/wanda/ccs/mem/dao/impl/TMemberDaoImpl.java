package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.mem.dao.ITMemberDao;
import com.wanda.ccs.model.TMember;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
/**
 * 会员
 * @author chenxm
 *
 */
@Repository
public class TMemberDaoImpl extends BaseDaoImpl<TMember> implements
		ITMemberDao {

}
