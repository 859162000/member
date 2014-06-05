package com.wanda.mms.control.stream;


import java.sql.Connection;
import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.spi.LineHandle;

/**
 *	更新影票总积分
 * @author wangshuai
 * @date 2013-07-09	
 */



public class TotalActivityPointLineHandle implements LineHandle{
	private Connection conn;
	public TotalActivityPointLineHandle(Connection conn){
		this.conn=conn;
	}
	public TotalActivityPointLineHandle(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 // 两个状态字段来判断 第 一 是否为 sale 销售 第二个状态是为 影票与卖品
		int r=0;
		Field lpfield=fieldset.getFieldByName("LEVEL_POINT");
		Field apfield=fieldset.getFieldByName("ACTIVITY_POINT");
 
		Field pfield=fieldset.getFieldByName("POINT");
			 
					if(apfield.destValue==null){
						apfield.destValue="0";
					}
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
