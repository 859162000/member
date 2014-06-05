package com.wanda.member.activity.model;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class ActivityPointMemberFieldSetMapper implements FieldSetMapper<ActivityPointMember> {

	@Override
	public ActivityPointMember mapFieldSet(FieldSet fieldSet)
			throws BindException {
		ActivityPointMember member = new ActivityPointMember();
		member.setBizDate(fieldSet.readString("bizDate"));
		member.setExtPointCriteriaId(fieldSet.readInt("extPointCriteriaId"));
		member.setExtPointRuleId(fieldSet.readInt("extPointRuleId"));
		member.setMemberKey(fieldSet.readString("memberKey"));
		member.setPointAdditionCode(fieldSet.readInt("pointAdditionCode"));
		return member;
	}

}
