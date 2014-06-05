package com.wanda.ccs.intf.dao.impl;


import org.springframework.stereotype.Repository;

import com.wanda.ccs.intf.dao.ITIntfClientinfoDao;
import com.wanda.ccs.model.TIntfClientinfo;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;

@Repository
public class TIntfClientinfoDaoImpl extends BaseDaoImpl<TIntfClientinfo> implements
		ITIntfClientinfoDao {

}

