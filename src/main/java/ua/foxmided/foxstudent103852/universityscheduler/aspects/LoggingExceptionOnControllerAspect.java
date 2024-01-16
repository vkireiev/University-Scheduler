package ua.foxmided.foxstudent103852.universityscheduler.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@Log4j2
public class LoggingExceptionOnControllerAspect extends AbstractMessageLogger {

    @AfterThrowing(pointcut = "execution(* ua.foxmided.foxstudent103852.universityscheduler..controllers..*(..))", throwing = "exception")
    public void afterThrowingExceptionOnController(JoinPoint joinPoint, Throwable exception) {
        log.debug("[     Controller] Exception: {}", exception.getMessage(), exception);
    }

}
