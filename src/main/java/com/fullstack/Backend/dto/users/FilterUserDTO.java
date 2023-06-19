package com.fullstack.Backend.dto.users;

import com.fullstack.Backend.entities.SystemRole;
import com.fullstack.Backend.validation.annotations.ValidEmail;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterUserDTO {
    private String badgeId;

    private String userName;

    private String firstName;

    private String lastName;

    @ValidEmail
    private String email;

    private String phoneNumber;

    private String project;

    private Set<SystemRole> systemRoles = new HashSet<>();
}
