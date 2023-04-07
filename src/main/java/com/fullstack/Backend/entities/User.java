package com.fullstack.Backend.entities;

import java.util.Collection;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User extends BaseEntity{
	@Column(nullable = false)
	private String badgeId;
	
	@Column(nullable = false)
	private String userName;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String phoneNumber;
	
	@Column(nullable = false)
	private String project;
	
	@OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
	private Set<Device> devices;

	@OneToMany(mappedBy="currentKeeperRequest", fetch = FetchType.EAGER)
    private Collection<Request> currentKeepers;

    @OneToMany(mappedBy="nextKeeperRequest", fetch = FetchType.EAGER)
    private Collection<Request> nextKeepers;
    
	@OneToOne()
	@JoinColumn(name = "systemRoles_Id", nullable = false, foreignKey = @ForeignKey(name = "systemRoles_Id_FK"))
	private SystemRole systemRoles;
}
