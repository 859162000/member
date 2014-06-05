package com.wanda.ccs.jobhub.member.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.wanda.ccs.jobhub.member.dao.ImportExcelMemberDao;
import com.wanda.ccs.jobhub.member.service.ImportExcelMemberService;

public class ImportExcelMemberServiceImpl implements ImportExcelMemberService {

	@InstanceIn(path = "ImportExcelMemberDao")	
	private ImportExcelMemberDao importExcelMemberDao;


	/* (non-Javadoc)
	 * @see com.wanda.ccs.jobhub.member.service.impl.ImportExcelMemberService#execute(java.util.Map)
	 */
	@Transactional
	public void execute(Long fileAttachId, String refObjectType, String userId) throws Exception {
		try {
			//删除历史错误信息
			importExcelMemberDao.delAbatchErrorLog(fileAttachId);
			//删除文件历史记录
			importExcelMemberDao.delContactHistoryTemp(fileAttachId);
			//删除文件会员的历史记录
			importExcelMemberDao.delContactHistoryTemp(fileAttachId);
			
			//获取文件中的手机号
			importExcelMemberDao.saveMembersFromFile(refObjectType, fileAttachId, userId);
			
			//检测所有的手机号是否合法,并且是否存在
//			List<ContactHistoryTempBean> list = importExcelMemberDao.checkMobiles(mobiles, fileId, fileName, userId);
			
			//若list不为空，则所有手机号合法并存在，保存手机号所关联的会员到临时波次目标中
//			if(list != null && !list.isEmpty()){
//				//保存临时表时,首先删除历史记录
//				
//				importExcelMemberDao.saveContactHistoryTemp(list, userId);
//			}
			//this.logHelper.info("**************Job Service end***************");
		} catch (Exception e) {
			throw e;
		}
	}
	
}
