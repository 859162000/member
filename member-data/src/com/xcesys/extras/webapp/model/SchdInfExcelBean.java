package com.xcesys.extras.webapp.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class SchdInfExcelBean extends ExcelCommonBean {

	private Set<String> sets;
	
	public Set<String> getSets() {
		return sets;
	}
	public void setSets(Set<String> sets) {
		this.sets = sets;
	}
	
	public static void main(String[] args) {
		Field[] declaredFields = SchdInfExcelBean.class.getDeclaredFields();
		Field[] f2 = SchdInfExcelBean.class.getFields();
		Method[] m = SchdInfExcelBean.class.getMethods();
		
		System.out.println();
		for (Method m1 : m) {
			System.out.println(m1.getName());
		}
	}
}
