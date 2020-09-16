package pl.com.bottega.docflowjee.docflow.adapters.rest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ObscenityValidator.class)
public @interface ValidateObscenity {

    String message() default "contains obscene words";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
