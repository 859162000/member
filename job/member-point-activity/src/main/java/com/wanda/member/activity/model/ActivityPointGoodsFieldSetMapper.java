package com.wanda.member.activity.model;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class ActivityPointGoodsFieldSetMapper implements FieldSetMapper<ActivityPointGoods> {

	@Override
	public ActivityPointGoods mapFieldSet(FieldSet fieldSet)
			throws BindException {
		ActivityPointGoods goods = new ActivityPointGoods();
		goods.setBizDate(fieldSet.readString("bizDate"));
		goods.setCsOrderCode(fieldSet.readString("csOrderCode"));
		goods.setExtPointCriteriaId(fieldSet.readInt("extPointCriteriaId"));
		goods.setExtPointRuleId(fieldSet.readInt("extPointRuleId"));
		goods.setInnerCode(fieldSet.readString("innerCode"));
		goods.setItemCode(fieldSet.readString("itemCode"));
		goods.setMemberKey(fieldSet.readString("memberKey"));
		goods.setPointPercent(fieldSet.readInt("pointPercent"));
		goods.setPointAddition(fieldSet.readInt("pointAddition"));
		goods.setSaleAmount(fieldSet.readBigDecimal("saleAmount"));
		return goods;
	}

}
