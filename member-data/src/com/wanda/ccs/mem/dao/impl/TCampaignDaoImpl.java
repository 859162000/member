package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;
import com.wanda.ccs.mem.dao.ITCampaignDao;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
import com.wanda.ccs.model.TCampaign;

@Repository
public class TCampaignDaoImpl extends BaseDaoImpl<TCampaign> implements
		ITCampaignDao {

}
