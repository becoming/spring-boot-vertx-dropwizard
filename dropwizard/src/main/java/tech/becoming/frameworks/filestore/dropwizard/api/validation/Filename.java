package tech.becoming.frameworks.filestore.dropwizard.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy={})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp="^[^*&%/]+$")
public @interface Filename {
    String message() default "{invalid.filename}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
