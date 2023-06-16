package com.fullstack.Backend.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "userName"),
                @UniqueConstraint(columnNames = "email")})
public class User extends BaseEntity {
    @Column(nullable = false)
    private String badgeId;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 20)
    private String userName;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 20)
    private String firstName;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 20)
    private String lastName;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String project;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<Device> devices;

    @OneToMany(mappedBy = "currentKeeper", fetch = FetchType.EAGER)
    private Collection<Request> currentKeepers;

    @OneToMany(mappedBy = "nextKeeper", fetch = FetchType.EAGER)
    private Collection<Request> nextKeepers;

    @ManyToMany()
    @JoinColumn(name = "systemRoles_Id", nullable = false, foreignKey = @ForeignKey(name = "systemRoles_Id_FK"))
    private Set<SystemRole> systemRoles = new HashSet<>();

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;
}
