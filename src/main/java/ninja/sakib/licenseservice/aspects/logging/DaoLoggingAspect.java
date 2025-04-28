package ninja.sakib.licenseservice.aspects.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class DaoLoggingAspect {

    @Before("execution(* ninja.sakib.licenseservice.daos.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(value = "execution(* ninja.sakib.licenseservice.daos.*.*(..))", returning = "result")
    public void afterReturn(JoinPoint joinPoint, Object result) {
        log.info("Exiting method: {} with result: {}", joinPoint.getSignature(), result);
    }

}
