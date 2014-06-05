package com.wanda.ccs.auth.service;





import java.util.List;
import java.util.Map;

import com.wanda.ccs.model.TAuthModule;
import com.xcesys.extras.core.service.ICrudService;

/**
 * 
 * 模块service
 * @author Yang
 * @date 2012-2-1
 */
public interface TAuthModuleService extends ICrudService<TAuthModule> {
	
	public Map<Long , Map<Long, String>> getSystemModule();
	
	public List<TAuthModule> getModuleBySystemId(Long systemId);
	
}
