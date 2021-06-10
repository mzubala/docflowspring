package pl.com.bottega.docflowjee.sandbox.myapp;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Aspect
public class LoggingAspect {

    @Before(value = "execution(* pl.com.bottega.docflowjee.sandbox.myapp.*.*(java.math.BigDecimal)) && args(value)",
            argNames = "value")
    //@After(value = "within(pl.com.bottega.docflowjee.sandbox.myapp.*) && args(value)", argNames = "value")
    public void logIt(JoinPoint jp, BigDecimal value) {
        System.out.println("And the value is = " + value);
    }

}
