package com.sgurin.inetback.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

public class AspectUtils {
    public static Object getMethodParamValueByClass(ProceedingJoinPoint pjp,
                                                    Class<? extends Annotation> annotationClass) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        Object[] args = pjp.getArgs();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotationClass.isInstance(annotation)) {
                    return args[i];
                }
            }
        }
        return null;
    }
}
