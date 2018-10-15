package pl.com.bottega.docflowjee.docflow.commands.validation;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ObscenityValidator implements ConstraintValidator<ValidateObscenity, String> {

    private Set<String> obsceneWords = HashSet.of("motyla noga", "kurczę pióro");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        return obsceneWords.find((word) -> value.contains(word)).isEmpty();
    }
}
