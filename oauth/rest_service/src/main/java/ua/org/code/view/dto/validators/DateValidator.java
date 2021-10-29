package ua.org.code.view.dto.validators;

import ua.org.code.view.dto.annotations.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<Date, String> {
    @Override
    public void initialize(Date constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s,
            ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate.parse(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
