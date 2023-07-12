package com.fullstack.Backend.dto.users;

import com.fullstack.Backend.validation.annotations.ValidPassword;
import lombok.Data;

@Data
public class ForgotPasswordDTO {
    private  String token;

    private String newPassword;

    private String confirmPassword;
}
