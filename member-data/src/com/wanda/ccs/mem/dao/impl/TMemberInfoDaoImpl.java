package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;
import com.wanda.ccs.mem.dao.ITMemberAddrDao;
import com.wanda.ccs.model.TMemberAddr;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
/**
 * 会员住址
 * @author chenxm
 *
 */
@Repository
public class TMemberInfoDaoImpl extends BaseDaoImpl<TMemberAddr> implements
		ITMemberAddrDao {

}
