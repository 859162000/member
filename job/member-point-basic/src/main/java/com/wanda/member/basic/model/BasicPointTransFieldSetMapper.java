package com.wanda.member.basic.model;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class BasicPointTransFieldSetMapper implements FieldSetMapper<BasicPointTrans> {

	@Override
	public BasicPointTrans mapFieldSet(FieldSet fieldSet)
			throws BindException {
		//bizDate,innerCode,memberId,orderCode,itemCode,amount
		BasicPointTrans trans = new BasicPointTrans();
		trans.setInnerCode(fieldSet.readString("innerCode"));
		trans.setMemberId(fieldSet.readString("memberId"));
		trans.setAmount(fieldSet.readBigDecimal("amount"));
		trans.setOrderCode(fieldSet.readString("orderCode"));
		trans.setItemCode(fieldSet.readString("itemCode"));
		trans.setBizDate(fieldSet.readString("bizDate"));
		return trans;
	}

}
