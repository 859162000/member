package com.wanda.ccs.intf.service;

import com.wanda.ccs.model.TIntfClientinfo;
import com.xcesys.extras.core.service.ICrudService;

public interface TIntfClientinfoService extends ICrudService<TIntfClientinfo> {
	public boolean checkConnuser(Long clientid,String connuser);
	public boolean checkIp(Long clientid, String ip);
	/**
	 * 下发基础数据
		delete from t_task_out_status where code in ('8位影城编码');
		delete from t_task_out_realtime where code in ('8位影城编码');
		INSERT INTO T_TASK_OUT_REALTIME (SEQID,CODE,CREATE_DATE,TASK_STATUS) VALUES (seqid,'8位影城编码',sysdate,'0');
		update t_intf_clientinfo set isopen=1 where cinemacode in ('8位影城编码');
		
	 * @param cinemacode
	 */
	public void publish(String cinemacode);
}
