package com.wanda;

public @interface TestCase {
	 public String id();
	 public String creater();
	 public String description() default "no description";
}
