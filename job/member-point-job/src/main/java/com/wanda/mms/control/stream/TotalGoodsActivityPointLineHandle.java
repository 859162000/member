package com.wanda.mms.control.stream;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.db.ResultQuery;
import com.solar.etl.db.SqlHelp;
import com.solar.etl.spi.LineHandle;

/**
 *	更新卖品总积分
 * @author wangshuai
 * @date 2013-07-09	
 */



public class TotalGoodsActivityPointLineHandle implements LineHandle{
	private Connection conn;
	public TotalGoodsActivityPointLineHandle(Connection conn){
		this.conn=conn;
	}
	public TotalGoodsActivityPointLineHandle(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 // 两个状态字段来判断 第 一 是否为 sale 销售 第二个状态是为 影票与卖品
		int r=0;
		Dimdef dd = new Dimdef();
	 
		 
 
		Field lpfield=fieldset.getFieldByName("LEVEL_POINT");
		Field apfield=fieldset.getFieldByName("ACTIVITY_POINT");
 
		Field pfield=fieldset.getFieldByName("POINT");
 
	 

 
		
 
							int a7=	Integer.valueOf(apfield.destValue)+Integer.valueOf(lpfield.destValue);
							String s7=String.valueOf(a7);
							pfield.destValue=(s7);
			 
		r=1;
		return r;
	}
	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}
}
