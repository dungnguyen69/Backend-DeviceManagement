package com.fullstack.Backend.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fullstack.Backend.dto.device.DeviceAddDTO;
import com.fullstack.Backend.enums.Origin;
import com.fullstack.Backend.enums.Project;
import com.fullstack.Backend.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "Devices", uniqueConstraints = @UniqueConstraint(columnNames = "serialNumber", name = "serialNumber"))
public class Device extends BaseEntity {
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private Status status;

	@OneToOne()
	@JoinColumn(name = "platform_Id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "platform_Id_FK"))
	@JsonIgnore()
	private Platform platform;
	@Column(name = "platform_Id")
	private int platform_Id;

	@OneToOne()
	@JoinColumn(name = "item_type_Id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "item_type_Id_FK"))
	@JsonIgnore()
	private ItemType itemType;
	@Column(name = "item_type_Id")
	private int item_type_Id;

	@OneToOne()
	@JoinColumn(name = "ram_Id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "ram_Id_FK"))
	@JsonIgnore()
	private Ram ram;
	@Column(name = "ram_Id")
	private int ram_Id;

	@OneToOne()
	@JoinColumn(name = "screen_Id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "screen_Id_FK"))
	@JsonIgnore()
	private Screen screen;
	@Column(name = "screen_Id")
	private int screen_Id;

	@OneToOne()
	@JoinColumn(name = "storage_Id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "storage_Id_FK"))
	@JsonIgnore()
	private Storage storage;
	@Column(name = "storage_Id")
	private int storage_Id;

	@ManyToOne()
	@JoinColumn(name = "owner_Id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "owner_Id_FK"))
	@JsonIgnore()
	private User owner;
	@Column(name = "owner_Id")
	private int owner_Id;

	@Column(nullable = false)
	private String inventoryNumber;

	@Column(nullable = false)
	private String serialNumber;

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private Origin origin;

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private Project project;

	@Column()
	private String comments;

	public void loadToEntity(DeviceAddDTO deviceAddDTO) {
		this.name = deviceAddDTO.getName();
		this.item_type_Id = deviceAddDTO.getItemTypeId();
		this.status = Status.values()[deviceAddDTO.getStatusId()];
		this.platform_Id = deviceAddDTO.getPlatformId();
		this.ram_Id = deviceAddDTO.getRamId();
		this.screen_Id = deviceAddDTO.getScreenId();
		this.storage_Id = deviceAddDTO.getStorageId();
		this.inventoryNumber = deviceAddDTO.getInventoryNumber();
		this.serialNumber = deviceAddDTO.getSerialNumber();
		this.comments = deviceAddDTO.getComments();
		this.project = Project.values()[deviceAddDTO.getProjectId()];
		this.origin = Origin.values()[deviceAddDTO.getOriginId()];
		this.owner_Id = deviceAddDTO.getOwnerId();
		this.setCreatedDate(new Date());
	}
}
