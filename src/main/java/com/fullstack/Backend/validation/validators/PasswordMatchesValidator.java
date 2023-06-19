package com.fullstack.Backend.validation.validators;

import com.fullstack.Backend.dto.users.RegisterDTO;
import com.fullstack.Backend.validation.annotations.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        RegisterDTO user = (RegisterDTO) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
