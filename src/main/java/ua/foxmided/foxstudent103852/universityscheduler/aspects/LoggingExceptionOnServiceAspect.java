package ua.foxmided.foxstudent103852.universityscheduler.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@Log4j2
public class LoggingExceptionOnServiceAspect {

    @AfterThrowing(pointcut = "execution(* ua.foxmided.foxstudent103852.universityscheduler.service..*(..))", throwing = "exception")
    public void afterThrowingExceptionOnService(JoinPoint joinPoint, Throwable exception) {
        log.debug("[        Service] Exception: {}", exception.getMessage(), exception);
    }

}
