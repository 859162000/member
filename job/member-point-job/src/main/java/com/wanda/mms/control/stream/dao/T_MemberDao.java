package com.wanda.mms.control.stream.dao;

import java.sql.Connection;

import com.wanda.mms.control.stream.vo.T_Member;

/**
 *	会员表DAO
 * @author wangshuai
 * @date 2013-05-28	
 */
public interface T_MemberDao {
	
	
	public T_Member fandT_MemberByMOBILE(Connection conn, String T_Dimdefid);
	
	public T_Member fandT_MemberByID(Connection conn,Long memberId );

}
