package ua.org.code.view.dto.validators;

import ua.org.code.view.dto.annotations.EqualPasswords;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class EqualPasswordsValidator implements
        ConstraintValidator<EqualPasswords, Object> {

    private String baseField;
    private String matchField;

    @Override
    public void initialize(EqualPasswords constraintAnnotation) {
        baseField = constraintAnnotation.baseField();
        matchField = constraintAnnotation.matchField();
    }

    @Override
    public boolean isValid(Object object,
            ConstraintValidatorContext constraintValidatorContext) {
        try {
            Object baseFieldValue = getFieldValue(object, baseField);
            Object matchFieldValue = getFieldValue(object, matchField);
            return baseFieldValue != null && baseFieldValue.equals(matchFieldValue);
        } catch (Exception e) {
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field passwordField = clazz.getDeclaredField(fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }
}