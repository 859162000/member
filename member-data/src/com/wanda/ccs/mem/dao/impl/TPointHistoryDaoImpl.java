package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;
import com.wanda.ccs.mem.dao.ITPointHistoryDao;
import com.wanda.ccs.model.TPointHistory;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
/**
 * 会员积分历史
 * @author chenxm
 *
 */
@Repository
public class TPointHistoryDaoImpl extends BaseDaoImpl<TPointHistory> implements
		ITPointHistoryDao {

}
