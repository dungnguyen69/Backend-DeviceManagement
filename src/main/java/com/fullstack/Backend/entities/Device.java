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
public class Device extends BaseEntity {
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String status;

	@OneToOne()
	@JoinColumn(name = "platform_Id", referencedColumnName = "id")
	private Platform platform;
	@Column(name = "platform_Id", insertable = false, updatable = false)
	private Long platform_Id;

	@OneToOne()
	@JoinColumn(name = "itemType_Id", referencedColumnName = "id")
	private ItemType itemType;
	@Column(name = "itemType_Id", insertable = false, updatable = false)
	private Long itemType_Id;
	
	@OneToOne()
	@JoinColumn(name = "ram_Id", referencedColumnName = "id")
	private Ram ram;
	@Column(name = "ram_Id", insertable = false, updatable = false)
	private Long ram_Id;
	
	@OneToOne()
	@JoinColumn(name = "screen_Id", referencedColumnName = "id")
	private Screen screen;
	@Column(name = "screen_Id", insertable = false, updatable = false)
	private Long screen_Id;
	
	@OneToOne()
	@JoinColumn(name = "storage_Id", referencedColumnName = "id")
	private Storage storage;
	@Column(name = "storage_Id", insertable = false, updatable = false)
	private Long storage_Id;
	
	@ManyToOne
	@JoinColumn(name = "owner_Id", referencedColumnName = "id")
	private User owner;
	@Column(name = "owner_Id", insertable = false, updatable = false)
	private Long owner_Id;

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
