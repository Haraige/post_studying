package ua.org.code.view.dto.annotations;

import ua.org.code.view.dto.validators.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface Date {
    String message() default "Date format invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
