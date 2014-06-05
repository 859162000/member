package com.wanda.member.activity.utils;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class OracleLongTypeHandler implements TypeHandler<Object>{
	
	@Override  
    public Object getResult(ResultSet rs, String columnName) throws SQLException { 
		Reader rd = rs.getCharacterStream(columnName);
		BufferedReader br = new BufferedReader(rd); 
		System.out.println(br.toString()+"==========");
        return rs.getString(columnName);  
    }  
  
    @Override  
    public Object getResult(CallableStatement arg0, int arg1)  
            throws SQLException {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    @Override  
    public void setParameter(PreparedStatement ps, int paraIndex, Object object,  
            JdbcType jt) throws SQLException {  
        ps.setString(paraIndex, (String)object);  
    }

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}  
  
}

