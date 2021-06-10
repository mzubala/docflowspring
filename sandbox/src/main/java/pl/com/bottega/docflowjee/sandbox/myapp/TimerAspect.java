package pl.com.bottega.docflowjee.sandbox.myapp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimerAspect {

    @Around("@annotation(pl.com.bottega.docflowjee.sandbox.myapp.Timed)")
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
        long ts = System.currentTimeMillis();
        var returnValue = joinPoint.proceed();
        long te = System.currentTimeMillis();
        System.out.printf("%s took %d ms", joinPoint.toLongString(), te - ts);
        return returnValue;
    }

}
