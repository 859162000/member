package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.mem.dao.ITMemTicketTransOrderDao;
import com.wanda.ccs.model.TTicketTransOrder;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
/**
 * 会员票房消费
 * @author chenxm
 *
 */
@Repository
public class TMemTicketTransOrderDaoImpl extends BaseDaoImpl<TTicketTransOrder> implements
		ITMemTicketTransOrderDao{

}
