package com.itheima.anno;

import com.itheima.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented//元注解  用于标识到时的这个State注解是可以抽取到帮助文档里面
@Target({ FIELD})//元注解   标识将来的State注解可以用在哪些地方  这里设置了FIELD，表示将来可以用在属性上
@Retention(RUNTIME)//元注解  标识在哪个阶段会被保留（编译阶段、源码阶段、运行时阶段）  ，这里设置了RUNTIME，表示运行时阶段
@Constraint(validatedBy = { StateValidation.class})//指定提供校验规则的类
public @interface State {
    //提供校验失败后的提示信息
    String message() default "state参数的值只能是已发布或者草稿";
    //指定分组
    Class<?>[] groups() default { };
    //负载  获取到State注解的附加信息
    Class<? extends Payload>[] payload() default { };
}
