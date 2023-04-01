package com.fullstack.Backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Devices")
public class Device extends BaseEntity{
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String status;

	@OneToOne()
	@JoinColumn(name = "platform_Id", referencedColumnName = "id")
	private Platform platform;

	@OneToOne()
	@JoinColumn(name = "itemType_Id", referencedColumnName = "id")
	private ItemType itemType;

	@OneToOne()
	@JoinColumn(name = "ram_Id", referencedColumnName = "id")
	private Ram ram;

	@OneToOne()
	@JoinColumn(name = "screen_Id", referencedColumnName = "id")
	private Screen screen;

	@OneToOne()
	@JoinColumn(name = "storage_Id", referencedColumnName = "id")
	private Storage storage;

	@ManyToOne
	@JoinColumn(name = "owner_Id", referencedColumnName = "id")
	private User owner;
	
	@Column(nullable = false)
	private String inventoryNumber;

	@Column(nullable = false, unique = true)
	private String serialNumber;

	@Column(nullable = false)
	private String origin;

	@Column(nullable = false)
	private String project;

	@Column()
	private String comments;
}
