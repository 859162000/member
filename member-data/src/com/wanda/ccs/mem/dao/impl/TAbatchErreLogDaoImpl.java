package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.mem.dao.ITAbatchErreLogDao;
import com.wanda.ccs.model.TAbatchErreLog;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;
/**
 * 批量导入错误信息日志
 * @author chenxm
 *
 */
@Repository
public class TAbatchErreLogDaoImpl extends BaseDaoImpl<TAbatchErreLog> implements
		ITAbatchErreLogDao {

}
