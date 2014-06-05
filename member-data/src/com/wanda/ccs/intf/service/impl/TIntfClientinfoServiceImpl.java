package com.wanda.ccs.intf.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.intf.dao.ITIntfClientinfoDao;
import com.wanda.ccs.intf.service.TIntfClientinfoService;
import com.wanda.ccs.model.TIntfClientinfo;
import com.wanda.ccs.schedule.dao.ITTaskOutRealTimeDao;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TIntfClientinfoServiceImpl extends
		BaseCrudServiceImpl<TIntfClientinfo> implements TIntfClientinfoService {

	@Autowired
	private ITIntfClientinfoDao dao = null;
	@Autowired
	private ITTaskOutRealTimeDao taskOutRealTimeDao;
	
	@Override
	public IBaseDao<TIntfClientinfo> getDao() {
		return dao;
	}

	@Override
	public PageResult<TIntfClientinfo> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = " select c from TIntfClientinfo c";
		String[] whereBodies = new String[] { 
				"c.cinemacode = :cinemacode"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<TIntfClientinfo> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public boolean checkConnuser(Long clientid, String connuser) {
		if(connuser == null || connuser.equals(""))
			return false;
		String hql = "from TIntfClientinfo c where c.connuser = :connuser";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("connuser", connuser);
		if(clientid != null){
			hql += " and c.clientid != :clientid";
			params.put("clientid", clientid);
		}
		if(super.queryCountHql(hql, params, null) > 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean checkIp(Long clientid,String ip) {
		if(ip == null || ip.equals(""))
			return false;
		String hql = "from TIntfClientinfo c where c.ipaddress = :ipaddress";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ipaddress", ip);
		if(clientid != null){
			hql += " and c.clientid != :clientid";
			params.put("clientid", clientid);
		}
		if(super.queryCountHql(hql, params, null) > 0){
			return true;
		}
		return false;
	}

	@Override
	public void publish(String cinemacode) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("cinemacode", cinemacode);
		
		// t_task_out_status
		String hql = "delete from t_task_out_status where code in :cinemacode";
		getDao().updateNativeSQL(hql, params);
		
		// delete t_task_out_realtime
		hql = "delete from t_task_out_realtime where code in :cinemacode";
		getDao().updateNativeSQL(hql, params);
		
		// insert t_task_out_realtime
		taskOutRealTimeDao.create(cinemacode);
		
		// 
		hql = "update t_intf_clientinfo set isopen=1 where cinemacode in :cinemacode";
		getDao().updateNativeSQL(hql, params);
	}

}
