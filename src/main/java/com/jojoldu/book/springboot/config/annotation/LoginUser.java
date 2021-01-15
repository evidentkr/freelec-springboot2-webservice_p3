package com.jojoldu.book.springboot.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 메서드의 파라미터로 선언된 객체에만 사용 설정
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {} // 이 파일을 어노테이션 클래스로 지정