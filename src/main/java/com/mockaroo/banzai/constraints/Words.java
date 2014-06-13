package com.mockaroo.banzai.constraints;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(value={java.lang.annotation.ElementType.FIELD})
@Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Words 
{
	int min() default 10;
	int max() default 20;
}
