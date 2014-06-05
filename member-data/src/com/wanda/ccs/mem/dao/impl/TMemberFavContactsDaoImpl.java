package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.mem.dao.ITMemFavContactsDao;
import com.wanda.ccs.model.TMemFavContact;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
/**
 * 会员
 * @author chenxm
 *
 */
@Repository
public class TMemberFavContactsDaoImpl extends BaseDaoImpl<TMemFavContact> implements
		ITMemFavContactsDao {

}
