package com.fullstack.Backend.validation.validators;

import com.fullstack.Backend.dto.users.RegisterDTO;
import com.fullstack.Backend.validation.annotations.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("matchingPassword").addConstraintViolation();
        RegisterDTO user = (RegisterDTO) obj;
        return (user.getPassword().equals(user.getMatchingPassword()));
    }
}
