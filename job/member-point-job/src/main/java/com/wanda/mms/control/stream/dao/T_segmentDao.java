package com.wanda.mms.control.stream.dao;

import java.sql.Connection;

import com.wanda.mms.control.stream.vo.T_segment;

public interface T_segmentDao {
	
	public T_segment fandT_segmentById(Connection conn,long T_segmentId);

}
