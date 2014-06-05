package com.wanda.ccs.nc.service;

import java.util.List;

import com.wanda.ccs.model.TNcBdCorp;
import com.wanda.ccs.model.TNcBdDeptdoc;
import com.wanda.ccs.model.TNcBdPsnbasdoc;
/**
 * 
 * NC service
 * 
 * @author Yangjb
 * @date 2012-11-6
 */
public interface NcService {
	
	/**
	 * 获取根组织结构
	 * @return
	 */
	public List<TNcBdCorp> getRootCorp(String rootPkCorp);
	
	/**
	 * 根据上级组织结构获取组织结构
	 * @return
	 */
	public List<TNcBdCorp> getCorpByFatherCorp(String pkFatherCorp);
	
	/**
	 * 获取组织结构的路径
	 * @param pkDeptdoc
	 * @return
	 */
	public String getDeptPath(String pkDeptdoc);
	
	/**
	 * 获取组织结构的根部门
	 * @return
	 */
	public List<TNcBdDeptdoc> getRootDept(String pkCorp,String rootPkDept);
	
	/**
	 * 根据上级上级部门获取部门
	 * @return
	 */
	public List<TNcBdDeptdoc> getDeptByFatherDept(String pkFatherDept);
	
	/**
	 * 通过主键获取人员的基本信息
	 * @param psnbasdoc
	 * @return
	 */
	public TNcBdPsnbasdoc getTNcBdPsnbasdocByPsnbasdoc(String psnbasdoc);
	
}