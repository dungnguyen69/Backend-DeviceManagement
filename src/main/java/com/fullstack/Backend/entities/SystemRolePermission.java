package com.fullstack.Backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "SystemRolePermission")
public class SystemRolePermission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;
	@OneToOne()
	@JoinColumn(name = "systemRole_Id", referencedColumnName = "id",foreignKey = @ForeignKey(name = "systemRole_Id_FK"))
	private SystemRole systemRole;
	@OneToOne()
	@JoinColumn(name = "permission_Id", referencedColumnName = "id",foreignKey = @ForeignKey(name = "permission_Id_FK"))
	private Permission permissions;
}
