package com.example.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Class, Interface 에다가 붙이는 애노테이션인 경우 ElementType.TYPE 을 사용한다.
// RetentionPolicy.RUNTIME 은 실제 RUNTIME 일 때에도 이 애노테이션이 살아있는 경우를 말한다.
// RUNTIME 말고 SOURCE 란 것도 있는데 이건 컴파일하면 컴파일된 파일은 이 애노테이션이 사라져버린다. 그래서 동적으로 이 애노테이션을 읽을 수 없다. 우리가 원하는게 아니다.
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassAop {

}
