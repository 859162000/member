package com.wanda.mms.control.stream.dao;

import java.sql.Connection;

import com.wanda.mms.control.stream.vo.T_event_upgrade;

public interface T_event_upgradeDao {
	
	public int addTeventupgrade(Connection conn ,T_event_upgrade teu);

}
