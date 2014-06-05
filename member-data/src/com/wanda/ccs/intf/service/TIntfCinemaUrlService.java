package com.wanda.ccs.intf.service;

import com.wanda.ccs.model.TIntfCinemaUrl;
import com.xcesys.extras.core.service.ICrudService;

public interface TIntfCinemaUrlService extends ICrudService<TIntfCinemaUrl> {
	
	public TIntfCinemaUrl getUrlByCinemaId(Long cinemaId);

}
