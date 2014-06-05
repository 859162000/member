package com.wanda.member.upgrade.service;

import com.wanda.member.upgrade.data.TLevelHistory;

public interface MemberLevelHisService {

	void doInsertMemberLevelHis(TLevelHistory record);

	void doDeleteMemberLevelHis(long seq);
}
