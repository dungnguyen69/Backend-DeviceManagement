package com.fullstack.Backend.dto.users;

import com.fullstack.Backend.validation.annotations.ValidPassword;
import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String oldPassword;

    private  int id;

//    @ValidPassword
    private String newPassword;

//    @ValidPassword
    private String confirmPassword;
}
