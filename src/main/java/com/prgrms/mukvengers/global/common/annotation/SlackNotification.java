package com.prgrms.mukvengers.global.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.jandex.AnnotationTarget;

import net.bytebuddy.implementation.attribute.AnnotationRetention;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SlackNotification {
}
