package com.fullstack.Backend.dto.users;

import com.fullstack.Backend.validation.annotations.ValidPassword;
import lombok.Data;

@Data
public class PasswordDTO {
    private String oldPassword;

    private  String token;

    @ValidPassword
    private String newPassword;
}
