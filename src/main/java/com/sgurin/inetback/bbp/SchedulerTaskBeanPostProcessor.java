package com.sgurin.inetback.bbp;

import com.sgurin.inetback.annotation.scheduler.SchedulerTask;
import com.sgurin.inetback.annotation.scheduler.SchedulerTaskContainer;
import com.sgurin.inetback.service.scheduler.SchedulerTaskProcess;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class SchedulerTaskBeanPostProcessor implements BeanPostProcessor {
    private Map<String, SchedulerTaskProcess> taskServices = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        if (beanClass.isAnnotationPresent(SchedulerTask.class)) {
            if (bean instanceof SchedulerTaskProcess) {
                SchedulerTask annotation = beanClass.getAnnotation(SchedulerTask.class);
                taskServices.put(annotation.name(), (SchedulerTaskProcess) bean);
            }
        }

        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            SchedulerTaskContainer annotation = field.getAnnotation(SchedulerTaskContainer.class);
            if (annotation != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, taskServices);
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
