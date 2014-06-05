package com.wanda.mms.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
@Service("myBatisDAO")
public class MyBatisDAO {
	@Resource(name="sqlSession")
    public SqlSessionTemplate sqlSession;
	@Resource(name="sqlSessionDw")
	public SqlSessionTemplate sqlSessionDw;  
	
	@Resource
	protected DataSource dataSource =null;
	@Resource
	protected DataSource dataSourceDw =null;
	
	
	public Connection getConnection(){
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Connection getDwConnection(){
		try {
			return dataSourceDw.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
