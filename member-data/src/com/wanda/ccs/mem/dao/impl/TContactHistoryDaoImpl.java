package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;
import com.wanda.ccs.mem.dao.ITContactHistoryDao;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
import com.wanda.ccs.model.TContactHistory;

@Repository
public class TContactHistoryDaoImpl extends BaseDaoImpl<TContactHistory>
		implements ITContactHistoryDao {

}
