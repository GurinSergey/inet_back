package com.sgurin.inetback.aspect;

import com.google.common.base.Throwables;
import com.sgurin.inetback.annotation.aspect.LogMethod;
import com.sgurin.inetback.domain.Protocol;
import com.sgurin.inetback.service.ProtocolService;
import com.sgurin.inetback.utils.Rethrower;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@Slf4j
public class LoggerAspect {
    private final ProtocolService protocolService;

    @Autowired
    public LoggerAspect(ProtocolService protocolService) {
        this.protocolService = protocolService;
    }

    //interception on method executions from external calls only
    //interception on public members only (private/protected can't be intercepted)
    @Around("@annotation(logMethod)")
    public Object logMethodExecution(ProceedingJoinPoint pjp,
                                     LogMethod logMethod) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();

        String className = signature.getDeclaringType().getName();
        String methodName = signature.getName();

        Protocol protocol = new Protocol("Aspect " + getLogName(methodName, logMethod));
        protocol.addLineProtocol(String.format("class : %s", className));
        protocol.addLineProtocol(String.format("method : %s", methodName));

        try {
            final String arguments = IntStream.iterate(0, i -> i + 1)
                    .limit(Math.min(signature.getParameterNames().length, pjp.getArgs().length))
                    .mapToObj(i -> signature.getParameterNames()[i] + "=" + pjp.getArgs()[i])
                    .collect(Collectors.joining(","));

            log.info("Start execution of [{}] with arguments: {}", methodName, arguments);
            protocol.addLineProtocol(String.format("Start execution of [%s] with arguments: %s", methodName, arguments));

            Object result = pjp.proceed();

            log.info("Finish execution of [{}]", methodName);
            protocol.addLineProtocol(String.format("Finish execution of [%s]", methodName));

            if (logMethod.needSuccessLog()) {
                protocolService.save(protocol);
            }

            return result;
        } catch (Exception ex) {
            log.error("Fail execution of [{} {}]", className, methodName, ex);
            String stackTrace = Throwables.getStackTraceAsString(ex);
            protocol.addErrCount();
            protocol.addLineProtocol(ex.getLocalizedMessage()).addLineProtocol(stackTrace);

            protocolService.save(protocol);

            Rethrower.rethrow(ex);

            throw new IllegalStateException("Should never get here", ex);
        }
    }

    private String getLogName(String methodName,
                              LogMethod logMethod) {
        if (!StringUtils.isBlank(logMethod.logName())) {
            return logMethod.logName();
        }

        return methodName;
    }

    /*
        @Pointcut("@annotation(com.guestengine.aspect.LogMethod)")
        public void logMethods() {
        }

        @Before("logMethods()")
        public void before(JoinPoint jp) {
            String args = Arrays.stream(jp.getArgs())
                    .map(a -> a.toString())
                    .collect(Collectors.joining(","));

            log.info("before : " + jp.toString() + ", args=[" + args + "]");
        }

        @After("logMethods()")
        public void after(JoinPoint jp) {
            log.info("after : " + jp.toString());
        }

        @AfterReturning(
                pointcut = "logMethods()",
                returning = "retVal")
        public void afterReturning(JoinPoint jp,
                                   Object retVal) {
            log.info("returning : " + retVal.toString());
        }

        @AfterThrowing(
                pointcut = "logMethods()",
                throwing = "ex")
        public void afterThrowing(JoinPoint jp,
                                  Exception ex) {
            Protocol protocol = new Protocol("Aspect " + getLogName(jp));

            protocol.addLineProtocol("class : " + jp.getTarget().getClass().getName());
            protocol.addLineProtocol("method : " + jp.getSignature().getName());

            String stackTrace = Throwables.getStackTraceAsString(ex);
            protocol.addErrCount();
            protocol.addLineProtocol(ex.getLocalizedMessage()).addLineProtocol(stackTrace);

            protocolService.save(protocol);

            log.error("error : " + stackTrace);
        }

        private String getLogName(JoinPoint jp) {
            Class<?> targetClass = jp.getTarget().getClass();
            String methodName = jp.getSignature().getName();

            return Arrays.stream(targetClass.getMethods())
                    .filter(m -> m.getName().equals(methodName))
                    .filter(m -> m.isAnnotationPresent(LogMethod.class))
                    .filter(m -> !StringUtils.isBlank(m.getAnnotation(LogMethod.class).logName()))
                    .map(m -> m.getAnnotation(LogMethod.class).logName())
                    .findFirst()
                    .orElse(methodName);
        }
    */
}