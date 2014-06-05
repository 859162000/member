package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.mem.dao.ITLevelHistoryDao;
import com.wanda.ccs.model.TLevelHistory;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
/**
 * 会员等级历史
 * @author chenxm
 *
 */
@Repository
public class TLevelHistoryDaoImpl extends BaseDaoImpl<TLevelHistory> implements
		ITLevelHistoryDao {

}
