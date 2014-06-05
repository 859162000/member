package com.wanda.ccs.jobhub.member.service;


public interface ImportExcelMemberService {

	/**
	 * batch insert all daily table
	 * @param salesDate
	 */
	void execute(Long fileAttachId, String refObjectType, String userId) throws Exception;

}