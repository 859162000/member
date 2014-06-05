package com.wanda.ccs.jobhub.smsreport.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据数据库字段反射
 * 
 * @author zhurui
 * @date 2014年1月8日 下午2:42:33
 */
public class FieldUtil {
	
	// 数据库和对象之间的映射关系的缓存
	public static Map<String, String> FIELD_MAPPER = new HashMap<String, String>();
	
	/**
	 * 通过反射设置值
	 * 
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws Exception
	 */
	public static void setValue(Object obj, String fieldName, Object value) throws Exception {
		if(FIELD_MAPPER.containsKey(fieldName)) {
			fieldName = FIELD_MAPPER.get(fieldName);
		} else {
			String field = "";
			if(fieldName.indexOf("_") > 0) {
				String[] s = fieldName.split("_");
				for(int i=0,len=s.length;i<len;i++) {
					if(i == 0) {
						field += s[i].toLowerCase();
					} else {
						field += (s[i].substring(0, 1)+s[i].substring(1).toLowerCase());
					}
				}
			} else {
				field = fieldName.toLowerCase();
			}
			
			FIELD_MAPPER.put(fieldName, field);
			fieldName = field;
		}
		
		Field privateStringField = obj.getClass().getDeclaredField(fieldName);
		privateStringField.setAccessible(true);
		privateStringField.set(obj, value);
	}
	
}
