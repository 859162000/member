package com.wanda.ccs.mbr.tag.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service("myBatisDAO")
public class MyBatisDAO {
	@Resource  
    public SqlSessionTemplate sqlSession;  
	@Resource
	protected DataSource dataSource =null;
	public Connection getConnection(){
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
