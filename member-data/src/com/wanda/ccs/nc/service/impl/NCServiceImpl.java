package com.wanda.ccs.nc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.model.TNcBdCorp;
import com.wanda.ccs.model.TNcBdDeptdoc;
import com.wanda.ccs.model.TNcBdPsnbasdoc;
import com.wanda.ccs.nc.service.NcService;
import com.xcesys.extras.core.dao.IUniversalDao;

@Service
public class NCServiceImpl implements NcService {

	@Autowired
	private IUniversalDao dao;

	/**
	 * 获取根组织结构
	 */
	@Override
	public List<TNcBdCorp> getRootCorp(String rootPkCorp) {
		String sql = "";
		Map<String, Object> params = new HashMap<String, Object>();
		if (rootPkCorp == null || rootPkCorp.equals("")) {
			sql = " select * from T_NC_BD_CORP c where c.father_corp is null";
		} else {
			if (rootPkCorp.length() < 4) {
				rootPkCorp = rootPkCorp
						+ String.format(
								"%" + String.valueOf(4 - rootPkCorp.length())
										+ "s", "");
			}
			params.put("pkCorp", rootPkCorp);
			sql = " select * from T_NC_BD_CORP c where c.pk_corp = :pkCorp";
		}
		List<TNcBdCorp> deptList = dao.queryNativeSQL(TNcBdCorp.class, sql,
				params, null);

		// 检验是否有子节点
		for (TNcBdCorp dept : deptList) {
			Map<String, Object> checkparams = new HashMap<String, Object>();
			checkparams.put("fatherCorp", dept.getPkCorp());
			String checksql = " select * from T_NC_BD_CORP c where c.father_corp = :fatherCorp";
			List<TNcBdCorp> checkdeptList = dao.queryNativeSQL(TNcBdCorp.class,
					checksql, checkparams, null);
			if (checkdeptList != null && !checkdeptList.isEmpty()) {
				dept.setHasChild(true);
			} else {
				dept.setHasChild(false);
			}
		}
		return deptList;
	}

	/**
	 * 根据上级组织机构获取组织结构
	 */
	@Override
	public List<TNcBdCorp> getCorpByFatherCorp(String pkFatherCorp) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (pkFatherCorp.length() < 4) {
			pkFatherCorp = pkFatherCorp
					+ String.format(
							"%" + String.valueOf(4 - pkFatherCorp.length())
									+ "s", "");
		}
		params.put("fatherCorp", pkFatherCorp);
		String sql = " select * from T_NC_BD_CORP c where c.father_corp = :fatherCorp";
		List<TNcBdCorp> deptList = dao.queryNativeSQL(TNcBdCorp.class, sql,
				params, null);

