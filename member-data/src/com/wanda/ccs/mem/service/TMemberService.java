package com.wanda.ccs.mem.service;

import com.aggrepoint.adk.IModuleRequest;
import com.wanda.ccs.model.TMember;
import com.xcesys.extras.core.service.ICrudService;

public interface TMemberService extends ICrudService<TMember> {
	/**
	 * 逻辑删除会员
	 * @param ids
	 */
	public void deleteTMembers(Long[] ids);
	/**
	 * 检验手机号是否重复
	 * @param mobileNo
	 * 手机号
	 * @return
	 */
	public boolean checkMobile(String mobileNo,Long status);
	/**
	 * 获取会员编码最大值，按规则生成编码
	 * @param cinemaId
	 * 注册影城
	 * @return
	 */
    public String getMemNumTop(Long cinemaId);
    /**
     * 获取生成会员编号使用的序列的当前序列值
     * @return
     */
    public String getMemSeqForMemNum();
    /**
     * 修改会员状态
     * @param strIds
     */
    public void updateMemberStatus(String strIds , IModuleRequest req,Long status,String changeResion);
    
    /**
	 * 检验手机号是否重复
	 * @param mobileNo
	 * 手机号
	 * @return
	 */
	public String checkMobile(String mobileNo);
}
