package pl.com.bottega.docflowjee.docflow.adapters.interceptors;

import pl.com.bottega.docflowjee.docflow.ValidateCommand;
import pl.com.bottega.eventsourcing.Command;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Interceptor
@ValidateCommand
public class ValidationInterceptor {

    @Inject
    private Validator validator;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        return ctx.proceed();
    }

}
