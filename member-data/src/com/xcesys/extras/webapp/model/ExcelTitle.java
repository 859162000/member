package com.xcesys.extras.webapp.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)  
public @interface ExcelTitle {
  
    String isTitle() default "true";  
  
} 
