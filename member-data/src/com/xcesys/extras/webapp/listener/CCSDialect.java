package com.xcesys.extras.webapp.listener;

import java.sql.Types;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StandardBasicTypes;
/**
 * oracle数据类型转换成hibernate数据类型
 * 自定义方言
 * @author chenxm
 *
 */
public class CCSDialect extends Oracle10gDialect {
	public CCSDialect() {
		super();
		registerHibernateType(Types.CHAR, StandardBasicTypes.STRING.getName());   
        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());   
//        registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.STRING.getName()); 
	}

}
