package com.wanda.member.activity.classify;

import org.springframework.classify.annotation.Classifier;

import com.wanda.member.activity.model.ActivityPointUpdataParameter;

public class ProcessSuccessLogClassifier {
	@Classifier
	public String classify(ActivityPointUpdataParameter classifiable) {
	    return classifiable.getRuleType();
	}
}
