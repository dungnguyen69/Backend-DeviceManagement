package com.fullstack.Backend.dto.users;

import com.fullstack.Backend.entities.SystemRole;
import com.fullstack.Backend.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDTO {
    private String badgeId;

    private String userName;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String project;

    private List<String> systemRoles;

    private String isEnable;

    public UserDTO(User user) {
        this.badgeId = user.getBadgeId();
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.project = user.getProject();
        if (user.isEnabled())
            this.isEnable = "Yes";
        else
            this.isEnable = "No";
        this.systemRoles = user.getSystemRoles().stream()
                .map(SystemRole::getName)
                .collect(Collectors.toList());
    }
}
