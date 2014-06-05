package com.wanda.mms.control.stream;

import java.sql.Connection;

import com.solar.etl.config.mapping.Field;
import com.solar.etl.config.mapping.FieldSet;
import com.solar.etl.log.LogUtils;
import com.solar.etl.spi.LineHandle;

/**
 *	全量重算
 * @author wangshuai
 * @date 2013-07-09	
 */



public class GroupByCinemaRepair implements LineHandle{
	private Connection conn;
	public GroupByCinemaRepair(Connection conn){
		this.conn=conn;
	}
	public GroupByCinemaRepair(){
		conn=Basic.mbr;
	}
	@Override
	public int handle(FieldSet fieldset) {
		 // 两个状态字段来判断 第 一 是否为 sale 销售 第二个状态是为 影票与卖品
		int r=0;
		Field cicfield=fieldset.getFieldByName("CINEMA_INNER_CODE");
		Field bzfield=fieldset.getFieldByName("BIZ_DATE");
		String da = bzfield.destValue;
		String te = da.substring(0, 10);
		LogUtils.debug(cicfield.destValue);
		LogUtils.debug(te);


		String [] aa={"recaluc",te,cicfield.destValue};
		Basic.main(aa);
		r=0;
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
