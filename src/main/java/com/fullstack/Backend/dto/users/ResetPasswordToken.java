package com.fullstack.Backend.dto.users;

import com.fullstack.Backend.entities.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class ResetPasswordToken {
    private Long id;
    private String token;

    private User user;

    private Date expiryDate;
}