		// 检验是否有子节点
		for (TNcBdCorp dept : deptList) {
			params.put("fatherCorp", dept.getPkCorp());
			String checksql = " select * from T_NC_BD_CORP c where c.father_corp = :fatherCorp";
			List<TNcBdCorp> checkdeptList = dao.queryNativeSQL(TNcBdCorp.class,
					checksql, params, null);
			if (checkdeptList != null && checkdeptList.size() > 0) {
				dept.setHasChild(true);
			} else {
				dept.setHasChild(false);
			}
		}
		return deptList;
	}

	/**
	 * 获取部门的路径
	 * 
	 * @param pkDeptdoc
	 * @return
	 */
	@Override
	public String getDeptPath(String pkDeptdoc) {
		if (pkDeptdoc == null || pkDeptdoc.equals(""))
			return null;
		String deptPath = "";
		Map<String, Object> params = new HashMap<String, Object>();
		if (pkDeptdoc.length() < 4) {
			pkDeptdoc = pkDeptdoc
					+ String.format(
							"%" + String.valueOf(4 - pkDeptdoc.length()) + "s",
							"");
		}
		params.put("pkCorp", pkDeptdoc);
		String sql = "select * from T_NC_BD_CORP c where c.pk_corp = :pkCorp";
		List<TNcBdCorp> deptList = dao.queryNativeSQL(TNcBdCorp.class, sql,
				params, null);
		if (deptList != null && !deptList.isEmpty()) {
			deptPath = deptList.get(0).getUnitname();
			while (true) {
				if (deptList.get(0).getFatherCorp() == null
						|| deptList.get(0).getFatherCorp().equals(""))
					return deptPath;
				Map<String, Object> fatherParams = new HashMap<String, Object>();
				fatherParams.put("pkCorp", deptList.get(0).getFatherCorp());
				String fatherSql = "select * from T_NC_BD_CORP c where c.pk_corp = :pkCorp";
				deptList = dao.queryNativeSQL(TNcBdCorp.class, fatherSql,
						fatherParams, null);
				if (deptList != null && !deptList.isEmpty()) {
					deptPath = deptList.get(0).getUnitname() + "\\" + deptPath;
				}
			}

		}
		return deptPath;
	}

	/**
	 * 获取组织结构的根部门
	 * 
	 * @return
	 */
	public List<TNcBdDeptdoc> getRootDept(String pkCorp, String rootPkDept) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select * from T_NC_BD_DEPTDOC c where c.pk_corp = :pkCorp";
		params.put("pkCorp", pkCorp);
		if (rootPkDept == null || rootPkDept.equals("")) {
			sql += " and c.pk_fathedept is null";
		} else {
			if (rootPkDept.length() < 20) {
				rootPkDept = rootPkDept
						+ String.format(
								"%" + String.valueOf(20 - rootPkDept.length())
										+ "s", "");
			}
			params.put("pkDeptdoc", rootPkDept);
			sql += " and c.pk_deptdoc = :pkDeptdoc";
		}
		List<TNcBdDeptdoc> deptList = dao.queryNativeSQL(TNcBdDeptdoc.class,
				sql, params, null);
		for (TNcBdDeptdoc dept : deptList) {
			Map<String, Object> checkparams = new HashMap<String, Object>();
			checkparams.put("pkFathedept", dept.getPkDeptdoc());
			String checksql = " select * from T_NC_BD_DEPTDOC c where c.pk_fathedept = :pkFathedept";
			List<TNcBdDeptdoc> checkdeptList = dao.queryNativeSQL(
					TNcBdDeptdoc.class, checksql, checkparams, null);
			if (checkdeptList != null && checkdeptList.size() > 0) {
				dept.setHasChild(true);
			} else {
				dept.setHasChild(false);
			}
		}
		return deptList;
	}

	/**
	 * 根据上级组织机构获取组织结构
	 */
	@Override
	public List<TNcBdDeptdoc> getDeptByFatherDept(String pkFathedept) {
		if (pkFathedept == null || pkFathedept.equals(""))
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		if (pkFathedept.length() < 20) {
			pkFathedept = pkFathedept
					+ String.format(
							"%" + String.valueOf(20 - pkFathedept.length())
									+ "s", "");
		}
		params.put("pkFathedept", pkFathedept);
		String sql = " select * from T_NC_BD_DEPTDOC c where c.pk_fathedept = :pkFathedept";
		List<TNcBdDeptdoc> deptList = dao.queryNativeSQL(TNcBdDeptdoc.class,
				sql, params, null);
		for (TNcBdDeptdoc dept : deptList) {
			params.put("pkFathedept", dept.getPkDeptdoc());
			String checksql = " select * from T_NC_BD_DEPTDOC c where c.pk_fathedept = :pkFathedept";
			List<TNcBdDeptdoc> checkdeptList = dao.queryNativeSQL(
					TNcBdDeptdoc.class, checksql, params, null);
			if (checkdeptList != null && checkdeptList.size() > 0) {
				dept.setHasChild(true);
			} else {
				dept.setHasChild(false);
			}
		}
		return deptList;
	}

	@Override
	public TNcBdPsnbasdoc getTNcBdPsnbasdocByPsnbasdoc(String psnbasdoc) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pkPsnbasdoc", psnbasdoc);
		String sql = " select * from T_NC_BD_PSNBASDOC c where c.PK_PSNBASDOC = :pkPsnbasdoc";
		List<TNcBdPsnbasdoc> panbasdocList = dao.queryNativeSQL(TNcBdPsnbasdoc.class,
				sql, params, null);
		if(panbasdocList != null && !panbasdocList.isEmpty())
			return panbasdocList.get(0);
		return null;
	}

}
