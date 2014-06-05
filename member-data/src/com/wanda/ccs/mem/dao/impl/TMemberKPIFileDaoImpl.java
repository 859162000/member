package com.wanda.ccs.mem.dao.impl;

import org.springframework.stereotype.Repository;

import com.wanda.ccs.mem.dao.ITMemberKPIFileDao;
import com.wanda.ccs.model.TFileAttach;
import com.xcesys.extras.core.dao.impl.BaseDaoImpl;

/**
 * kpi导入dao接口实现类
 * @author yaoguoqing
 *
 */
@Repository
public class TMemberKPIFileDaoImpl extends  BaseDaoImpl<TFileAttach> implements ITMemberKPIFileDao{

}
